package ca.uwo.cs2212.demo;

import java.util.List;

/**
 * Central controller responsible for managing the overall gameplay flow.
 * <br><br>
 * The {@code GameEngine} coordinates the interaction between
 * {@link TypingSession}, {@link LevelManager}, and {@link WordGenerator}.
 * It starts a game for a given {@link Player}, selects levels through
 * {@link #startLevel(int)}, and ends sessions by returning a
 * {@link SessionResult}. While persistent player data belongs to the
 * account domain, this class may temporarily reference the current player
 * and level during runtime in order to coordinate gameplay execution and
 * progression updates.
 * <p>
 * Note: the current Swing gameplay implementation in {@code ca.uwo.cs2212.ui.GameplayScreen}
 * still manages its round loop directly and does not yet delegate live gameplay
 * through this controller. As a result, this class is currently exercised mainly
 * by the model-layer tests and remains available for future controller refactoring.
 * </p>
 *
 *
 * @version 1.0
 * @author Robert Niu
 * @author Qixuan Zhu
 */
public class GameEngine {
    /**
     * The currently active typing session.
     */
    private TypingSession currentSession;

    /**
     * The level manager used to retrieve and unlock levels.
     */
    private LevelManager levelManager;

    /**
     * The word generator used to produce words during gameplay.
     */
    private WordGenerator wordGenerator;

    /**
     * A temporary runtime reference to the current player.
     */
    private Player currentPlayer;

    /**
     * A temporary runtime reference to the current level.
     */
    private Level currentLevel;

    /**
     * Constructs a game engine with the required gameplay components.
     *
     * @param levelManager the manager responsible for level retrieval and progression
     * @param wordGenerator the generator responsible for producing gameplay words
     */
    public GameEngine(LevelManager levelManager, WordGenerator wordGenerator) {
        this.levelManager = levelManager;
        this.wordGenerator = wordGenerator;
        this.currentSession = null;
        this.currentPlayer = null;
        this.currentLevel = null;
    }

    /**
     * Starts a new game for the given player.
     * <br><br>
     * This creates a new {@link TypingSession} and stores a temporary runtime
     * reference to the given player so that session results can later be
     * forwarded to the account domain.
     *
     * @param p the player starting the game
     * @throws IllegalArgumentException if the given player is {@code null}
     */
    public void startGame(Player p) {
        if (p == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        this.currentPlayer = p;
        this.currentSession = new TypingSession();
        this.currentLevel = null;
    }

    /**
     * Starts the level with the given identifier.
     * <br><br>
     * The level is retrieved through the {@link LevelManager}. If no session
     * is currently active, a new {@link TypingSession} is created.
     *
     * @param levelId the identifier of the level to start
     * @throws IllegalStateException if a game has not been started yet
     * @throws IllegalArgumentException if no matching level exists
     */
    public void startLevel(int levelId) {
        if (currentPlayer == null) {
            throw new IllegalStateException("Game must be started before starting a level.");
        }

        Level level = levelManager.getLevel(levelId);
        if (level == null) {
            throw new IllegalArgumentException("Level not found: " + levelId);
        }

        this.currentLevel = level;

        if (currentSession == null) {
            currentSession = new TypingSession();
        }
    }

    /**
     * Ends the current session and returns its final result.
     * <br><br>
     * A {@link SessionResult} is produced from the current
     * {@link TypingSession}. When possible, the result is also forwarded
     * to the current player's statistics and progression.
     * <p>
     * This method reflects the model/controller path; the desktop UI currently
     * uses a separate round-management path in {@code ca.uwo.cs2212.ui.GameplayScreen}.
     * </p>
     *
     * @return the completed session result
     * @throws IllegalStateException if there is no active session
     */
    public SessionResult endSession() {
        if (currentSession == null) {
            throw new IllegalStateException("No active session.");
        }

        SessionResult result = currentSession.computeResult();

        if (currentPlayer != null) {
            if (currentPlayer.getStatistics() != null) {
                currentPlayer.getStatistics().updateFrom(result);
            }

            if (currentPlayer.getProgress() != null && currentLevel != null) {
                if (currentPlayer.getProgress().getCurrentLevel() <= currentLevel.getLevelId()) {
                    currentPlayer.getProgress().recordCompletedLevel(
                            currentLevel.getLevelId(),
                            Math.max(1, levelManager.getLevels().size()));
                }
            }
        }

        currentSession = null;
        currentLevel = null;
        return result;
    }

    /**
     * Returns the next generated word for the current level.
     *
     * @return the next word, or an empty string if no level is active
     */
    public String getNextWord() {
        if (currentLevel == null) {
            return "";
        }
        List<String> words = wordGenerator.generateWords(currentLevel, 1);
        if (words.isEmpty()) {
            return "";
        }
        return words.get(0);
    }

    /**
     * Returns the currently active typing session.
     *
     * @return the current session, or {@code null} if no session is active
     */
    public TypingSession getCurrentSession() {
        return currentSession;
    }
}
