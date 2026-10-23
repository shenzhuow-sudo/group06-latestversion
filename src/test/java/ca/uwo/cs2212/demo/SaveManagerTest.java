package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SaveManager} class.
 *
 * <p>This test suite verifies that SaveManager correctly:
 * <ul>
 *     <li>validates constructor arguments</li>
 *     <li>loads players from storage</li>
 *     <li>saves or updates players by username</li>
 *     <li>saves and loads all players</li>
 *     <li>handles blank or invalid JSON safely</li>
 * </ul>
 * </p>
 *
 * @author Robert Niu
 */
class SaveManagerTest {
    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass()");
    }
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
    }
    @BeforeEach
    public void setUp() {
        System.out.println("setUp()");
    }
    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
    }

    /**
     * A simple fake storage service for unit testing.
     *
     * <p>This version stores only one text content value, which is sufficient
     * for testing SaveManager because the class works with a single players file.</p>
     */
    private static class FakeStorageService implements StorageService {
        private String content;
        private String lastPath;

        @Override
        public String read(String path) {
            lastPath = path;
            return content;
        }

        @Override
        public void write(String path, String content) {
            lastPath = path;
            this.content = content;
        }

        /**
         * Sets raw content directly into fake storage.
         *
         * @param raw raw file content
         */
        public void setRaw(String raw) {
            this.content = raw;
        }

        /**
         * Returns the raw stored content.
         *
         * @return stored content
         */
        public String getRaw() {
            return content;
        }

        /**
         * Returns the last accessed path.
         *
         * @return last path used in read/write
         */
        public String getLastPath() {
            return lastPath;
        }
    }

    /**
     * Tests that the constructor rejects null storage.
     */
    @Test
    void constructor_nullStorage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SaveManager(null));
    }

    /**
     * Tests that the constructor rejects a null JSON path.
     */
    @Test
    void constructor_nullPath_throwsNullPointerException() {
        FakeStorageService storage = new FakeStorageService();

        assertThrows(NullPointerException.class,
                () -> new SaveManager(storage, null));
    }

    /**
     * Tests that loading a player from empty storage returns null.
     */
    @Test
    void loadPlayer_emptyStorage_returnsNull() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        Player result = manager.loadPlayer("alice");

        assertNull(result);
    }

    /**
     * Tests that loadPlayer finds a matching player by username.
     */
    @Test
    void loadPlayer_existingUsername_returnsPlayer() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        Player alice = new Player("alice", "hash1");
        Player bob = new Player("bob", "hash2");
        manager.saveAll(List.of(alice, bob));

        Player result = manager.loadPlayer("bob");

        assertNotNull(result);
        assertEquals("bob", result.getUsername());
        assertEquals("hash2", result.getPasswordHash());
    }

    /**
     * Tests that loadPlayer returns null when the username does not exist.
     */
    @Test
    void loadPlayer_missingUsername_returnsNull() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        manager.saveAll(List.of(new Player("alice", "hash1")));

        Player result = manager.loadPlayer("charlie");

        assertNull(result);
    }

    /**
     * Tests that loadPlayer rejects a null username.
     */
    @Test
    void loadPlayer_nullUsername_throwsNullPointerException() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        assertThrows(NullPointerException.class,
                () -> manager.loadPlayer(null));
    }

    /**
     * Tests that savePlayer stores a new player.
     */
    @Test
    void savePlayer_newPlayer_isStored() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        Player alice = new Player("alice", "hash1");
        manager.savePlayer(alice);

        List<Player> players = manager.loadAllPlayers();

        assertEquals(1, players.size());
        assertEquals("alice", players.get(0).getUsername());
    }

    /**
     * Tests that savePlayer replaces an existing player with the same username.
     */
    @Test
    void savePlayer_existingUsername_replacesPreviousEntry() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        Player original = new Player("alice", "oldHash");
        Player updated = new Player("alice", "newHash");

        manager.savePlayer(original);
        manager.savePlayer(updated);

        List<Player> players = manager.loadAllPlayers();

        assertEquals(1, players.size());
        assertEquals("alice", players.get(0).getUsername());
        assertEquals("newHash", players.get(0).getPasswordHash());
    }

    /**
     * Tests that savePlayer rejects null input.
     */
    @Test
    void savePlayer_nullPlayer_throwsNullPointerException() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        assertThrows(NullPointerException.class,
                () -> manager.savePlayer(null));
    }

    /**
     * Tests that saveAll stores all given players.
     */
    @Test
    void saveAll_storesAllPlayers() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        List<Player> input = List.of(
                new Player("alice", "hash1"),
                new Player("bob", "hash2")
        );

        manager.saveAll(input);

        List<Player> result = manager.loadAllPlayers();

        assertEquals(2, result.size());
        assertEquals("alice", result.get(0).getUsername());
        assertEquals("bob", result.get(1).getUsername());
    }

    /**
     * Tests that saveAll treats null as an empty player list.
     */
    @Test
    void saveAll_nullList_savesEmptyList() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        manager.saveAll(null);

        List<Player> result = manager.loadAllPlayers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that loadAllPlayers returns an empty list when storage content is blank.
     */
    @Test
    void loadAllPlayers_blankStorage_returnsEmptyList() {
        FakeStorageService storage = new FakeStorageService();
        storage.setRaw("   ");

        SaveManager manager = new SaveManager(storage);

        List<Player> result = manager.loadAllPlayers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that loadAllPlayers returns a defensive copy.
     */
    @Test
    void loadAllPlayers_returnsCopyNotInternalList() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage);

        manager.saveAll(List.of(new Player("alice", "hash1")));

        List<Player> copy = manager.loadAllPlayers();
        copy.clear();

        List<Player> reloaded = manager.loadAllPlayers();

        assertEquals(1, reloaded.size(),
                "Modifying the returned list should not affect stored data.");
    }

    /**
     * Tests that invalid JSON content causes an IllegalStateException.
     */
    @Test
    void loadAllPlayers_invalidJson_throwsIllegalStateException() {
        FakeStorageService storage = new FakeStorageService();
        storage.setRaw("{ invalid json");

        SaveManager manager = new SaveManager(storage);

        assertThrows(IllegalStateException.class, manager::loadAllPlayers);
    }

    /**
     * Tests that a custom path is actually used by the manager.
     */
    @Test
    void constructor_customPath_usesCustomPath() {
        FakeStorageService storage = new FakeStorageService();
        SaveManager manager = new SaveManager(storage, "customPlayers.json");

        manager.saveAll(List.of(new Player("alice", "hash1")));

        assertEquals("customPlayers.json", storage.getLastPath());
        assertNotNull(storage.getRaw());
    }
}