package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Supported power-up types.
 *
 * @author Qixuan Zhu
 */
public enum PowerUpType {
    /**
     * Temporarily freezes a challenge or timer (game-specific behavior).
     */
    FREEZE,

    /**
     * Provides a protective effect (game-specific behavior).
     */
    SHIELD,

    /**
     * Temporarily doubles score earned from correct input (game-specific behavior).
     */
    DOUBLE_SCORE;

    @JsonCreator
    public static PowerUpType fromJson(String value) {
        if (value == null) {
            return null;
        }
        return switch (value.trim().toUpperCase()) {
            case "BONUS_SCORE", "DOUBLE_SCORE" -> DOUBLE_SCORE;
            case "FREEZE" -> FREEZE;
            case "SHIELD" -> SHIELD;
            default -> throw new IllegalArgumentException("Unknown power-up type: " + value);
        };
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
