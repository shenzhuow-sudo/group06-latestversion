package ca.uwo.cs2212.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Persists parent-child account links to local JSON.
 * <br><br>
 * This store provides lookup, link, and unlink operations for parent-child
 * relationships backed by {@code data/parent_links.json}. It centralizes JSON
 * parsing and mutation of saved link data so the parent dashboard can work in
 * terms of usernames rather than raw file operations.
 *
 * @version 1.0
 * @author Kaiwen Li
 */
public class ParentLinkStore {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String LINKS_PATH = "data/parent_links.json";

    private final StorageService storageService;

    /**
     * Creates a store that reads and writes link data through the given
     * storage backend.
     *
     * @param storageService storage backend for local persistence
     */
    public ParentLinkStore(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Returns the linked child usernames for the given parent account.
     *
     * @param parentUsername parent account username
     * @return linked child usernames, or an empty list if none exist
     */
    public List<String> getLinkedPlayers(String parentUsername) {
        ParentLinkEntry entry = findEntry(parentUsername);
        return entry == null ? new ArrayList<>() : new ArrayList<>(entry.getLinkedPlayerUsernames());
    }

    /**
     * Adds a child username to the given parent's persisted link list.
     *
     * @param parentUsername parent account username
     * @param playerUsername child player username
     * @return {@code true} if the link was added, {@code false} if it already existed
     */
    public boolean linkPlayer(String parentUsername, String playerUsername) {
        ParentLinksData data = readData();
        ParentLinkEntry entry = ensureEntry(data, parentUsername);
        if (entry.getLinkedPlayerUsernames().contains(playerUsername)) {
            return false;
        }
        entry.getLinkedPlayerUsernames().add(playerUsername);
        writeData(data);
        return true;
    }

    /**
     * Removes a child username from the given parent's persisted link list.
     *
     * @param parentUsername parent account username
     * @param playerUsername child player username
     * @return {@code true} if the link was removed
     */
    public boolean unlinkPlayer(String parentUsername, String playerUsername) {
        ParentLinksData data = readData();
        ParentLinkEntry entry = ensureEntry(data, parentUsername);
        boolean removed = entry.getLinkedPlayerUsernames().remove(playerUsername);
        if (removed) {
            writeData(data);
        }
        return removed;
    }

    /**
     * Finds the persisted entry for the given parent username.
     *
     * @param parentUsername parent account username
     * @return matching entry, or {@code null} if none exists
     */
    private ParentLinkEntry findEntry(String parentUsername) {
        ParentLinksData data = readData();
        for (ParentLinkEntry entry : data.getParents()) {
            if (entry != null && entry.getParentUsername().equals(parentUsername)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns the persisted entry for the given parent username, creating one if needed.
     *
     * @param data root link data object
     * @param parentUsername parent account username
     * @return existing or newly created link entry
     */
    private ParentLinkEntry ensureEntry(ParentLinksData data, String parentUsername) {
        for (ParentLinkEntry entry : data.getParents()) {
            if (entry != null && entry.getParentUsername().equals(parentUsername)) {
                return entry;
            }
        }
        ParentLinkEntry created = new ParentLinkEntry(parentUsername, new ArrayList<>());
        data.getParents().add(created);
        return created;
    }

    /**
     * Reads the root parent link document from persistent storage.
     *
     * @return parsed root link data
     * @throws IllegalStateException if JSON parsing fails
     */
    private ParentLinksData readData() {
        String raw = storageService.read(LINKS_PATH);
        if (raw == null || raw.isBlank()) {
            return new ParentLinksData();
        }
        try {
            return MAPPER.readValue(raw, ParentLinksData.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse parent link data", e);
        }
    }

    /**
     * Writes the root parent link document to persistent storage.
     *
     * @param data root link data to serialize
     * @throws IllegalStateException if JSON serialization fails
     */
    private void writeData(ParentLinksData data) {
        try {
            storageService.write(LINKS_PATH, MAPPER.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to write parent link data", e);
        }
    }
}
