package ca.uwo.cs2212.demo;

/**
 * Represents a single active typing session during gameplay.
 * <br><br>
 * A {@code TypingSession} stores transient runtime data such as start time,
 * typed character count, error count, score, and current WPM. It is responsible
 * for recording player input through {@link #recordInput(char, char)},
 * applying temporary gameplay effects through {@link #applyPowerUp(PowerUp)},
 * and producing a final {@link SessionResult} through
 * {@link #computeResult()}.
 * By separating session logic from result storage, the design keeps runtime
 * state independent from long-term player statistics.
 *
 * @version 1.0
 * @author Robert Niu
 * @author Qixuan Zhu
 */
public class TypingSession {
    private static final long DOUBLE_SCORE_DURATION_MS = 5_000L;

    /**
     * The time at which this session started, measured in milliseconds.
     */
    private long startTime;

    /**
     * The total number of typed characters recorded in this session.
     */
    private int typedCount;

    /**
     * The total number of incorrect inputs recorded in this session.
     */
    private int errorCount;

    /**
     * The current score accumulated in this session.
     */
    private int score;

    /**
     * The current estimated words per minute value.
     */
    private double wpmCurrent;

    /**
     * The wall-clock time until which the bonus-score effect remains active.
     */
    private long doubleScoreExpiresAt;

    /**
     * Constructs a new TypingSession and initializes all runtime values.
     */
    public TypingSession() {
        this.startTime = System.currentTimeMillis();
        this.typedCount = 0;
        this.errorCount = 0;
        this.score = 0;
        this.wpmCurrent = 0.0;
        this.doubleScoreExpiresAt = 0L;
    }

    /**
     * Records a typed character and updates the session state.
     * <br><br>
     * Each recorded input increases the typed character count. A correct input
     * increases the score, while an incorrect input increases the error count
     * and applies a small score penalty. The current WPM is updated after
     * processing the input.
     *
     * @param ch the character typed by the player
     * @param expected the expected target character
     */
    public void recordInput(char ch, char expected) {
        typedCount++;

        if (ch == expected) {
            score += isDoubleScoreActive() ? 20 : 10;
        } else {
            errorCount++;
            score = Math.max(0, score - 2);
        }

        updateWpm();
    }

    /**
     * Applies the effect of a power-up to the current session.
     * <br><br>
     * The exact effect depends on the provided {@link PowerUp}. This
     * implementation uses the power-up name to determine a simple runtime
     * adjustment, such as a score bonus, error reduction, or speed boost.
     * If the given power-up is {@code null}, no action is taken.
     *
     * @param powerUp the power-up to apply
     */
    public void applyPowerUp(PowerUp powerUp) {
        if (powerUp == null) {
            return;
        }

        switch (powerUp.getType()) {
            case SHIELD:
                errorCount = Math.max(0, errorCount - 1);
                break;
            case DOUBLE_SCORE:
                doubleScoreExpiresAt = System.currentTimeMillis() + DOUBLE_SCORE_DURATION_MS;
                break;
            case FREEZE:
                wpmCurrent += 5.0;
                break;
        }
    }

    /**
     * Computes the final result of this session.
     * <br><br>
     * The final {@link SessionResult} includes the score, WPM, accuracy,
     * error count, and total time spent. Accuracy is calculated as the
     * percentage of correct inputs over total typed inputs. If no input has
     * been recorded, the accuracy is treated as {@code 100.0}.
     *
     * @return the final session result
     */
    public SessionResult computeResult() {
        int timeSec = (int) ((System.currentTimeMillis() - startTime) / 1000L);
        double accuracy;

        if (typedCount == 0) {
            accuracy = 100.0;
        } else {
            accuracy = ((double) (typedCount - errorCount) / typedCount) * 100.0;
        }

        updateWpm();

        return new SessionResult(score, wpmCurrent, accuracy, errorCount, timeSec);
    }

    /**
     * Returns the current score of this session.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the current WPM estimate based on elapsed time and typed input.
     * <br><br>
     * This method uses the standard approximation of five typed characters
     * per word when computing words per minute.
     */
    private void updateWpm() {
        double minutesElapsed = (System.currentTimeMillis() - startTime) / 60000.0;

        if (minutesElapsed > 0) {
            wpmCurrent = (typedCount / 5.0) / minutesElapsed;
        } else {
            wpmCurrent = 0.0;
        }
    }

    private boolean isDoubleScoreActive() {
        return System.currentTimeMillis() < doubleScoreExpiresAt;
    }
}
