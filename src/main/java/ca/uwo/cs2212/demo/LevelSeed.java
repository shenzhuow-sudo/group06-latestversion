package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON seed data for constructing runtime {@link Level} values.
 * <br><br>
 * A {@code LevelSeed} mirrors one entry from {@code levels.json}, including
 * the level identifier, difficulty, time limit, and raw word list. It acts as
 * a transport object between Jackson deserialization and concrete
 * {@link Level} creation performed by {@link LevelDataLoader}.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class LevelSeed {
    private final int levelId;
    private final Difficulty difficulty;
    private final int timeLimitSec;
    private final List<String> words;

    /**
     * Creates a seed entry from deserialized JSON values.
     *
     * @param levelId level identifier
     * @param difficulty difficulty tier for the level
     * @param timeLimitSec level time limit in seconds
     * @param words source words associated with the level
     */
    @JsonCreator
    public LevelSeed(
            @JsonProperty("levelId") int levelId,
            @JsonProperty("difficulty") Difficulty difficulty,
            @JsonProperty("timeLimitSec") int timeLimitSec,
            @JsonProperty("words") List<String> words) {
        this.levelId = levelId;
        this.difficulty = difficulty;
        this.timeLimitSec = timeLimitSec;
        this.words = words != null ? new ArrayList<>(words) : new ArrayList<>();
    }

    /**
     * Returns the level identifier.
     *
     * @return level id
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Returns the configured difficulty tier.
     *
     * @return difficulty value
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the configured time limit in seconds.
     *
     * @return time limit in seconds
     */
    public int getTimeLimitSec() {
        return timeLimitSec;
    }

    /**
     * Returns a defensive copy of the seed word list.
     *
     * @return copied list of words
     */
    public List<String> getWords() {
        return new ArrayList<>(words);
    }
}
