package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.ParentAccountEntry;

/**
 * Shared authentication and registration validation rules used by multiple
 * Swing screens.
 */
public final class AuthLogic {
    /**
     * Prevents instantiation of this utility class.
     */
    private AuthLogic() {
    }

    /**
     * Validates basic player login input.
     *
     * @param username entered username
     * @param password entered password
     * @return validation error, or {@code null} when valid
     */
    public static String validateLoginInput(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "Enter a username and password";
        }
        return null;
    }

    /**
     * Validates new-player self-registration input.
     *
     * @param username requested username
     * @param password requested password
     * @param confirmPassword confirmation password
     * @return validation error, or {@code null} when valid
     */
    public static String validateRegistrationInput(String username, String password, String confirmPassword) {
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()) {
            return "Fill in all registration fields";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (password.length() < 4) {
            return "Password must be at least 4 characters";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        return null;
    }

    /**
     * Validates player creation input entered from the parent/teacher flow.
     *
     * @param username requested username
     * @param password requested password
     * @param confirmPassword confirmation password
     * @return validation error, or {@code null} when valid
     */
    public static String validateCreatePlayerInput(String username, String password, String confirmPassword) {
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()) {
            return "Fill in username and both password fields";
        }
        if (username.length() < 3) {
            return "Username must be at least 3 characters";
        }
        if (password.length() < 4) {
            return "Password must be at least 4 characters";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
        return null;
    }

    /**
     * Validates required fields for parent login.
     *
     * @param username entered parent username
     * @param password entered parent password
     * @return validation error, or {@code null} when valid
     */
    public static String validateParentLoginInput(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "Enter parent username and password";
        }
        if (password.length() < 4) {
            return "Password must be at least 4 characters";
        }
        return null;
    }

    /**
     * Validates either an existing parent login attempt or a new parent account
     * registration gated by an access code.
     *
     * @param existingAccount existing parent account, if one already exists
     * @param password entered password
     * @param accessCode entered registration/access code
     * @param expectedAccessCode expected code for creating new parent accounts
     * @return validation error, or {@code null} when valid
     */
    public static String validateParentAccountAttempt(ParentAccountEntry existingAccount, String password, String accessCode, String expectedAccessCode) {
        if (existingAccount != null) {
            if (!password.equals(existingAccount.getPasswordHash())) {
                return "Incorrect password for that parent account";
            }
            return null;
        }
        if (!expectedAccessCode.equals(accessCode)) {
            return "Invalid registration code for new parent accounts";
        }
        return null;
    }
}
