package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Encapsulates the final outcome of a completed typing session.
 * <br><br>
 * A {@code SessionResult} stores the summary data produced at the end of a
 * {@link TypingSession}, including score, words per minute, accuracy,
 * error count, and total time spent. It acts as a lightweight result object
 * that can be passed from gameplay logic to other parts of the system, such
 * as player statistics and progression tracking, without exposing the internal
 * runtime state of the session itself.
 *
 *
 * @version 1.0
 * @author Robert Niu
 * @author Qixuan Zhu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResult {

    /**
     * The final score achieved in the session.
     */
    private int score;

    /**
     * The final words per minute value.
     */
    private double wpm;

    /**
     * The final typing accuracy percentage.
     */
    private double accuracy;

    /**
     * The total number of errors made in the session.
     */
    private int errors;

    /**
     * The total time spent in the session, measured in seconds.
     */
    private int timeSec;

    /**
     * The number of correctly completed words recorded for this session.
     */
    private int wordsTyped;

    /**
     * The date on which this session was completed.
     */
    private String completedAt;

    /**
     * Whether the session ended in a win state.
     */
    private boolean won;

    /**
     * Constructs a SessionResult with all final session metrics.
     *
     * @param score the final score
     * @param wpm the final words per minute
     * @param accuracy the final typing accuracy percentage
     * @param errors the total number of errors
     * @param timeSec the total time spent in seconds
     */
    public SessionResult(
            int score,
            double wpm,
            double accuracy,
            int errors,
            int timeSec) {
        this(score, wpm, accuracy, errors, timeSec, estimateWordsTyped(wpm, timeSec), LocalDate.now().toString(), false);
    }

    /**
     * Constructs a SessionResult with all final session metrics and an explicit completed-word count.
     *
     * @param score the final score
     * @param wpm the final words per minute
     * @param accuracy the final typing accuracy percentage
     * @param errors the total number of errors
     * @param timeSec the total time spent in seconds
     * @param wordsTyped correctly completed word count for the session
     */
    public SessionResult(
            int score,
            double wpm,
            double accuracy,
            int errors,
            int timeSec,
            int wordsTyped) {
        this(score, wpm, accuracy, errors, timeSec, wordsTyped, LocalDate.now().toString(), false);
    }

    /**
     * Constructs a SessionResult with all final session metrics and metadata.
     *
     * @param score the final score
     * @param wpm the final words per minute
     * @param accuracy the final typing accuracy percentage
     * @param errors the total number of errors
     * @param timeSec the total time spent in seconds
     * @param wordsTyped correctly completed word count for the session
     * @param completedAt session completion date
     * @param won whether the session is considered a win
     */
    public SessionResult(
            int score,
            double wpm,
            double accuracy,
            int errors,
            int timeSec,
            int wordsTyped,
            String completedAt,
            boolean won) {
        this(score, wpm, accuracy, errors, timeSec, completedAt, wordsTyped, won);
    }

    /**
     * Constructs a SessionResult with all final session metrics and metadata.
     *
     * @param score the final score
     * @param wpm the final words per minute
     * @param accuracy the final typing accuracy percentage
     * @param errors the total number of errors
     * @param timeSec the total time spent in seconds
     * @param completedAt session completion date
     * @param wordsTyped correctly completed word count for the session
     * @param won whether the session is considered a win
     */
    @JsonCreator
    public SessionResult(
            @JsonProperty("score") int score,
            @JsonProperty("wpm") double wpm,
            @JsonProperty("accuracy") double accuracy,
            @JsonProperty("errors") int errors,
            @JsonProperty("timeSec") int timeSec,
            @JsonProperty("completedAt") String completedAt,
            @JsonProperty("wordsTyped") Integer wordsTyped,
            @JsonProperty("won") Boolean won) {
        this.score = score;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.errors = errors;
        this.timeSec = timeSec;
        this.completedAt = (completedAt == null || completedAt.isBlank()) ? LocalDate.now().toString() : completedAt;
        this.wordsTyped = wordsTyped != null ? Math.max(0, wordsTyped) : estimateWordsTyped(wpm, timeSec);
        this.won = won != null && won;
    }

    /**
     * Returns the final score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the final words per minute.
     *
     * @return the wpm
     */
    public double getWpm() {
        return wpm;
    }

    /**
     * Returns the final accuracy percentage.
     *
     * @return the accuracy
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Returns the total number of errors.
     *
     * @return the errors
     */
    public int getErrors() {
        return errors;
    }

    /**
     * Returns the total time spent in the session, in seconds.
     *
     * @return elapsed time in seconds
     */
    public int getTimeSec() {
        return timeSec;
    }

    /**
     * Returns the number of correctly completed words recorded for this session.
     *
     * @return completed word count
     */
    public int getWordsTyped() {
        return wordsTyped;
    }

    /**
     * Legacy alias retained for older tests that still refer to "time spent".
     *
     * @return elapsed time in seconds
     */
    @JsonIgnore
    public double getTimeSpent() {
        return timeSec;
    }

    /**
     * Returns the completion date of this session.
     *
     * @return completion date string
     */
    public String getCompletedAt() {
        return completedAt;
    }

    /**
     * Returns whether this session ended in a win state.
     *
     * @return {@code true} if won
     */
    public boolean isWon() {
        return won;
    }

    private static int estimateWordsTyped(double wpm, int timeSec) {
        return Math.max(0, (int) Math.round(wpm * Math.max(0, timeSec) / 60.0d));
    }
}
