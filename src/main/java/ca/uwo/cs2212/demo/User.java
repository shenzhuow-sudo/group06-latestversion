package ca.uwo.cs2212.demo;

import java.util.Objects;

/**
 * Abstract base type for application users with stored credentials.
 * <p>
 * The stored value is named {@code passwordHash}; {@link #verifyPassword(String)} compares
 * the argument to that stored string using equality only and does not perform hashing here.
 * </p>
 *
 * @author Qixuan Zhu
 */
public abstract class User {

    private final String username;
    private final String passwordHash;

    /**
     * Creates a user with the given username and password hash string.
     *
     * @param username     non-null, non-blank username
     * @param passwordHash non-null, non-blank stored password hash string
     * @throws IllegalArgumentException if {@code username} or {@code passwordHash} is invalid
     */
    protected User(String username, String passwordHash) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must be non-null and non-blank");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must be non-null and non-blank");
        }
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the username.
     *
     * @return the username, never {@code null}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the stored password hash string.
     *
     * @return the password hash, never {@code null}
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns whether the given password string equals the stored {@code passwordHash}.
     * <p>
     * Uses {@link Objects#equals(Object, Object)} (i.e. {@link String} equality semantics);
     * no hashing or salting is performed in this method.
     * </p>
     *
     * @param pw candidate password or comparable string (caller-defined convention)
     * @return {@code true} if {@code pw} equals the stored hash string, otherwise {@code false}
     */
    public boolean verifyPassword(String pw) {
        return Objects.equals(passwordHash, pw);
    }
}
