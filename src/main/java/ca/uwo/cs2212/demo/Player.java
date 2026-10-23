package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A standard game account that holds persistent player data: {@link Statistics},
 * {@link Progress}, {@link Inventory}, and an optional {@link Character} selection.
 * <p>
 * JSON persistence for {@code players.json} expects fields such as {@code username},
 * {@code passwordHash}, {@code statistics}, {@code progress}, {@code inventory},
 * {@code recentActivity}, and {@code selectedCharacter} on concrete player types.
 * </p>
 *
 * @author Qixuan Zhu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player extends User {
    private static final int DEFAULT_COINS = 3000;

    private final Statistics statistics;
    private final Progress progress;
    private final Inventory inventory;
    private final List<SessionResult> recentActivity;
    private int coins;
    private Character selectedCharacter;

    /**
     * Creates a player with the given credentials and default nested state.
     * <p>
     * Initializes {@link #statistics} with a new {@link Statistics} instance (all zeros),
     * {@link #inventory} with an empty {@link Inventory}, and {@link #progress} with
     * {@code levelsCompleted = 0}, {@code currentLevel = 0}, and default {@link Settings}
     * ({@link Theme#LIGHT}, sound on). {@link #selectedCharacter} starts as {@code null}.
     * </p>
     *
     * @param username     non-blank username (see {@link User})
     * @param passwordHash non-blank stored password hash (see {@link User})
     * @throws IllegalArgumentException if {@code username} or {@code passwordHash} is invalid
     */
    public Player(String username, String passwordHash) {
        super(username, passwordHash);
        this.statistics = new Statistics();
        this.inventory = new Inventory();
        this.progress = new Progress(0, 0, new Settings(Theme.LIGHT, true));
        this.recentActivity = new ArrayList<>();
        this.coins = DEFAULT_COINS;
        this.selectedCharacter = null;
    }

    /**
     * Jackson constructor for {@code players.json} deserialization.
     *
     * @param username          username
     * @param passwordHash      password hash
     * @param statistics        statistics aggregate; if {@code null}, defaults to empty {@link Statistics}
     * @param progress          progress; if {@code null}, defaults to new {@link Progress}(0,0,default settings)
     * @param inventory         inventory; if {@code null}, defaults to empty {@link Inventory}
     * @param recentActivity    recent session history; if {@code null}, defaults to empty list
     * @param coins             player currency balance; if {@code null}, defaults to starter coins
     * @param selectedCharacter optional character selection
     */
    @JsonCreator
    public Player(
            @JsonProperty("username") String username,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("statistics") Statistics statistics,
            @JsonProperty("progress") Progress progress,
            @JsonProperty("inventory") Inventory inventory,
            @JsonProperty("recentActivity") List<SessionResult> recentActivity,
            @JsonProperty("coins") Integer coins,
            @JsonProperty("selectedCharacter") Character selectedCharacter) {
        super(username, passwordHash);
        this.statistics = statistics != null ? statistics : new Statistics();
        this.inventory = inventory != null ? inventory : new Inventory();
        this.progress = progress != null
                ? progress
                : new Progress(0, 0, new Settings(Theme.LIGHT, true));
        this.recentActivity = recentActivity != null ? new ArrayList<>(recentActivity) : new ArrayList<>();
        this.coins = coins != null ? Math.max(0, coins) : DEFAULT_COINS;
        this.selectedCharacter = selectedCharacter;
    }

    /**
     * Sets the selected character, or clears the selection.
     * <p>
     * {@code selectedCharacter} is optional ({@code 0..1}); {@code null} clears the selection.
     * </p>
     *
     * @param c character to select, or {@code null} for none
     */
    public void selectCharacter(Character c) {
        selectedCharacter = c;
    }

    /**
     * Returns the selected character, if any.
     *
     * @return selected character, or {@code null} if none
     */
    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    /**
     * Returns this player's aggregate statistics.
     *
     * @return statistics, never {@code null}
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Returns this player's level progress and settings holder.
     *
     * @return progress, never {@code null}
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * Returns this player's power-up inventory.
     *
     * @return inventory, never {@code null}
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns this player's recent session history.
     *
     * @return defensive copy of recent activity
     */
    public List<SessionResult> getRecentActivity() {
        return new ArrayList<>(recentActivity);
    }

    /**
     * Replaces recent activity history from JSON deserialization.
     *
     * @param results new activity list; {@code null} clears the history
     */
    @JsonProperty("recentActivity")
    public void setRecentActivity(List<SessionResult> results) {
        recentActivity.clear();
        if (results != null) {
            recentActivity.addAll(results);
        }
    }

    /**
     * Appends a completed session result to this player's history.
     * Keeps only the most recent 10 sessions.
     *
     * @param result completed session result
     */
    public void addRecentActivity(SessionResult result) {
        if (result == null) {
            return;
        }
        recentActivity.add(0, result);
        while (recentActivity.size() > 10) {
            recentActivity.remove(recentActivity.size() - 1);
        }
    }

    /**
     * Returns the player's coin balance.
     *
     * @return current coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Adds coins to the player's balance.
     *
     * @param amount coins to add
     */
    public void addCoins(int amount) {
        if (amount > 0) {
            coins += amount;
        }
    }

    /**
     * Attempts to spend coins from the player's balance.
     *
     * @param amount coins to spend
     * @return {@code true} if successful
     */
    public boolean spendCoins(int amount) {
        if (amount <= 0 || coins < amount) {
            return false;
        }
        coins -= amount;
        return true;
    }
}
