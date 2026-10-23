package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates saving and loading of player account data using a {@link StorageService}.
 * <p>
 * Persists a {@link PlayersData} document to JSON at a configured path. This round-trip supports
 * concrete {@link Player} instances only (no {@link ParentTeacher} polymorphism in JSON).
 * </p>
 *
 * @author Qixuan Zhu
 */
public class SaveManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final StorageService storage;
    private final String playersJsonPath;

    /**
     * Creates a save manager using the provided storage service and default {@code data/players.json} path.
     *
     * @param storage non-null storage service
     * @throws NullPointerException if {@code storage} is {@code null}
     */
    public SaveManager(StorageService storage) {
        this(storage, "data/players.json");
    }

    /**
     * Creates a save manager using the provided storage service and players file path.
     *
     * @param storage         non-null storage service
     * @param playersJsonPath path to the JSON file (e.g. {@code data/players.json})
     * @throws NullPointerException if {@code storage} or {@code playersJsonPath} is {@code null}
     */
    public SaveManager(StorageService storage, String playersJsonPath) {
        this.storage = Objects.requireNonNull(storage, "storage");
        this.playersJsonPath = Objects.requireNonNull(playersJsonPath, "playersJsonPath");
    }

    /**
     * Loads a player by username from persistent storage.
     *
     * @param username username to load
     * @return matching player, or {@code null} if not found
     * @throws NullPointerException if {@code username} is {@code null}
     */
    public Player loadPlayer(String username) {
        Objects.requireNonNull(username, "username");
        PlayersData data = readPlayersData();
        List<Player> list = data.getPlayers();
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (Player p : list) {
            if (p != null && username.equals(p.getUsername())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Saves or updates a single player in persistent storage.
     * <p>
     * Replaces an existing entry with the same username, or appends if none exists.
     * </p>
     *
     * @param p player to save
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public void savePlayer(Player p) {
        Objects.requireNonNull(p, "p");
        PlayersData data = readPlayersData();
        List<Player> list = new ArrayList<>();
        if (data.getPlayers() != null) {
            list.addAll(data.getPlayers());
        }
        String uname = p.getUsername();
        for (Iterator<Player> it = list.iterator(); it.hasNext(); ) {
            Player existing = it.next();
            if (existing != null && uname.equals(existing.getUsername())) {
                it.remove();
                break;
            }
        }
        list.add(p);
        data.setPlayers(list);
        writePlayersData(data);
    }

    /**
     * Replaces all players in persistent storage with the given list.
     *
     * @param players list of players; {@code null} is treated as empty
     */
    public void saveAll(List<Player> players) {
        PlayersData data = new PlayersData();
        data.setPlayers(players != null ? new ArrayList<>(players) : new ArrayList<>());
        writePlayersData(data);
    }

    /**
     * Loads all players from persistent storage.
     *
     * @return all stored players
     */
    public List<Player> loadAllPlayers() {
        PlayersData data = readPlayersData();
        if (data.getPlayers() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(data.getPlayers());
    }

    private PlayersData readPlayersData() {
        String raw = storage.read(playersJsonPath);
        if (raw == null || raw.isBlank()) {
            return new PlayersData();
        }
        try {
            return MAPPER.readValue(raw, PlayersData.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse players JSON", e);
        }
    }

    private void writePlayersData(PlayersData data) {
        try {
            String json = MAPPER.writeValueAsString(data);
            storage.write(playersJsonPath, json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to write players JSON", e);
        }
    }
}
