package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link WordGenerator} class.
 *
 * <p>This test class verifies that the WordGenerator correctly generates words
 * based on the given level and difficulty settings. It checks that the generator
 * returns words from expected difficulty range, handles invalid input safely,
 * and falls back to the original word list when no words match the requested
 * difficulty.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a WordGenerator object and prepare a Level with known words</li>
 *     <li><b>Act</b>: call the generateWords method with a specific difficulty and count</li>
 *     <li><b>Assert</b>: verify the returned words matches the expected behaviour</li>
 * </ul>
 *
 * These tests ensure the correctness and reliability of the word generator and difficulty-based filtering.</p>
 *
 * @author Yangyue Qin
 */

class WordGeneratorTest {

    /**
     * Tests whether generateWords returns an empty list when the level is null.
     *
     * <p> This is a boundary test to ensure that the method handles a null
     * level safely without throwing errors</p>
     */
    @Test
    void generateWords_nullLevel_returnsEmptyList() {
        WordGenerator wordGenerator = new WordGenerator();
        List<String> result = wordGenerator.generateWords(null, 3);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests whether generateWords returns an empty list when the request count is 0.
     *
     * <p> This is a boundary test to ensure that the method handles non-positive counts
     * safely without throwing errors</p>
     */
    @Test
    void generateWords_nonPositiveCount_returnEmptyList() {
        WordGenerator wordGenerator = new WordGenerator();
        Level level = new Level(1, Difficulty.EASY, 60, Arrays.asList("dog", "cat", "deer"));
        List<String> result = wordGenerator.generateWords(level, 0);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests whether generateWords returns easy words list when the difficulty is easy.
     *
     * <p> This test verifies that all returned words have length less than or equal to 5,
     * which matches the easy difficulty.</p>
     */
    @Test
    void generateWords_EASY_returnEasyWords() {
        WordGenerator wordGenerator = new WordGenerator();
        Level level = new Level(1, Difficulty.EASY, 60,
                Arrays.asList("dog", "cat", "deer", "apple", "giraffe"));
        List<String> result = wordGenerator.generateWords(level, 10);
        assertFalse(result.isEmpty());
        for(String word : result) {
            assertTrue(word.length() <= 5);
        }
    }

    /**
     * Tests whether generateWords returns medium words list when the difficulty is medium.
     *
     * <p> This test verifies that all returned words have length between 6 and 8,
     * which matches the medium difficulty.</p>
     */
    @Test
    void generateWords_MEDIUM_returnMediumWords() {
        WordGenerator wordGenerator = new WordGenerator();
        Level level = new Level(2, Difficulty.MEDIUM, 60,
                Arrays.asList("dog", "banana", "deer", "apple", "giraffe"));
        List<String> result = wordGenerator.generateWords(level, 10);
        assertFalse(result.isEmpty());
        for(String word : result) {
            assertTrue(word.length() >= 6 && word.length() <= 8);
        }
    }

    /**
     * Tests whether generateWords returns long words list when the difficulty is hard.
     *
     * <p> This test verifies that all returned words have length more than or equal to 9,
     * which matches the hard difficulty.</p>
     */
    @Test
    void generateWords_HARD_returnHardWords() {
        WordGenerator wordGenerator = new WordGenerator();
        Level level = new Level(3, Difficulty.HARD, 60,
                Arrays.asList("character", "banana", "deer", "apple", "giraffe"));
        List<String> result = wordGenerator.generateWords(level, 10);
        assertFalse(result.isEmpty());
        for(String word : result) {
            assertTrue(word.length() >= 9);
        }
    }

    /**
     * Tests whether generateWords falls back to the original word list when
     * no words match the requested difficulty.
     *
     * <p>This test ensures that the fallback logic works correctly and still
     * returns valid words instead of an empty list.</p>
     */
    @Test
    void generateWords_noMatchingDifficultyWords_fallsBackToOriginalList() {
        WordGenerator wordGenerator = new WordGenerator();
        List<String> words = Arrays.asList("cat", "dog", "sun");
        Level level = new Level(3, Difficulty.HARD, 60, words);

        List<String> result = wordGenerator.generateWords(level, 5);

        assertEquals(5, result.size());
        for (String word : result) {
            assertTrue(words.contains(word));
        }
    }

    /**
     * Tests whether recently generated words are avoided while alternatives exist.
     */
    @Test
    void generateWords_repeatedSingleRequests_avoidRecentRepeats() {
        WordGenerator wordGenerator = new WordGenerator();
        Level level = new Level(1, Difficulty.EASY, 60,
                Arrays.asList("cat", "dog", "sun"));

        String first = wordGenerator.generateWords(level, 1).get(0);
        String second = wordGenerator.generateWords(level, 1).get(0);
        String third = wordGenerator.generateWords(level, 1).get(0);

        assertNotEquals(first, second);
        assertNotEquals(second, third);
        assertNotEquals(first, third);
    }

    /**
     * Tests whether null and blank words are excluded from fallback generation.
     */
    @Test
    void generateWords_fallbackSkipsNullAndBlankWords() {
        WordGenerator wordGenerator = new WordGenerator();
        List<String> expected = Arrays.asList("cat", "dog");
        Level level = new Level(4, Difficulty.HARD, 60,
                Arrays.asList(null, "", "   ", "cat", "dog"));

        List<String> result = wordGenerator.generateWords(level, 6);

        assertEquals(6, result.size());
        for (String word : result) {
            assertTrue(expected.contains(word));
        }
    }

    /**
     * Tests whether requesting the full easy pool returns each easy word before repeating.
     */
    @Test
    void generateWords_fullPoolRequest_returnsUniqueWordsBeforeRepeating() {
        WordGenerator wordGenerator = new WordGenerator();
        List<String> words = Arrays.asList("ant", "bird", "crab");
        Level level = new Level(5, Difficulty.EASY, 60, words);

        List<String> result = wordGenerator.generateWords(level, 3);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().distinct().count());
        for (String word : result) {
            assertTrue(words.contains(word));
        }
    }
}
