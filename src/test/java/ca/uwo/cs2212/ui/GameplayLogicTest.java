package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SessionResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameplayLogicTest {

    @Test
    void calculateWordScore_appliesLevelAndDoubleScore() {
        assertEquals(150, GameplayLogic.calculateWordScore("apple", 1, false));
        assertEquals(300, GameplayLogic.calculateWordScore("apple", 1, true));
        assertEquals(0, GameplayLogic.calculateWordScore("", 2, false));
    }

    @Test
    void levelProgress_clampsToOne() {
        assertEquals(0f, GameplayLogic.levelProgress(0, 8));
        assertEquals(0.5f, GameplayLogic.levelProgress(4, 8));
        assertEquals(1f, GameplayLogic.levelProgress(10, 8));
        assertEquals(0f, GameplayLogic.levelProgress(3, 0));
    }

    @Test
    void buildSessionResult_calculatesDerivedFieldsAndWinState() {
        SessionResult result = GameplayLogic.buildSessionResult(420, 30, 10, 8, 8, 8);

        assertEquals(420, result.getScore());
        assertEquals(16.0, result.getWpm());
        assertEquals(80.0, result.getAccuracy());
        assertEquals(2, result.getErrors());
        assertEquals(30, result.getTimeSec());
        assertEquals(8, result.getWordsTyped());
        assertTrue(result.isWon());
    }

    @Test
    void hasMeaningfulSession_requiresTimeOrInteraction() {
        SessionResult empty = GameplayLogic.buildSessionResult(0, 0, 0, 0, 0, 8);
        SessionResult active = GameplayLogic.buildSessionResult(10, 2, 1, 1, 1, 8);

        assertFalse(GameplayLogic.hasMeaningfulSession(empty, 0, 0, 0));
        assertTrue(GameplayLogic.hasMeaningfulSession(active, 1, 1, 10));
    }

    @Test
    void applySessionOutcome_updatesPlayerOnlyForMeaningfulSessionsAndWins() {
        Player player = new Player("alice", "hash");
        SessionResult win = GameplayLogic.buildSessionResult(420, 30, 10, 8, 8, 8);

        GameplayLogic.applySessionOutcome(player, win, 10, 8, 420, 3, 8, 200);

        assertEquals(1, player.getRecentActivity().size());
        assertEquals(420, player.getStatistics().getHighScore());
        assertEquals(2, player.getStatistics().getErrorCount());
        assertEquals(3200, player.getCoins());
        assertEquals(4, player.getProgress().getCurrentLevel());
    }

    @Test
    void applySessionOutcome_skipsRewardsForMeaninglessLoss() {
        Player player = new Player("alice", "hash");
        SessionResult loss = GameplayLogic.buildSessionResult(0, 0, 0, 0, 0, 8);

        GameplayLogic.applySessionOutcome(player, loss, 0, 0, 0, 2, 8, 200);

        assertTrue(player.getRecentActivity().isEmpty());
        assertEquals(0, player.getStatistics().getHighScore());
        assertEquals(3000, player.getCoins());
        assertEquals(0, player.getProgress().getCurrentLevel());
    }
}
