package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.PowerUpType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopLogicTest {

    @Test
    void purchase_cancelled_leavesPlayerStateUnchanged() {
        Player player = new Player("alice", "hash");

        ShopLogic.PurchaseResult result = ShopLogic.purchase(
                player, "Freeze", PowerUpType.FREEZE, 600, false, () -> {});

        assertEquals(ShopLogic.PurchaseStatus.CANCELLED, result.status());
        assertEquals(3000, player.getCoins());
        assertEquals(0, player.getInventory().getCount(PowerUpType.FREEZE));
    }

    @Test
    void purchase_insufficientFunds_doesNotChangeInventory() {
        Player player = new Player("alice", "hash");

        ShopLogic.PurchaseResult result = ShopLogic.purchase(
                player, "Freeze", PowerUpType.FREEZE, 4000, true, () -> {});

        assertEquals(ShopLogic.PurchaseStatus.INSUFFICIENT_FUNDS, result.status());
        assertEquals(3000, player.getCoins());
        assertEquals(0, player.getInventory().getCount(PowerUpType.FREEZE));
    }

    @Test
    void purchase_success_spendsCoinsAddsInventoryAndFormatsMessage() {
        Player player = new Player("alice", "hash");

        ShopLogic.PurchaseResult result = ShopLogic.purchase(
                player, "Shield", PowerUpType.SHIELD, 500, true, () -> {});

        assertEquals(ShopLogic.PurchaseStatus.SAVED, result.status());
        assertEquals(2500, player.getCoins());
        assertEquals(1, player.getInventory().getCount(PowerUpType.SHIELD));
        assertTrue(result.message().contains("Purchased Shield"));
        assertTrue(result.message().contains("Owned x1"));
    }

    @Test
    void purchase_saveFailure_rollsBackCoinsAndInventory() {
        Player player = new Player("alice", "hash");
        RuntimeException saveError = new IllegalStateException("disk failed");

        ShopLogic.PurchaseResult result = ShopLogic.purchase(
                player, "Double Score", PowerUpType.DOUBLE_SCORE, 750, true, () -> {
                    throw saveError;
                });

        assertEquals(ShopLogic.PurchaseStatus.SAVE_FAILED, result.status());
        assertSame(saveError, result.error());
        assertEquals(3000, player.getCoins());
        assertEquals(0, player.getInventory().getCount(PowerUpType.DOUBLE_SCORE));
    }
}
