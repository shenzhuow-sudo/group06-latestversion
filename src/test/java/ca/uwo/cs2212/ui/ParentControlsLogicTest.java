package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SessionResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParentControlsLogicTest {

    @Test
    void validateResetPasswordInput_checksRequiredLengthAndMatch() {
        assertEquals("Both password fields are required.", ParentControlsLogic.validateResetPasswordInput("", "pass"));
        assertEquals("Password must be at least 4 characters.", ParentControlsLogic.validateResetPasswordInput("123", "123"));
        assertEquals("Passwords do not match.", ParentControlsLogic.validateResetPasswordInput("pass", "word"));
        assertNull(ParentControlsLogic.validateResetPasswordInput("pass", "pass"));
    }

    @Test
    void rebuildPlayerWithPassword_preservesExistingState() {
        Player original = new Player("alice", "old");
        original.addCoins(500);
        original.addRecentActivity(new SessionResult(100, 60.0, 95.0, 0, 30, 5, "2026-03-29", true));

        Player updated = ParentControlsLogic.rebuildPlayerWithPassword(original, "newPw");

        assertNotNull(updated);
        assertEquals("alice", updated.getUsername());
        assertEquals("newPw", updated.getPasswordHash());
        assertEquals(original.getCoins(), updated.getCoins());
        assertEquals(1, updated.getRecentActivity().size());
    }

    @Test
    void resetPlayerStatistics_clearsStatsAndRecentActivity() {
        Player player = new Player("alice", "hash");
        player.getStatistics().updateFrom(new SessionResult(100, 60.0, 95.0, 0, 30, 5, "2026-03-29", true));
        player.addRecentActivity(new SessionResult(100, 60.0, 95.0, 0, 30, 5, "2026-03-29", true));

        ParentControlsLogic.resetPlayerStatistics(player);

        assertEquals(0, player.getStatistics().getHighScore());
        assertEquals(0, player.getStatistics().getTotalTime());
        assertTrue(player.getRecentActivity().isEmpty());
    }

    @Test
    void allPlayerNames_filtersNullsAndReturnsFallback() {
        Player alice = new Player("alice", "hash");
        Player bob = new Player("bob", "hash");

        assertArrayEquals(new String[]{"alice", "bob"}, ParentControlsLogic.allPlayerNames(Arrays.asList(alice, null, bob)));
        assertArrayEquals(new String[]{"No players found"}, ParentControlsLogic.allPlayerNames(List.of()));
    }

    @Test
    void resetHighScores_onlyClearsHighScoreValues() {
        Player alice = new Player("alice", "hash");
        Player bob = new Player("bob", "hash");
        alice.getStatistics().updateFrom(new SessionResult(100, 60.0, 95.0, 0, 30, 5, "2026-03-29", true));
        bob.getStatistics().updateFrom(new SessionResult(200, 70.0, 90.0, 1, 40, 6, "2026-03-29", true));

        ParentControlsLogic.resetHighScores(List.of(alice, bob));

        assertEquals(0, alice.getStatistics().getHighScore());
        assertEquals(0, bob.getStatistics().getHighScore());
        assertTrue(alice.getStatistics().getAvgWPM() > 0);
        assertTrue(bob.getStatistics().getAvgWPM() > 0);
    }
}
