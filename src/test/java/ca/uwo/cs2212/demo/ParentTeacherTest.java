package ca.uwo.cs2212.demo;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ParentTeacher} class.
 *
 * <p>
 * This test suite verifies that the ParentTeacher class correctly:
 * <ul>
 *     <li>Rejects invalid constructor arguments</li>
 *     <li>Stores and returns the access code correctly</li>
 *     <li>Validates inputs for account-management methods</li>
 *     <li>Allows resetting the high score table without throwing exceptions</li>
 * </ul>
 * </p>
 *
 * <p>IntelliJ was used to generate the initial test class and blank unit test placeholders.</p>

 *
 * @author Robert Niu
 */
class ParentTeacherTest {
    private Path playersFile;
    private String originalPlayersJson;

    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass()");
    }
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
    }
    @BeforeEach
    public void setUp() throws IOException {
        System.out.println("setUp()");
        playersFile = locatePlayersFile();
        originalPlayersJson = Files.exists(playersFile)
                ? Files.readString(playersFile, StandardCharsets.UTF_8)
                : null;
    }
    @AfterEach
    public void tearDown() throws IOException {
        System.out.println("tearDown()");
        if (originalPlayersJson == null) {
            Files.deleteIfExists(playersFile);
        } else {
            Files.createDirectories(playersFile.getParent());
            Files.writeString(playersFile, originalPlayersJson, StandardCharsets.UTF_8);
        }
    }

    /**
     * Tests that the constructor stores valid values correctly.
     */
    @Test
    void constructor_validInputs_createsObject() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertNotNull(pt);
        assertEquals("ACCESS123", pt.getAccessCode());
        assertEquals("parentUser", pt.getUsername());
    }

    /**
     * Tests that the constructor rejects a null access code.
     */
    @Test
    void constructor_nullAccessCode_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new ParentTeacher("parentUser", "hashedPw", null));
    }

    /**
     * Tests that the constructor rejects a blank access code.
     */
    @Test
    void constructor_blankAccessCode_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new ParentTeacher("parentUser", "hashedPw", "   "));
    }

    /**
     * Tests that getAccessCode() returns the value passed to the constructor.
     */
    @Test
    void getAccessCode_returnsConstructorValue() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ADMIN999");

        assertEquals("ADMIN999", pt.getAccessCode());
    }

    /**
     * Tests that createAccount() rejects a null username.
     */
    @Test
    void createAccount_nullUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.createAccount(null, "pw123"));
    }

    /**
     * Tests that createAccount() rejects a blank username.
     */
    @Test
    void createAccount_blankUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.createAccount("   ", "pw123"));
    }

    /**
     * Tests that createAccount() rejects a null password.
     */
    @Test
    void createAccount_nullPassword_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.createAccount("student1", null));
    }

    /**
     * Tests that createAccount() rejects a blank password.
     */
    @Test
    void createAccount_blankPassword_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.createAccount("student1", "   "));
    }

    /**
     * Tests that createAccount persists a new player account.
     */
    @Test
    void createAccount_validInput_persistsPlayer() {
        writePlayersJson("{\"players\":[]}");
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        pt.createAccount("student1", "pw123");

        SaveManager manager = new SaveManager(new FileStorageService());
        Player saved = manager.loadPlayer("student1");
        assertNotNull(saved);
        assertEquals("student1", saved.getUsername());
        assertEquals("pw123", saved.getPasswordHash());
    }

    /**
     * Tests that createAccount rejects duplicate usernames already in storage.
     */
    @Test
    void createAccount_duplicateUsername_throwsIllegalArgumentException() {
        SaveManager manager = new SaveManager(new FileStorageService());
        manager.saveAll(List.of(new Player("student1", "pw123")));
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.createAccount("student1", "newPw"));
    }

    /**
     * Tests that resetPassword() rejects a null player username.
     */
    @Test
    void resetPassword_nullPlayerUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetPassword(null, "newPw"));
    }

    /**
     * Tests that resetPassword() rejects a blank player username.
     */
    @Test
    void resetPassword_blankPlayerUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetPassword("   ", "newPw"));
    }

    /**
     * Tests that resetPassword() rejects a null new password.
     */
    @Test
    void resetPassword_nullNewPassword_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetPassword("student1", null));
    }

    /**
     * Tests that resetPassword() rejects a blank new password.
     */
    @Test
    void resetPassword_blankNewPassword_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetPassword("student1", "   "));
    }

    /**
     * Tests that resetPassword updates persisted player credentials.
     */
    @Test
    void resetPassword_existingPlayer_updatesPersistedPassword() {
        SaveManager manager = new SaveManager(new FileStorageService());
        manager.saveAll(List.of(new Player("student1", "oldPw")));
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        pt.resetPassword("student1", "newPw");

        Player updated = manager.loadPlayer("student1");
        assertNotNull(updated);
        assertEquals("newPw", updated.getPasswordHash());
    }

    /**
     * Tests that resetPassword rejects a missing player username.
     */
    @Test
    void resetPassword_missingPlayer_throwsIllegalArgumentException() {
        writePlayersJson("{\"players\":[]}");
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetPassword("missing", "newPw"));
    }

    /**
     * Tests that resetStatistics() rejects a null player username.
     */
    @Test
    void resetStatistics_nullPlayerUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetStatistics(null));
    }

    /**
     * Tests that resetStatistics() rejects a blank player username.
     */
    @Test
    void resetStatistics_blankPlayerUsername_throwsIllegalArgumentException() {
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetStatistics("   "));
    }

    /**
     * Tests that resetStatistics clears persisted statistics for the target player.
     */
    @Test
    void resetStatistics_existingPlayer_clearsStatistics() {
        Player player = new Player("student1", "pw123");
        player.getStatistics().updateFrom(new SessionResult(120, 70.0, 92.0, 1, 60, "2026-03-29", 12, true));
        SaveManager manager = new SaveManager(new FileStorageService());
        manager.saveAll(List.of(player));
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        pt.resetStatistics("student1");

        Player updated = manager.loadPlayer("student1");
        assertNotNull(updated);
        assertEquals(0, updated.getStatistics().getHighScore());
        assertEquals(0, updated.getStatistics().getTotalTime());
        assertEquals(0.0, updated.getStatistics().getAvgWPM());
    }

    /**
     * Tests that resetStatistics rejects a missing player username.
     */
    @Test
    void resetStatistics_missingPlayer_throwsIllegalArgumentException() {
        writePlayersJson("{\"players\":[]}");
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertThrows(IllegalArgumentException.class,
                () -> pt.resetStatistics("missing"));
    }

    /**
     * Tests that resetHighScoreTable() can be called without throwing an exception.
     */
    @Test
    void resetHighScoreTable_doesNotThrow() {
        SaveManager manager = new SaveManager(new FileStorageService());
        Player alice = new Player("alice", "pw1");
        Player bob = new Player("bob", "pw2");
        alice.getStatistics().updateFrom(new SessionResult(150, 75.0, 90.0, 1, 60, "2026-03-29", 10, true));
        bob.getStatistics().updateFrom(new SessionResult(90, 60.0, 85.0, 2, 60, "2026-03-29", 8, true));
        manager.saveAll(List.of(alice, bob));
        ParentTeacher pt = new ParentTeacher("parentUser", "hashedPw", "ACCESS123");

        assertDoesNotThrow(pt::resetHighScoreTable);

        List<Player> updated = manager.loadAllPlayers();
        assertEquals(2, updated.size());
        assertTrue(updated.stream().allMatch(player -> player.getStatistics().getHighScore() == 0));
    }

    private void writePlayersJson(String json) {
        try {
            Files.createDirectories(playersFile.getParent());
            Files.writeString(playersFile, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path locatePlayersFile() {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            Path candidate = current.resolve("data/players.json");
            if (Files.exists(candidate) || Files.exists(current.resolve(".git"))) {
                return candidate;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not locate data/players.json");
    }
}
