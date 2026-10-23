package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Player} class.
 *
 * <p>
 * This test suite verifies that the Player class correctly:
 * <ul>
 *     <li>Initializes default values in constructors</li>
 *     <li>Handles JSON constructor null inputs safely</li>
 *     <li>Manages selected character assignment</li>
 *     <li>Maintains recent activity list with size constraints</li>
 *     <li>Implements defensive copying for activity lists</li>
 *     <li>Handles coin addition and spending logic correctly</li>
 * </ul>
 * </p>
 *
 * <p>IntelliJ was used to generate the initial test class and blank unit test placeholders.</p>

 *
 * @author Robert Niu
 */
class PlayerTest {
    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass()");
    }
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
    }
    @BeforeEach
    public void setUp() {
        System.out.println("setUp()");
    }
    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
    }

    /**
     * Tests that the default constructor initializes all fields correctly.
     */
    @Test
    void constructor_initializesDefaultsCorrectly() {
        Player p = new Player("user", "hash");

        assertNotNull(p.getStatistics());
        assertNotNull(p.getProgress());
        assertNotNull(p.getInventory());
        assertNotNull(p.getRecentActivity());

        assertEquals(3000, p.getCoins());
        assertNull(p.getSelectedCharacter());
    }

    /**
     * Tests that the JSON constructor replaces null inputs with default values.
     */
    @Test
    void jsonConstructor_handlesNullFields() {
        Player p = new Player(
                "user", "hash",
                null, null, null, null,
                null, null
        );

        assertNotNull(p.getStatistics());
        assertNotNull(p.getProgress());
        assertNotNull(p.getInventory());
        assertNotNull(p.getRecentActivity());
        assertEquals(3000, p.getCoins());
    }

    /**
     * Tests that negative coin values are corrected to zero in the JSON constructor.
     */
    @Test
    void jsonConstructor_negativeCoinsBecomesZero() {
        Player p = new Player(
                "user", "hash",
                null, null, null, null,
                -100, null
        );

        assertEquals(0, p.getCoins());
    }

    /**
     * Tests selecting a character correctly updates the player's state.
     */
    @Test
    void selectCharacter_setsCharacter() {
        Player p = new Player("user", "hash");
        Character c = new Character("hero", "id1");

        p.selectCharacter(c);

        assertEquals(c, p.getSelectedCharacter());
    }

    /**
     * Tests that selecting null clears the current character.
     */
    @Test
    void selectCharacter_nullClearsSelection() {
        Player p = new Player("user", "hash");

        p.selectCharacter(null);

        assertNull(p.getSelectedCharacter());
    }

    /**
     * Tests that a session result is added to the front of the activity list.
     */
    @Test
    void addRecentActivity_addsToFront() {
        Player p = new Player("user", "hash");
        SessionResult r = new SessionResult(10, 80.0, 90.0, 2, 60);

        p.addRecentActivity(r);

        List<SessionResult> list = p.getRecentActivity();
        assertEquals(1, list.size());
        assertEquals(r, list.get(0));
    }

    /**
     * Tests that the activity list is capped at 10 entries.
     */
    @Test
    void addRecentActivity_limitsTo10() {
        Player p = new Player("user", "hash");

        for (int i = 0; i < 15; i++) {
            p.addRecentActivity(new SessionResult(i, 80.0, 90.0, 1, 60));
        }

        assertEquals(10, p.getRecentActivity().size());
    }

    /**
     * Tests replacing the recent activity list using the setter.
     */
    @Test
    void setRecentActivity_replacesList() {
        Player p = new Player("user", "hash");

        List<SessionResult> newList = List.of(
                new SessionResult(1, 90.0, 95.0, 0, 30)
        );

        p.setRecentActivity(newList);

        assertEquals(1, p.getRecentActivity().size());
        assertEquals(1, p.getRecentActivity().get(0).getScore());
    }

    /**
     * Tests that getRecentActivity returns a defensive copy.
     */
    @Test
    void getRecentActivity_returnsCopy() {
        Player p = new Player("user", "hash");
        p.addRecentActivity(new SessionResult(1, 90.0, 95.0, 0, 30));

        List<SessionResult> list = p.getRecentActivity();
        list.clear();

        assertEquals(1, p.getRecentActivity().size());
    }

    /**
     * Tests that adding positive coins increases the player's balance.
     */
    @Test
    void addCoins_increasesBalance() {
        Player p = new Player("user", "hash");

        p.addCoins(500);

        assertEquals(3500, p.getCoins());
    }

    /**
     * Tests that adding a negative amount of coins does not change the balance.
     */
    @Test
    void addCoins_negativeDoesNothing() {
        Player p = new Player("user", "hash");

        p.addCoins(-100);

        assertEquals(3000, p.getCoins());
    }

    /**
     * Tests successful coin spending.
     */
    @Test
    void spendCoins_success() {
        Player p = new Player("user", "hash");

        boolean result = p.spendCoins(500);

        assertTrue(result);
        assertEquals(2500, p.getCoins());
    }

    /**
     * Tests that spending more coins than available fails.
     */
    @Test
    void spendCoins_insufficientFunds() {
        Player p = new Player("user", "hash");

        boolean result = p.spendCoins(5000);

        assertFalse(result);
        assertEquals(3000, p.getCoins());
    }

    /**
     * Tests that invalid spend amounts are rejected.
     */
    @Test
    void spendCoins_invalidAmount() {
        Player p = new Player("user", "hash");

        assertFalse(p.spendCoins(0));
        assertFalse(p.spendCoins(-100));
    }
}