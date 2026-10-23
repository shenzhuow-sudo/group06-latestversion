package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Root DTO for runtime level data loaded from {@code levels.json}.
 * <br><br>
 * This container mirrors the top-level JSON object structure used by runtime
 * level configuration, exposing a {@code levels} array of {@link LevelSeed}
 * entries for Jackson serialization and deserialization.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class LevelsData {

    @JsonProperty("levels")
    private List<LevelSeed> levels = new ArrayList<>();

    /**
     * Returns the list of level seed entries.
     *
     * @return configured level seeds
     */
    public List<LevelSeed> getLevels() {
        return levels;
    }

    /**
     * Replaces the list of level seed entries.
     *
     * @param levels new seed list; {@code null} resets to an empty list
     */
    public void setLevels(List<LevelSeed> levels) {
        this.levels = levels != null ? levels : new ArrayList<>();
    }
}
