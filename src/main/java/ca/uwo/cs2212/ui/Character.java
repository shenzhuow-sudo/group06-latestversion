package ca.uwo.cs2212.ui;

import java.util.Objects;

/**
 * Lightweight UI-facing avatar descriptor used by menu and dashboard screens.
 */
public class Character {
    private final String name;
    private final String appearanceId;

    /**
     * Creates a UI character option with a display name and appearance key.
     *
     * @param name display name shown in the UI
     * @param appearanceId asset or appearance identifier
     */
    public Character(String name, String appearanceId) {
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
     * Returns the display name for this avatar option.
     *
     * @return avatar name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the appearance identifier used to resolve avatar artwork.
     *
     * @return appearance id
     */
    public String getAppearanceId() {
        return appearanceId;
    }
}
