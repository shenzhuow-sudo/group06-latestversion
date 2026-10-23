package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Root DTO for {@code players.json} with shape {@code { "players": [ ... ] }}.
 *
 * @author Qixuan Zhu
 */
public class PlayersData {

    @JsonProperty("players")
    private List<Player> players = new ArrayList<>();

    /**
     * Creates an empty players container.
     */
    public PlayersData() {
        // Default list initialized on field.
    }

    /**
     * Returns the list of players.
     *
     * @return players list, never {@code null}
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players.
     *
     * @param players list to store; {@code null} is treated as empty
     */
    public void setPlayers(List<Player> players) {
        this.players = players != null ? players : new ArrayList<>();
    }
}
