package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Stores user-configurable game settings.
 *
 * @author Qixuan Zhu
 */
public class Settings {

    private boolean soundOn;
    private Theme theme;

    /**
     * Creates settings with the given theme and sound preference.
     * <p>
     * Also used by Jackson for JSON deserialization.
     * </p>
     *
     * @param theme theme to apply, must not be {@code null}
     * @param soundOn whether sound is enabled
     * @throws IllegalArgumentException if {@code theme} is {@code null}
     */
    @JsonCreator
    public Settings(
            @JsonProperty("theme") Theme theme,
            @JsonProperty("soundOn") Boolean soundOn) {
        if (Objects.isNull(theme)) {
            throw new IllegalArgumentException("theme must not be null");
        }
        this.theme = theme;
        this.soundOn = soundOn == null || soundOn;
    }

    /**
     * Toggles the theme between {@link Theme#LIGHT} and {@link Theme#DARK}.
     */
    public void toggleTheme() {
        theme = (theme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
    }

    /**
     * Toggles the sound preference between enabled and disabled.
     */
    public void toggleSound() {
        soundOn = !soundOn;
    }

    /**
     * Returns the current theme.
     *
     * @return current theme
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Sets the current theme.
     *
     * @param theme new theme value
     */
    public void setTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("theme must not be null");
        }
        this.theme = theme;
    }

    /**
     * Returns whether sound is enabled.
     *
     * @return {@code true} if sound is enabled, otherwise {@code false}
     */
    public boolean isSoundOn() {
        return soundOn;
    }

    /**
     * Sets whether sound is enabled.
     *
     * @param soundOn {@code true} to enable sound, {@code false} to disable it
     */
    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}
