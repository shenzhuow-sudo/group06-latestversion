package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TypingSession} class.
 *
 * <p>This test class verifies that the TypingSession object correctly record
 * player's input, updates score and errors, applies power up effects, and conputes
 * the final.result. It checks both normal gameplay and edge behaviour to insure the
 * session logic works as expected.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a TypingSession object and prepare any required input.</li>
 *     <li><b>Act</b>: call a TypingSession method</li>
 *     <li><b>Assert</b>: verify the session state or returned result is correct</li>
 * </ul>
 *
 * These tests ensure the correctness and reliability of gameplay session logic.</p>
 *
 * @author Yangyue Qin
 */

class TypingSessionTest {

    /**
     * Tests whether correct input increases the score.
     *
     * <p>This test verifies that recording a correct character adds points to correct
     * session score.</p>
     */
    @Test
    void recordInput_correctChar_increasesScore(){
        TypingSession typingSession = new TypingSession();
        typingSession.recordInput('a', 'a');
        assertEquals(10, typingSession.getScore());
    }

    /**
     * Tests whether incorrect input increases the error count.
     *
     * <p>This test verifies that recording an incorrect character updates the 
     * final session result with 1 error.</p>
     */
    @Test
    void recordInput_incorrectChar_increasesErrors(){
        TypingSession typingSession = new TypingSession();
        typingSession.recordInput('a', 'b');
        SessionResult sessionResult = typingSession.computeResult();
        assertEquals(1, sessionResult.getErrors());
    }

    /**
     * Tests whether incorrect input doesn't reduce the score below 0.
     *
     * <p>This test is a boundary test to ensure that the score remains non-negative.</p>
     */
    @Test
    void recordInput_incorrectChar_remainsNonNegativeScore(){
        TypingSession typingSession = new TypingSession();
        typingSession.recordInput('a', 'b');
        assertEquals(0, typingSession.getScore());
    }

    /**
     * Tests whether applying a DOUBLE_SCORE power-up increases the score.
     *
     * <p>This test verifies that the power-up DOUBLE_SCORE doubles points     
     * correctly.</p>
     */
    @Test
    void applyPowerUp_doubleScore_increasesScore(){
        TypingSession typingSession = new TypingSession();
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        typingSession.applyPowerUp(powerUp);
        typingSession.recordInput('a', 'a');
        typingSession.recordInput('b', 'b');
        assertEquals(40, typingSession.getScore());
    }

    /**
     * Tests whether applying a DOUBLE_SCORE power-up increases the score temporarily.
     * 
     * <p>This test verifies that the power-up DOUBLE_SCORE doubles points     
     * temporarily.</p>
     */
    @Test
    void applyPowerUp_doubleScore_temporarily(){
        TypingSession typingSession = new TypingSession();
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        typingSession.applyPowerUp(powerUp);
        typingSession.recordInput('a', 'a');
        
        assertEquals(20, typingSession.getScore());
    }

    /**
     * Tests whether applying a SHIELD power-up reduces the error count.
     *
     * <p>This test verifies that the power-up SHIELD reduces error count by 1
     * to current session score.</p>
     */
    @Test
    void applyPowerUp_shield_reducesErrors(){
        TypingSession typingSession = new TypingSession();
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 1);

        typingSession.recordInput('a', 'b');
        typingSession.applyPowerUp(powerUp);
        SessionResult sessionResult = typingSession.computeResult();
        assertEquals(0, sessionResult.getErrors());
    }

    /**
     * Tests whether applying a FREEZE power-up increases the words per minute by 5.
     *
     * <p>This test verifies that the power-up FREEZE adds 5.0 to wpm for current
     * session.</p>
     */
    @Test
    void applyPowerUp_freeze_doesNotThrpwException() {
        TypingSession typingSession = new TypingSession();
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 1);
        typingSession.applyPowerUp(powerUp);
        assertDoesNotThrow(() -> typingSession.applyPowerUp(powerUp));
    }

    /**
     * Tests whether applying a null power-up leaves the session unchanged.
     *
     * <p>This test verifies that the passing null to applyPowerUp doesn't throw
     * an exception to current session score.</p>
     */
    @Test
    void applyPowerUp_nullPowerUp_unchangesScore(){
        TypingSession typingSession = new TypingSession();
        typingSession.applyPowerUp(null);
        assertEquals(0, typingSession.getScore());
    }

    /**
     * Tests whether computeResult returns the expected score and error count.
     *
     * <p>This test verifies that the session result correctly reflects gameplay
     * action recorded during the session.</p>
     */
    @Test
    void computeResult_returnsExpectedValues() {
        TypingSession typingSession = new TypingSession();

        typingSession.recordInput('a', 'a');
        typingSession.recordInput('b', 'c');
        SessionResult sessionResult = typingSession.computeResult();

        assertEquals(8, sessionResult.getScore());
        assertEquals(1, sessionResult.getErrors());
    }

    /**
     * Tests whether computeResult returns 100 accuracy when no input was recorded.
     *
     * <p>This is a boundary test to ensure that an empty typing session is treated
     * as having 100% accuracy by design.</p>
     */
    @Test
    void computeResult_noInput_returnsFullAccuracy(){
        TypingSession typingSession = new TypingSession();
        SessionResult sessionResult = typingSession.computeResult();
        assertEquals(100.0, sessionResult.getAccuracy(), 0.0001);
    }
}