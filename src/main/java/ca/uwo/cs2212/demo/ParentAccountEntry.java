package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Persistent parent account entry.
 * <br><br>
 * A {@code ParentAccountEntry} stores the minimal saved data for one
 * parent/teacher login account: username and password hash. It is used as the
 * JSON element type inside {@link ParentAccountsData}.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentAccountEntry {
    private final String username;
    private final String passwordHash;

    /**
     * Creates a persistent parent account entry from JSON-backed values.
     *
     * @param username saved parent username
     * @param passwordHash saved password hash
     */
    @JsonCreator
    public ParentAccountEntry(
            @JsonProperty("username") String username,
            @JsonProperty("passwordHash") String passwordHash) {
        this.username = username == null ? "" : username;
        this.passwordHash = passwordHash == null ? "" : passwordHash;
    }

    /**
     * Returns the saved parent username.
     *
     * @return username value
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the saved password hash.
     *
     * @return password hash value
     */
    public String getPasswordHash() {
        return passwordHash;
    }
}
