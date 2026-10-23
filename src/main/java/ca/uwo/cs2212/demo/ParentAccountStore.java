package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * Persists parent accounts to local JSON storage.
 * <br><br>
 * This store provides simple lookup and save operations for parent/teacher
 * login accounts backed by {@code data/parent_accounts.json}. It isolates JSON
 * parsing and file I/O from the UI layer so parent login screens only need to
 * work with {@link ParentAccountEntry} values.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentAccountStore {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ACCOUNTS_PATH = "data/parent_accounts.json";

    private final StorageService storageService;

    /**
     * Creates a store that reads and writes parent account data through the
     * given storage backend.
     *
     * @param storageService storage backend for local persistence
     */
    public ParentAccountStore(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Loads a saved parent account by username.
     *
     * @param username target parent username
     * @return matching account entry, or {@code null} if not found
     */
    public ParentAccountEntry load(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        ParentAccountsData data = readData();
        for (ParentAccountEntry entry : data.getParents()) {
            if (entry != null && username.equals(entry.getUsername())) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Saves or replaces a parent account entry.
     *
     * @param username saved parent username
     * @param passwordHash saved password hash
     */
    public void save(String username, String passwordHash) {
        Objects.requireNonNull(username, "username");
        ParentAccountsData data = readData();
        data.getParents().removeIf(entry -> entry != null && username.equals(entry.getUsername()));
        data.getParents().add(new ParentAccountEntry(username, passwordHash));
        writeData(data);
    }

    /**
     * Reads the root parent account document from persistent storage.
     *
     * @return parsed root data object
     * @throws IllegalStateException if JSON parsing fails
     */
    private ParentAccountsData readData() {
        String raw = storageService.read(ACCOUNTS_PATH);
        if (raw == null || raw.isBlank()) {
            return new ParentAccountsData();
        }
        try {
            return MAPPER.readValue(raw, ParentAccountsData.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse parent accounts", e);
        }
    }

    /**
     * Writes the root parent account document to persistent storage.
     *
     * @param data root data to serialize
     * @throws IllegalStateException if JSON serialization fails
     */
    private void writeData(ParentAccountsData data) {
        try {
            storageService.write(ACCOUNTS_PATH, MAPPER.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to write parent accounts", e);
        }
    }
}
