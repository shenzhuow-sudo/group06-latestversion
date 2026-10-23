package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Level} class.
 *
 * <p>This test class verifies that the Level object correctly stores level configuration data,
 * manages its word list, and returns words appropriately.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a Level object with known input values</li>
 *     <li><b>Act</b>: call a Level method</li>
 *     <li><b>Assert</b>: verify that the returned value or updated state is correct</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of level configuration
 * in the typing game.</p>
 *
 * @author Yangyue Qin
 */
class LevelTest {

    /**
     * Test whether the constructor stores levelId, difficulty, timeLimitSec and wordList
     */
    @Test
    void storesFieldsCorrectly() {
        List<String> words = Arrays.asList("car", "deer", "world");
        Level level = new Level(1, Difficulty.EASY, 60, words);
        assertEquals(Difficulty.EASY, level.getDifficulty());
        assertEquals(1, level.getLevelId());
        assertEquals(60, level.getTimeLimitSec());
        assertEquals(words, level.getWordList());

    }

    /**
     * Tests whether getNextWord returns an empty string when the word list is empty.
     */
    @Test
    void getNextWord_emptyWordList_returnsEmptyString() {
        Level level = new Level(1, Difficulty.EASY, 60, null);
        assertEquals("", level.getNextWord());
    }

    /**
     * Tests whether getNextWord returns a word from the level word list.
     */
    @Test
    void getNextWord_nonEmptyWordList_returnsWordFromList() {
        List<String> words = Arrays.asList("cat", "dog", "deer");
        Level level = new Level(1, Difficulty.EASY, 60, words);
        String result = level.getNextWord();
        assertTrue(words.contains(result));
    }

    /**
     * Tests whether addWord adds a valid word to the word list.
     */
    @Test
    void addWord_validWord_addsToWordList() {
        Level level = new Level(1, Difficulty.EASY, 60, null);
        level.addWord("apple");
        assertEquals(1, level.getWordList().size());
        assertTrue(level.getWordList().contains("apple"));
    }

    /**
     * Tests whether addWord ignores a null word.
     */
    @Test
    void addWord_ignoreNullWord() {
        Level level = new Level(1, Difficulty.EASY, 60, null);
        level.addWord(null);
        assertTrue(level.getWordList().isEmpty());
    }

    /**
     * Tests whether addWord ignores a blank word.
     */
    @Test
    void addWord_blankWord_doesNotAddToWordList() {
        Level level = new Level(1, Difficulty.EASY, 60, null);
        level.addWord("   ");
        assertTrue(level.getWordList().isEmpty());
    }

    /**
     * Tests whether getWordList returns a copy instead of the internal list.
     */
    @Test
    void getWordList_returnsCopyOfWordList() {
        Level level = new Level(1, Difficulty.EASY, 60, Arrays.asList("cat", "dog"));
        List<String> words = level.getWordList();
        words.clear();
        assertEquals(2, level.getWordList().size());
    }
}
