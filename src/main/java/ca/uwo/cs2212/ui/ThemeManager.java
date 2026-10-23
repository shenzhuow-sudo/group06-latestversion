package ca.uwo.cs2212.ui;

/**
 * Stores the currently selected UI theme for desktop screens.
 */
public class ThemeManager {
    private static Theme currentTheme = Theme.DARK;

    /**
     * Returns the active UI theme.
     *
     * @return current theme
     */
    public static Theme getTheme() {
        return currentTheme;
    }

    /**
     * Updates the active UI theme.
     *
     * @param theme theme to apply
     */
    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    /**
     * Returns whether the active UI theme is dark mode.
     *
     * @return {@code true} when the current theme is {@link Theme#DARK}
     */
    public static boolean isDark() {
        return currentTheme == Theme.DARK;
    }
}
