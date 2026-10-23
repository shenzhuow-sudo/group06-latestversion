package ca.uwo.cs2212.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the configuration data for a single game level.
 * <br><br>
 * A {@code Level} stores the identifier, difficulty, time limit, and
 * associated word list for one stage of the typing game. It acts primarily
 * as a configuration object and provides access to words that may be used
 * during gameplay. By separating level data from gameplay control, the design
 * keeps configuration concerns independent from runtime session logic.
 *
 *
 * @version 1.0
 * @author Robert Niu
 * @author Qixuan Zhu
 */
public class Level {

    /**
     * The unique identifier for this level.
     */
    private int levelId;

    /**
     * The difficulty assigned to this level.
     */
    private Difficulty difficulty;

    /**
     * The time limit for this level, measured in seconds.
     */
    private int timeLimitSec;

    /**
     * The collection of words associated with this level.
     */
    private List<String> wordList;

    /**
     * Constructs a level with the given identifier, difficulty, time limit, and words.
     *
     * @param levelId the level identifier
     * @param difficulty the difficulty of the level
     * @param timeLimitSec the time limit in seconds
     * @param wordList the words associated with this level
     */
    public Level(int levelId, Difficulty difficulty, int timeLimitSec, List<String> wordList) {
        this.levelId = levelId;
        this.difficulty = difficulty;
        this.timeLimitSec = timeLimitSec;
        this.wordList = (wordList == null) ? new ArrayList<>() : new ArrayList<>(wordList);
    }

    /**
     * Returns a word associated with this level.
     * <br><br>
     * If the word list is empty, this method returns an empty string.
     * Otherwise, one word is selected at random from the level's word list.
     *
     * @return a word from this level, or an empty string if no words exist
     */
    public String getNextWord() {
        if (wordList.isEmpty()) {
            return "";
        }

        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }

    /**
     * Adds a word to the level's word list.
     * <br><br>
     * This helper method supports initialization and testing of levels.
     *
     * @param word the word to add
     */
    public void addWord(String word) {
        if (word != null && !word.isBlank()) {
            wordList.add(word);
        }
    }

    /**
     * Returns the level identifier.
     *
     * @return the level id
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Returns the difficulty of this level.
     *
     * @return the difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the time limit in seconds.
     *
     * @return the time limit
     */
    public int getTimeLimitSec() {
        return timeLimitSec;
    }

    /**
     * Returns a copy of the word list.
     *
     * @return a copy of the level's words
     */
    public List<String> getWordList() {
        return new ArrayList<>(wordList);
    }
}