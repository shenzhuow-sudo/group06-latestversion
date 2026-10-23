package ca.uwo.cs2212.ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lightweight sound helper for local UI effects and gameplay BGM.
 * <p>
 * This implementation uses the macOS {@code afplay} command so it can play
 * the current MP3 assets without introducing extra Java audio dependencies.
 * </p>
 */
public final class SoundManager {
    private static final Path PRIMARY_SOUND_DIR = Paths.get("src", "main", "resources", "assets", "sounds");
    private static final Path LEGACY_SOUND_DIR = Paths.get("assets", "sounds");
    private static final String CLICK_RESOURCE = "assets/sounds/click.wav";
    private static final String WARNING_RESOURCE = "assets/sounds/warning.wav";
    private static final String GAMEPLAY_BGM_RESOURCE = "assets/sounds/bgm.wav";

    private static volatile boolean enabled = true;
    private static volatile Clip clickClip;
    private static volatile Clip warningClip;
    private static volatile Clip gameplayBgmClip;

    private SoundManager() {
    }

    /**
     * Enables or disables all sound playback managed by this helper.
     *
     * @param enabled whether audio playback should be allowed
     */
    public static void setEnabled(boolean enabled) {
        SoundManager.enabled = enabled;
        if (!enabled) {
            stopGameplayBgm();
        }
    }

    /**
     * Plays the configured click UI sound effect once when available.
     */
    public static void playClick() {
        playClip(getClickClip());
    }

    /**
     * Plays the configured warning UI sound effect once when available.
     */
    public static void playWarning() {
        playClip(getWarningClip());
    }

    /**
     * Starts looping the gameplay background music if audio is enabled.
     */
    public static synchronized void startGameplayBgm() {
        if (!enabled) {
            return;
        }
        Clip clip = getGameplayBgmClip();
        if (clip == null || clip.isRunning()) {
            return;
        }
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops gameplay background music playback and rewinds it to the beginning.
     */
    public static synchronized void stopGameplayBgm() {
        Clip clip = gameplayBgmClip;
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    private static void playClip(Clip clip) {
        if (!enabled || clip == null) {
            return;
        }
        synchronized (clip) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private static Clip getClickClip() {
        if (clickClip == null) {
            clickClip = loadClip(CLICK_RESOURCE);
        }
        return clickClip;
    }

    private static Clip getWarningClip() {
        if (warningClip == null) {
            warningClip = loadClip(WARNING_RESOURCE);
        }
        return warningClip;
    }

    private static Clip getGameplayBgmClip() {
        if (gameplayBgmClip == null) {
            gameplayBgmClip = loadClip(GAMEPLAY_BGM_RESOURCE);
        }
        return gameplayBgmClip;
    }

    private static Clip loadClip(String resourcePath) {
        Path filePath = resolveFilePath(resourcePath);
        if (filePath != null) {
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(filePath.toFile())) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                return clip;
            } catch (Exception ignored) {
                // Fall through to classpath lookup.
            }
        }

        try (InputStream rawStream = SoundManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (rawStream == null) {
                return null;
            }
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(rawStream)) {
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                return clip;
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Path resolveFilePath(String resourcePath) {
        String fileName = Paths.get(resourcePath).getFileName().toString();
        Path primary = PRIMARY_SOUND_DIR.resolve(fileName).toAbsolutePath();
        if (Files.exists(primary)) {
            return primary;
        }

        Path legacy = LEGACY_SOUND_DIR.resolve(fileName).toAbsolutePath();
        if (Files.exists(legacy)) {
            return legacy;
        }
        return null;
    }
}
