package ca.uwo.cs2212.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Helper for presenting derived gameplay statistics.
 * <p>
 * This class provides minimal, non-UI accessors to the account domain data.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class GameStatsDisplay {

    /**
     * Creates a new stats display helper.
     */
    public GameStatsDisplay() {
        // No-op.
    }

    /**
     * Returns a performance overview for the given player.
     *
     * @param p player
     * @return statistics for the player
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public Statistics getPerformanceOverview(Player p) {
        Objects.requireNonNull(p, "p");
        return p.getStatistics();
    }

    /**
     * Returns recent activity for the given player.
     *
     * @param p player
     * @return recent activity list
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public List<SessionResult> getRecentActivity(Player p) {
        Objects.requireNonNull(p, "p");
        return p.getRecentActivity();
    }

    /**
     * Returns saved progress for the given player.
     *
     * @param p player
     * @return progress for the player
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public Progress getSavedProgress(Player p) {
        Objects.requireNonNull(p, "p");
        return p.getProgress();
    }
}
