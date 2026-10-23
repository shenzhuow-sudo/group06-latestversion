package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SessionResult;

import java.time.LocalDate;

/**
 * Shared gameplay calculations kept separate from Swing code so they can be
 * reused and tested without constructing UI classes.
 */
final class GameplayLogic {
    /**
     * Prevents instantiation of this utility class.
     */
    private GameplayLogic() {
    }

    /**
     * Calculates the score reward for a correctly typed word.
     *
     * @param word typed target word
     * @param currentLevel active level number
     * @param doubleScoreActive whether the double-score power-up is active
     * @return score to award for the word
     */
    static int calculateWordScore(String word, int currentLevel, boolean doubleScoreActive) {
        if (word == null || word.isBlank()) {
            return 0;
        }
        int baseScore = 100 + word.length() * 10 * currentLevel;
        return doubleScoreActive ? baseScore * 2 : baseScore;
    }

    /**
     * Converts defeated word count into normalized level progress.
     *
     * @param defeatedWords number of enemies defeated
     * @param targetWords number of defeats required to clear the level
     * @return progress value clamped to the range {@code [0, 1]}
     */
    static float levelProgress(int defeatedWords, int targetWords) {
        if (targetWords <= 0) {
            return 0f;
        }
        return Math.min(1f, defeatedWords / (float) targetWords);
    }

    /**
     * Builds a stable session summary from the current round counters.
     *
     * @param score final score for the round
     * @param elapsed elapsed round time in seconds
     * @param submittedWords number of submitted word attempts
     * @param correctWords number of correctly completed words
     * @param defeatedWords number of enemies defeated
     * @param targetWords number of defeats required to win
     * @return immutable session result snapshot
     */
    static SessionResult buildSessionResult(int score, int elapsed, int submittedWords, int correctWords, int defeatedWords, int targetWords) {
        int safeElapsed = Math.max(1, elapsed);
        double wpm = correctWords <= 0 ? 0.0 : (correctWords * 60.0) / safeElapsed;
        double accuracy = submittedWords <= 0 ? 0.0 : (correctWords * 100.0) / submittedWords;
        int errors = Math.max(0, submittedWords - correctWords);
        boolean won = levelProgress(defeatedWords, targetWords) >= 1f;
        return new SessionResult(
                score,
                wpm,
                accuracy,
                errors,
                safeElapsed,
                correctWords,
                LocalDate.now().toString(),
                won);
    }

    /**
     * Decides whether a session contains enough activity to be recorded.
     *
     * @param result computed session result
     * @param submittedWords number of submitted word attempts
     * @param defeatedWords number of enemies defeated
     * @param score current score
     * @return {@code true} when the session reflects actual gameplay
     */
    static boolean hasMeaningfulSession(SessionResult result, int submittedWords, int defeatedWords, int score) {
        return result.getTimeSec() > 1 || submittedWords > 0 || defeatedWords > 0 || score > 0;
    }

    /**
     * Applies the finished round to player persistence state.
     *
     * @param player player being updated
     * @param result computed session result
     * @param submittedWords number of submitted word attempts
     * @param defeatedWords number of enemies defeated
     * @param score final score
     * @param currentLevel level that was played
     * @param maxLevel highest playable level
     * @param winCoinReward coins awarded on a win
     */
    static void applySessionOutcome(Player player, SessionResult result, int submittedWords, int defeatedWords, int score,
                                    int currentLevel, int maxLevel, int winCoinReward) {
        if (player == null || result == null) {
            throw new IllegalArgumentException("Player and result are required");
        }
        if (hasMeaningfulSession(result, submittedWords, defeatedWords, score)) {
            player.addRecentActivity(result);
            player.getStatistics().updateFrom(result);
        }
        if (result.isWon()) {
            player.addCoins(winCoinReward);
            player.getProgress().recordCompletedLevel(currentLevel, maxLevel);
        } else {
            player.getProgress().recordCurrentLevelCheckpoint(currentLevel, defeatedWords);
        }
    }
}
