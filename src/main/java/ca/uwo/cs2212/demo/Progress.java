package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Tracks the player's level progression and UI-related {@link Settings}.
 * <p>
 * Field names align with JSON: {@code levelsCompleted}, {@code currentLevel},
 * {@code levelWordCheckpoints}, {@code settings}.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class Progress {

    private int levelsCompleted;
    private int currentLevel;
    private final Map<Integer, Integer> levelWordCheckpoints;
    private final Settings settings;

    /**
     * Creates progress with the given level state and settings.
     * <p>
     * Also used by Jackson for JSON deserialization.
     * </p>
     *
     * @param levelsCompleted number of levels completed
     * @param currentLevel    current level index or identifier (as defined by the game)
     * @param settings        non-null settings snapshot
     * @throws IllegalArgumentException if {@code settings} is {@code null}
     */
    @JsonCreator
    public Progress(
            @JsonProperty("levelsCompleted") int levelsCompleted,
            @JsonProperty("currentLevel") int currentLevel,
            @JsonProperty("levelWordCheckpoints") Map<Integer, Integer> levelWordCheckpoints,
            @JsonProperty("currentLevelWordsCompleted") Integer currentLevelWordsCompleted,
            @JsonProperty("settings") Settings settings) {
        if (Objects.isNull(settings)) {
            throw new IllegalArgumentException("settings must not be null");
        }
        this.levelsCompleted = levelsCompleted;
        this.currentLevel = currentLevel;
        this.levelWordCheckpoints = new HashMap<>();
        if (levelWordCheckpoints != null) {
            for (Map.Entry<Integer, Integer> entry : levelWordCheckpoints.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    this.levelWordCheckpoints.put(Math.max(0, entry.getKey()), Math.max(0, entry.getValue()));
                }
            }
        }
        if (currentLevelWordsCompleted != null && currentLevelWordsCompleted > 0) {
            this.levelWordCheckpoints.put(Math.max(0, currentLevel), currentLevelWordsCompleted);
        }
        this.settings = settings;
    }

    /**
     * Creates progress state without any saved per-level checkpoint entries.
     *
     * @param levelsCompleted number of levels completed
     * @param currentLevel current unlocked or selected level
     * @param settings settings snapshot tied to this progress state
     */
    public Progress(int levelsCompleted, int currentLevel, Settings settings) {
        this(levelsCompleted, currentLevel, null, 0, settings);
    }

    /**
     * Advances to the next level: increments {@link #currentLevel} and increments
     * {@link #levelsCompleted} to reflect one additional completed level step.
     */
    public void advanceLevel() {
        currentLevel++;
        levelsCompleted++;
        clearLevelCheckpoint(currentLevel - 1);
    }

    /**
     * Records completion of a specific level and unlocks the next playable level when applicable.
     * <p>
     * This method is more robust than {@link #advanceLevel()} when callers may replay earlier
     * stages or resume from progress data that was edited externally. Completion counts and the
     * unlocked current level are both raised to at least the values implied by {@code completedLevel},
     * but never reduced.
     * </p>
     *
     * @param completedLevel the level just completed
     * @param maxLevel highest level available in the campaign
     */
    public void recordCompletedLevel(int completedLevel, int maxLevel) {
        int normalizedMax = Math.max(1, maxLevel);
        int normalizedCompleted = Math.max(1, Math.min(normalizedMax, completedLevel));
        levelsCompleted = Math.max(levelsCompleted, normalizedCompleted);
        currentLevel = Math.max(currentLevel, Math.min(normalizedMax, normalizedCompleted + 1));
        clearLevelCheckpoint(normalizedCompleted);
    }

    /**
     * Returns the number of levels completed.
     *
     * @return levels completed count
     */
    public int getLevelsCompleted() {
        return levelsCompleted;
    }

    /**
     * Returns the current level.
     *
     * @return current level value
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets the current level.
     *
     * @param currentLevel current level value
     */
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = Math.max(0, currentLevel);
    }

    /**
     * Returns how many words have already been completed in the current level checkpoint.
     *
     * @return saved in-level word progress
     */
    public int getCurrentLevelWordsCompleted() {
        return getLevelWordsCompleted(currentLevel);
    }

    /**
     * Updates the current in-level word checkpoint.
     *
     * @param currentLevelWordsCompleted completed words for the active level
     */
    public void setCurrentLevelWordsCompleted(int currentLevelWordsCompleted) {
        recordCurrentLevelCheckpoint(currentLevel, currentLevelWordsCompleted);
    }

    /**
     * Saves a resume checkpoint for the supplied level and completed-word count.
     *
     * @param level active level to resume
     * @param wordsCompleted words already completed within that level
     */
    public void recordCurrentLevelCheckpoint(int level, int wordsCompleted) {
        int normalizedLevel = Math.max(0, level);
        currentLevel = normalizedLevel;
        levelWordCheckpoints.put(normalizedLevel, Math.max(0, wordsCompleted));
    }

    /**
     * Returns the saved checkpoint word count for the supplied level.
     *
     * @param level level to inspect
     * @return saved completed-word count, or zero when none exists
     */
    public int getLevelWordsCompleted(int level) {
        return Math.max(0, levelWordCheckpoints.getOrDefault(Math.max(0, level), 0));
    }

    /**
     * Clears any saved in-level checkpoint for the supplied level.
     *
     * @param level level whose checkpoint should be removed
     */
    public void clearLevelCheckpoint(int level) {
        levelWordCheckpoints.remove(Math.max(0, level));
    }

    /**
     * Returns a copy of all per-level checkpoints.
     *
     * @return saved level checkpoints
     */
    public Map<Integer, Integer> getLevelWordCheckpoints() {
        return new HashMap<>(levelWordCheckpoints);
    }

    /**
     * Returns the settings associated with this progress.
     *
     * @return settings, never {@code null}
     */
    public Settings getSettings() {
        return settings;
    }
}
