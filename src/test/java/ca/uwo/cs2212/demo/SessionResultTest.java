package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SessionResult} class.
 *
 * <p>
 * This test suite verifies:
 * <ul>
 *     <li>Correct initialization via the numeric constructors (including fixed {@code won=false} for 5/6-arg forms)</li>
 *     <li>{@link SessionResult#SessionResult(int, double, double, int, int, String, Integer, Boolean)} JSON-style constructor</li>
 *     <li>Default value handling for {@code completedAt} and {@code won}</li>
 *     <li>Getter methods return expected values</li>
 * </ul>
 * </p>
 *
 * <p>IntelliJ was used to generate the initial test class and blank unit test placeholders.</p>
 *
 * @author Robert Niu
 */
class SessionResultTest {
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
     * Tests the 5-arg constructor: delegates to today's date, {@code won} is always false.
     */
    @Test
    void constructor_setsDefaultsCorrectly() {
        SessionResult r = new SessionResult(100, 80.0, 90.0, 2, 60);

        assertEquals(100, r.getScore());
        assertEquals(80.0, r.getWpm());
        assertEquals(90.0, r.getAccuracy());
        assertEquals(2, r.getErrors());
        assertEquals(60, r.getTimeSec());

        assertNotNull(r.getCompletedAt());
        assertFalse(r.isWon());
    }

    /**
     * Tests that the 5-arg constructor always yields {@code won == false} regardless of accuracy.
     */
    @Test
    void constructor_fiveArg_alwaysNotWon() {
        SessionResult r = new SessionResult(100, 80.0, 80.0, 2, 60);

        assertFalse(r.isWon());
    }

    /**
     * Tests JSON-style constructor with all values provided (8 parameters).
     */
    @Test
    void jsonConstructor_allFieldsProvided() {
        SessionResult r = new SessionResult(
                200, 75.0, 88.0, 3, 70,
                "2024-01-01",
                12,
                true
        );

        assertEquals(200, r.getScore());
        assertEquals(75.0, r.getWpm());
        assertEquals(88.0, r.getAccuracy());
        assertEquals(3, r.getErrors());
        assertEquals(70, r.getTimeSec());
        assertEquals("2024-01-01", r.getCompletedAt());
        assertEquals(12, r.getWordsTyped());
        assertTrue(r.isWon());
    }

    /**
     * Tests JSON constructor fallback for null completedAt (defaults to today).
     */
    @Test
    void jsonConstructor_nullCompletedAt_defaultsToToday() {
        SessionResult r = new SessionResult(
                100, 70.0, 90.0, 1, 50,
                null,
                5,
                true
        );

        assertNotNull(r.getCompletedAt());
        assertFalse(r.getCompletedAt().isBlank());
    }

    /**
     * Tests JSON constructor: null {@code won} is treated as false (no accuracy inference).
     */
    @Test
    void jsonConstructor_nullWon_defaultsToFalse() {
        SessionResult r = new SessionResult(
                100, 70.0, 90.0, 1, 50,
                "2024-01-01",
                3,
                null
        );

        assertFalse(r.isWon());
    }

    /**
     * Tests JSON constructor: null {@code won} stays false even when accuracy is low.
     */
    @Test
    void jsonConstructor_nullWon_staysFalseWithLowAccuracy() {
        SessionResult r = new SessionResult(
                100, 70.0, 80.0, 1, 50,
                "2024-01-01",
                2,
                null
        );

        assertFalse(r.isWon());
    }

    /**
     * Tests that blank completedAt is replaced with today's date string.
     */
    @Test
    void jsonConstructor_blankCompletedAt_defaultsToToday() {
        SessionResult r = new SessionResult(
                100, 70.0, 90.0, 1, 50,
                "",
                4,
                true
        );

        assertNotNull(r.getCompletedAt());
        assertFalse(r.getCompletedAt().isBlank());
    }

    /**
     * Tests getter methods return correct values (JSON-style 8-arg constructor).
     */
    @Test
    void getters_returnCorrectValues() {
        SessionResult r = new SessionResult(
                150, 65.5, 87.2, 4, 80,
                "2024-05-01",
                9,
                false
        );

        assertEquals(150, r.getScore());
        assertEquals(65.5, r.getWpm());
        assertEquals(87.2, r.getAccuracy());
        assertEquals(4, r.getErrors());
        assertEquals(80, r.getTimeSec());
        assertEquals("2024-05-01", r.getCompletedAt());
        assertEquals(9, r.getWordsTyped());
        assertFalse(r.isWon());
    }
}
