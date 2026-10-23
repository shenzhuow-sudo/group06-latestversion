package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.PowerUpType;

/**
 * Purchase validation and transactional shop updates extracted from the screen
 * so the decision logic can be reused and unit tested.
 */
final class ShopLogic {
    /**
     * Prevents instantiation of this utility class.
     */
    private ShopLogic() {
    }

    /**
     * Enumerates the possible outcomes of attempting a shop purchase.
     */
    enum PurchaseStatus {
        CANCELLED,
        INSUFFICIENT_FUNDS,
        SAVED,
        SAVE_FAILED
    }

    /**
     * Immutable purchase outcome passed back to the UI for messaging.
     *
     * @param status result classification
     * @param message player-facing status message
     * @param error persistence error when saving fails
     */
    record PurchaseResult(PurchaseStatus status, String message, RuntimeException error) {
    }

    /**
     * Callback used by the UI to persist a successful purchase.
     */
    @FunctionalInterface
    interface SaveAction {
        /**
         * Persists the current shop/player state.
         */
        void save();
    }

    /**
     * Applies a purchase attempt including confirmation, coin deduction, inventory
     * update, and rollback if persistence fails.
     *
     * @param player player making the purchase
     * @param itemName player-facing item name
     * @param type purchased power-up type
     * @param price coin cost
     * @param confirmed whether the user confirmed the purchase
     * @param saveAction persistence callback for committing the purchase
     * @return purchase outcome for the UI
     */
    static PurchaseResult purchase(Player player, String itemName, PowerUpType type, int price, boolean confirmed, SaveAction saveAction) {
        if (!confirmed) {
            return new PurchaseResult(PurchaseStatus.CANCELLED, "Purchase cancelled", null);
        }
        if (player == null || type == null || itemName == null || itemName.isBlank() || price <= 0) {
            throw new IllegalArgumentException("Purchase requires valid player, item, and price");
        }
        if (!player.spendCoins(price)) {
            return new PurchaseResult(PurchaseStatus.INSUFFICIENT_FUNDS, "Not enough coins for " + itemName, null);
        }

        player.getInventory().addPowerUp(type, 1);
        try {
            saveAction.save();
            return new PurchaseResult(
                    PurchaseStatus.SAVED,
                    "Purchased " + itemName + "  •  Owned x" + player.getInventory().getCount(type),
                    null);
        } catch (RuntimeException ex) {
            player.addCoins(price);
            player.getInventory().consumePowerUp(type, 1);
            return new PurchaseResult(PurchaseStatus.SAVE_FAILED, "Purchase could not be saved", ex);
        }
    }
}
