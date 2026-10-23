package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Lightweight representation of a playable or selectable character.
 * <p>
 * Field names match JSON: {@code name}, {@code appearanceId}.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class Character {

    private final String name;
    private final String appearanceId;

    /**
     * Creates a character with a display name and asset identifier.
     * <p>
     * Also used by Jackson for JSON deserialization.
     * </p>
     *
     * @param name         non-null, non-blank display name
     * @param appearanceId non-null, non-blank appearance / sprite key
     * @throws IllegalArgumentException if {@code name} or {@code appearanceId} is invalid
     */
    @JsonCreator
    public Character(
            @JsonProperty("name") String name,
            @JsonProperty("appearanceId") String appearanceId) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("name must be non-null and non-blank");
        }
        if (Objects.isNull(appearanceId) || appearanceId.isBlank()) {
            throw new IllegalArgumentException("appearanceId must be non-null and non-blank");
        }
        this.name = name;
        this.appearanceId = appearanceId;
    }

    /**
     * Returns the character display name.
     *
     * @return name, never {@code null}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the appearance or asset identifier for this character.
     *
     * @return appearance id, never {@code null}
     */
    public String getAppearanceId() {
        return appearanceId;
    }
}
