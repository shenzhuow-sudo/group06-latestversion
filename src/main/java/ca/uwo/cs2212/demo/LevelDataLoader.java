package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Loads runtime level definitions from JSON into a {@link LevelManager}.
 * <br><br>
 * This helper reads {@code levels.json} from a small set of supported runtime
 * paths and converts the JSON seed data into concrete {@link Level} instances.
 * By centralizing file parsing and manager construction in one class, the UI
 * and gameplay layers can consume a ready-to-use {@link LevelManager} without
 * duplicating JSON handling logic.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class LevelDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String[] DEFAULT_PATHS = {"src/main/resources/levels.json", "levels.json"};

    private final StorageService storageService;

    /**
     * Creates a loader that reads level data through the given storage service.
     *
     * @param storageService storage backend used to read runtime JSON
     */
    public LevelDataLoader(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Loads all configured runtime levels into a new {@link LevelManager}.
     *
     * @return a populated level manager
     * @throws IllegalStateException if no valid runtime level data can be read
     */
    public LevelManager loadLevelManager() {
        LevelsData data = readLevelsData();
        LevelManager manager = new LevelManager();
        for (LevelSeed seed : data.getLevels()) {
            if (seed == null) {
                continue;
            }
            List<String> words = seed.getWords();
            manager.addLevel(new Level(
                    seed.getLevelId(),
                    seed.getDifficulty(),
                    seed.getTimeLimitSec(),
                    words));
        }
        return manager;
    }

    /**
     * Reads and parses the root {@link LevelsData} document from the supported paths.
     *
     * @return parsed runtime level data
     * @throws IllegalStateException if parsing fails or no data source is found
     */
    private LevelsData readLevelsData() {
        for (String path : DEFAULT_PATHS) {
            String raw = storageService.read(path);
            if (raw == null || raw.isBlank()) {
                continue;
            }
            try {
                return MAPPER.readValue(raw, LevelsData.class);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to parse level data from " + path, e);
            }
        }
        throw new IllegalStateException("No levels.json data found in runtime paths.");
    }
}
