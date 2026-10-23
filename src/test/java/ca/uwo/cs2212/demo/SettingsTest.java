package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Settings} class.
 *
 * <p>This test class verifies that the Settings object correctly stores theme
 * and sound preference, rejects invalid constructor input, and updates theme
 * and sound settings properly.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create a Settings object with known input values</li>
 *     <li><b>Act</b>: call a getter method, constructor, or state-changing method</li>
 *     <li><b>Assert</b>: verify that the returned value or updated state is correct</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of settings data
 * in the typing game.</p>
 *
 * @author Yangyue Qin
 */
class SettingsTest {


    /**
     * Tests whether the constructor correctly stores theme and sound preference.
     */
    @Test
    void storesFieldsCorrectly() {
        Settings settings = new Settings(Theme.LIGHT, true);
        assertEquals(Theme.LIGHT, settings.getTheme());
        assertTrue(settings.isSoundOn());
    }

    /**
     * Tests whether the constructor rejects a null theme.
     */
    @Test
    void nullThemeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Settings(null, true));
    }

    /**
     * Tests whether the constructor treats null soundOn as true.
     */
    @Test
    void nullSoundDefaultsToTrue() {
        Settings settings = new Settings(Theme.DARK, null);
        assertTrue(settings.isSoundOn());
    }

    /**
     * Tests whether the constructor stores false sound preference correctly.
     */
    @Test
    void storesFalseSoundCorrectly() {
        Settings settings = new Settings(Theme.LIGHT, false);
        assertFalse(settings.isSoundOn());
    }

    /**
     * Tests whether toggleTheme changes LIGHT to DARK.
     */
    @Test
    void toggleThemeChangesLightToDark() {
        Settings settings = new Settings(Theme.LIGHT, true);
        settings.toggleTheme();
        assertEquals(Theme.DARK, settings.getTheme());
    }

    /**
     * Tests whether toggleTheme changes DARK to LIGHT.
     */
    @Test
    void toggleThemeChangesDarkToLight() {
        Settings settings = new Settings(Theme.DARK, false);
        settings.toggleTheme();
        assertEquals(Theme.LIGHT, settings.getTheme());
    }

    /**
     * Tests whether toggleSound changes true to false.
     */
    @Test
    void toggleSoundChangesTrueToFalse() {
        Settings settings = new Settings(Theme.LIGHT, true);
        settings.toggleSound();
        assertFalse(settings.isSoundOn());
    }

    /**
     * Tests whether toggleSound changes false to true.
     */
    @Test
    void toggleSoundChangesFalseToTrue() {
        Settings settings = new Settings(Theme.DARK, false);
        settings.toggleSound();
        assertTrue(settings.isSoundOn());
    }

    /**
     * Tests whether setTheme updates the theme correctly.
     */
    @Test
    void setThemeUpdatesTheme() {
        Settings settings = new Settings(Theme.LIGHT, true);
        settings.setTheme(Theme.DARK);
        assertEquals(Theme.DARK, settings.getTheme());
    }

    /**
     * Tests whether setTheme rejects a null theme.
     */
    @Test
    void setThemeNullThrowsException() {
        Settings settings = new Settings(Theme.LIGHT, true);
        assertThrows(IllegalArgumentException.class, () -> settings.setTheme(null));
    }

    /**
     * Tests whether setSoundOn updates the sound setting correctly.
     */
    @Test
    void setSoundOnUpdatesSoundSetting() {
        Settings settings = new Settings(Theme.LIGHT, true);
        settings.setSoundOn(false);
        assertFalse(settings.isSoundOn());
    }

    /**
     * Tests whether changing sound does not affect theme.
     */
    @Test
    void changingSoundDoesNotAffectTheme() {
        Settings settings = new Settings(Theme.DARK, true);
        settings.toggleSound();
        settings.setSoundOn(true);
        assertEquals(Theme.DARK, settings.getTheme());
    }

    /**
     * Tests whether changing theme does not affect sound setting.
     */
    @Test
    void changingThemeDoesNotAffectSound() {
        Settings settings = new Settings(Theme.LIGHT, false);
        settings.toggleTheme();
        settings.setTheme(Theme.LIGHT);
        assertFalse(settings.isSoundOn());
    }
}