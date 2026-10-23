package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LevelManager} class.
 *
 * <p>This test class verifies that the LevelManager correctly stores levels,
 * retrieves them by id, unlocks progression through Progress, and returns
 * a copy of managed level list.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create LevelManager, Level, and Progress objects</li>
 *     <li><b>Act</b>: call a LevelManager method</li>
 *     <li><b>Assert</b>: verify that the returned value or updated state is correct</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of level
 * management in the typing game.</p>
 *
 * @author Yangyue Qin
 */

class LevelManagerTest {

    /**
     * Tests whether addLevel stores a level that can later be retrieved by id.
     */
    @Test
    void addLevel_validLevel_addsLevelToManager() {
        LevelManager levelManager = new LevelManager();
        Level level = new Level(1, Difficulty.EASY, 60, Arrays.asList("cat"));
        levelManager.addLevel(level);
        assertEquals(level, levelManager.getLevel(1));
    }

    /**
     * Tests whether getLevel returns null when no matching level exists.
     */
    @Test
    void getLevel_returnsNull_noMatch() {
        LevelManager levelManager = new LevelManager();
        assertNull(levelManager.getLevel(88));
    }

    /**
     * Tests whether the constructor stores a provided level list correctly.
     */
    @Test
    void constructor_withLevelList_storesLevelsCorrectly() {
        Level level1 = new Level(1, Difficulty.EASY, 60, Arrays.asList("cat"));
        Level level2 = new Level(2, Difficulty.MEDIUM, 45, Arrays.asList("giraffe"));
        LevelManager levelManager = new LevelManager(Arrays.asList(level1, level2));
        assertEquals(level1, levelManager.getLevel(1));
        assertEquals(level2, levelManager.getLevel(2));
    }

    /**
     * Tests whether unlockNext advances player progress when a valid Progress object is provided.
     */
    @Test
    void unlockNextValidProgress_advancesLevel() {
        LevelManager levelManager = new LevelManager();
        Progress progress = new Progress(0, 0, new Settings(Theme.LIGHT, true));
        levelManager.unlockNext(progress);
        assertEquals(1, progress.getCurrentLevel());
    }

    /**
     * Tests whether unlockNext does nothing when the Progress object is null.
     */
    @Test
    void unlockNextNullProgress_doesNothing() {
        LevelManager levelManager = new LevelManager();
        assertDoesNotThrow(() -> levelManager.unlockNext(null));
    }
}