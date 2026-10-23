package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Inventory} class.
 *
 * <p>This test class verifies that the Inventory object correctly stores,
 * adds, removes, and returns available power-ups.</p>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern:
 * <ul>
 *     <li><b>Arrange</b>: create an Inventory object and prepare PowerUp objects</li>
 *     <li><b>Act</b>: call an Inventory method</li>
 *     <li><b>Assert</b>: verify that the inventory state matches expectations</li>
 * </ul>
 *
 * These tests help ensure the correctness and reliability of inventory management
 * in the typing game.</p>
 *
 * @author Yangyue Qin
 */

class InventoryTest {

    /**
     * Tests whether a new inventory starts empty.
     */
    @Test
    void startWithEmptyInventory(){
        Inventory inventory = new Inventory();
        assertTrue(inventory.getAvailable().isEmpty());
    }

    /**
     * Tests whether add correctly stores a power-up in the inventory.
     */
    @Test
    void add_validPowerUp(){
        Inventory inventory = new Inventory();
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        inventory.add(powerUp);
        List<PowerUp> powerUps = inventory.getAvailable();
        assertEquals(1, powerUps.size());
        assertTrue(powerUps.contains(powerUp));
    }

    /**
     * Tests whether remove correctly removes a power-up from the inventory.
     */
    @Test
    void remove_validPowerUp(){
        Inventory inventory = new Inventory();
        PowerUp powerUp = new PowerUp(PowerUpType.SHIELD, 1);
        inventory.add(powerUp);
        inventory.remove(powerUp);
        assertTrue(inventory.getAvailable().isEmpty());
    }

    /**
     * Tests whether add throws an exception when the power-up is null.
     */
    @Test
    void add_nullPowerUp_throwException(){
        Inventory inventory = new Inventory();
        assertThrows(NullPointerException.class, () -> inventory.add(null));
    }

    /**
     * Tests whether remove throws an exception when the power-up is null.
     */
    @Test
    void remove_nullPowerUp_throwException(){
        Inventory inventory = new Inventory();
        assertThrows(NullPointerException.class, () -> inventory.remove(null));
    }

    /**
     * Tests whether getAvailable returns a copy instead of the internal list.
     */
    @Test
    void getAvailable_returnInventoryCopy(){
        Inventory inventory = new Inventory();
        PowerUp powerUp = new PowerUp(PowerUpType.DOUBLE_SCORE, 1);
        inventory.add(powerUp);
        List<PowerUp> powerUps = inventory.getAvailable();
        powerUps.clear();
        assertEquals(1, inventory.getAvailable().size());
    }
}