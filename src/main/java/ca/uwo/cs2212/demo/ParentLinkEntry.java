package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores linked child usernames for a parent account.
 * <br><br>
 * A {@code ParentLinkEntry} represents one saved parent-to-child mapping,
 * identified by the parent username and backed by a list of linked player
 * usernames. It is used as the element type inside {@link ParentLinksData}.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentLinkEntry {
    private final String parentUsername;
    private final List<String> linkedPlayerUsernames;

    /**
     * Creates a parent-child link entry from JSON-backed values.
     *
     * @param parentUsername parent account username
     * @param linkedPlayerUsernames linked child usernames
     */
    @JsonCreator
    public ParentLinkEntry(
            @JsonProperty("parentUsername") String parentUsername,
            @JsonProperty("linkedPlayerUsernames") List<String> linkedPlayerUsernames) {
        this.parentUsername = parentUsername == null ? "" : parentUsername;
        this.linkedPlayerUsernames = linkedPlayerUsernames != null ? new ArrayList<>(linkedPlayerUsernames) : new ArrayList<>();
    }

    /**
     * Returns the owning parent username.
     *
     * @return parent username
     */
    public String getParentUsername() {
        return parentUsername;
    }

    /**
     * Returns the mutable list of linked child usernames.
     *
     * @return linked child usernames
     */
    public List<String> getLinkedPlayerUsernames() {
        return linkedPlayerUsernames;
    }
}
