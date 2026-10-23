package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.Level;
import ca.uwo.cs2212.demo.LevelDataLoader;
import ca.uwo.cs2212.demo.LevelManager;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.Progress;
import ca.uwo.cs2212.demo.PowerUpType;
import ca.uwo.cs2212.demo.SaveManager;
import ca.uwo.cs2212.demo.SessionResult;
import ca.uwo.cs2212.demo.WordGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main gameplay window that runs an active typing round for a saved player.
 */
public class GameplayScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final int MAX_LEVEL = 8;
    private static final int MAX_LIVES = 5;
    private static final int FREEZE_DURATION_TICKS = 100;
    private static final int DOUBLE_SCORE_DURATION_TICKS = 150;
    private static final int WIN_COIN_REWARD = 200;
    private static final int PIERCE_EFFECT_TICKS = 15;
    private static final double SPECIAL_ENEMY_CHANCE = 0.30;
    private static final int SPECIAL_FREEZE_TICKS = 30;   // 1 second at 30ms timer
    private static final int MEMORY_DEATH_TICKS = 18;
    private static final int HUNT_DEATH_TICKS = 18;
    private static final int ABUNDANCE_DEATH_TICKS = 18;
    private static final int ABUNDANCE_HEAL = 1;
    private static final String RESOURCE_ROOT = "src/main/resources";
    private static final String BACKGROUND_RESOURCE_DIR = "assets/backgrounds";


    // ── Dark palette ──
    private static final Color D_BG             = new Color(10, 10, 15);
    private static final Color D_TEXT           = new Color(232, 228, 220);
    private static final Color D_MUTED          = new Color(232, 228, 220, 115);
    private static final Color D_BODY           = new Color(232, 228, 220, 153);
    private static final Color D_LABEL          = new Color(232, 228, 220, 102);
    private static final Color D_ACCENT         = new Color(201, 165, 90);
    private static final Color D_ACCENT_DIM     = new Color(201, 165, 90, 100);
    private static final Color D_ACCENT_GLOW    = new Color(201, 165, 90, 38);
    private static final Color D_HUD_BG         = new Color(0, 0, 0, 166);
    private static final Color D_HUD_BORDER     = new Color(201, 165, 90, 30);
    private static final Color D_INPUT_BG       = new Color(255, 255, 255, 15);
    private static final Color D_INPUT_BORDER   = new Color(201, 165, 90, 64);
    private static final Color D_PROGRESS_TRACK = new Color(255, 255, 255, 20);
    private static final Color D_ENEMY_BG       = new Color(255, 255, 255, 10);
    private static final Color D_ENEMY_BORDER   = new Color(255, 255, 255, 20);
    private static final Color D_PU_BG          = new Color(255, 255, 255, 10);
    private static final Color D_PU_BORDER      = new Color(255, 255, 255, 20);
    private static final Color D_PU_ACT_BG      = new Color(201, 165, 90, 20);
    private static final Color D_PU_ACT_BDR     = new Color(201, 165, 90, 77);
    private static final Color D_HEART          = new Color(201, 165, 90);
    private static final Color D_HEART_EMPTY    = new Color(255, 255, 255, 25);
    private static final Color D_GAME_BG        = new Color(255, 255, 255, 5);
    private static final Color D_KBD_BG         = new Color(255, 255, 255, 15);
    private static final Color D_KBD_BORDER     = new Color(255, 255, 255, 30);
    private static final Color D_EXIT_BG        = new Color(255, 255, 255, 10);
    private static final Color D_EXIT_BORDER    = new Color(255, 255, 255, 25);
    private static final Color D_EXIT_TEXT      = new Color(232, 228, 220, 153);
    private static final Color D_PAUSE_OVERLAY  = new Color(0, 0, 0, 150);
    private static final Color D_FREEZE_OVERLAY = new Color(120, 190, 255, 82);
    private static final Color D_FREEZE_EDGE    = new Color(170, 220, 255, 120);
    private static final Color D_SHIELD_GLOW    = new Color(110, 215, 255, 90);
    private static final Color D_SHIELD_RING    = new Color(150, 225, 255, 170);
    private static final Color D_DOUBLE_GLOW    = new Color(255, 205, 90, 60);
    private static final Color D_DOUBLE_SPARK   = new Color(255, 220, 130, 170);
    private static final Color D_TOGGLE_BG      = new Color(0, 0, 0, 166);
    private static final Color D_TOGGLE_BDR     = new Color(201, 165, 90, 30);

    // ── Light palette ──
    private static final Color L_BG             = new Color(240, 236, 228);
    private static final Color L_TEXT           = new Color(26, 26, 26);
    private static final Color L_MUTED          = new Color(26, 26, 26, 90);
    private static final Color L_BODY           = new Color(26, 26, 26, 140);
    private static final Color L_LABEL          = new Color(26, 26, 26, 97);
    private static final Color L_ACCENT         = new Color(139, 105, 20);
    private static final Color L_ACCENT_DIM     = new Color(139, 105, 20, 90);
    private static final Color L_ACCENT_GLOW    = new Color(139, 105, 20, 25);
    private static final Color L_HUD_BG         = new Color(255, 255, 255, 200);
    private static final Color L_HUD_BORDER     = new Color(139, 105, 20, 25);
    private static final Color L_INPUT_BG       = new Color(255, 255, 255, 204);
    private static final Color L_INPUT_BORDER   = new Color(139, 105, 20, 64);
    private static final Color L_PROGRESS_TRACK = new Color(0, 0, 0, 18);
    private static final Color L_ENEMY_BG       = new Color(255, 255, 255, 178);
    private static final Color L_ENEMY_BORDER   = new Color(0, 0, 0, 20);
    private static final Color L_PU_BG          = new Color(0, 0, 0, 6);
    private static final Color L_PU_BORDER      = new Color(0, 0, 0, 18);
    private static final Color L_PU_ACT_BG      = new Color(139, 105, 20, 15);
    private static final Color L_PU_ACT_BDR     = new Color(139, 105, 20, 64);
    private static final Color L_HEART          = new Color(139, 105, 20);
    private static final Color L_HEART_EMPTY    = new Color(0, 0, 0, 20);
    private static final Color L_GAME_BG        = new Color(0, 0, 0, 4);
    private static final Color L_KBD_BG         = new Color(0, 0, 0, 10);
    private static final Color L_KBD_BORDER     = new Color(0, 0, 0, 25);
    private static final Color L_EXIT_BG        = new Color(0, 0, 0, 8);
    private static final Color L_EXIT_BORDER    = new Color(0, 0, 0, 25);
    private static final Color L_EXIT_TEXT      = new Color(26, 26, 26, 128);
    private static final Color L_PAUSE_OVERLAY  = new Color(255, 255, 255, 150);
    private static final Color L_FREEZE_OVERLAY = new Color(150, 215, 255, 72);
    private static final Color L_FREEZE_EDGE    = new Color(110, 190, 255, 110);
    private static final Color L_SHIELD_GLOW    = new Color(110, 185, 255, 80);
    private static final Color L_SHIELD_RING    = new Color(90, 165, 255, 155);
    private static final Color L_DOUBLE_GLOW    = new Color(255, 210, 120, 52);
    private static final Color L_DOUBLE_SPARK   = new Color(210, 160, 70, 155);
    private static final Color L_TOGGLE_BG      = new Color(255, 255, 255, 200);
    private static final Color L_TOGGLE_BDR     = new Color(139, 105, 20, 25);

    // ── Fonts ──
    private static final Font F_HUD_LBL   = new Font("SansSerif", Font.BOLD, 10);
    private static final Font F_HUD_VAL   = new Font("Monospaced", Font.BOLD, 16);
    private static final Font F_LVL_TXT   = new Font("Monospaced", Font.PLAIN, 11);
    private static final Font F_ENEMY     = new Font("Monospaced", Font.BOLD, 13);
    private static final Font F_STAT_LBL  = new Font("SansSerif", Font.BOLD, 10);
    private static final Font F_STAT_VAL  = new Font("Monospaced", Font.BOLD, 14);
    private static final Font F_INPUT     = new Font("Monospaced", Font.BOLD, 16);
    private static final Font F_INPUT_LBL = new Font("SansSerif", Font.BOLD, 10);
    private static final Font F_KBD       = new Font("Monospaced", Font.PLAIN, 10);
    private static final Font F_PU_KEY    = new Font("Monospaced", Font.BOLD, 11);
    private static final Font F_PU_NAME   = new Font("Serif", Font.BOLD, 11);
    private static final Font F_PU_INFO   = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font F_PU_LBL    = new Font("SansSerif", Font.BOLD, 9);
    private static final Font F_PLAYER    = new Font("Serif", Font.BOLD, 13);
    private static final Font F_PLAYER_ID = new Font("Monospaced", Font.PLAIN, 10);
    private static final Font F_PLAYER_LV = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font F_EXIT      = new Font("Serif", Font.BOLD, 12);
    private static final Font F_TOGGLE    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font F_FOOTER    = new Font("SansSerif", Font.PLAIN, 9);
    private static final Font F_HEART     = new Font("SansSerif", Font.PLAIN, 14);

    private GradientBgPanel bgPanel;
    private JPanel mainPanel;
    private ThemeTogglePanel themeToggle;
    private JLabel saveStatusLabel;
    private Timer saveStatusTimer;

    private HudBar hudBar;
    private GameArea gameArea;
    private StatsBar statsBar;
    private InputBar inputBar;
    private PowerupsBar powerupsBar;
    private BottomBar bottomBar;

    private Timer gameTimer, spawnTimer, elapsedTimer;
    private int score = 0;
    private int elapsed = 0;
    private int lives = MAX_LIVES;
    private int defeatedWords;
    private boolean paused;
    private int freezeCharges;
    private int shieldCharges;
    private int doubleScoreCharges;
    private int freezeTicksRemaining;
    private int doubleScoreTicksRemaining;
    private int shieldBlockTicksRemaining;
    private boolean shieldActive;
    private final String username;
    private final int currentLevel;
    private final boolean resumeFromCheckpoint;
    private final Player player;
    private final LevelManager levelManager;
    private final WordGenerator wordGenerator;
    private final Level activeLevel;
    private boolean sessionSaved;
    private int submittedWords;
    private int correctWords;
    private final BufferedImage darkLevelBackground;
    private final BufferedImage lightLevelBackground;
    private final BufferedImage memoryEnemyImage;
    private final BufferedImage huntEnemyImage;
    private final BufferedImage abundanceEnemyImage;

    /**
     * Prevents creating a gameplay window without an authenticated player.
     */
    public GameplayScreen() {
        throw new IllegalStateException("GameplayScreen requires an existing player username.");
    }

    /**
     * Creates a gameplay screen for the given player at level 1.
     *
     * @param username existing player username
     */
    public GameplayScreen(String username) {
        this(username, 1, false);
    }

    /**
     * Creates a gameplay screen for the given player and requested level.
     *
     * @param username existing player username
     * @param level requested level number
     */
    public GameplayScreen(String username, int level) {
        this(username, level, false);
    }

    /**
     * Creates a gameplay screen for the given player and requested level.
     *
     * @param username existing player username
     * @param level requested level number
     * @param resumeFromCheckpoint whether to restore saved in-level progress
     */
    public GameplayScreen(String username, int level, boolean resumeFromCheckpoint) {
        this.username = requireUsername(username);
        this.currentLevel = Math.max(1, Math.min(MAX_LEVEL, level));
        this.resumeFromCheckpoint = resumeFromCheckpoint;
        this.player = loadExistingPlayer(this.username);
        ThemeManager.setTheme(UiRuntimeGuard.toUiTheme(player.getProgress().getSettings().getTheme()));
        SoundManager.setEnabled(player.getProgress().getSettings().isSoundOn());
        this.levelManager = new LevelDataLoader(new FileStorageService()).loadLevelManager();
        this.wordGenerator = new WordGenerator();
        this.activeLevel = levelManager.getLevel(this.currentLevel);
        if (this.activeLevel == null) {
            throw new IllegalStateException("Missing runtime level data for level " + this.currentLevel);
        }
        initializeLevelProgress();
        this.darkLevelBackground = loadLevelBackground(this.currentLevel, true);
        this.lightLevelBackground = loadLevelBackground(this.currentLevel, false);
        this.freezeCharges = player.getInventory().getCount(PowerUpType.FREEZE);
        this.shieldCharges = player.getInventory().getCount(PowerUpType.SHIELD);
        this.doubleScoreCharges = player.getInventory().getCount(PowerUpType.DOUBLE_SCORE);
        this.memoryEnemyImage = loadOverlayImage("assets/enemy/memory.png");
        this.huntEnemyImage = loadOverlayImage("assets/enemy/hunt.png");
        this.abundanceEnemyImage = loadOverlayImage("assets/enemy/abundance.png");
        setTitle("The Hunt - Gameplay");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        applyTheme();
        startGame();
        setContentPane(bgPanel);
        setVisible(true);
    }

    // ── Helper ──
    private Color c(Color d, Color l) { return ThemeManager.isDark() ? d : l; }

    private LevelConfig levelConfig() {
        return LevelConfig.forLevel(currentLevel);
    }

    // ═══════════════════════════════════════════════════════
    //  BUILD
    // ═══════════════════════════════════════════════════════

    private void buildUI() {
        bgPanel = new GradientBgPanel();
        bgPanel.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        themeToggle = new ThemeTogglePanel();
        themeToggle.setDarkSelected(ThemeManager.isDark());
        themeToggle.setToggleListener(d -> {
            Theme selectedTheme = d ? Theme.DARK : Theme.LIGHT;
            ThemeManager.setTheme(selectedTheme);
            player.getProgress().getSettings().setTheme(UiRuntimeGuard.toModelTheme(selectedTheme));
            savePlayerState();
            applyTheme();
        });

        hudBar = new HudBar();
        hudBar.setPreferredSize(new Dimension(0, 52));
        hudBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        gameArea = new GameArea();

        statsBar = new StatsBar();
        statsBar.setPreferredSize(new Dimension(0, 40));
        statsBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        inputBar = new InputBar();
        inputBar.setPreferredSize(new Dimension(0, 56));
        inputBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        powerupsBar = new PowerupsBar();
        powerupsBar.setPreferredSize(new Dimension(0, 52));
        powerupsBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        bottomBar = new BottomBar();
        bottomBar.setPreferredSize(new Dimension(0, 54));
        bottomBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        mainPanel.add(hudBar);
        mainPanel.add(gameArea);
        mainPanel.add(statsBar);
        mainPanel.add(inputBar);
        mainPanel.add(powerupsBar);
        mainPanel.add(bottomBar);

        JLayeredPane layered = new JLayeredPane();
        layered.setLayout(new OverlayLayout(layered));
        mainPanel.setAlignmentX(0f); mainPanel.setAlignmentY(0f);
        layered.add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel toggleWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 14));
        toggleWrap.setOpaque(false);
        saveStatusLabel = new JLabel(" ");
        saveStatusLabel.setFont(F_FOOTER);
        toggleWrap.add(saveStatusLabel);
        toggleWrap.add(themeToggle);
        toggleWrap.setAlignmentX(0f); toggleWrap.setAlignmentY(0f);
        layered.add(toggleWrap, JLayeredPane.PALETTE_LAYER);

        bgPanel.add(layered, BorderLayout.CENTER);
    }

    private void startGame() {
        SoundManager.startGameplayBgm();
        spawnInitialEnemies();
        gameTimer = new Timer(30, e -> { gameArea.tick(); gameArea.repaint(); });
        gameTimer.start();
        elapsedTimer = new Timer(1000, e -> {
            elapsed++;
            if (elapsed >= 40) {
                hudBar.repaint();
                statsBar.repaint();
                exitToMenu();
                return;
            }
            hudBar.repaint();
            statsBar.repaint();
        });
        elapsedTimer.start();
        LevelConfig config = levelConfig();
        spawnTimer = new Timer(config.spawnIntervalMs, e -> {
            if (gameArea.enemies.size() < config.maxEnemies) gameArea.spawn();
        });
        spawnTimer.start();
        installPauseKeybinding();
        SwingUtilities.invokeLater(() -> inputBar.field.requestFocusInWindow());
    }

    /**
     * Initializes the active level either from a saved checkpoint or from a fresh level start.
     */
    private void initializeLevelProgress() {
        Progress progress = player.getProgress();
        if (progress == null) {
            return;
        }
        if (resumeFromCheckpoint) {
            defeatedWords = Math.min(progress.getLevelWordsCompleted(currentLevel), levelConfig().targetWords);
            return;
        }
        progress.clearLevelCheckpoint(currentLevel);
        progress.setCurrentLevel(currentLevel);
    }

    /**
     * Spawns the initial enemy set, taking any restored checkpoint progress into account.
     */
    private void spawnInitialEnemies() {
        int initialEnemies = Math.max(1, Math.min(3, levelConfig().targetWords - defeatedWords));
        for (int i = 0; i < initialEnemies; i++) {
            gameArea.spawn();
        }
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        bgPanel.dark = darkThemeEnabled; bgPanel.repaint();
        hudBar.repaint(); gameArea.repaint(); statsBar.repaint();
        inputBar.applyColors(); inputBar.repaint();
        powerupsBar.repaint(); bottomBar.repaint();
        themeToggle.applyTheme(c(D_TOGGLE_BG, L_TOGGLE_BG), c(D_TOGGLE_BDR, L_TOGGLE_BDR),
                c(D_MUTED, L_MUTED), c(D_ACCENT, L_ACCENT), c(D_BG, L_BG));
        themeToggle.setDarkSelected(darkThemeEnabled);
    }

    private void exitToMenu() {
        if (gameTimer != null) gameTimer.stop();
        if (spawnTimer != null) spawnTimer.stop();
        if (elapsedTimer != null) elapsedTimer.stop();
        SoundManager.stopGameplayBgm();
        SessionResult result = persistSessionResult();
        dispose();
        new GameOverScreen(
                username,
                score,
                elapsed,
                formatWpm(result.getWpm()),
                formatAccuracy(result.getAccuracy()),
                currentLevel,
                result.isWon());
    }

    private String fmtTime(int s) { return String.format("%02d:%02d", s / 60, s % 60); }

    private int currentBestScore() {
        return Math.max(score, player.getStatistics().getHighScore());
    }

    private double currentSessionWpm() {
        if (elapsed <= 0 || correctWords <= 0) {
            return 0.0;
        }
        return (correctWords * 60.0) / elapsed;
    }

    private double currentSessionAccuracy() {
        if (submittedWords <= 0) {
            return 0.0;
        }
        return (correctWords * 100.0) / submittedWords;
    }

    /**
     * Registers gameplay keyboard shortcuts for pause/resume, exit, and power-up activation.
     */
    private void installPauseKeybinding() {
        JRootPane root = getRootPane();
        if (root == null) return;
        InputMap inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = root.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "togglePause");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitGameplay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "activateFreeze");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "activateShield");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "activateDoubleScore");
        actionMap.put("togglePause", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
        actionMap.put("exitGameplay", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                exitToMenu();
            }
        });
        actionMap.put("activateFreeze", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                activatePowerup(1);
            }
        });
        actionMap.put("activateShield", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                activatePowerup(2);
            }
        });
        actionMap.put("activateDoubleScore", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                activatePowerup(3);
            }
        });
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            if (gameTimer != null) gameTimer.stop();
            if (spawnTimer != null) spawnTimer.stop();
            if (elapsedTimer != null) elapsedTimer.stop();
            SoundManager.stopGameplayBgm();
        } else {
            if (gameTimer != null) gameTimer.start();
            if (spawnTimer != null) spawnTimer.start();
            if (elapsedTimer != null) elapsedTimer.start();
            SoundManager.startGameplayBgm();
            SwingUtilities.invokeLater(() -> inputBar.field.requestFocusInWindow());
        }
        inputBar.setPaused(paused);
        bottomBar.repaint();
        gameArea.repaint();
        inputBar.repaint();
    }

    private float levelProgress() {
        return GameplayLogic.levelProgress(defeatedWords, levelConfig().targetWords);
    }

    private void activatePowerup(int slot) {
        if (paused) return;
        switch (slot) {
            case 1 -> {
                if (freezeCharges <= 0 || freezeTicksRemaining > 0) return;
                if (!player.getInventory().consumePowerUp(PowerUpType.FREEZE, 1)) return;
                freezeCharges--;
                freezeTicksRemaining = FREEZE_DURATION_TICKS;
                SoundManager.playClick();
            }
            case 2 -> {
                if (shieldCharges <= 0 || shieldActive) return;
                if (!player.getInventory().consumePowerUp(PowerUpType.SHIELD, 1)) return;
                shieldCharges--;
                shieldActive = true;
                SoundManager.playClick();
            }
            case 3 -> {
                if (doubleScoreCharges <= 0 || doubleScoreTicksRemaining > 0) return;
                if (!player.getInventory().consumePowerUp(PowerUpType.DOUBLE_SCORE, 1)) return;
                doubleScoreCharges--;
                doubleScoreTicksRemaining = DOUBLE_SCORE_DURATION_TICKS;
                SoundManager.playClick();
            }
            default -> {
                return;
            }
        }
        savePlayerState();
        powerupsBar.repaint();
        gameArea.repaint();
        statsBar.repaint();
    }

    private void updatePowerupTimers() {
        if (freezeTicksRemaining > 0) {
            freezeTicksRemaining--;
        }
        if (doubleScoreTicksRemaining > 0) {
            doubleScoreTicksRemaining--;
        }
        if (shieldBlockTicksRemaining > 0) {
            shieldBlockTicksRemaining--;
        }
        powerupsBar.repaint();
    }

    private int secondsLeft(int ticksRemaining) {
        return Math.max(1, (int) Math.ceil(ticksRemaining / 30.0));
    }

    private void loseLife() {
        if (lives <= 0) return;
        if (shieldActive) {
            shieldActive = false;
            shieldBlockTicksRemaining = 35;
            powerupsBar.repaint();
            statsBar.repaint();
            bottomBar.repaint();
            gameArea.repaint();
            return;
        }
        lives--;
        statsBar.repaint();
        gameArea.repaint();
        if (lives <= 0) {
            exitToMenu();
        }
    }


    private void processTypedWord() {
        String typed = inputBar.field.getText().trim();
        if (typed.isEmpty()) return;
        if (paused) return;
        submittedWords++;

        Enemy target = null;
        for (Enemy enemy : gameArea.enemies) {
            if (enemy.dying) {
                continue;
            }
            if (enemy.word.equalsIgnoreCase(typed)) {
                if (target == null || enemy.y > target.y) {
                    target = enemy;
                }
            }
        }

        if (target != null) {
            target.dying = true;
            target.speed = 0f;

            switch (target.type) {
                case MEMORY -> {
                    target.deathTicks = MEMORY_DEATH_TICKS;
                    target.deathMaxTicks = MEMORY_DEATH_TICKS;
                    freezeTicksRemaining = Math.max(freezeTicksRemaining, SPECIAL_FREEZE_TICKS);
                }
                case HUNT -> {
                    target.deathTicks = HUNT_DEATH_TICKS;
                    target.deathMaxTicks = HUNT_DEATH_TICKS;

                    Enemy randomVictim = gameArea.pickRandomOtherAliveEnemy(target);
                    if (randomVictim != null) {
                        target.beamTarget = randomVictim;
                        target.beamTicks = 12;

                        randomVictim.dying = true;
                        randomVictim.speed = 0f;
                        randomVictim.deathTicks = PIERCE_EFFECT_TICKS;
                        randomVictim.deathMaxTicks = PIERCE_EFFECT_TICKS;

                        // Award score for the enemy killed by the Hunt beam as well
                        score += GameplayLogic.calculateWordScore(
                                randomVictim.word,
                                currentLevel,
                                doubleScoreTicksRemaining > 0
                        );
                    }
                }
                case ABUNDANCE -> {
                    target.deathTicks = ABUNDANCE_DEATH_TICKS;
                    target.deathMaxTicks = ABUNDANCE_DEATH_TICKS;
                    lives = Math.min(MAX_LIVES, lives + ABUNDANCE_HEAL);
                    statsBar.repaint();
                }
                default -> {
                    target.deathTicks = PIERCE_EFFECT_TICKS;
                    target.deathMaxTicks = PIERCE_EFFECT_TICKS;
                }
            }

            defeatedWords++;
            correctWords++;

            score += GameplayLogic.calculateWordScore(target.word, currentLevel, doubleScoreTicksRemaining > 0);
            player.getProgress().recordCurrentLevelCheckpoint(currentLevel, defeatedWords);
            savePlayerState();

            if (defeatedWords >= levelConfig().targetWords) {
                hudBar.repaint();
                gameArea.repaint();
                inputBar.field.setText("");
                Timer winDelay = new Timer(200, e -> {
                    ((Timer) e.getSource()).stop();
                    exitToMenu();
                });
                winDelay.setRepeats(false);
                winDelay.start();
                return;
            }
        } else {
            score = Math.max(0, score - 25);
            loseLife();
            if (lives <= 0) {
                return;
            }
        }

        inputBar.field.setText("");
        hudBar.repaint();
        statsBar.repaint();
        gameArea.repaint();
    }

    /**
     * Persists the current session outcome once and returns the computed result.
     * <p>
     * Recent activity is recorded only when the player actually interacted with
     * the round. Aggregate statistics reflect any meaningful played session,
     * while progression and coin rewards are only applied to winning sessions.
     * </p>
     *
     * @return persisted session result snapshot
     */
    private SessionResult persistSessionResult() {
        if (sessionSaved) {
            return buildSessionResult();
        }
        SessionResult result = buildSessionResult();
        GameplayLogic.applySessionOutcome(
                player,
                result,
                submittedWords,
                defeatedWords,
                score,
                currentLevel,
                MAX_LEVEL,
                WIN_COIN_REWARD);
        savePlayerState();
        sessionSaved = true;
        return result;
    }

    /**
     * Builds a stable snapshot of the current round state for persistence and UI handoff.
     *
     * @return session summary for the active round
     */
    private SessionResult buildSessionResult() {
        return GameplayLogic.buildSessionResult(
                score,
                elapsed,
                submittedWords,
                correctWords,
                defeatedWords,
                levelConfig().targetWords);
    }

    /**
     * Returns whether the current round did enough work to deserve a recent-activity entry.
     *
     * @param result computed session result
     * @return {@code true} when the session contains actual gameplay
     */
    private boolean hasMeaningfulSession(SessionResult result) {
        return GameplayLogic.hasMeaningfulSession(result, submittedWords, defeatedWords, score);
    }

    private void updateProgressForSession() {
        if (levelProgress() < 1f) {
            return;
        }
        player.getProgress().recordCompletedLevel(currentLevel, MAX_LEVEL);
    }

    private String formatWpm(double wpm) {
        return String.format("%.0f", wpm);
    }

    private String formatAccuracy(double accuracy) {
        return String.format("%.0f%%", accuracy);
    }

    private static Player loadExistingPlayer(String username) {
        SaveManager saveManager = new SaveManager(new FileStorageService());
        Player loaded = saveManager.loadPlayer(username);
        if (loaded != null) {
            return loaded;
        }
        throw new IllegalStateException("No saved player found for username: " + username);
    }

    private static String requireUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        return username.trim();
    }

    /**
     * Flushes the current in-memory player state to persistent storage.
     */
    private void savePlayerState() {
        try {
            new SaveManager(new FileStorageService()).savePlayer(player);
            showSaveConfirmation("Progress saved");
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "save player data", ex);
                return;
            }
            throw ex;
        }
    }

    private void showSaveConfirmation(String message) {
        if (saveStatusLabel == null) {
            return;
        }
        saveStatusLabel.setText(message == null || message.isBlank() ? "Saved" : message);
        saveStatusLabel.setForeground(c(D_ACCENT, L_ACCENT));
        if (saveStatusTimer != null && saveStatusTimer.isRunning()) {
            saveStatusTimer.stop();
        }
        saveStatusTimer = new Timer(2200, e -> {
            saveStatusLabel.setText(" ");
            saveStatusLabel.setForeground(c(D_MUTED, L_MUTED));
        });
        saveStatusTimer.setRepeats(false);
        saveStatusTimer.start();
    }

    private String nextWordForSpawn() {
        List<String> words = wordGenerator.generateWords(activeLevel, 1);
        if (words.isEmpty()) {
            return "";
        }
        return words.get(0);
    }

    private BufferedImage currentLevelBackground() {
        return ThemeManager.isDark() ? darkLevelBackground : lightLevelBackground;
    }

    private static BufferedImage loadLevelBackground(int level, boolean dark) {
        String prefix = "level" + Math.max(1, Math.min(MAX_LEVEL, level)) + "_";
        String themeSuffix = dark ? "_dark.png" : "_light.png";
        File backgroundDir = new File(RESOURCE_ROOT, BACKGROUND_RESOURCE_DIR);
        File[] files = backgroundDir.listFiles((dir, name) ->
                name.startsWith(prefix) && name.toLowerCase().endsWith(themeSuffix));
        if (files != null && files.length > 0) {
            try {
                return ImageIO.read(files[0]);
            } catch (IOException ex) {
                System.err.println("Failed to load level background: " + files[0].getPath());
            }
        }

        try (InputStream stream = GameplayScreen.class.getClassLoader()
                .getResourceAsStream(BACKGROUND_RESOURCE_DIR + "/" + prefix + (dark ? "dark.png" : "light.png"))) {
            return stream == null ? null : ImageIO.read(stream);
        } catch (IOException ex) {
            System.err.println("Failed to load classpath level background for prefix: " + prefix);
            return null;
        }
    }

    private static BufferedImage loadOverlayImage(String path) {
        try (InputStream stream = GameplayScreen.class.getClassLoader().getResourceAsStream(path)) {
            return stream == null ? null : ImageIO.read(stream);
        } catch (IOException ex) {
            System.err.println("Failed to load overlay image: " + path);
            return null;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  HUD BAR
    // ═══════════════════════════════════════════════════════
    private class HudBar extends JPanel {
        HudBar() { setOpaque(false); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(c(D_HUD_BG, L_HUD_BG)); g2.fillRect(0, 0, w, h);
            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(0, h - 1, w, 1);

            int cellW = (w - 48) / 4, x = 24;

            // Level progression
            g2.setFont(F_HUD_LBL); g2.setColor(c(D_LABEL, L_LABEL));
            g2.drawString("LEVEL PROGRESSION", x, 20);
            g2.setFont(F_LVL_TXT); g2.setColor(c(D_TEXT, L_TEXT));
            String lv = defeatedWords + " / " + levelConfig().targetWords;
            g2.drawString(lv, x + cellW - 20 - g2.getFontMetrics().stringWidth(lv), 20);
            int bY = 30, bH = 5, bW = cellW - 40;
            g2.setColor(c(D_PROGRESS_TRACK, L_PROGRESS_TRACK));
            g2.fillRoundRect(x, bY, bW, bH, bH, bH);
            g2.setColor(c(D_ACCENT, L_ACCENT));
            g2.fillRoundRect(x, bY, (int)(bW * levelProgress()), bH, bH, bH);

            // Stats
            String[][] stats = {
                    {"SCORE", String.format("%,d", score)},
                    {"BEST SCORE", String.format("%,d", currentBestScore())},
                    {"TIME", fmtTime(elapsed)}
            };
            for (int i = 0; i < 3; i++) {
                int cx = 24 + cellW * (i + 1);
                g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(cx, 10, 1, h - 20);
                cx += 20;
                g2.setFont(F_HUD_LBL); g2.setColor(c(D_LABEL, L_LABEL)); g2.drawString(stats[i][0], cx, 22);
                g2.setFont(F_HUD_VAL); g2.setColor(i == 0 ? c(D_ACCENT, L_ACCENT) : c(D_TEXT, L_TEXT)); g2.drawString(stats[i][1], cx, 42);
            }
            g2.dispose();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  GAME AREA — falling enemies, plain bg (background TBD)
    // ═══════════════════════════════════════════════════════
    private class GameArea extends JPanel {
        final List<Enemy> enemies = new ArrayList<>();
        private final Random rng = new Random();

        GameArea() { setOpaque(false); }

        void spawn() {
            LevelConfig config = levelConfig();
            String w = nextWordForSpawn();
            if (w.isBlank()) {
                return;
            }
            float x = 0.08f + rng.nextFloat() * 0.75f;
            float spd = config.minSpeed + rng.nextFloat() * (config.maxSpeed - config.minSpeed);
            int face = rng.nextInt(3);

            EnemyType type = EnemyType.NORMAL;
            if (rng.nextDouble() < SPECIAL_ENEMY_CHANCE) {
                int pick = rng.nextInt(3);
                if (pick == 0) type = EnemyType.MEMORY;
                else if (pick == 1) type = EnemyType.HUNT;
                else type = EnemyType.ABUNDANCE;
            }

            enemies.add(new Enemy(w, x, -0.1f - rng.nextFloat() * 0.2f, spd, face, type));
        }


        void tick() {
            updatePowerupTimers();
            Iterator<Enemy> iterator = enemies.iterator();

            while (iterator.hasNext()) {
                Enemy e = iterator.next();

                if (e.dying) {
                    e.deathTicks--;
                    e.bob += 0.08f;

                    if (e.deathTicks <= 0) {
                        iterator.remove();
                    }
                    continue;
                }

                if (freezeTicksRemaining <= 0) {
                    e.y += e.speed;
                }
                e.bob += 0.04f;

                if (e.y > 1.0f) {
                    iterator.remove();
                }
            }
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();

            BufferedImage levelBackground = currentLevelBackground();
            if (levelBackground != null) {
                g2.drawImage(levelBackground, 0, 0, w, h, null);
            } else {
                g2.setColor(c(D_GAME_BG, L_GAME_BG));
                g2.fillRect(0, 0, w, h);
            }

            Color overlay = ThemeManager.isDark()
                    ? new Color(6, 8, 12, 96)
                    : new Color(255, 248, 236, 70);
            g2.setColor(overlay);
            g2.fillRect(0, 0, w, h);

            // Vignette
            g2.setPaint(new RadialGradientPaint(
                    w / 2f, h / 2f, Math.max(w, h) * 0.6f,
                    new float[]{0, 1},
                    new Color[]{
                            new Color(0, 0, 0, 0),
                            new Color(c(D_BG, L_BG).getRed(),
                                    c(D_BG, L_BG).getGreen(),
                                    c(D_BG, L_BG).getBlue(), 100)
                    }));
            g2.fillRect(0, 0, w, h);

            // Enemies
            String typed = inputBar != null ? inputBar.field.getText() : "";
            for (Enemy e : enemies) {
                int ex = (int) (e.x * w) + (int) (Math.sin(e.bob) * 6);
                int ey = (int) (e.y * h);
                drawEnemy(g2, ex, ey, e, typed);
            }

            if (freezeTicksRemaining > 0) {
                g2.setColor(c(D_FREEZE_OVERLAY, L_FREEZE_OVERLAY));
                g2.fillRect(0, 0, w, h);
                g2.setColor(c(D_FREEZE_EDGE, L_FREEZE_EDGE));
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(8, 8, w - 16, h - 16, 28, 28);
                g2.setStroke(new BasicStroke(1.5f));
                for (int i = 0; i < 6; i++) {
                    int sx = 22 + i * 26;
                    g2.drawLine(sx, 0, sx + 10, 18);
                    g2.drawLine(w - sx, 0, w - sx - 10, 18);
                    g2.drawLine(sx, h, sx + 10, h - 18);
                    g2.drawLine(w - sx, h, w - sx - 10, h - 18);
                }
                g2.setFont(new Font("Serif", Font.BOLD, 34));
                g2.setColor(c(D_TEXT, L_TEXT));
                String frozen = "FROZEN";
                FontMetrics ffm = g2.getFontMetrics();
                g2.drawString(frozen, (w - ffm.stringWidth(frozen)) / 2, 54);
                g2.setFont(F_LVL_TXT);
                String hint = secondsLeft(freezeTicksRemaining) + "s";
                FontMetrics hfm = g2.getFontMetrics();
                g2.drawString(hint, (w - hfm.stringWidth(hint)) / 2, 76);
            }

            if (doubleScoreTicksRemaining > 0) {
                drawDoubleScoreEffect(g2, w, h);
            }

            if (paused) {
                g2.setColor(c(D_PAUSE_OVERLAY, L_PAUSE_OVERLAY));
                g2.fillRect(0, 0, w, h);
                g2.setFont(new Font("Serif", Font.BOLD, 36));
                g2.setColor(c(D_TEXT, L_TEXT));
                String pausedText = "PAUSED";
                FontMetrics titleFm = g2.getFontMetrics();
                g2.drawString(pausedText, (w - titleFm.stringWidth(pausedText)) / 2, h / 2 - 6);
                g2.setFont(F_LVL_TXT);
                FontMetrics hintFm = g2.getFontMetrics();
                String resumeHint = "Space = Resume";
                String exitHint = "Esc = Exit";
                g2.drawString(resumeHint, (w - hintFm.stringWidth(resumeHint)) / 2, h / 2 + 20);
                g2.drawString(exitHint, (w - hintFm.stringWidth(exitHint)) / 2, h / 2 + 40);
            }

            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER));
            g2.fillRect(0, h - 1, w, 1);
            g2.dispose();
        }

        Enemy pickRandomOtherAliveEnemy(Enemy source) {
            List<Enemy> candidates = new ArrayList<>();
            for (Enemy enemy : enemies) {
                if (enemy == source) continue;
                if (enemy.dying) continue;
                candidates.add(enemy);
            }
            if (candidates.isEmpty()) {
                return null;
            }
            return candidates.get(rng.nextInt(candidates.size()));
        }

        private BufferedImage imageForEnemyType(EnemyType type) {
            return switch (type) {
                case MEMORY -> memoryEnemyImage;
                case HUNT -> huntEnemyImage;
                case ABUNDANCE -> abundanceEnemyImage;
                default -> null;
            };
        }
        private void drawMemoryEffect(Graphics2D g2, int x, int y, Enemy e) {
            float progress = 1f - (e.deathTicks / (float) e.deathMaxTicks);
            progress = Math.max(0f, Math.min(1f, progress));

            int size = (int) (50 + progress * 26); // 膨胀
            Composite old = g2.getComposite();

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - progress * 0.35f));
            drawEnemyBody(g2, x, y, size, e.face);

            BufferedImage portrait = imageForEnemyType(EnemyType.MEMORY);
            if (portrait != null) {
                int portraitSize = (int) (36 + progress * 10);
                g2.drawImage(portrait, x - portraitSize / 2, y - portraitSize / 2, portraitSize, portraitSize, null);
            }

            g2.setComposite(old);

            g2.setColor(new Color(180, 220, 255, 120));
            g2.setStroke(new BasicStroke(2f + progress * 3f));
            g2.draw(new Ellipse2D.Float(x - size / 2f - 4, y - size / 2f - 4, size + 8, size + 8));
        }
        private void drawHuntEffect(Graphics2D g2, int x, int y, Enemy e) {
            drawPierceEffect(g2, x, y, e);

            if (e.beamTarget != null && e.beamTarget.deathTicks > 0) {
                int w = getWidth();
                int h = getHeight();
                int tx = (int) (e.beamTarget.x * w) + (int) (Math.sin(e.beamTarget.bob) * 6);
                int ty = (int) (e.beamTarget.y * h);

                Stroke oldStroke = g2.getStroke();
                Composite oldComposite = g2.getComposite();

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 220, 120, 220));
                g2.drawLine(x, y, tx, ty);

                g2.setStroke(new BasicStroke(1.8f));
                g2.setColor(new Color(255, 255, 255, 220));
                g2.drawLine(x, y, tx, ty);

                g2.setComposite(oldComposite);
                g2.setStroke(oldStroke);
            }
        }
        private void drawAbundanceEffect(Graphics2D g2, int x, int y, Enemy e) {
            float progress = 1f - (e.deathTicks / (float) e.deathMaxTicks);
            progress = Math.max(0f, Math.min(1f, progress));

            drawPierceEffect(g2, x, y, e);

            int burst = (int) (20 + progress * 30);
            g2.setColor(new Color(120, 255, 160, 90));
            g2.fill(new Ellipse2D.Float(x - burst, y - burst, burst * 2f, burst * 2f));

            g2.setColor(new Color(170, 255, 200, 180));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new Ellipse2D.Float(x - burst * 0.8f, y - burst * 0.8f, burst * 1.6f, burst * 1.6f));
        }
        private void drawEnemy(Graphics2D g2, int x, int y, Enemy e, String typed) {
            if (e.dying) {
                switch (e.type) {
                    case MEMORY -> drawMemoryEffect(g2, x, y, e);
                    case HUNT -> drawHuntEffect(g2, x, y, e);
                    case ABUNDANCE -> drawAbundanceEffect(g2, x, y, e);
                    default -> drawPierceEffect(g2, x, y, e);
                }
                return;
            }

            int sz = 50;
            drawEnemyBody(g2, x, y, sz, e.face);

            // Face
            BufferedImage portrait = imageForEnemyType(e.type);
            if (portrait != null) {
                int portraitSize = 36;
                g2.drawImage(portrait, x - portraitSize / 2, y - portraitSize / 2, portraitSize, portraitSize, null);
            } else {
                g2.setColor(c(D_MUTED, L_MUTED));
                g2.fill(new Ellipse2D.Float(x - 8, y - 6, 5, 5));
                g2.fill(new Ellipse2D.Float(x + 3, y - 6, 5, 5));
                g2.setStroke(new BasicStroke(1.5f));
                if (e.face == 0) g2.drawArc(x - 7, y + 1, 14, 8, 200, 140);
                else if (e.face == 1) g2.drawLine(x - 5, y + 5, x + 5, y + 5);
                else g2.drawArc(x - 7, y + 5, 14, 8, 20, 140);
            }

            // Word tag
            g2.setFont(F_ENEMY);
            FontMetrics fm = g2.getFontMetrics();
            String word = e.word;
            int twW = fm.stringWidth(word) + 24, twH = 24;
            int tx = x - twW / 2, ty = y + sz / 2 + 8;

            g2.setColor(c(D_ENEMY_BG, L_ENEMY_BG));
            g2.fill(new RoundRectangle2D.Float(tx, ty, twW, twH, 8, 8));
            g2.setColor(c(D_ENEMY_BORDER, L_ENEMY_BORDER));
            g2.draw(new RoundRectangle2D.Float(tx, ty, twW, twH, 8, 8));

            int match = 0;
            if (!typed.isEmpty()) {
                for (int i = 0; i < Math.min(typed.length(), word.length()); i++) {
                    if (typed.substring(i, i+1).equalsIgnoreCase(word.substring(i, i+1))) {
                        match = i + 1;
                    } else {
                        break;
                    }
                }
            }

            int textX = tx + 12, textY = ty + twH / 2 + fm.getAscent() / 2 - 1;
            if (match > 0) {
                g2.setColor(c(D_ACCENT, L_ACCENT));
                g2.drawString(word.substring(0, match), textX, textY);
                g2.setColor(c(D_TEXT, L_TEXT));
                g2.drawString(word.substring(match), textX + fm.stringWidth(word.substring(0, match)), textY);
            } else {
                g2.setColor(c(D_TEXT, L_TEXT));
                g2.drawString(word, textX, textY);
            }
        }

        private void drawPierceEffect(Graphics2D g2, int x, int y, Enemy e) {
            float progress = 1f - (e.deathTicks / (float) e.deathMaxTicks);
            progress = Math.max(0f, Math.min(1f, progress));

            Composite oldComposite = g2.getComposite();
            Stroke oldStroke = g2.getStroke();

            int alpha = (int) (255 * (1f - progress));
            alpha = Math.max(0, Math.min(255, alpha));

            // 1. Enemy fading out
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - progress));
            int sz = 50;
            drawEnemyBody(g2, x, y, sz, e.face);

            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fill(new Ellipse2D.Float(x - 8, y - 6, 5, 5));
            g2.fill(new Ellipse2D.Float(x + 3, y - 6, 5, 5));

            g2.setStroke(new BasicStroke(1.5f));
            if (e.face == 0) g2.drawArc(x - 7, y + 1, 14, 8, 200, 140);
            else if (e.face == 1) g2.drawLine(x - 5, y + 5, x + 5, y + 5);
            else g2.drawArc(x - 7, y + 5, 14, 8, 20, 140);

            // 2. Main piercing beam
            g2.setComposite(oldComposite);
            g2.setStroke(new BasicStroke(3f + 5f * progress, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(160, 220, 255, alpha));
            g2.drawLine(x - 45, y - 45, x + 45, y + 45);

            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.drawLine(x - 55, y - 55, x + 55, y + 55);

            // 3. Cross slash
            g2.setStroke(new BasicStroke(2f + 3f * progress, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(180, 200, 255, Math.max(0, alpha - 40)));
            g2.drawLine(x - 32, y + 28, x + 32, y - 28);

            // 4. Center burst
            int burst = (int) (10 + progress * 24);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fill(new Ellipse2D.Float(x - burst / 2f, y - burst / 2f, burst, burst));

            g2.setColor(new Color(120, 190, 255, alpha / 2));
            g2.fill(new Ellipse2D.Float(x - burst, y - burst, burst * 2, burst * 2));

            // 5. Small spark lines
            g2.setStroke(new BasicStroke(1.5f));
            for (int i = 0; i < 8; i++) {
                double angle = (Math.PI * 2 * i) / 8.0 + progress * 0.8;
                int r1 = 10;
                int r2 = (int) (22 + progress * 18);
                int x1 = x + (int) (Math.cos(angle) * r1);
                int y1 = y + (int) (Math.sin(angle) * r1);
                int x2 = x + (int) (Math.cos(angle) * r2);
                int y2 = y + (int) (Math.sin(angle) * r2);

                g2.setColor(new Color(210, 230, 255, alpha));
                g2.drawLine(x1, y1, x2, y2);
            }

            // 6. Word tag fading with enemy
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - progress));
            g2.setFont(F_ENEMY);
            FontMetrics fm = g2.getFontMetrics();
            int twW = fm.stringWidth(e.word) + 24;
            int twH = 24;
            int tx = x - twW / 2;
            int ty = y + sz / 2 + 8;

            g2.setColor(new Color(255, 255, 255, Math.max(20, alpha / 2)));
            g2.fill(new RoundRectangle2D.Float(tx, ty, twW, twH, 8, 8));
            g2.setColor(new Color(180, 180, 200, alpha));
            g2.draw(new RoundRectangle2D.Float(tx, ty, twW, twH, 8, 8));
            g2.setColor(new Color(40, 40, 60, alpha));
            g2.drawString(e.word, tx + 12, ty + twH / 2 + fm.getAscent() / 2 - 1);

            g2.setComposite(oldComposite);
            g2.setStroke(oldStroke);
        }

        private void drawEnemyBody(Graphics2D g2, int x, int y, int size, int face) {
            Shape shape;
            Color bodyFill;
            Color bodyBorder;
            if (face == 0) {
                shape = new RoundRectangle2D.Float(x - size / 2f, y - size / 2f, size, size, 18, 18);
                bodyFill = ThemeManager.isDark() ? new Color(128, 196, 255, 42) : new Color(102, 156, 235, 55);
                bodyBorder = ThemeManager.isDark() ? new Color(172, 220, 255, 70) : new Color(86, 126, 190, 70);
            } else if (face == 1) {
                Area ghost = new Area(new RoundRectangle2D.Float(x - size / 2f, y - size / 2f + 4, size, size - 8, 18, 18));
                ghost.add(new Area(new Ellipse2D.Float(x - size / 2f, y - size / 2f - 2, size, size - 12)));
                ghost.subtract(new Area(new Ellipse2D.Float(x - 18, y + 12, 10, 10)));
                ghost.subtract(new Area(new Ellipse2D.Float(x - 2, y + 14, 10, 10)));
                ghost.subtract(new Area(new Ellipse2D.Float(x + 14, y + 12, 10, 10)));
                shape = ghost;
                bodyFill = ThemeManager.isDark() ? new Color(195, 160, 255, 38) : new Color(168, 135, 230, 48);
                bodyBorder = ThemeManager.isDark() ? new Color(220, 185, 255, 72) : new Color(150, 110, 215, 70);
            } else {
                Path2D.Float blob = new Path2D.Float();
                blob.moveTo(x, y - size / 2f);
                blob.curveTo(x + 20, y - 22, x + 26, y - 4, x + 22, y + 10);
                blob.curveTo(x + 18, y + 24, x + 8, y + 28, x, y + size / 2f);
                blob.curveTo(x - 10, y + 28, x - 24, y + 22, x - 23, y + 6);
                blob.curveTo(x - 24, y - 10, x - 18, y - 22, x, y - size / 2f);
                blob.closePath();
                shape = blob;
                bodyFill = ThemeManager.isDark() ? new Color(129, 235, 183, 36) : new Color(99, 189, 141, 46);
                bodyBorder = ThemeManager.isDark() ? new Color(165, 245, 204, 70) : new Color(74, 158, 118, 70);
            }

            g2.setColor(bodyFill);
            g2.fill(shape);
            if (freezeTicksRemaining > 0) {
                g2.setColor(c(D_FREEZE_EDGE, L_FREEZE_EDGE));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(shape);
            }
            g2.setColor(bodyBorder);
            g2.setStroke(new BasicStroke(1));
            g2.draw(shape);
        }

        private void drawDoubleScoreEffect(Graphics2D g2, int w, int h) {
            g2.setColor(c(D_DOUBLE_GLOW, L_DOUBLE_GLOW));
            g2.fillRect(0, 0, w, h);

            g2.setFont(new Font("Serif", Font.BOLD, 40));
            String text = "DOUBLE SCORE";
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(text)) / 2;
            int ty = h - 34;
            g2.setColor(c(D_DOUBLE_SPARK, L_DOUBLE_SPARK));
            g2.drawString(text, tx, ty);

            int[][] bursts = {
                    {w - 110, 86}, {w - 210, 142}, {w - 56, 168}, {92, 82}
            };
            for (int[] burst : bursts) {
                drawSparkBurst(g2, burst[0], burst[1], 18);
            }
        }

        private void drawSparkBurst(Graphics2D g2, int cx, int cy, int radius) {
            g2.setColor(c(D_DOUBLE_SPARK, L_DOUBLE_SPARK));
            for (int i = 0; i < 10; i++) {
                double angle = (Math.PI * 2 * i) / 10.0;
                int x2 = cx + (int) (Math.cos(angle) * radius);
                int y2 = cy + (int) (Math.sin(angle) * radius);
                g2.drawLine(cx, cy, x2, y2);
            }
            g2.fill(new Ellipse2D.Float(cx - 3, cy - 3, 6, 6));
        }
    }

    private enum EnemyType {
        NORMAL,
        MEMORY,
        HUNT,
        ABUNDANCE
    }

    private static class Enemy {
        String word;
        float x, y, speed, bob;
        int face;

        EnemyType type;

        boolean dying;
        int deathTicks;
        int deathMaxTicks;

        boolean effectTriggered;   // 避免特效重复触发

        Enemy beamTarget;          // 巡猎敌人的目标
        int beamTicks;             // 光束持续时间

        Enemy(String w, float x, float y, float s, int f, EnemyType type) {
            word = w;
            this.x = x;
            this.y = y;
            speed = s;
            face = f;
            this.type = type;
            bob = (float) (Math.random() * Math.PI * 2);

            dying = false;
            deathTicks = 0;
            deathMaxTicks = 0;
            effectTriggered = false;
            beamTarget = null;
            beamTicks = 0;
        }
    }

    private static class LevelConfig {
        final float minSpeed;
        final float maxSpeed;
        final int spawnIntervalMs;
        final int maxEnemies;
        final int targetWords;

        LevelConfig(float minSpeed, float maxSpeed, int spawnIntervalMs, int maxEnemies, int targetWords) {
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            this.spawnIntervalMs = spawnIntervalMs;
            this.maxEnemies = maxEnemies;
            this.targetWords = targetWords;
        }

        static LevelConfig forLevel(int level) {
            return switch (Math.max(1, Math.min(MAX_LEVEL, level))) {
                case 1 -> new LevelConfig(0.0042f, 0.0054f, 3200, 4, 8);
                case 2 -> new LevelConfig(0.0048f, 0.0061f, 2900, 5, 10);
                case 3 -> new LevelConfig(0.0056f, 0.0071f, 2550, 5, 12);
                case 4 -> new LevelConfig(0.0059f, 0.0072f, 2450, 6, 14);
                case 5 -> new LevelConfig(0.0065f, 0.0080f, 2250, 6, 16);
                case 6 -> new LevelConfig(0.0070f, 0.0087f, 2050, 7, 18);
                case 7 -> new LevelConfig(0.0076f, 0.0094f, 1900, 7, 20);
                default -> new LevelConfig(0.0082f, 0.0100f, 1750, 8, 22);
            };
        }
    }

    // ═══════════════════════════════════════════════════════
    //  STATS BAR
    // ═══════════════════════════════════════════════════════
    private class StatsBar extends JPanel {
        StatsBar() { setOpaque(false); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(c(D_HUD_BG, L_HUD_BG)); g2.fillRect(0, 0, w, h);
            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(0, h - 1, w, 1);

            int x = 28;
            String currentWpm = String.valueOf((int) Math.round(currentSessionWpm()));
            String currentAccuracy = String.format("%.0f%%", currentSessionAccuracy());
            g2.setFont(F_STAT_LBL); g2.setColor(c(D_LABEL, L_LABEL)); g2.drawString("WPM", x, h / 2 + 4); x += 40;
            g2.setFont(F_STAT_VAL); g2.setColor(c(D_ACCENT, L_ACCENT)); g2.drawString(currentWpm, x, h / 2 + 5); x += 60;
            g2.setFont(F_STAT_LBL); g2.setColor(c(D_LABEL, L_LABEL)); g2.drawString("ACCURACY", x, h / 2 + 4); x += 72;
            g2.setFont(F_STAT_VAL); g2.setColor(c(D_ACCENT, L_ACCENT)); g2.drawString(currentAccuracy, x, h / 2 + 5);

            // Lives
            int rx = w - 28;
            g2.setFont(F_HEART);
            for (int i = MAX_LIVES - 1; i >= 0; i--) {
                rx -= 18;
                g2.setColor(i < lives ? c(D_HEART, L_HEART) : c(D_HEART_EMPTY, L_HEART_EMPTY));
                g2.drawString("\u2665", rx, h / 2 + 5);
            }
            if (shieldActive || shieldBlockTicksRemaining > 0) {
                int badgeW = 78;
                int badgeX = rx - badgeW - 16;
                Color shieldBg = shieldBlockTicksRemaining > 0 ? c(D_SHIELD_GLOW, L_SHIELD_GLOW) : c(D_PU_ACT_BG, L_PU_ACT_BG);
                Color shieldBorder = shieldBlockTicksRemaining > 0 ? c(D_SHIELD_RING, L_SHIELD_RING) : c(D_PU_ACT_BDR, L_PU_ACT_BDR);
                g2.setColor(shieldBg);
                g2.fill(new RoundRectangle2D.Float(badgeX, 8, badgeW, 22, 10, 10));
                g2.setColor(shieldBorder);
                g2.draw(new RoundRectangle2D.Float(badgeX, 8, badgeW, 22, 10, 10));
                g2.setFont(F_STAT_LBL);
                g2.setColor(c(D_TEXT, L_TEXT));
                g2.drawString(shieldBlockTicksRemaining > 0 ? "BLOCKED" : "SHIELD", badgeX + 10, 23);
            }
            rx -= 8;
            g2.setFont(F_STAT_LBL); g2.setColor(c(D_LABEL, L_LABEL));
            String ll = "LIVES"; rx -= g2.getFontMetrics().stringWidth(ll);
            g2.drawString(ll, rx, h / 2 + 4);
            g2.dispose();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  INPUT BAR
    // ═══════════════════════════════════════════════════════
    private class InputBar extends JPanel {
        final JTextField field;
        private final JLabel lbl;

        InputBar() {
            setOpaque(false);
            setLayout(new BorderLayout(12, 0));
            setBorder(new EmptyBorder(8, 28, 8, 120)); // right space for kbd hints

            lbl = new JLabel("TYPE WORD");
            lbl.setFont(F_INPUT_LBL);
            lbl.setPreferredSize(new Dimension(80, 40));

            field = new JTextField();
            field.setFont(F_INPUT);
            field.setBorder(new EmptyBorder(0, 16, 0, 16));
            field.setOpaque(false);
            field.addActionListener(e -> processTypedWord());
            field.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        e.consume();
                        field.setText("");
                    }
                }

                @Override public void keyTyped(KeyEvent e) {
                    char ch = e.getKeyChar();
                    if (ch == '1' || ch == '2' || ch == '3') {
                        e.consume();
                    }
                }
            });

            add(lbl, BorderLayout.WEST);
            add(field, BorderLayout.CENTER);
        }

        void applyColors() {
            field.setForeground(c(D_TEXT, L_TEXT));
            field.setCaretColor(c(D_TEXT, L_TEXT));
            lbl.setForeground(c(D_LABEL, L_LABEL));
        }

        void setPaused(boolean paused) {
            field.setEditable(!paused);
            field.setFocusable(!paused);
            lbl.setText(paused ? "GAME PAUSED" : "TYPE WORD");
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(c(D_HUD_BG, L_HUD_BG)); g2.fillRect(0, 0, w, h);
            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(0, h - 1, w, 1);

            // Input field bg
            Rectangle ib = field.getBounds();
            g2.setColor(c(D_INPUT_BG, L_INPUT_BG));
            g2.fill(new RoundRectangle2D.Float(ib.x - 2, ib.y, ib.width + 4, ib.height, 12, 12));
            g2.setColor(c(D_INPUT_BORDER, L_INPUT_BORDER));
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(ib.x - 2, ib.y, ib.width + 4, ib.height, 12, 12));

            // Keyboard hints
            g2.setFont(F_KBD);
            int rx = w - 28;
            String[][] hints = {{"Esc", "exit"}, {"Space", "pause"}, {"Tab", "skip"}, {"Enter", "submit"}};
            for (int i = hints.length - 1; i >= 0; i--) {
                FontMetrics fm = g2.getFontMetrics();
                int aW = fm.stringWidth(hints[i][1]);
                int kW = fm.stringWidth(hints[i][0]) + 12;
                rx -= aW;
                g2.setColor(c(D_MUTED, L_MUTED)); g2.drawString(hints[i][1], rx, h / 2 + fm.getAscent() / 2);
                rx -= kW + 4;
                g2.setColor(c(D_KBD_BG, L_KBD_BG));
                g2.fill(new RoundRectangle2D.Float(rx, h / 2f - 9, kW, 18, 5, 5));
                g2.setColor(c(D_KBD_BORDER, L_KBD_BORDER));
                g2.draw(new RoundRectangle2D.Float(rx, h / 2f - 9, kW, 18, 5, 5));
                g2.setColor(c(D_BODY, L_BODY)); g2.drawString(hints[i][0], rx + 6, h / 2 + fm.getAscent() / 2 - 1);
                rx -= 16;
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ═══════════════════════════════════════════════════════
    //  POWERUPS BAR
    // ═══════════════════════════════════════════════════════
    private class PowerupsBar extends JPanel {
        PowerupsBar() { setOpaque(false); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(c(D_HUD_BG, L_HUD_BG)); g2.fillRect(0, 0, w, h);
            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(0, h - 1, w, 1);

            // Label
            g2.setFont(F_PU_LBL); g2.setColor(c(D_LABEL, L_LABEL)); g2.drawString("POWERUPS", 28, 20);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 8)); g2.setColor(c(D_MUTED, L_MUTED)); g2.drawString("Press 1/2/3 to activate", 28, 34);

            // Slots
            String[][] slots = {
                    {"1", "Freeze", freezeTicksRemaining > 0 ? "Active -> " + secondsLeft(freezeTicksRemaining) + "s" : "\u00D7" + freezeCharges + " remaining", String.valueOf(freezeTicksRemaining > 0)},
                    {"2", "Shield", shieldActive ? "Blocking next hit" : (shieldCharges > 0 ? "\u00D7" + shieldCharges + " remaining" : "Depleted"), String.valueOf(shieldActive)},
                    {"3", "Double Score", doubleScoreTicksRemaining > 0 ? "Active -> " + secondsLeft(doubleScoreTicksRemaining) + "s" : (doubleScoreCharges > 0 ? "\u00D7" + doubleScoreCharges + " remaining" : "Depleted"), String.valueOf(doubleScoreTicksRemaining > 0)}
            };
            int slotW = (w - 200) / 3, sH = 36, sY = 8, sx = 160;

            for (String[] s : slots) {
                boolean active = s[3].equals("true");
                g2.setColor(active ? c(D_PU_ACT_BG, L_PU_ACT_BG) : c(D_PU_BG, L_PU_BG));
                g2.fill(new RoundRectangle2D.Float(sx, sY, slotW - 12, sH, 10, 10));
                g2.setColor(active ? c(D_PU_ACT_BDR, L_PU_ACT_BDR) : c(D_PU_BORDER, L_PU_BORDER));
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(sx, sY, slotW - 12, sH, 10, 10));

                // Key badge
                int kx = sx + 10, ky = sY + 8;
                g2.setColor(c(D_KBD_BG, L_KBD_BG));
                g2.fill(new RoundRectangle2D.Float(kx, ky, 20, 20, 5, 5));
                g2.setColor(c(D_KBD_BORDER, L_KBD_BORDER));
                g2.draw(new RoundRectangle2D.Float(kx, ky, 20, 20, 5, 5));
                g2.setFont(F_PU_KEY); g2.setColor(c(D_ACCENT, L_ACCENT)); g2.drawString(s[0], kx + 6, ky + 15);

                // Name
                g2.setFont(F_PU_NAME); g2.setColor(c(D_TEXT, L_TEXT)); g2.drawString(s[1], kx + 28, ky + 14);

                // Info
                g2.setFont(F_PU_INFO);
                g2.setColor(active ? c(D_ACCENT, L_ACCENT) : c(D_MUTED, L_MUTED));
                FontMetrics ifm = g2.getFontMetrics();
                g2.drawString(s[2], sx + slotW - 12 - 10 - ifm.stringWidth(s[2]), ky + 14);

                sx += slotW;
            }
            g2.dispose();
        }

        private int secondsLeft(int ticksRemaining) {
            return Math.max(1, (int) Math.ceil(ticksRemaining / 30.0));
        }
    }

    // ═══════════════════════════════════════════════════════
    //  BOTTOM BAR
    // ═══════════════════════════════════════════════════════
    private class BottomBar extends JPanel {
        private boolean exitHovered;
        private boolean pauseHovered;
        private final Rectangle exitRect = new Rectangle();
        private final Rectangle pauseRect = new Rectangle();

        BottomBar() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    boolean over = exitRect.contains(e.getPoint());
                    boolean pauseOver = pauseRect.contains(e.getPoint());
                    if (over != exitHovered || pauseOver != pauseHovered) {
                        exitHovered = over;
                        pauseHovered = pauseOver;
                        setCursor((over || pauseOver) ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (pauseRect.contains(e.getPoint())) togglePause();
                    else if (exitRect.contains(e.getPoint())) exitToMenu();
                }
                @Override public void mouseExited(MouseEvent e) { exitHovered = false; pauseHovered = false; repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(c(D_HUD_BG, L_HUD_BG)); g2.fillRect(0, 0, w, h);

            // Avatar
            int ax = 28, ay = (h - 32) / 2, as = 32;
            g2.setColor(c(D_ACCENT_GLOW, L_ACCENT_GLOW));
            g2.fill(new Ellipse2D.Float(ax, ay, as, as));
            g2.setColor(c(D_ACCENT_DIM, L_ACCENT_DIM));
            g2.setStroke(new BasicStroke(1.5f)); g2.draw(new Ellipse2D.Float(ax, ay, as, as));
            Color accent = c(D_ACCENT, L_ACCENT);
            PlayerIdentityUi.drawAvatar(
                    g2,
                    ax,
                    ay,
                    as,
                    PlayerIdentityUi.resolveAvatarAppearanceId(player, "bunny"),
                    accent);
            if (shieldActive || shieldBlockTicksRemaining > 0) {
                float glow = shieldBlockTicksRemaining > 0 ? 1f : 0.7f;
                Color shieldGlow = c(D_SHIELD_GLOW, L_SHIELD_GLOW);
                g2.setColor(new Color(shieldGlow.getRed(), shieldGlow.getGreen(), shieldGlow.getBlue(), (int) (120 * glow)));
                g2.fill(new Ellipse2D.Float(ax - 8, ay - 8, as + 16, as + 16));
                g2.setColor(c(D_SHIELD_RING, L_SHIELD_RING));
                g2.setStroke(new BasicStroke(shieldBlockTicksRemaining > 0 ? 3f : 2f));
                g2.draw(new Ellipse2D.Float(ax - 6, ay - 6, as + 12, as + 12));
            }

            // Player info
            int tx = ax + as + 12;
            String playerName = player.getUsername();
            String playerIdText = PlayerIdentityUi.buildPlayerIdText(player);
            g2.setFont(F_PLAYER); g2.setColor(c(D_TEXT, L_TEXT)); g2.drawString(playerName, tx, h / 2 + 1);
            g2.setFont(F_PLAYER_ID); g2.setColor(c(D_MUTED, L_MUTED)); g2.drawString(playerIdText, tx, h / 2 + 14);

            // Level divider
            FontMetrics playerNameMetrics = g2.getFontMetrics(F_PLAYER);
            FontMetrics playerIdMetrics = g2.getFontMetrics(F_PLAYER_ID);
            int identityWidth = Math.max(playerNameMetrics.stringWidth(playerName), playerIdMetrics.stringWidth(playerIdText));
            int dx = tx + identityWidth + 24;
            g2.setColor(c(D_HUD_BORDER, L_HUD_BORDER)); g2.fillRect(dx, 12, 1, h - 24);
            g2.setFont(F_PLAYER_LV); g2.setColor(c(D_BODY, L_BODY)); g2.drawString("Level " + currentLevel, dx + 14, h / 2 + 5);
            if (shieldBlockTicksRemaining > 0) {
                g2.setFont(F_PLAYER_ID);
                g2.setColor(c(D_SHIELD_RING, L_SHIELD_RING));
                g2.drawString("Shield blocked hit", dx + 14, h / 2 + 18);
            }

            // Pause button
            int pw = 118, ph = 32;
            int px = w / 2 - 140, py = (h - ph) / 2;
            pauseRect.setBounds(px, py, pw, ph);
            Color pBg = pauseHovered ? c(D_PU_ACT_BG, L_PU_ACT_BG) : c(D_EXIT_BG, L_EXIT_BG);
            Color pBdr = pauseHovered ? c(D_PU_ACT_BDR, L_PU_ACT_BDR) : c(D_EXIT_BORDER, L_EXIT_BORDER);
            Color pTxt = pauseHovered ? c(D_ACCENT, L_ACCENT) : c(D_EXIT_TEXT, L_EXIT_TEXT);
            g2.setColor(pBg);
            g2.fill(new RoundRectangle2D.Float(px, py, pw, ph, 12, 12));
            g2.setColor(pBdr);
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(px + .5f, py + .5f, pw - 1, ph - 1, 12, 12));
            g2.setFont(F_EXIT);
            g2.setColor(pTxt);
            String pauseLabel = paused ? "RESUME" : "PAUSE";
            FontMetrics pfm = g2.getFontMetrics();
            g2.drawString(pauseLabel, px + (pw - pfm.stringWidth(pauseLabel)) / 2, py + ph / 2 + pfm.getAscent() / 2 - 1);

            // Exit button
            int ew = 100, eh = 32;
            int ex = w / 2 + 24, ey = (h - eh) / 2;
            exitRect.setBounds(ex, ey, ew, eh);

            Color eBg = exitHovered ? new Color(184, 84, 80, 25) : c(D_EXIT_BG, L_EXIT_BG);
            Color eBdr = exitHovered ? new Color(184, 84, 80, 90) : c(D_EXIT_BORDER, L_EXIT_BORDER);
            Color eTxt = exitHovered ? new Color(184, 84, 80) : c(D_EXIT_TEXT, L_EXIT_TEXT);

            g2.setColor(eBg);
            g2.fill(new RoundRectangle2D.Float(ex, ey, ew, eh, 12, 12));
            g2.setColor(eBdr); g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(ex + .5f, ey + .5f, ew - 1, eh - 1, 12, 12));
            g2.setFont(F_EXIT); g2.setColor(eTxt);
            FontMetrics efm = g2.getFontMetrics();
            g2.drawString("EXIT", ex + (ew - efm.stringWidth("EXIT")) / 2, ey + eh / 2 + efm.getAscent() / 2 - 1);

            // Footer
            g2.setFont(F_FOOTER); g2.setColor(c(D_MUTED, L_MUTED));
            FontMetrics ffm = g2.getFontMetrics();
            g2.drawString(FOOTER_TEXT, w - 28 - ffm.stringWidth(FOOTER_TEXT), h / 2 + ffm.getAscent() / 2);
            g2.dispose();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  SHARED COMPONENTS
    // ═══════════════════════════════════════════════════════

    private static Graphics2D gfx(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    private static class GradientBgPanel extends JPanel {
        boolean dark = true;
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(dark ? D_BG : L_BG); g2.fillRect(0, 0, w, h);
            Color gl1 = dark ? new Color(201, 165, 90, 12) : new Color(139, 105, 20, 8);
            g2.setPaint(new RadialGradientPaint(w * .2f, h * .15f, w * .6f, new float[]{0, 1}, new Color[]{gl1, new Color(0, 0, 0, 0)}));
            g2.fillRect(0, 0, w, h);
            Color gl2 = dark ? new Color(201, 165, 90, 6) : new Color(139, 105, 20, 4);
            g2.setPaint(new RadialGradientPaint(w * .8f, h * .85f, w * .5f, new float[]{0, 1}, new Color[]{gl2, new Color(0, 0, 0, 0)}));
            g2.fillRect(0, 0, w, h);
            g2.setColor(dark ? new Color(232, 228, 220, 10) : new Color(26, 26, 26, 10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50, 50, 50, 120); g2.drawLine(50, 50, 120, 50);
            g2.drawLine(w - 50, h - 50, w - 50, h - 120); g2.drawLine(w - 50, h - 50, w - 120, h - 50);
            g2.dispose();
        }
    }

    private static class ThemeTogglePanel extends JPanel {
        private boolean darkSelected = true;
        private final JLabel lightLabel, darkLabel;
        private Color bgColor, borderColor, mutedColor, selectedColor, selectedTextColor;
        private java.util.function.Consumer<Boolean> listener;

        ThemeTogglePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(194, 40));
            setMaximumSize(new Dimension(194, 40));
            setMinimumSize(new Dimension(194, 40));
            setLayout(new GridLayout(1, 2, 0, 0));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lightLabel = new JLabel("Light", SwingConstants.CENTER);
            darkLabel = new JLabel("Dark", SwingConstants.CENTER);
            lightLabel.setFont(F_TOGGLE); darkLabel.setFont(F_TOGGLE);
            lightLabel.setOpaque(false); darkLabel.setOpaque(false);
            add(lightLabel); add(darkLabel);
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    darkSelected = e.getX() >= getWidth() / 2; repaint();
                    if (listener != null) listener.accept(darkSelected);
                }
            });
        }

        void setDarkSelected(boolean d) { darkSelected = d; repaint(); }
        void setToggleListener(java.util.function.Consumer<Boolean> l) { listener = l; }
        void applyTheme(Color bg, Color b, Color m, Color s, Color st) {
            bgColor = bg; borderColor = b; mutedColor = m; selectedColor = s; selectedTextColor = st; repaint();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = gfx(g);
            int w = getWidth(), h = getHeight(), arc = h;
            g2.setColor(bgColor != null ? bgColor : new Color(255, 255, 255, 10));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(borderColor != null ? borderColor : new Color(255, 255, 255, 20));
            g2.setStroke(new BasicStroke(1)); g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
            int pw = w / 2, px = darkSelected ? pw - 1 : 3;
            g2.setColor(selectedColor != null ? selectedColor : new Color(201, 165, 90));
            g2.fillRoundRect(px, 3, pw - 2, h - 6, h - 6, h - 6);
            g2.dispose();
            Color sc = selectedTextColor != null ? selectedTextColor : Color.BLACK;
            Color uc = mutedColor != null ? mutedColor : new Color(200, 200, 200, 120);
            lightLabel.setForeground(!darkSelected ? sc : uc);
            darkLabel.setForeground(darkSelected ? sc : uc);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");
            super.paintComponent(g);
        }
    }
}
