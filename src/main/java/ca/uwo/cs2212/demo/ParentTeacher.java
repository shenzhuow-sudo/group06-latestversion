package ca.uwo.cs2212.demo;

import java.util.List;

/**
 * Administrative {@link Player} role with account-management operations.
 * <p>
 * In addition to standard player credentials, this account type carries an
 * {@code accessCode} used for privileged flows (exact policy is application-defined).
 * This helper operates on the same persisted player data used by the desktop UI so
 * administrative actions remain consistent with what leaderboard and dashboard screens display.
 * </p>
 *
 * @author Qixuan Zhu
 */
public class ParentTeacher extends Player {

    private final String accessCode;

    /**
     * Creates a parent/teacher account with credentials and an access code.
     *
     * @param username     non-blank username (see {@link User})
     * @param passwordHash non-blank stored password hash (see {@link User})
     * @param accessCode   non-blank access code for this role
     * @throws IllegalArgumentException if {@code username}, {@code passwordHash}, or {@code accessCode} is invalid
     */
    public ParentTeacher(String username, String passwordHash, String accessCode) {
        super(username, passwordHash);
        if (accessCode == null || accessCode.isBlank()) {
            throw new IllegalArgumentException("accessCode must be non-null and non-blank");
        }
        this.accessCode = accessCode;
    }

    /**
     * Returns the access code associated with this parent/teacher account.
     *
     * @return access code, never {@code null}
     */
    public String getAccessCode() {
        return accessCode;
    }

    /**
     * Registers or persists a new player account.
     *
     * @param username chosen username
     * @param pw       plain or hashed password (convention defined by the application)
     * @throws IllegalArgumentException if input is invalid or username already exists
     */
    public void createAccount(String username, String pw) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must be non-null and non-blank");
        }
        if (pw == null || pw.isBlank()) {
            throw new IllegalArgumentException("pw must be non-null and non-blank");
        }

        SaveManager saveManager = buildSaveManager();
        if (saveManager.loadPlayer(username) != null) {
            throw new IllegalArgumentException("player username already exists: " + username);
        }
        saveManager.savePlayer(new Player(username, pw));
    }

    /**
     * Resets another player's password.
     *
     * @param playerUsername username of the target player
     * @param newPw          replacement password or hash (caller-defined convention)
     * @throws IllegalArgumentException if input is invalid or target player does not exist
     */
    public void resetPassword(String playerUsername, String newPw) {
        if (playerUsername == null || playerUsername.isBlank()) {
            throw new IllegalArgumentException("playerUsername must be non-null and non-blank");
        }
        if (newPw == null || newPw.isBlank()) {
            throw new IllegalArgumentException("newPw must be non-null and non-blank");
        }

        SaveManager saveManager = buildSaveManager();
        Player existing = saveManager.loadPlayer(playerUsername);
        if (existing == null) {
            throw new IllegalArgumentException("player not found: " + playerUsername);
        }

        Player updated = new Player(
                existing.getUsername(),
                newPw,
                existing.getStatistics(),
                existing.getProgress(),
                existing.getInventory(),
                existing.getRecentActivity(),
                existing.getCoins(),
                existing.getSelectedCharacter());
        saveManager.savePlayer(updated);
    }

    /**
     * Resets statistics for the named player.
     *
     * @param playerUsername username of the target player
     * @throws IllegalArgumentException if input is invalid or target player does not exist
     */
    public void resetStatistics(String playerUsername) {
        if (playerUsername == null || playerUsername.isBlank()) {
            throw new IllegalArgumentException("playerUsername must be non-null and non-blank");
        }

        SaveManager saveManager = buildSaveManager();
        Player existing = saveManager.loadPlayer(playerUsername);
        if (existing == null) {
            throw new IllegalArgumentException("player not found: " + playerUsername);
        }

        existing.getStatistics().reset();
        saveManager.savePlayer(existing);
    }

    /**
     * Clears persisted high scores for all saved players.
     */
    public void resetHighScoreTable() {
        SaveManager saveManager = buildSaveManager();
        List<Player> allPlayers = saveManager.loadAllPlayers();
        for (Player player : allPlayers) {
            if (player != null && player.getStatistics() != null) {
                player.getStatistics().resetHighScore();
            }
        }
        saveManager.saveAll(allPlayers);
    }

    private SaveManager buildSaveManager() {
        return new SaveManager(new FileStorageService());
    }
}
