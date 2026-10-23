package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Character} class.
 *
 * <p>This test class verifies that the Character object correctly stores
 * character’s name and appearance id, and rejects invalid constructor input.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a Character object with known input values</li>
 *     <li><b>Act</b>: call a getter method or constructor</li>
 *     <li><b>Assert</b>: verify that the returned value or exception matches expectations</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of character
 * selection data in the typing game.</p>
 *
 * @author Yangyue Qin
 */

class CharacterTest {

    /**
     * Tests whether the constructor correctly stores the character name
     * and appearance id.
     */
    @Test
    void constructor_storesCorrectly() {
        Character character = new Character("Hunter", "hunter_lucian");

        assertEquals("Hunter", character.getName());
        assertEquals("hunter_lucian", character.getAppearanceId());
    }

    /**
     * Tests whether the constructor rejects a null name.
     */
    @Test
    void constructor_nullName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Character(null, "hunter_lucian"));
    }

    /**
     * Tests whether the constructor rejects a blank name.
     */
    @Test
    void constructor_blankName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Character(" ", "hunter_lucian"));
    }

    /**
     * Tests whether the constructor rejects a null appearance id.
     */
    @Test
    void constructor_nullAppearanceId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Character("Hunter", null));
    }

    /**
     * Tests whether the constructor rejects a blank appearance id.
     */
    @Test
    void constructor_blankAppearanceId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Character("Hunter", ""));
    }

}