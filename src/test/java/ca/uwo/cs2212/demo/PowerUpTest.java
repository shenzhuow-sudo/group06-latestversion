package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PowerUp} class.
 *
 * <p>This test class verifies that the PowerUp object correctly stores
 * power-up type and count, and rejects invalid constructor input.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a PowerUp object with known input values</li>
 *     <li><b>Act</b>: call a getter method or constructor</li>
 *     <li><b>Assert</b>: verify that the returned value or exception matches expectations</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of power-up data
 * in the game.</p>
 *
 * @author Yangyue Qin
 */

class PowerUpTest {

    /**
     * Tests whether the constructor correctly stores the power-up type.
     */
    @Test
    void storeTypeCorrectly(){
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        assertEquals(PowerUpType.DOUBLE_SCORE, powerUp.getType());
    }

    /**
     * Tests whether the constructor correctly stores the power-up count.
     */
    @Test
    void storeCountCorrectly(){
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 3);
        assertEquals(3, powerUp.getCount());
    }

    /**
     * Tests whether apply throws no exception when a valid typing session is provided.
     */
    @Test
    void apply_validSession_doesNotThrowException(){
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 1);
        TypingSession typingSession = new TypingSession();
        assertDoesNotThrow(() -> powerUp.apply(typingSession));
    }

    /**
     * Test whether different power-up types can be created successfully.
     */
    @Test
    void constructor_acceptDifferentValidTypes(){
        PowerUp shield = new PowerUp(PowerUpType.SHIELD, 1);
        PowerUp freeze = new PowerUp(PowerUpType.FREEZE, 2);
        PowerUp doubleScore = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        assertEquals(PowerUpType.SHIELD, shield.getType());
        assertEquals(PowerUpType.FREEZE, freeze.getType());
        assertEquals(PowerUpType.DOUBLE_SCORE, doubleScore.getType());
    }

    /**
     * Tests whether the constructor rejects a null power-up type.
     */
    @Test
    void constructor_nullType_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> new PowerUp(null,1));
    }

    /**
     * Tests whether apply with a null session throws no exception.
     *
     * <p>This test matches the current implementation, where apply does not
     * actively validate the session parameter.</p>
     */
    @Test
    void apply_nullSession_doesNotThrowException() {
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        assertDoesNotThrow(() -> powerUp.apply(null));
    }

    /**
     * Tests whether apply does not change the stored power-up type or count.
     *
     * <p>This test matches the current minimal implementation of apply.</p>
     */
    @Test
    void apply_doesNotChangeData(){
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 2);
        TypingSession typingSession = new TypingSession();
        powerUp.apply(typingSession);
        assertEquals(PowerUpType.FREEZE, powerUp.getType());
        assertEquals(2, powerUp.getCount());
    }

    /**
     * Tests whether addCount increases the count when the input is positive.
     */
    @Test
    void addCount_positiveValue_increasesCount(){
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 2);
        powerUp.addCount(3);
        assertEquals(5, powerUp.getCount());
    }

    /**
     * Tests whether addCount does nothing when the input is 0.
     */
    @Test
    void addCount_zero_doesNothing(){
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 2);
        powerUp.addCount(0);
        assertEquals(2, powerUp.getCount());
    }

    /**
     * Tests whether addCount does nothing when the input is negative.
     */
    @Test
    void addCount_negativeValue_doesNothing(){
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 2);
        powerUp.addCount(-1);
        assertEquals(2, powerUp.getCount());
    }

    /**
     * Tests whether consumeCount decreases the count correctly.
     */
    @Test
    void consumeCount_decreasesCount(){
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 5);
        powerUp.consumeCount(2);
        assertEquals(3, powerUp.getCount());
    }

    /**
     * Tests whether consumeCount doesn't reduce the count below 0.
     */
    @Test
    void consumeCount_doesNotGoBelowZero(){
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 2);
        powerUp.consumeCount(5);
        assertEquals(0, powerUp.getCount());
    }

    /**
     * Tests whether consumeCount does nothing when the input is 0.
     */
    @Test
    void consumeCount_zero_doesNothing(){
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 4);
        powerUp.consumeCount(0);
        assertEquals(4, powerUp.getCount());
    }

    /**
     * Tests whether consumeCount does nothing when the input is negative.
     */
    @Test
    void consumeCount_negativeValue_doesNothing(){
        PowerUp powerUp = new PowerUp(PowerUpType.FREEZE, 4);
        powerUp.consumeCount(-2);
        assertEquals(4, powerUp.getCount());
    }
}
