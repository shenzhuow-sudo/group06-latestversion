package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Root DTO for parent account persistence.
 * <br><br>
 * This object mirrors the top-level JSON structure stored in
 * {@code parent_accounts.json}, exposing a list of {@link ParentAccountEntry}
 * values under the {@code parents} field.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentAccountsData {
    @JsonProperty("parents")
    private List<ParentAccountEntry> parents = new ArrayList<>();

    /**
     * Returns the persisted parent account entries.
     *
     * @return parent account list
     */
    public List<ParentAccountEntry> getParents() {
        return parents;
    }

    /**
     * Replaces the persisted parent account entries.
     *
     * @param parents new account list; {@code null} resets to empty
     */
    public void setParents(List<ParentAccountEntry> parents) {
        this.parents = parents != null ? parents : new ArrayList<>();
    }
}
