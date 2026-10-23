package ca.uwo.cs2212.demo;

import java.util.Objects;

/**
 * Represents a single score entry in the high score table.
 *
 * @author Qixuan Zhu
 */
public class ScoreEntry {

    private final String username;
    private final int score;
    private final String date;

    /**
     * Creates a score entry.
     *
     * @param username player username
     * @param score    score value
     * @param date     date string (format defined by the application)
     * @throws NullPointerException if {@code username} or {@code date} is {@code null}
     */
    public ScoreEntry(String username, int score, String date) {
        this.username = Objects.requireNonNull(username, "username");
        this.score = score;
        this.date = Objects.requireNonNull(date, "date");
    }

    /**
     * Returns the player username for this entry.
     *
     * @return username, never {@code null}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the score value.
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the date string associated with this entry.
     *
     * @return date, never {@code null}
     */
    public String getDate() {
        return date;
    }
}
