package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Collects {@link PowerUp} entries owned by a {@link Player}.
 * <p>
 * Backed by an {@link ArrayList}. The JSON field name for persistence is {@code powerUps}.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class Inventory {

    private final List<PowerUp> powerUps;

    /**
     * Creates an empty inventory.
     */
    public Inventory() {
        this.powerUps = new ArrayList<>();
    }

    /**
     * Adds a power-up entry to this inventory.
     *
     * @param p non-null power-up
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public void add(PowerUp p) {
        Objects.requireNonNull(p, "p");
        powerUps.add(p);
    }

    /**
     * Removes a power-up entry from this inventory, if present.
     * <p>
     * Removal uses {@link List#remove(Object)} semantics (typically reference equality unless
     * {@link PowerUp} overrides {@code equals}).
     * </p>
     *
     * @param p non-null power-up to remove
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public void remove(PowerUp p) {
        Objects.requireNonNull(p, "p");
        powerUps.remove(p);
    }

    /**
     * Returns a snapshot of currently available power-ups.
     * <p>
     * The returned list is a new {@link ArrayList} instance; changes to it do not affect this
     * inventory. Use {@link #add(PowerUp)} and {@link #remove(PowerUp)} to mutate inventory state.
     * </p>
     *
     * @return a copied list of power-ups
     */
    @JsonIgnore
    public List<PowerUp> getAvailable() {
        return new ArrayList<>(powerUps);
    }

    /**
     * Returns power-ups for JSON serialization under {@code powerUps}.
     *
     * @return a defensive copy of the internal list
     */
    @JsonProperty("powerUps")
    public List<PowerUp> getPowerUps() {
        return new ArrayList<>(powerUps);
    }

    /**
     * Replaces inventory contents from JSON deserialization.
     *
     * @param items new list; {@code null} clears the inventory
     */
    @JsonProperty("powerUps")
    public void setPowerUps(List<PowerUp> items) {
        powerUps.clear();
        if (items != null) {
            powerUps.addAll(items);
        }
    }

    /**
     * Returns the owned count for the given power-up type.
     *
     * @param type power-up type
     * @return owned quantity, or {@code 0} if none
     */
    public int getCount(PowerUpType type) {
        if (type == null) {
            return 0;
        }
        for (PowerUp powerUp : powerUps) {
            if (powerUp != null && powerUp.getType() == type) {
                return Math.max(0, powerUp.getCount());
            }
        }
        return 0;
    }

    /**
     * Adds quantity for the given power-up type.
     *
     * @param type power-up type
     * @param amount quantity to add
     */
    public void addPowerUp(PowerUpType type, int amount) {
        if (type == null || amount <= 0) {
            return;
        }
        for (PowerUp powerUp : powerUps) {
            if (powerUp != null && powerUp.getType() == type) {
                powerUp.addCount(amount);
                return;
            }
        }
        powerUps.add(new PowerUp(type, amount));
    }

    /**
     * Consumes quantity for the given power-up type if enough is owned.
     *
     * @param type power-up type
     * @param amount quantity to consume
     * @return {@code true} if the inventory was updated
     */
    public boolean consumePowerUp(PowerUpType type, int amount) {
        if (type == null || amount <= 0) {
            return false;
        }
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            if (powerUp != null && powerUp.getType() == type) {
                if (powerUp.getCount() < amount) {
                    return false;
                }
                powerUp.consumeCount(amount);
                if (powerUp.getCount() <= 0) {
                    powerUps.remove(i);
                }
                return true;
            }
        }
        return false;
    }
}
