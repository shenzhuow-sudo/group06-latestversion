package ca.uwo.cs2212.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Stores and manages high score entries.
 *
 * @author Qixuan Zhu
 */
public class HighScoreTable {

    /**
     * The stored high score entries.
     */
    private final List<ScoreEntry> entries;

    /**
     * Creates an empty high score table.
     */
    public HighScoreTable() {
        this.entries = new ArrayList<>();
    }

    /**
     * Adds a score entry for the given username.
     *
     * @param username player username
     * @param score    score value
     * @throws NullPointerException if {@code username} is {@code null}
     */
    public void addScore(String username, int score) {
        Objects.requireNonNull(username, "username");
        String date = LocalDate.now().toString();
        entries.add(new ScoreEntry(username, score, date));
    }

    /**
     * Returns the top {@code n} entries by score.
     * <p>
     * Minimal skeleton behavior: returns a new list containing up to {@code n}
     * entries sorted by descending score.
     * </p>
     *
     * @param n number of top entries to return
     * @return list of top entries
     */
    public List<ScoreEntry> getTop(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }

        ArrayList<ScoreEntry> copy = new ArrayList<>(entries);
        copy.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        int limit = Math.min(n, copy.size());
        return new ArrayList<>(copy.subList(0, limit));
    }

    /**
     * Clears all stored high score entries.
     */
    public void reset() {
        entries.clear();
    }
}
