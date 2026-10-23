package ca.uwo.cs2212.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all available levels and controls level retrieval and progression.
 * <br><br>
 * The {@code LevelManager} stores a collection of {@link Level} objects and
 * provides methods to retrieve a level by its identifier and unlock the next
 * level based on a player's {@link Progress}. By centralizing level storage
 * and progression logic in one class, the design keeps {@link GameEngine}
 * focused on gameplay flow rather than level management.
 *
 *
 * @version 1.0
 * @author Robert Niu
 * @author Qixuan Zhu
 */
public class LevelManager {

    /**
     * The collection of levels managed by this object.
     */
    private List<Level> levels;

    /**
     * Constructs a LevelManager with an empty level collection.
     */
    public LevelManager() {
        this.levels = new ArrayList<>();
    }

    /**
     * Constructs a LevelManager with the given level collection.
     *
     * @param levels levels to manage
     */
    public LevelManager(List<Level> levels) {
        this.levels = (levels == null) ? new ArrayList<>() : new ArrayList<>(levels);
    }

    /**
     * Returns the level with the given identifier.
     * <br><br>
     * If no level with the specified id exists, this method returns
     * {@code null}.
     *
     * @param id the identifier of the requested level
     * @return the matching level, or {@code null} if none is found
     */
    public Level getLevel(int id) {
        for (Level level : levels) {
            if (level.getLevelId() == id) {
                return level;
            }
        }
        return null;
    }

    /**
     * Unlocks the next level using the given progress object.
     * <br><br>
     * This method delegates progression control to {@link Progress}.
     * If the given progress object is {@code null}, no action is taken.
     *
     * @param progress the player's progress information
     */
    public void unlockNext(Progress progress) {
        if (progress != null) {
            progress.recordCompletedLevel(progress.getCurrentLevel(), levels.size());
        }
    }

    /**
     * Adds a level to the manager.
     * <br><br>
     * This helper method supports initialization of the level collection.
     *
     * @param level the level to add
     */
    public void addLevel(Level level) {
        if (level != null) {
            levels.add(level);
        }
    }

    /**
     * Returns a copy of the level collection.
     *
     * @return a copy of the managed levels
     */
    public List<Level> getLevels() {
        return new ArrayList<>(levels);
    }
}
