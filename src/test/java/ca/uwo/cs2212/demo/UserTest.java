package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link User} abstract class.
 *
 * <p>
 * Because {@code User} is abstract, these tests instantiate a minimal
 * concrete subclass for verification.
 * </p>
 *
 * <p>
 * This test suite verifies:
 * <ul>
 *     <li>Constructor validation for username and passwordHash</li>
 *     <li>Correct storage of username and passwordHash</li>
 *     <li>Correct password verification behavior</li>
 * </ul>
 * </p>
 *
 * <p>IntelliJ was used to generate the initial test class and blank unit test placeholders.</p>
 *
 * @author Robert Niu
 */
class UserTest {

    /**
     * Minimal concrete subclass used only for testing {@link User}.
     */
    private static class TestUser extends User {

        /**
         * Creates a test user with the given username and password hash.
         *
         * @param username the username
         * @param passwordHash the password hash
         */
        TestUser(String username, String passwordHash) {
            super(username, passwordHash);
        }
    }

    /**
     * Tests that the constructor stores valid username and password hash correctly.
     */
    @Test
    void constructor_validInputs_storesFieldsCorrectly() {
        User user = new TestUser("alice", "hashedPw");

        assertEquals("alice", user.getUsername());
        assertEquals("hashedPw", user.getPasswordHash());
    }

    /**
     * Tests that the constructor rejects a null username.
     */
    @Test
    void constructor_nullUsername_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestUser(null, "hashedPw"));
    }

    /**
     * Tests that the constructor rejects a blank username.
     */
    @Test
    void constructor_blankUsername_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestUser("   ", "hashedPw"));
    }

    /**
     * Tests that the constructor rejects a null password hash.
     */
    @Test
    void constructor_nullPasswordHash_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestUser("alice", null));
    }

    /**
     * Tests that the constructor rejects a blank password hash.
     */
    @Test
    void constructor_blankPasswordHash_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestUser("alice", "   "));
    }

    /**
     * Tests that verifyPassword returns true when the input matches
     * the stored password hash exactly.
     */
    @Test
    void verifyPassword_matchingString_returnsTrue() {
        User user = new TestUser("alice", "hashedPw");

        assertTrue(user.verifyPassword("hashedPw"));
    }

    /**
     * Tests that verifyPassword returns false when the input does not match
     * the stored password hash.
     */
    @Test
    void verifyPassword_nonMatchingString_returnsFalse() {
        User user = new TestUser("alice", "hashedPw");

        assertFalse(user.verifyPassword("wrongPw"));
    }

    /**
     * Tests that verifyPassword returns false when the input is null.
     */
    @Test
    void verifyPassword_nullInput_returnsFalse() {
        User user = new TestUser("alice", "hashedPw");

        assertFalse(user.verifyPassword(null));
    }

    /**
     * Tests that verifyPassword uses exact String equality semantics.
     */
    @Test
    void verifyPassword_caseSensitiveComparison() {
        User user = new TestUser("alice", "hashedPw");

        assertFalse(user.verifyPassword("HASHEDPW"));
    }
}