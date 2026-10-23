package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PowerUpType} enum.
 *
 * <p>This test class verifies that PowerUpType correctly converts values
 * from JSON strings and serializes enum values back to JSON.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: prepare a JSON string or enum value</li>
 *     <li><b>Act</b>: call fromJson or toJson</li>
 *     <li><b>Assert</b>: verify that the returned value or exception matches expectations</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of power-up type
 * serialization and deserialization in the game.</p>
 *
 * @author Yangyue Qin
 */
class PowerUpTypeTest {

    /**
     * Tests whether fromJson returns FREEZE correctly.
     */
    @Test
    void fromJson_returnsFreeze() {
        assertEquals(PowerUpType.FREEZE, PowerUpType.fromJson("FREEZE"));
    }

    /**
     * Tests whether fromJson returns SHIELD correctly and ignores case.
     */
    @Test
    void fromJson_returnsShieldIgnoringCase() {
        assertEquals(PowerUpType.SHIELD, PowerUpType.fromJson("shield"));
    }

    /**
     * Tests whether fromJson returns DOUBLE_SCORE correctly.
     */
    @Test
    void fromJson_returnsDoubleScore() {
        assertEquals(PowerUpType.DOUBLE_SCORE, PowerUpType.fromJson("DOUBLE_SCORE"));
    }

    /**
     * Tests whether fromJson maps BONUS_SCORE to DOUBLE_SCORE.
     */
    @Test
    void fromJson_bonusScoreMapsToDoubleScore() {
        assertEquals(PowerUpType.DOUBLE_SCORE, PowerUpType.fromJson("BONUS_SCORE"));
    }

    /**
     * Tests whether fromJson returns null for null input.
     */
    @Test
    void fromJson_nullReturnsNull() {
        assertNull(PowerUpType.fromJson(null));
    }

    /**
     * Tests whether fromJson rejects an unknown value.
     */
    @Test
    void fromJson_unknownValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                PowerUpType.fromJson("UNKNOWN"));
    }

    /**
     * Tests whether toJson returns the enum name.
     */
    @Test
    void toJson_returnsEnumName() {
        assertEquals("DOUBLE_SCORE", PowerUpType.DOUBLE_SCORE.toJson());
        assertEquals("FREEZE", PowerUpType.FREEZE.toJson());
        assertEquals("SHIELD", PowerUpType.SHIELD.toJson());
    }
}