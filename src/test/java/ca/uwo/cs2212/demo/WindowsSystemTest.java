package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Windows-oriented system smoke tests for The Hunt.
 *
 * <p>This suite intentionally avoids constructing Swing {@code JFrame} screens so it can run reliably
 * in headless environments (e.g., CI) while still validating the end-to-end persistence and domain
 * logic that Windows acceptance testing depends on.</p>
 *
 * <p>Coverage focus:</p>
 * <ul>
 *   <li>Player account persistence: save/load/multi-user separation</li>
 *   <li>Progress checkpoint persistence</li>
 *   <li>Statistics aggregation and persistence after a session</li>
 *   <li>High score table ordering behavior (domain helper)</li>
 *   <li>Parent account persistence and parent-child link persistence</li>
 * </ul>
 *
 * <p>Rationale: The production app persists data through {@link FileStorageService} to JSON files under
 * the {@code data/} directory. These tests use an in-memory {@link StorageService} to simulate the same
 * storage API and verify that the JSON round-trips preserve key user-visible state on Windows.</p>
 */
class WindowsSystemTest {

    /**
     * In-memory storage backend that mimics file path based reads/writes.
     * It enables end-to-end JSON round-trip verification without touching the filesystem.
     */
    private static final class MapStorageService implements StorageService {
        private final Map<String, String> files = new HashMap<>();

        @Override
        public String read(String path) {
            return files.getOrDefault(path, "");
        }

        @Override
        public void write(String path, String content) {
            files.put(path, content == null ? "" : content);
        }

        public String raw(String path) {
            return files.get(path);
        }
    }

    @Test
    void playerPersistence_saveLoad_multipleUsersRemainSeparate() {
        MapStorageService storage = new MapStorageService();
        SaveManager saveManager = new SaveManager(storage);

        Player p1 = new Player("win_user_01", "pass1234");
        Player p2 = new Player("win_user_02", "pass5678");

        p1.addCoins(250);
        p2.addCoins(50);

        saveManager.savePlayer(p1);
        saveManager.savePlayer(p2);

        Player reloaded1 = saveManager.loadPlayer("win_user_01");
        Player reloaded2 = saveManager.loadPlayer("win_user_02");

        assertNotNull(reloaded1);
        assertNotNull(reloaded2);
        assertEquals("win_user_01", reloaded1.getUsername());
        assertEquals("win_user_02", reloaded2.getUsername());
        assertTrue(reloaded1.verifyPassword("pass1234"));
        assertTrue(reloaded2.verifyPassword("pass5678"));
        assertNotEquals(reloaded1.getCoins(), reloaded2.getCoins(), "Coin balances should remain user-specific.");

        List<Player> all = saveManager.loadAllPlayers();
        assertEquals(2, all.size(), "Two distinct players should be persisted.");
        assertNotNull(storage.raw("data/players.json"), "Players JSON should be written to the default path.");
    }

    @Test
    void progressCheckpoint_roundTripsThroughSaveManager() {
        MapStorageService storage = new MapStorageService();
        SaveManager saveManager = new SaveManager(storage);

        Player p = new Player("checkpoint_user", "pw");
        p.getProgress().setCurrentLevel(3);
        p.getProgress().recordCurrentLevelCheckpoint(3, 7);
        saveManager.savePlayer(p);

        Player reloaded = saveManager.loadPlayer("checkpoint_user");
        assertNotNull(reloaded);
        assertEquals(3, reloaded.getProgress().getCurrentLevel());
        assertEquals(7, reloaded.getProgress().getLevelWordsCompleted(3));
    }

    @Test
    void statistics_updateFromSessionResult_persistsAcrossReload() {
        MapStorageService storage = new MapStorageService();
        SaveManager saveManager = new SaveManager(storage);

        Player p = new Player("stats_user", "pw");

        SessionResult r1 = new SessionResult(
                420,
                55.0,
                92.0,
                3,
                40,
                36,
                "2026-03-31",
                true
        );
        p.getStatistics().updateFrom(r1);
        p.addRecentActivity(r1);
        p.getProgress().recordCompletedLevel(1, 8);
        saveManager.savePlayer(p);

        Player reloaded = saveManager.loadPlayer("stats_user");
        assertNotNull(reloaded);
        assertEquals(420, reloaded.getStatistics().getHighScore());
        assertEquals(55.0, reloaded.getStatistics().getPeakWPM(), 0.0001);
        assertEquals(92.0, reloaded.getStatistics().getAccuracy(), 0.0001);
        assertEquals(40, reloaded.getStatistics().getTotalTime());
        assertEquals(36, reloaded.getStatistics().getWordsTyped());
        assertEquals(3, reloaded.getStatistics().getErrorCount());
        assertFalse(reloaded.getRecentActivity().isEmpty(), "Recent activity should persist via JSON.");
        assertTrue(reloaded.getProgress().getCurrentLevel() >= 1, "Progress should remain valid after persistence.");
    }

    @Test
    void highScoreTable_returnsTopSortedDescending() {
        HighScoreTable table = new HighScoreTable();
        table.addScore("alice", 100);
        table.addScore("bob", 700);
        table.addScore("carl", 250);

        List<ScoreEntry> top2 = table.getTop(2);
        assertEquals(2, top2.size());
        assertEquals("bob", top2.get(0).getUsername());
        assertEquals(700, top2.get(0).getScore());
        assertEquals("carl", top2.get(1).getUsername());
        assertEquals(250, top2.get(1).getScore());
    }

    @Test
    void parentAccountStore_saveLoad_roundTrip() {
        MapStorageService storage = new MapStorageService();
        ParentAccountStore store = new ParentAccountStore(storage);

        store.save("parent_01", "secret");
        ParentAccountEntry loaded = store.load("parent_01");

        assertNotNull(loaded);
        assertEquals("parent_01", loaded.getUsername());
        assertEquals("secret", loaded.getPasswordHash());
        assertNotNull(storage.raw("data/parent_accounts.json"));
    }

    @Test
    void parentLinkStore_linkUnlink_persistsAndIsIdempotent() {
        MapStorageService storage = new MapStorageService();
        ParentLinkStore store = new ParentLinkStore(storage);

        assertTrue(store.linkPlayer("parent_01", "child_01"), "First link should be accepted.");
        assertFalse(store.linkPlayer("parent_01", "child_01"), "Duplicate link should be rejected.");

        List<String> linked = store.getLinkedPlayers("parent_01");
        assertEquals(List.of("child_01"), linked);
        assertNotNull(storage.raw("data/parent_links.json"));

        assertTrue(store.unlinkPlayer("parent_01", "child_01"));
        assertTrue(store.getLinkedPlayers("parent_01").isEmpty());
        assertFalse(store.unlinkPlayer("parent_01", "child_01"), "Unlinking a missing child should return false.");
    }
}

