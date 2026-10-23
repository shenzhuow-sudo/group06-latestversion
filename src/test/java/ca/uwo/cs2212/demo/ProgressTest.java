package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Progress} class.
 *
 * <p>This test class verifies that the Progress object correctly stores
 * levels completed, current level, and settings, and updates progression
 * when advanceLevel is called.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a Progress object with known input values</li>
 *     <li><b>Act</b>: call a getter method, constructor, or advanceLevel</li>
 *     <li><b>Assert</b>: verify that the returned value or updated state is correct</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of level
 * progression data in the typing game.</p>
 *
 * @author Yangyue Qin
 */
class ProgressTest {

    /**
     * Tests whether the constructor correctly stores levelsCompleted,
     * currentLevel, and settings.
     */
    @Test
    void storesFieldsCorrectly() {
        Settings settings = new Settings(Theme.LIGHT, true);
        Progress progress = new Progress(2, 3, settings);
        assertEquals(2, progress.getLevelsCompleted());
        assertEquals(3, progress.getCurrentLevel());
        assertEquals(0, progress.getCurrentLevelWordsCompleted());
        assertEquals(settings, progress.getSettings());
    }

    /**
     * Tests whether the constructor rejects null settings.
     */
    @Test
    void nullSettingsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Progress(0, 0, null));
    }

    /**
     * Tests whether advanceLevel increases currentLevel and levelsCompleted by one.
     */
    @Test
    void advanceLevelUpdatesProgress() {
        Settings settings = new Settings(Theme.LIGHT, true);
        Progress progress = new Progress(1, 2, settings);
        progress.advanceLevel();
        assertEquals(2, progress.getLevelsCompleted());
        assertEquals(3, progress.getCurrentLevel());
    }

    /**
     * Tests whether getSettings returns the same settings object stored in Progress.
     */
    @Test
    void getSettingsReturnsStoredObject() {
        Settings settings = new Settings(Theme.DARK, false);
        Progress progress = new Progress(0, 1, settings);
        assertSame(settings, progress.getSettings());
    }

    /**
     * Tests whether recordCompletedLevel advances current level and completion count.
     */
    @Test
    void recordCompletedLevel_advancesWithinBounds() {
        Progress progress = new Progress(0, 0, new Settings(Theme.LIGHT, true));

        progress.recordCompletedLevel(2, 5);

        assertEquals(2, progress.getLevelsCompleted());
        assertEquals(3, progress.getCurrentLevel());
    }

    /**
     * Tests whether recordCompletedLevel does not reduce existing progress.
     */
    @Test
    void recordCompletedLevel_doesNotRegressProgress() {
        Progress progress = new Progress(4, 5, new Settings(Theme.LIGHT, true));

        progress.recordCompletedLevel(2, 5);

        assertEquals(4, progress.getLevelsCompleted());
        assertEquals(5, progress.getCurrentLevel());
    }

    /**
     * Tests whether recordCompletedLevel clamps values to the maximum level.
     */
    @Test
    void recordCompletedLevel_clampsToMaxLevel() {
        Progress progress = new Progress(0, 0, new Settings(Theme.LIGHT, true));

        progress.recordCompletedLevel(99, 3);

        assertEquals(3, progress.getLevelsCompleted());
        assertEquals(3, progress.getCurrentLevel());
    }

    /**
     * Tests whether setCurrentLevel normalizes negative values to zero.
     */
    @Test
    void setCurrentLevel_negativeValue_becomesZero() {
        Progress progress = new Progress(0, 2, new Settings(Theme.LIGHT, true));

        progress.setCurrentLevel(-5);

        assertEquals(0, progress.getCurrentLevel());
    }

    @Test
    void recordCurrentLevelCheckpoint_storesWordsCompleted() {
        Progress progress = new Progress(3, 4, new Settings(Theme.LIGHT, true));

        progress.recordCurrentLevelCheckpoint(4, 3);

        assertEquals(4, progress.getCurrentLevel());
        assertEquals(3, progress.getCurrentLevelWordsCompleted());
        assertEquals(3, progress.getLevelWordsCompleted(4));
    }

    @Test
    void recordCompletedLevel_clearsSavedCheckpoint() {
        Progress progress = new Progress(3, 4, new Settings(Theme.LIGHT, true));
        progress.recordCurrentLevelCheckpoint(4, 3);

        progress.recordCompletedLevel(4, 8);

        assertEquals(5, progress.getCurrentLevel());
        assertEquals(4, progress.getLevelsCompleted());
        assertEquals(0, progress.getCurrentLevelWordsCompleted());
        assertEquals(0, progress.getLevelWordsCompleted(4));
    }

    @Test
    void checkpoints_areTrackedPerLevel() {
        Progress progress = new Progress(3, 4, new Settings(Theme.LIGHT, true));

        progress.recordCurrentLevelCheckpoint(1, 2);
        progress.recordCurrentLevelCheckpoint(4, 3);

        assertEquals(2, progress.getLevelWordsCompleted(1));
        assertEquals(3, progress.getLevelWordsCompleted(4));
    }

    @Test
    void constructor_migratesLegacyCurrentLevelCheckpoint() {
        Progress progress = new Progress(0, 2, null, 5, new Settings(Theme.LIGHT, true));

        assertEquals(5, progress.getLevelWordsCompleted(2));
    }
}
