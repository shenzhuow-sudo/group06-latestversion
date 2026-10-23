package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents an owned power-up and its available quantity.
 * <p>
 * Field names align with JSON: {@code type}, {@code count}.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class PowerUp {

    private final PowerUpType type;
    private int count;

    /**
     * Creates a power-up entry with the given type and count.
     * <p>
     * Also used by Jackson for JSON deserialization.
     * </p>
     *
     * @param type  power-up type, must not be {@code null}
     * @param count available quantity
     * @throws IllegalArgumentException if {@code type} is {@code null}
     */
    @JsonCreator
    public PowerUp(
            @JsonProperty("type") PowerUpType type,
            @JsonProperty("count") int count) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("type must not be null");
        }
        this.type = type;
        this.count = count;
    }

    /**
     * Applies this power-up to the given typing session.
     * <p>
     * Minimal implementation: no-op by design (behavior to be defined by the game engine).
     * </p>
     *
     * @param session target typing session
     */
    public void apply(TypingSession session) {
        // Intentionally left minimal per design documentation.
    }

    /**
     * Returns the power-up type.
     *
     * @return type
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * Returns the quantity of this power-up.
     *
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Increases this power-up quantity.
     *
     * @param delta amount to add
     */
    public void addCount(int delta) {
        if (delta > 0) {
            count += delta;
        }
    }

    /**
     * Decreases this power-up quantity without going below zero.
     *
     * @param delta amount to remove
     */
    public void consumeCount(int delta) {
        if (delta > 0) {
            count = Math.max(0, count - delta);
        }
    }
}
