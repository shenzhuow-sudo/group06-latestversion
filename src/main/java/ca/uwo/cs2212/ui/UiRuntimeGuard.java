package ca.uwo.cs2212.ui;

import javax.swing.*;
import java.awt.*;
import java.io.UncheckedIOException;

/**
 * Centralizes UI-safe handling for runtime storage/configuration failures and
 * theme conversion between model and UI enums.
 */
final class UiRuntimeGuard {
    /**
     * Prevents instantiation of this utility class.
     */
    private UiRuntimeGuard() {
    }

    /**
     * Returns whether the supplied runtime exception should be treated as a data
     * access problem that the UI can explain to the user.
     *
     * @param ex runtime exception thrown by storage or loading code
     * @return {@code true} when the exception represents a data access failure
     */
    static boolean isDataAccessFailure(RuntimeException ex) {
        return ex instanceof IllegalStateException || ex instanceof UncheckedIOException;
    }

    /**
     * Shows a standard dialog for local data/configuration access failures.
     *
     * @param parent parent component for the dialog
     * @param action short human-readable description of the failed action
     * @param ex underlying runtime exception
     */
    static void showDataAccessError(Component parent, String action, RuntimeException ex) {
        JOptionPane.showMessageDialog(
                parent,
                "The game could not " + action + " because a local JSON file is missing, damaged, or unreadable.\n"
                        + "Check data/players.json and src/main/resources/levels.json before trying again.",
                "Data Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Converts a persisted model theme into the UI theme enum.
     *
     * @param theme persisted model theme
     * @return UI theme equivalent, defaulting to dark when missing
     */
    static Theme toUiTheme(ca.uwo.cs2212.demo.Theme theme) {
        if (theme == null) {
            return Theme.DARK;
        }
        return Theme.valueOf(theme.name());
    }

    /**
     * Converts a UI theme value back into the persisted model enum.
     *
     * @param theme UI theme
     * @return model theme equivalent, defaulting to dark when missing
     */
    static ca.uwo.cs2212.demo.Theme toModelTheme(Theme theme) {
        if (theme == null) {
            return ca.uwo.cs2212.demo.Theme.DARK;
        }
        return ca.uwo.cs2212.demo.Theme.valueOf(theme.name());
    }
}
