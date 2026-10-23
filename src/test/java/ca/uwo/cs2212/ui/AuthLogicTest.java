package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.ParentAccountEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthLogicTest {

    @Test
    void validateLoginInput_requiresBothFields() {
        assertEquals("Enter a username and password", AuthLogic.validateLoginInput("", "pw"));
        assertEquals("Enter a username and password", AuthLogic.validateLoginInput("user", ""));
        assertNull(AuthLogic.validateLoginInput("user", "pw"));
    }

    @Test
    void validateRegistrationInput_checksLengthAndMatch() {
        assertEquals("Fill in all registration fields", AuthLogic.validateRegistrationInput("", "pass", "pass"));
        assertEquals("Username must be at least 3 characters", AuthLogic.validateRegistrationInput("ab", "pass", "pass"));
        assertEquals("Password must be at least 4 characters", AuthLogic.validateRegistrationInput("user", "123", "123"));
        assertEquals("Passwords do not match", AuthLogic.validateRegistrationInput("user", "pass", "word"));
        assertNull(AuthLogic.validateRegistrationInput("user", "pass", "pass"));
    }

    @Test
    void validateCreatePlayerInput_usesCreatePlayerMessages() {
        assertEquals("Fill in username and both password fields", AuthLogic.validateCreatePlayerInput("", "pass", "pass"));
        assertEquals("Username must be at least 3 characters", AuthLogic.validateCreatePlayerInput("ab", "pass", "pass"));
        assertEquals("Password must be at least 4 characters", AuthLogic.validateCreatePlayerInput("user", "123", "123"));
        assertEquals("Passwords do not match", AuthLogic.validateCreatePlayerInput("user", "pass", "word"));
        assertNull(AuthLogic.validateCreatePlayerInput("user", "pass", "pass"));
    }

    @Test
    void validateParentLoginInput_checksMinimumPassword() {
        assertEquals("Enter parent username and password", AuthLogic.validateParentLoginInput("", "pass"));
        assertEquals("Password must be at least 4 characters", AuthLogic.validateParentLoginInput("parent", "123"));
        assertNull(AuthLogic.validateParentLoginInput("parent", "pass"));
    }

    @Test
    void validateParentAccountAttempt_distinguishesExistingAndNewAccounts() {
        ParentAccountEntry existing = new ParentAccountEntry("parent", "secret");

        assertEquals(
                "Incorrect password for that parent account",
                AuthLogic.validateParentAccountAttempt(existing, "wrong", "", "PARENT2026"));
        assertNull(AuthLogic.validateParentAccountAttempt(existing, "secret", "", "PARENT2026"));
        assertEquals(
                "Invalid registration code for new parent accounts",
                AuthLogic.validateParentAccountAttempt(null, "secret", "bad", "PARENT2026"));
        assertNull(AuthLogic.validateParentAccountAttempt(null, "secret", "PARENT2026", "PARENT2026"));
    }
}
