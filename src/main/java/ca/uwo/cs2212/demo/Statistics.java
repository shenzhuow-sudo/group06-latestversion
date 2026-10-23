package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;

/**
 * Aggregated typing performance metrics for a {@link Player}.
 * <p>
 * Values are suitable for JSON persistence (e.g. Jackson) using field names
 * {@code avgWPM}, {@code peakWPM}, {@code accuracy}, {@code totalTime},
 * {@code highScore}, {@code wordsTyped}, and {@code errorCount}.
 * </p>
 *
 * @author Qixuan Zhu
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Statistics {

    private double avgWPM;
    private double peakWPM;
    private double accuracy;
    private int totalTime;
    private int highScore;
    private int wordsTyped;
    private int errorCount;

    /**
     * Constructs statistics with all numeric fields initialized to zero.
     */
    public Statistics() {
        this.avgWPM = 0.0d;
        this.peakWPM = 0.0d;
        this.accuracy = 0.0d;
        this.totalTime = 0;
        this.highScore = 0;
        this.wordsTyped = 0;
        this.errorCount = 0;
    }

    /**
     * Incorporates a completed session into these cumulative statistics.
     * <p>
     * Updates {@link #highScore} and {@link #peakWPM} with maxima, adds session duration to
     * {@link #totalTime}, incorporates the session's completed-word count, and refreshes
     * {@link #avgWPM} and {@link #accuracy} using time-weighted averages when session time is positive.
     * </p>
     *
     * @param result completed session result, must not be {@code null}
     * @throws NullPointerException if {@code result} is {@code null}
     */
    public void updateFrom(SessionResult result) {
        Objects.requireNonNull(result, "result");

        int oldTotalTime = totalTime;
        int sessionSec = result.getTimeSec();
        double sessionWpm = result.getWpm();
        double sessionAccuracy = result.getAccuracy();

        highScore = Math.max(highScore, result.getScore());
        peakWPM = Math.max(peakWPM, sessionWpm);

        if (sessionSec > 0) {
            int newTotalTime = oldTotalTime + sessionSec;
            if (oldTotalTime == 0) {
                avgWPM = sessionWpm;
                accuracy = sessionAccuracy;
            } else {
                avgWPM = (avgWPM * oldTotalTime + sessionWpm * sessionSec) / newTotalTime;
                accuracy = (accuracy * oldTotalTime + sessionAccuracy * sessionSec) / newTotalTime;
            }
            totalTime = newTotalTime;
        }
        wordsTyped += Math.max(0, result.getWordsTyped());
        errorCount += Math.max(0, result.getErrors());
    }

    /**
     * Resets all tracked metrics to zero.
     */
    public void reset() {
        avgWPM = 0.0d;
        peakWPM = 0.0d;
        accuracy = 0.0d;
        totalTime = 0;
        highScore = 0;
        wordsTyped = 0;
        errorCount = 0;
    }

    /**
     * Returns the running average words per minute.
     *
     * @return average WPM
     */
    public double getAvgWPM() {
        return avgWPM;
    }

    /**
     * Returns the peak session WPM observed.
     *
     * @return peak WPM
     */
    public double getPeakWPM() {
        return peakWPM;
    }

    /**
     * Returns the running average accuracy percentage.
     *
     * @return accuracy percentage
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Returns cumulative play time in seconds.
     *
     * @return total time in seconds
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Returns the highest score recorded across incorporated sessions.
     *
     * @return the high score
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Returns the cumulative words typed (estimated from session WPM and duration).
     *
     * @return total words typed
     */
    public int getWordsTyped() {
        return wordsTyped;
    }

    /**
     * Returns the cumulative incorrect keystroke count across incorporated sessions.
     *
     * @return total error count
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * Resets only the recorded high score.
     */
    public void resetHighScore() {
        highScore = 0;
    }
}
