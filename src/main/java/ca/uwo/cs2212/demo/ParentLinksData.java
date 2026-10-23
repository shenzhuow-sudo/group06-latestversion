package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Root DTO for parent-child link persistence.
 * <br><br>
 * This object mirrors the top-level JSON structure stored in
 * {@code parent_links.json}, exposing the persisted list of
 * {@link ParentLinkEntry} values under the {@code parents} field.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentLinksData {
    @JsonProperty("parents")
    private List<ParentLinkEntry> parents = new ArrayList<>();

    /**
     * Returns the persisted parent-child link entries.
     *
     * @return parent link list
     */
    public List<ParentLinkEntry> getParents() {
        return parents;
    }

    /**
     * Replaces the persisted parent-child link entries.
     *
     * @param parents new link list; {@code null} resets to empty
     */
    public void setParents(List<ParentLinkEntry> parents) {
        this.parents = parents != null ? parents : new ArrayList<>();
    }
}
