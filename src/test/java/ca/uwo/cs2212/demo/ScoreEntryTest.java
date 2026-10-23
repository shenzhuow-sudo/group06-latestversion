package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ScoreEntry} class.
 *
 * <p>
 * This test suite verifies that the ScoreEntry class correctly:
 * <ul>
 *     <li>Stores valid constructor arguments</li>
 *     <li>Returns correct values through getter methods</li>
 *     <li>Rejects null username and null date inputs</li>
 * </ul>
 * </p>
 *
 * <p>IntelliJ was used to generate the initial test class and blank unit test placeholders.</p>
 *
 * @author Robert Niu
 */
class ScoreEntryTest {
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
     * Tests that the constructor stores valid values correctly.
     */
    @Test
    void constructor_validInputs_storesFieldsCorrectly() {
        ScoreEntry entry = new ScoreEntry("alice", 1200, "2026-03-26");

        assertEquals("alice", entry.getUsername());
        assertEquals(1200, entry.getScore());
        assertEquals("2026-03-26", entry.getDate());
    }

    /**
     * Tests that the constructor throws an exception
     * when the username is null.
     */
    @Test
    void constructor_nullUsername_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new ScoreEntry(null, 1200, "2026-03-26"));
    }

    /**
     * Tests that the constructor throws an exception
     * when the date is null.
     */
    @Test
    void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> new ScoreEntry("alice", 1200, null));
    }

    /**
     * Tests that zero score is stored correctly.
     */
    @Test
    void constructor_zeroScore_storesCorrectly() {
        ScoreEntry entry = new ScoreEntry("bob", 0, "2026-03-26");

        assertEquals("bob", entry.getUsername());
        assertEquals(0, entry.getScore());
        assertEquals("2026-03-26", entry.getDate());
    }

    /**
     * Tests that negative score values are also stored as given.
     */
    @Test
    void constructor_negativeScore_storesCorrectly() {
        ScoreEntry entry = new ScoreEntry("charlie", -50, "2026-03-26");

        assertEquals("charlie", entry.getUsername());
        assertEquals(-50, entry.getScore());
        assertEquals("2026-03-26", entry.getDate());
    }
}