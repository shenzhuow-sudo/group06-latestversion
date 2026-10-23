package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GameEngine} class.
 *
 * <p>This test class verifies that the GameEngine correctly manages
 * gameplay flow, including starting a game, starting a level,
 * retrieving words, ending a session, and updating player state.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create the required objects and initial state</li>
 *     <li><b>Act</b>: call one GameEngine method</li>
 *     <li><b>Assert</b>: verify the expected result or exception</li>
 * </ul>
 *
 * These tests focus on the public behavior of the GameEngine as the
 * central controller of the typing game system.</p>
 *
 * @author Robert Niu
 */
class GameEngineTest {
    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass()");
    }
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
    }
    @BeforeEach
    public void setUp() {
        System.out.println("setUp()");
    }
    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
    }

    /**
     * Tests whether a newly constructed GameEngine has no active session.
     */
    @Test
    void constructor_currentSessionStartsNull() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());

        assertNull(engine.getCurrentSession(),
                "A new GameEngine should start with no active session.");
    }

    /**
     * Tests whether startGame() creates a new active session
     * when given a valid player.
     */
    @Test
    void startGame_validPlayer_createsNewSession() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);

        assertNotNull(engine.getCurrentSession(),
                "startGame() should create a new TypingSession.");
    }

    /**
     * Tests whether startGame() throws an exception
     * when the given player is null.
     */
    @Test
    void startGame_nullPlayer_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());

        assertThrows(IllegalArgumentException.class, () -> engine.startGame(null),
                "startGame(null) should throw IllegalArgumentException.");
    }

    /**
     * Tests whether startLevel() throws an exception
     * if a game has not been started yet.
     */
    @Test
    void startLevel_beforeStartGame_throwsIllegalStateException() {
        LevelManager manager = new LevelManager();
        manager.addLevel(new Level(1, Difficulty.EASY, 60, List.of("apple")));

        GameEngine engine = new GameEngine(manager, new WordGenerator());

        assertThrows(IllegalStateException.class, () -> engine.startLevel(1),
                "startLevel() before startGame() should throw IllegalStateException.");
    }

    /**
     * Tests whether startLevel() throws an exception
     * when the requested level id does not exist.
     */
    @Test
    void startLevel_invalidLevelId_throwsIllegalArgumentException() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);

        assertThrows(IllegalArgumentException.class, () -> engine.startLevel(999),
                "startLevel() with a missing level id should throw IllegalArgumentException.");
    }

    /**
     * Tests whether getNextWord() returns an empty string
     * when there is no active level.
     */
    @Test
    void getNextWord_noCurrentLevel_returnsEmptyString() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());

        assertEquals("", engine.getNextWord(),
                "getNextWord() should return an empty string when no level is active.");
    }

    /**
     * Tests whether getNextWord() returns a word from the current level
     * after a valid game and level have been started.
     */
    @Test
    void getNextWord_validCurrentLevel_returnsWordFromLevel() {
        Level level = new Level(1, Difficulty.EASY, 60, List.of("apple", "banana"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);
        engine.startLevel(1);

        String word = engine.getNextWord();

        assertTrue(List.of("apple", "banana").contains(word),
                "getNextWord() should return a word from the active level.");
    }

    /**
     * Tests whether endSession() throws an exception
     * when there is no active session.
     */
    @Test
    void endSession_noActiveSession_throwsIllegalStateException() {
        GameEngine engine = new GameEngine(new LevelManager(), new WordGenerator());

        assertThrows(IllegalStateException.class, engine::endSession,
                "endSession() with no active session should throw IllegalStateException.");
    }

    /**
     * Tests whether endSession() returns a non-null result
     * and clears the current session after a valid game flow.
     */
    @Test
    void endSession_validSession_returnsResultAndClearsSession() {
        Level level = new Level(1, Difficulty.EASY, 60, List.of("apple"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);
        engine.startLevel(1);
        engine.getCurrentSession().recordInput('a', 'a');

        SessionResult result = engine.endSession();

        assertNotNull(result, "endSession() should return a SessionResult.");
        assertNull(engine.getCurrentSession(),
                "The current session should be cleared after endSession().");
    }

    /**
     * Tests whether endSession() updates the player's high score
     * through the Statistics object.
     */
    @Test
    void endSession_updatesPlayerHighScore() {
        Level level = new Level(1, Difficulty.EASY, 60, List.of("apple"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);
        engine.startLevel(1);
        engine.getCurrentSession().recordInput('a', 'a'); // score = 10
        engine.endSession();

        assertEquals(10, player.getStatistics().getHighScore(),
                "Ending the session should update the player's high score.");
    }

    /**
     * Tests whether endSession() advances the player's progress
     * when the current level has been completed.
     */
    @Test
    void endSession_advancesPlayerProgress() {
        Level level = new Level(1, Difficulty.EASY, 60, List.of("apple"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");

        assertEquals(0, player.getProgress().getCurrentLevel(),
                "The player should initially be at level 0.");

        engine.startGame(player);
        engine.startLevel(1);
        engine.endSession();

        assertEquals(1, player.getProgress().getCurrentLevel(),
                "Ending the session on level 1 should advance the player's progress.");
    }

    /**
     * Tests whether ending a replayed earlier level does not reduce player progress.
     */
    @Test
    void endSession_replayedEarlierLevel_doesNotRegressProgress() {
        Level level1 = new Level(1, Difficulty.EASY, 60, List.of("apple"));
        Level level2 = new Level(2, Difficulty.MEDIUM, 60, List.of("banana"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level1);
        manager.addLevel(level2);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");
        player.getProgress().setCurrentLevel(3);

        engine.startGame(player);
        engine.startLevel(1);
        engine.endSession();

        assertEquals(3, player.getProgress().getCurrentLevel(),
                "Replaying an older level should not lower unlocked progress.");
    }

    /**
     * Tests whether endSession clears the current level used by getNextWord().
     */
    @Test
    void endSession_clearsCurrentLevel_wordGenerationStops() {
        Level level = new Level(1, Difficulty.EASY, 60, List.of("apple"));
        LevelManager manager = new LevelManager();
        manager.addLevel(level);

        GameEngine engine = new GameEngine(manager, new WordGenerator());
        Player player = new Player("alice", "hash");

        engine.startGame(player);
        engine.startLevel(1);
        assertFalse(engine.getNextWord().isEmpty());

        engine.endSession();

        assertEquals("", engine.getNextWord(),
                "After endSession(), the engine should have no active level.");
    }
}
