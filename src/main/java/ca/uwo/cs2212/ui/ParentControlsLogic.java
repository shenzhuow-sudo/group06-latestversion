package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure helper logic used by the parent controls UI for validation and player
 * account mutations.
 */
final class ParentControlsLogic {
    /**
     * Prevents instantiation of this utility class.
     */
    private ParentControlsLogic() {
    }

    /**
     * Validates the reset-password modal input.
     *
     * @param newPassword proposed new password
     * @param confirmPassword confirmation field value
     * @return validation error message, or {@code null} when valid
     */
    static String validateResetPasswordInput(String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            return "Both password fields are required.";
        }
        if (newPassword.length() < 4) {
            return "Password must be at least 4 characters.";
        }
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        return null;
    }

    /**
     * Rebuilds a player with a new password while preserving all other saved state.
     *
     * @param actualPlayer existing player to copy
     * @param newPassword replacement password
     * @return rebuilt player instance, or {@code null} when no player was supplied
     */
    static Player rebuildPlayerWithPassword(Player actualPlayer, String newPassword) {
        if (actualPlayer == null) {
            return null;
        }
        return new Player(
                actualPlayer.getUsername(),
                newPassword,
                actualPlayer.getStatistics(),
                actualPlayer.getProgress(),
                actualPlayer.getInventory(),
                actualPlayer.getRecentActivity(),
                actualPlayer.getCoins(),
                actualPlayer.getSelectedCharacter());
    }

    /**
     * Clears accumulated statistics and recent activity for a player.
     *
     * @param actualPlayer player to reset
     */
    static void resetPlayerStatistics(Player actualPlayer) {
        if (actualPlayer == null) {
            return;
        }
        actualPlayer.getStatistics().reset();
        actualPlayer.setRecentActivity(new ArrayList<>());
    }

    /**
     * Produces display names for all loaded players.
     *
     * @param allPlayers players available in storage
     * @return array of player usernames, or a placeholder when none exist
     */
    static String[] allPlayerNames(List<Player> allPlayers) {
        List<String> names = new ArrayList<>();
        if (allPlayers != null) {
            for (Player player : allPlayers) {
                if (player != null) {
                    names.add(player.getUsername());
                }
            }
        }
        return names.isEmpty() ? new String[]{"No players found"} : names.toArray(new String[0]);
    }

    /**
     * Resets only the recorded high score for each supplied player.
     *
     * @param allPlayers players to update
     */
    static void resetHighScores(List<Player> allPlayers) {
        if (allPlayers == null) {
            return;
        }
        for (Player player : allPlayers) {
            if (player != null) {
                player.getStatistics().resetHighScore();
            }
        }
    }
}
