package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.PowerUp;
import ca.uwo.cs2212.demo.Progress;
import ca.uwo.cs2212.demo.SaveManager;
import ca.uwo.cs2212.demo.Statistics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Player dashboard that summarizes progress, performance, and recent activity.
 */
public class DashboardScreen extends JFrame {
    private enum BackTarget { MAIN_MENU, PARENT_DASHBOARD }
    private static final int MAX_LEVEL = 8;

    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final Character[] AVATARS = {
            new Character("Bunny Scout", "bunny"),
            new Character("Bear Cub", "bear"),
            new Character("Fox Ranger", "fox"),
            new Character("Cat Mage", "cat")
    };

    // ── Dark palette ──
    private static final Color DARK_BG            = new Color(10, 10, 15);
    private static final Color DARK_TEXT           = new Color(232, 228, 220);
    private static final Color DARK_MUTED          = new Color(232, 228, 220, 115);
    private static final Color DARK_BODY           = new Color(232, 228, 220, 174);
    private static final Color DARK_LABEL          = new Color(232, 228, 220, 128);
    private static final Color DARK_ACCENT         = new Color(201, 165, 90);
    private static final Color DARK_ACCENT_DIM     = new Color(201, 165, 90, 128);
    private static final Color DARK_CARD_BG        = new Color(255, 255, 255, 8);
    private static final Color DARK_CARD_BORDER    = new Color(255, 255, 255, 15);
    private static final Color DARK_STAT_BG        = new Color(255, 255, 255, 6);
    private static final Color DARK_STAT_BORDER    = new Color(255, 255, 255, 15);
    private static final Color DARK_ROW_BORDER     = new Color(255, 255, 255, 10);
    private static final Color DARK_ROW_HOVER      = new Color(201, 165, 90, 10);
    private static final Color DARK_PROGRESS_TRACK = new Color(255, 255, 255, 15);
    private static final Color DARK_AVATAR_BG      = new Color(201, 165, 90, 20);
    private static final Color DARK_AVATAR_BORDER  = new Color(201, 165, 90, 50);
    private static final Color DARK_BADGE_BG       = new Color(201, 165, 90, 25);
    private static final Color DARK_BADGE_BORDER   = new Color(201, 165, 90, 64);
    private static final Color DARK_BTN_BG         = new Color(255, 255, 255, 8);
    private static final Color DARK_BTN_BORDER     = new Color(255, 255, 255, 15);
    private static final Color DARK_BTN_HOVER_BG   = new Color(201, 165, 90, 15);
    private static final Color DARK_BTN_HOVER_BDR  = new Color(201, 165, 90, 64);
    private static final Color DARK_DIVIDER        = new Color(201, 165, 90, 60);
    private static final Color DARK_TOGGLE_BG      = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR     = new Color(255, 255, 255, 15);
    private static final Color DARK_DANGER         = new Color(184, 84, 80);
    private static final Color DARK_DANGER_BDR     = new Color(184, 84, 80, 64);
    private static final Color DARK_WIN            = new Color(92, 184, 92);
    private static final Color DARK_LOSS           = new Color(184, 84, 80);

    // ── Light palette ──
    private static final Color LIGHT_BG            = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT           = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED         = new Color(26, 26, 26, 102);
    private static final Color LIGHT_BODY          = new Color(26, 26, 26, 153);
    private static final Color LIGHT_LABEL         = new Color(26, 26, 26, 115);
    private static final Color LIGHT_ACCENT        = new Color(139, 105, 20);
    private static final Color LIGHT_ACCENT_DIM    = new Color(139, 105, 20, 115);
    private static final Color LIGHT_CARD_BG       = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER   = new Color(0, 0, 0, 18);
    private static final Color LIGHT_STAT_BG       = new Color(0, 0, 0, 5);
    private static final Color LIGHT_STAT_BORDER   = new Color(0, 0, 0, 15);
    private static final Color LIGHT_ROW_BORDER    = new Color(0, 0, 0, 12);
    private static final Color LIGHT_ROW_HOVER     = new Color(139, 105, 20, 10);
    private static final Color LIGHT_PROGRESS_TRACK= new Color(0, 0, 0, 18);
    private static final Color LIGHT_AVATAR_BG     = new Color(139, 105, 20, 15);
    private static final Color LIGHT_AVATAR_BORDER = new Color(139, 105, 20, 46);
    private static final Color LIGHT_BADGE_BG      = new Color(139, 105, 20, 15);
    private static final Color LIGHT_BADGE_BORDER  = new Color(139, 105, 20, 50);
    private static final Color LIGHT_BTN_BG        = new Color(0, 0, 0, 6);
    private static final Color LIGHT_BTN_BORDER    = new Color(0, 0, 0, 25);
    private static final Color LIGHT_BTN_HOVER_BG  = new Color(139, 105, 20, 12);
    private static final Color LIGHT_BTN_HOVER_BDR = new Color(139, 105, 20, 64);
    private static final Color LIGHT_DIVIDER       = new Color(139, 105, 20, 80);
    private static final Color LIGHT_TOGGLE_BG     = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR    = new Color(0, 0, 0, 20);
    private static final Color LIGHT_DANGER        = new Color(158, 58, 55);
    private static final Color LIGHT_DANGER_BDR    = new Color(158, 58, 55, 64);
    private static final Color LIGHT_WIN           = new Color(60, 140, 60);
    private static final Color LIGHT_LOSS          = new Color(158, 58, 55);

    // ── Fonts ──
    private static final Font FONT_SECTION  = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_NAV      = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_TOGGLE   = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_FOOTER   = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_BACK     = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_PLAYER   = new Font("Serif", Font.BOLD, 18);
    private static final Font FONT_ID       = new Font("Monospaced", Font.PLAIN, 11);
    private static final Font FONT_LABEL    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_VALUE    = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT_STAT_LBL = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_STAT_VAL = new Font("Monospaced", Font.BOLD, 24);
    private static final Font FONT_STAT_SUB = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_BADGE_LBL= new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_BADGE_VAL= new Font("Serif", Font.BOLD, 20);
    private static final Font FONT_PROGRESS = new Font("Monospaced", Font.BOLD, 12);
    private static final Font FONT_CONTINUE = new Font("Serif", Font.BOLD, 13);
    private static final Font FONT_TH       = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_TD       = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_ACT_BTN  = new Font("Serif", Font.BOLD, 12);
    private static final Font FONT_SMALL_BTN= new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_ARROW    = new Font("SansSerif", Font.BOLD, 14);

    // ── Activity data ──
    private static final String[] ACT_COLS = {"#", "Date", "Score", "WPM", "Acc", "Result"};

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private ThemeTogglePanel themeTogglePanel;
    private JLabel saveStatusLabel;
    private Timer saveStatusTimer;
    private SmallButton soundToggleButton;

    // Cards
    private GlassCard playerCard, perfCard, progressCard, activityCard;
    // Player info components
    private AvatarPanel avatarPanel;
    private JLabel playerNameLabel, playerIdLabel;
    private SmallButton changeAvatarBtn;
    private JLabel[] infoLabels, infoValues;
    private SmallButton levelPrev, levelNext;
    // Performance
    private StatBox[] statBoxes;
    private LevelBadgePanel levelBadge;
    // Saved progress
    private JLabel[] progressLabels, progressValues;
    private ProgressBarPanel progressBar;
    private JLabel progressBarLabel, progressBarPercent;
    private GoldButton continueBtn;
    // Activity table
    private JTable activityTable;
    private int actHoveredRow = -1;
    // Actions
    private ActionButton startBtn, shopBtn, highScoresBtn, logoutBtn;
    private JButton backButton;
    private JLabel footerLabel;

    private final String username;
    private final Player player;
    private final BackTarget backTarget;
    private final String parentDashboardUsername;
    private String selectedAvatarAppearanceId = AVATARS[0].getAppearanceId();
    private int selectedLevel = 2;

    /**
     * Prevents opening the dashboard without an existing player.
     */
    public DashboardScreen() {
        throw new IllegalStateException("DashboardScreen requires an existing player username.");
    }

    /**
     * Opens the dashboard for the saved player with the given username.
     *
     * @param username player username to load
     */
    public DashboardScreen(String username) {
        this(loadExistingPlayer(username), BackTarget.MAIN_MENU, null, false);
    }

    /**
     * Opens the dashboard for the provided player instance.
     *
     * @param player loaded player model
     */
    public DashboardScreen(Player player) {
        this(player, BackTarget.MAIN_MENU, null, false);
    }

    /**
     * Opens the dashboard for a player and configures whether back navigation returns to a parent dashboard.
     *
     * @param username player username to load
     * @param returnToParentDashboard whether the back action should target the parent dashboard
     */
    public DashboardScreen(String username, boolean returnToParentDashboard) {
        this(loadExistingPlayer(username), returnToParentDashboard ? BackTarget.PARENT_DASHBOARD : BackTarget.MAIN_MENU, null, false);
    }

    /**
     * Opens the dashboard for a loaded player with explicit parent-dashboard return context.
     *
     * @param player loaded player model
     * @param parentDashboardUsername parent dashboard username to return to
     */
    public DashboardScreen(Player player, String parentDashboardUsername) {
        this(player, BackTarget.PARENT_DASHBOARD, parentDashboardUsername, false);
    }

    /**
     * Opens the dashboard after a completed round and shows a one-time save confirmation.
     *
     * @param username player username whose refreshed dashboard should be opened
     * @return dashboard instance loaded from persisted player data
     */
    public static DashboardScreen fromLastGame(String username) {
        return new DashboardScreen(loadExistingPlayer(username), BackTarget.MAIN_MENU, null, true);
    }

    /**
     * Creates the dashboard with optional parent-return context and post-game confirmation state.
     *
     * @param player loaded player model
     * @param backTarget destination used by the back action
     * @param parentDashboardUsername parent dashboard username for return navigation, if any
     * @param showLastGameSavedConfirmation whether to display the post-game save confirmation popup
     */
    private DashboardScreen(Player player, BackTarget backTarget, String parentDashboardUsername, boolean showLastGameSavedConfirmation) {
        if (player == null) {
            throw new IllegalStateException("DashboardScreen requires an existing player.");
        }
        this.player = player;
        this.username = this.player.getUsername();
        this.backTarget = backTarget;
        this.parentDashboardUsername = parentDashboardUsername == null || parentDashboardUsername.isBlank()
                ? null
                : parentDashboardUsername.trim();
        normalizeProgressLevelState();
        this.selectedLevel = currentLevelValue();
        this.selectedAvatarAppearanceId = resolveAvatarAppearanceId(this.player);
        ThemeManager.setTheme(UiRuntimeGuard.toUiTheme(progressValue().getSettings().getTheme()));
        SoundManager.setEnabled(soundEnabled());
        setTitle("The Hunt - Dashboard");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        updateSavedProgressUI();
        applyTheme();

        setContentPane(bgPanel);
        setVisible(true);
        installEscapeShortcut();
        if (showLastGameSavedConfirmation) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this,
                    "Stats saved from last game",
                    "Save Complete",
                    JOptionPane.INFORMATION_MESSAGE
            ));
        }
    }

    // ═══════════════════════════════════════════════════════
    //  BUILD UI
    // ═══════════════════════════════════════════════════════

    private void buildUI() {
        bgPanel = new GradientBackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        buildTopBar();
        buildCenter();
        buildFooter();
        bgPanel.add(mainPanel, BorderLayout.CENTER);
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(18, 32, 14, 32));

        navLabel = new JLabel("THE HUNT / DASHBOARD");
        navLabel.setFont(FONT_NAV);
        navChipPanel = new RoundedPanel(18);
        navChipPanel.setLayout(new BorderLayout());
        navChipPanel.setBorder(new EmptyBorder(10, 16, 10, 18));
        navChipPanel.add(navLabel, BorderLayout.CENTER);

        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrap.setOpaque(false);
        leftWrap.add(navChipPanel);

        themeTogglePanel = new ThemeTogglePanel();
        themeTogglePanel.setDarkSelected(ThemeManager.isDark());
        themeTogglePanel.setToggleListener(d -> {
            Theme selectedTheme = d ? Theme.DARK : Theme.LIGHT;
            ThemeManager.setTheme(selectedTheme);
            progressValue().getSettings().setTheme(UiRuntimeGuard.toModelTheme(selectedTheme));
            savePlayer();
            applyTheme();
        });

        soundToggleButton = new SmallButton("");
        soundToggleButton.setPreferredSize(new Dimension(104, 32));
        soundToggleButton.addActionListener(e -> toggleSoundSetting());

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightWrap.setOpaque(false);
        saveStatusLabel = new JLabel(" ");
        saveStatusLabel.setFont(FONT_FOOTER);
        rightWrap.add(saveStatusLabel);
        rightWrap.add(soundToggleButton);
        rightWrap.add(themeTogglePanel);

        topBar.add(leftWrap, BorderLayout.WEST);
        topBar.add(rightWrap, BorderLayout.EAST);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(topBar, BorderLayout.CENTER);
        topBorderLine = new JPanel();
        topBorderLine.setPreferredSize(new Dimension(1, 2));
        topBorderLine.setOpaque(true);
        container.add(topBorderLine, BorderLayout.SOUTH);
        mainPanel.add(container, BorderLayout.NORTH);
    }

    private void buildCenter() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(4, 32, 4, 32));

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel topRow = new JPanel(new GridLayout(1, 2, 16, 0));
        topRow.setOpaque(false);
        topRow.setMinimumSize(new Dimension(1000, 230));
        topRow.setPreferredSize(new Dimension(1000, 248));

        playerCard = new GlassCard();
        playerCard.setLayout(new BoxLayout(playerCard, BoxLayout.Y_AXIS));
        playerCard.setBorder(new EmptyBorder(16, 22, 16, 22));
        buildPlayerCard();

        perfCard = new GlassCard();
        perfCard.setLayout(new BoxLayout(perfCard, BoxLayout.Y_AXIS));
        perfCard.setBorder(new EmptyBorder(16, 22, 16, 22));
        buildPerfCard();

        topRow.add(playerCard);
        topRow.add(perfCard);

        JPanel midRow = new JPanel(new GridLayout(1, 2, 16, 0));
        midRow.setOpaque(false);
        midRow.setMinimumSize(new Dimension(1000, 200));
        midRow.setPreferredSize(new Dimension(1000, 220));

        progressCard = new GlassCard();
        progressCard.setLayout(new BoxLayout(progressCard, BoxLayout.Y_AXIS));
        progressCard.setBorder(new EmptyBorder(16, 22, 16, 22));
        buildProgressCard();

        activityCard = new GlassCard();
        activityCard.setLayout(new BoxLayout(activityCard, BoxLayout.Y_AXIS));
        activityCard.setBorder(new EmptyBorder(16, 22, 12, 22));
        buildActivityCard();

        midRow.add(progressCard);
        midRow.add(activityCard);

        JPanel actionsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        actionsRow.setOpaque(false);
        actionsRow.setMinimumSize(new Dimension(1000, 40));
        actionsRow.setPreferredSize(new Dimension(1000, 40));

        startBtn = new ActionButton("START NEW GAME");
        shopBtn = new ActionButton("SHOP");
        highScoresBtn = new ActionButton("VIEW HIGH SCORES");
        logoutBtn = new ActionButton("LOGOUT");
        logoutBtn.setDanger(true);

        startBtn.addActionListener(e -> openLevelSelectionDialog());
        shopBtn.addActionListener(e -> openShopScreen());
        highScoresBtn.addActionListener(e -> openHighScoresScreen());
        logoutBtn.addActionListener(e -> { dispose(); new MainMenuScreen(); });
        installButtonSound(startBtn);
        installButtonSound(shopBtn);
        installButtonSound(highScoresBtn);
        installButtonSound(logoutBtn);

        actionsRow.add(startBtn);
        actionsRow.add(shopBtn);
        actionsRow.add(highScoresBtn);
        actionsRow.add(logoutBtn);

        // Back
        backButton = new JButton(backTarget == BackTarget.PARENT_DASHBOARD
                ? "\u2190  Back to Parent Dashboard"
                : "\u2190  Back to Main Menu");
        backButton.setFont(FONT_BACK);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> openBackTarget());
        installButtonSound(backButton);

        gbc.gridy = 0;
        gbc.weighty = 0.54;
        gbc.insets = new Insets(0, 0, 12, 0);
        center.add(topRow, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.38;
        gbc.insets = new Insets(0, 0, 14, 0);
        center.add(midRow, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 10, 0);
        center.add(actionsRow, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        center.add(backButton, gbc);

        outer.add(center, BorderLayout.CENTER);
        mainPanel.add(outer, BorderLayout.CENTER);
    }

    private void openBackTarget() {
        dispose();
        if (backTarget == BackTarget.PARENT_DASHBOARD) {
            if (parentDashboardUsername != null) {
                new ParentControlsScreen(parentDashboardUsername);
            } else {
                new ParentControlsScreen();
            }
            return;
        }
        new MainMenuScreen();
    }

    private void installEscapeShortcut() {
        JRootPane root = getRootPane();
        if (root == null) {
            return;
        }
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitDashboard");
        root.getActionMap().put("exitDashboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openBackTarget();
            }
        });
    }

    private void buildPlayerCard() {
        JLabel sec = new JLabel("PLAYER INFORMATION");
        sec.setFont(FONT_SECTION); sec.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header row
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        avatarPanel = new AvatarPanel();
        avatarPanel.setPreferredSize(new Dimension(48, 48));
        avatarPanel.setAppearanceId(selectedAvatarAppearanceId);

        JPanel nameCol = new JPanel();
        nameCol.setOpaque(false);
        nameCol.setLayout(new BoxLayout(nameCol, BoxLayout.Y_AXIS));
        playerNameLabel = new JLabel(player.getUsername());
        playerNameLabel.setFont(FONT_PLAYER);
        playerIdLabel = new JLabel(buildPlayerIdText());
        playerIdLabel.setFont(FONT_ID);
        nameCol.add(playerNameLabel);
        nameCol.add(Box.createVerticalStrut(2));
        nameCol.add(playerIdLabel);

        changeAvatarBtn = new SmallButton("Change Avatar");
        changeAvatarBtn.addActionListener(e -> openAvatarPicker());
        installButtonSound(changeAvatarBtn);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        btnWrap.setOpaque(false);
        btnWrap.add(changeAvatarBtn);

        header.add(avatarPanel, BorderLayout.WEST);
        header.add(nameCol, BorderLayout.CENTER);
        header.add(btnWrap, BorderLayout.EAST);

        // Info rows
        String[] labels = {"Current Level", "Coins", "Levels Completed"};
        String[] values = {
                "Level " + selectedLevel,
                formatInteger(player.getCoins()),
                String.valueOf(levelsCompletedValue())
        };
        infoLabels = new JLabel[3];
        infoValues = new JLabel[3];

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 3; i++) {
            infoLabels[i] = new JLabel(labels[i]);
            infoLabels[i].setFont(FONT_LABEL);
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, i == 0 ? 40 : 34));
            row.setBorder(new EmptyBorder(i == 0 ? 9 : 5, 0, i == 0 ? 3 : 5, 0));
            row.add(infoLabels[i], BorderLayout.WEST);

            if (i == 0) {
                JPanel levelNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
                levelNav.setOpaque(false);
                levelNav.setBorder(new EmptyBorder(2, 0, 0, 0));
                levelPrev = new SmallButton("\u2039");
                levelPrev.setPreferredSize(new Dimension(30, 30));
                levelNext = new SmallButton("\u203A");
                levelNext.setPreferredSize(new Dimension(30, 30));
                levelPrev.addActionListener(e -> changeSelectedLevel(-1));
                levelNext.addActionListener(e -> changeSelectedLevel(1));
                installButtonSound(levelPrev);
                installButtonSound(levelNext);
                infoValues[i] = new JLabel(values[i]);
                infoValues[i].setFont(FONT_VALUE);
                infoValues[i].setPreferredSize(new Dimension(88, 30));
                infoValues[i].setHorizontalAlignment(SwingConstants.CENTER);
                levelNav.add(levelPrev);
                levelNav.add(infoValues[i]);
                levelNav.add(levelNext);
                row.add(levelNav, BorderLayout.EAST);
            } else {
                infoValues[i] = new JLabel(values[i]);
                infoValues[i].setFont(FONT_VALUE);
                row.add(infoValues[i], BorderLayout.EAST);
            }
            rows.add(row);
        }

        playerCard.add(sec);
        playerCard.add(Box.createVerticalStrut(10));
        playerCard.add(header);
        playerCard.add(Box.createVerticalStrut(6));
        playerCard.add(rows);
    }

    private void openAvatarPicker() {
        JComboBox<String> avatarCombo = new JComboBox<>(getAvatarNames());
        avatarCombo.setSelectedIndex(getSelectedAvatarIndex());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Choose a default character"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(avatarCombo);

        int result = JOptionPane.showOptionDialog(
                this,
                panel,
                "Change Avatar",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Apply", "Cancel"},
                "Apply"
        );

        if (result == JOptionPane.OK_OPTION) {
            Character selectedAvatar = AVATARS[avatarCombo.getSelectedIndex()];
            selectedAvatarAppearanceId = selectedAvatar.getAppearanceId();
            avatarPanel.setAppearanceId(selectedAvatarAppearanceId);
            player.selectCharacter(new ca.uwo.cs2212.demo.Character(selectedAvatar.getName(), selectedAvatar.getAppearanceId()));
            savePlayer();
            avatarPanel.repaint();
        }
    }

    private String[] getAvatarNames() {
        String[] names = new String[AVATARS.length];
        for (int i = 0; i < AVATARS.length; i++) {
            names[i] = AVATARS[i].getName();
        }
        return names;
    }

    private int getSelectedAvatarIndex() {
        for (int i = 0; i < AVATARS.length; i++) {
            if (AVATARS[i].getAppearanceId().equals(selectedAvatarAppearanceId)) {
                return i;
            }
        }
        return 0;
    }

    private void changeSelectedLevel(int delta) {
        int updatedLevel = Math.max(1, Math.min(highestSelectableLevel(), selectedLevel + delta));
        selectedLevel = updatedLevel;
        updateSavedProgressUI();
    }

    private void updateLevelNavigationControls() {
        if (levelPrev == null || levelNext == null) {
            return;
        }
        int highestSelectable = highestSelectableLevel();
        boolean canMoveDown = selectedLevel > 1;
        boolean canMoveUp = selectedLevel < highestSelectable;
        levelPrev.setEnabled(canMoveDown);
        levelNext.setEnabled(canMoveUp);
        levelPrev.setToolTipText(canMoveDown ? "Select previous unlocked level" : "Level 1 is the lowest selectable level");
        levelNext.setToolTipText(canMoveUp ? "Select next unlocked level" : "No higher level is unlocked yet");
    }

    private void openLevelSelectionDialog() {
        Integer[] levels = new Integer[highestSelectableLevel()];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = i + 1;
        }
        Integer chosenLevel = (Integer) JOptionPane.showInputDialog(
                this,
                "Choose a level to start:",
                "Start New Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                levels,
                levels[0]
        );
        if (chosenLevel != null) {
            openGameplayScreen(chosenLevel, false);
        }
    }

    private void updateSavedProgressUI() {
        if (infoValues != null && infoValues.length > 0 && infoValues[0] != null) {
            infoValues[0].setText("Level " + selectedLevel);
        }
        updateLevelNavigationControls();
        if (infoValues != null && infoValues.length > 2) {
            infoValues[1].setText(formatInteger(player.getCoins()));
            infoValues[2].setText(String.valueOf(levelsCompletedValue()));
        }
        if (progressValues != null && progressValues.length > 1) {
            progressValues[0].setText("Level " + selectedLevel);
            progressValues[1].setText(formatInteger(statisticsValue().getHighScore()));
        }
        float progress = savedProgressValue();
        if (progressBarPercent != null) {
            progressBarPercent.setText(Math.round(progress * 100) + "%");
        }
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
        if (playerNameLabel != null) {
            playerNameLabel.setText(player.getUsername());
        }
        if (playerIdLabel != null) {
            playerIdLabel.setText(buildPlayerIdText());
        }
        if (levelBadge != null) {
            levelBadge.setValue("LEVEL " + highestLevelReached());
        }
        if (statBoxes != null && statBoxes.length == 7) {
            statBoxes[0].setValue(formatInteger(statisticsValue().getHighScore()));
            statBoxes[1].setValue(formatDecimal(statisticsValue().getAvgWPM()));
            statBoxes[2].setValue(formatDecimal(statisticsValue().getPeakWPM()));
            statBoxes[3].setValue(formatPercent(statisticsValue().getAccuracy()));
            statBoxes[4].setValue(PlayerIdentityUi.formatPlayTime(statisticsValue().getTotalTime()));
            statBoxes[5].setValue(formatInteger(statisticsValue().getWordsTyped()));
            statBoxes[6].setValue(formatInteger(statisticsValue().getErrorCount()));
        }
    }

    private void buildPerfCard() {
        JLabel sec = new JLabel("PERFORMANCE OVERVIEW");
        sec.setFont(FONT_SECTION); sec.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statRows = new JPanel();
        statRows.setOpaque(false);
        statRows.setLayout(new BoxLayout(statRows, BoxLayout.Y_AXIS));
        statRows.setAlignmentX(Component.LEFT_ALIGNMENT);
        statRows.setMaximumSize(new Dimension(Integer.MAX_VALUE, 192));

        JPanel statTopRow = new JPanel(new GridLayout(1, 4, 10, 0));
        statTopRow.setOpaque(false);
        statTopRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statTopRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JPanel statBottomRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statBottomRow.setOpaque(false);
        statBottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statBottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        String[][] stats = {
                {"Best Score", formatInteger(statisticsValue().getHighScore()), "All time personal best"},
                {"Average WPM", formatDecimal(statisticsValue().getAvgWPM()), "Across all sessions"},
                {"Best WPM", formatDecimal(statisticsValue().getPeakWPM()), "Single session peak"},
                {"Avg Accuracy", formatPercent(statisticsValue().getAccuracy()), "Correctness across sessions"},
                {"Play Time", PlayerIdentityUi.formatPlayTime(statisticsValue().getTotalTime()), "Total saved game time"},
                {"Correct Words", formatInteger(statisticsValue().getWordsTyped()), "Completed words saved"},
                {"Errors", formatInteger(statisticsValue().getErrorCount()), "Total incorrect keystrokes"}
        };
        statBoxes = new StatBox[stats.length];
        for (int i = 0; i < stats.length; i++) {
            statBoxes[i] = new StatBox(stats[i][0], stats[i][1], stats[i][2]);
            if (i < 4) {
                statTopRow.add(statBoxes[i]);
            } else {
                statBottomRow.add(statBoxes[i]);
            }
        }
        statRows.add(statTopRow);
        statRows.add(Box.createVerticalStrut(10));
        statRows.add(statBottomRow);

        levelBadge = new LevelBadgePanel("HIGHEST LEVEL REACHED", "LEVEL " + highestLevelReached());
        levelBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelBadge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        levelBadge.setPreferredSize(new Dimension(100, 56));

        perfCard.add(sec);
        perfCard.add(Box.createVerticalStrut(8));
        perfCard.add(statRows);
        perfCard.add(Box.createVerticalStrut(8));
        perfCard.add(levelBadge);
    }

    private void buildProgressCard() {
        JLabel sec = new JLabel("SAVED PROGRESS");
        sec.setFont(FONT_SECTION); sec.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] pLabels = {"Current Level", "Best Score"};
        String[] pValues = {"Level " + selectedLevel, formatInteger(statisticsValue().getHighScore())};
        progressLabels = new JLabel[2];
        progressValues = new JLabel[2];

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 2; i++) {
            progressLabels[i] = new JLabel(pLabels[i]);
            progressLabels[i].setFont(FONT_LABEL);
            progressValues[i] = new JLabel(pValues[i]);
            progressValues[i].setFont(FONT_VALUE);
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
            row.setBorder(new EmptyBorder(3, 0, 3, 0));
            row.add(progressLabels[i], BorderLayout.WEST);
            row.add(progressValues[i], BorderLayout.EAST);
            rows.add(row);
        }

        JPanel barSection = new JPanel();
        barSection.setOpaque(false);
        barSection.setLayout(new BoxLayout(barSection, BoxLayout.Y_AXIS));
        barSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        barSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel barLabelRow = new JPanel(new BorderLayout());
        barLabelRow.setOpaque(false);
        barLabelRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressBarLabel = new JLabel("Campaign Progress");
        progressBarLabel.setFont(FONT_LABEL);
        progressBarPercent = new JLabel(Math.round(savedProgressValue() * 100) + "%");
        progressBarPercent.setFont(FONT_PROGRESS);
        barLabelRow.add(progressBarLabel, BorderLayout.WEST);
        barLabelRow.add(progressBarPercent, BorderLayout.EAST);

        progressBar = new ProgressBarPanel(savedProgressValue());
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setPreferredSize(new Dimension(100, 8));

        barSection.add(barLabelRow);
        barSection.add(Box.createVerticalStrut(6));
        barSection.add(progressBar);

        continueBtn = new GoldButton("CONTINUE GAME");
        continueBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        continueBtn.addActionListener(e -> openGameplayScreen(selectedLevel, true));
        installButtonSound(continueBtn);

        progressCard.add(sec);
        progressCard.add(Box.createVerticalStrut(8));
        progressCard.add(rows);
        progressCard.add(Box.createVerticalStrut(8));
        progressCard.add(barSection);
        progressCard.add(Box.createVerticalStrut(10));
        progressCard.add(continueBtn);
    }

    private void buildActivityCard() {
        JLabel sec = new JLabel("RECENT ACTIVITY");
        sec.setFont(FONT_SECTION); sec.setAlignmentX(Component.LEFT_ALIGNMENT);

        DefaultTableModel model = new DefaultTableModel(buildActivityRows(), ACT_COLS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        activityTable = new JTable(model);
        activityTable.setOpaque(false);
        activityTable.setShowGrid(false);
        activityTable.setRowHeight(40);
        activityTable.setFocusable(false);
        activityTable.setRowSelectionAllowed(false);
        activityTable.setColumnSelectionAllowed(false);
        activityTable.setCellSelectionEnabled(false);
        activityTable.setIntercellSpacing(new Dimension(0, 0));
        activityTable.getTableHeader().setReorderingAllowed(false);
        activityTable.getTableHeader().setResizingAllowed(false);

        TableColumnModel cm = activityTable.getColumnModel();
        cm.getColumn(0).setPreferredWidth(30);
        cm.getColumn(1).setPreferredWidth(110);
        cm.getColumn(2).setPreferredWidth(70);
        cm.getColumn(3).setPreferredWidth(50);
        cm.getColumn(4).setPreferredWidth(50);
        cm.getColumn(5).setPreferredWidth(60);

        activityTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int r = activityTable.rowAtPoint(e.getPoint());
                if (r != actHoveredRow) { actHoveredRow = r; activityTable.repaint(); }
            }
        });
        activityTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseExited(MouseEvent e) { actHoveredRow = -1; activityTable.repaint(); }
        });

        JScrollPane sp = new JScrollPane(activityTable);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(100, 148));
        sp.setMinimumSize(new Dimension(100, 148));

        activityCard.add(sec);
        activityCard.add(Box.createVerticalStrut(8));
        activityCard.add(sp);
    }

    private Object[][] buildActivityRows() {
        java.util.List<ca.uwo.cs2212.demo.SessionResult> recentActivity = player.getRecentActivity();
        if (recentActivity.isEmpty()) {
            return new Object[][]{
                    {"-", "No recent sessions yet", "-", "-", "-", "--"}
            };
        }

        int limit = Math.min(3, recentActivity.size());
        Object[][] rows = new Object[limit][ACT_COLS.length];
        for (int i = 0; i < limit; i++) {
            ca.uwo.cs2212.demo.SessionResult result = recentActivity.get(i);
            rows[i][0] = String.valueOf(i + 1);
            rows[i][1] = formatActivityDate(result.getCompletedAt());
            rows[i][2] = formatInteger(result.getScore());
            rows[i][3] = formatDecimal(result.getWpm());
            rows[i][4] = formatPercent(result.getAccuracy());
            rows[i][5] = result.isWon() ? "WIN" : "LOSS";
        }
        return rows;
    }

    private String formatActivityDate(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) {
            return "--";
        }
        try {
            LocalDate date = LocalDate.parse(isoDate);
            return date.getMonth().name().substring(0, 1) + date.getMonth().name().substring(1, 3).toLowerCase() + " "
                    + date.getDayOfMonth() + ", " + date.getYear();
        } catch (DateTimeParseException ex) {
            return isoDate;
        }
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 8));
        footer.setOpaque(false);
        footerLabel = new JLabel(FOOTER_TEXT);
        footerLabel.setFont(FONT_FOOTER);
        footer.add(footerLabel);
        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    // ═══════════════════════════════════════════════════════
    //  THEME
    // ═══════════════════════════════════════════════════════

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        boolean d = darkThemeEnabled;
        Color divider = d ? DARK_DIVIDER : LIGHT_DIVIDER;
        bgPanel.setDark(d);

        navLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        navChipPanel.setBackground(d ? new Color(255,255,255,10) : new Color(255,255,255,145));
        navChipPanel.setForeground(divider);
        topBorderLine.setBackground(divider);

        playerCard.applyTheme(d ? DARK_CARD_BG : LIGHT_CARD_BG, d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER);
        perfCard.applyTheme(d ? DARK_CARD_BG : LIGHT_CARD_BG, d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER);
        progressCard.applyTheme(d ? DARK_CARD_BG : LIGHT_CARD_BG, d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER);
        activityCard.applyTheme(d ? DARK_CARD_BG : LIGHT_CARD_BG, d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER);

        // Section titles
        for (Component c : new Component[]{playerCard.getComponent(0), perfCard.getComponent(0),
                progressCard.getComponent(0), activityCard.getComponent(0)}) {
            if (c instanceof JLabel l) l.setForeground(d ? DARK_ACCENT : LIGHT_ACCENT);
        }

        // Player info
        avatarPanel.applyTheme(d ? DARK_AVATAR_BG : LIGHT_AVATAR_BG, d ? DARK_AVATAR_BORDER : LIGHT_AVATAR_BORDER, d ? DARK_ACCENT : LIGHT_ACCENT);
        playerNameLabel.setForeground(d ? DARK_TEXT : LIGHT_TEXT);
        playerIdLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        changeAvatarBtn.applyTheme(d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER, d ? DARK_BODY : LIGHT_BODY, d ? DARK_ACCENT : LIGHT_ACCENT);
        levelPrev.applyTheme(d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER, d ? DARK_MUTED : LIGHT_MUTED, d ? DARK_ACCENT : LIGHT_ACCENT);
        levelNext.applyTheme(d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER, d ? DARK_MUTED : LIGHT_MUTED, d ? DARK_ACCENT : LIGHT_ACCENT);
        for (int i = 0; i < infoLabels.length; i++) {
            infoLabels[i].setForeground(d ? DARK_LABEL : LIGHT_LABEL);
            infoValues[i].setForeground(d ? DARK_TEXT : LIGHT_TEXT);
        }

        // Performance
        for (StatBox sb : statBoxes)
            sb.applyTheme(d ? DARK_STAT_BG : LIGHT_STAT_BG, d ? DARK_STAT_BORDER : LIGHT_STAT_BORDER,
                    d ? DARK_ACCENT : LIGHT_ACCENT, d ? DARK_TEXT : LIGHT_TEXT, d ? DARK_MUTED : LIGHT_MUTED);
        levelBadge.applyTheme(d ? DARK_BADGE_BG : LIGHT_BADGE_BG, d ? DARK_BADGE_BORDER : LIGHT_BADGE_BORDER,
                d ? DARK_ACCENT : LIGHT_ACCENT, d ? DARK_TEXT : LIGHT_TEXT);

        // Progress
        for (int i = 0; i < 2; i++) {
            progressLabels[i].setForeground(d ? DARK_LABEL : LIGHT_LABEL);
            progressValues[i].setForeground(d ? DARK_TEXT : LIGHT_TEXT);
        }
        // bar label + pct
        progressBarLabel.setForeground(d ? DARK_LABEL : LIGHT_LABEL);
        progressBarPercent.setForeground(d ? DARK_ACCENT : LIGHT_ACCENT);
        progressBar.applyTheme(d ? DARK_PROGRESS_TRACK : LIGHT_PROGRESS_TRACK, d ? DARK_ACCENT : LIGHT_ACCENT);
        continueBtn.applyTheme(d ? DARK_ACCENT : LIGHT_ACCENT, d ? DARK_BG : new Color(255,255,255));

        // Activity table
        activityTable.getTableHeader().setDefaultRenderer(new ActHeaderRenderer());
        for (int i = 0; i < activityTable.getColumnCount(); i++)
            activityTable.getColumnModel().getColumn(i).setCellRenderer(new ActCellRenderer());

        // Action buttons
        startBtn.applyTheme(d ? DARK_BTN_BG : LIGHT_BTN_BG, d ? DARK_BTN_BORDER : LIGHT_BTN_BORDER,
                d ? DARK_TEXT : LIGHT_TEXT, d ? DARK_BTN_HOVER_BG : LIGHT_BTN_HOVER_BG, d ? DARK_BTN_HOVER_BDR : LIGHT_BTN_HOVER_BDR);
        shopBtn.applyTheme(d ? DARK_BTN_BG : LIGHT_BTN_BG, d ? DARK_BTN_BORDER : LIGHT_BTN_BORDER,
                d ? DARK_TEXT : LIGHT_TEXT, d ? DARK_BTN_HOVER_BG : LIGHT_BTN_HOVER_BG, d ? DARK_BTN_HOVER_BDR : LIGHT_BTN_HOVER_BDR);
        highScoresBtn.applyTheme(d ? DARK_BTN_BG : LIGHT_BTN_BG, d ? DARK_BTN_BORDER : LIGHT_BTN_BORDER,
                d ? DARK_TEXT : LIGHT_TEXT, d ? DARK_BTN_HOVER_BG : LIGHT_BTN_HOVER_BG, d ? DARK_BTN_HOVER_BDR : LIGHT_BTN_HOVER_BDR);
        logoutBtn.applyTheme(d ? DARK_BTN_BG : LIGHT_BTN_BG, d ? DARK_DANGER_BDR : LIGHT_DANGER_BDR,
                d ? DARK_DANGER : LIGHT_DANGER, d ? new Color(184,84,80,15) : new Color(158,58,55,10),
                d ? new Color(184,84,80,90) : new Color(158,58,55,90));
        soundToggleButton.setText(soundEnabled() ? "SOUND ON" : "SOUND OFF");
        soundToggleButton.applyTheme(
                d ? DARK_BTN_BORDER : LIGHT_BTN_BORDER,
                soundEnabled() ? (d ? DARK_ACCENT : LIGHT_ACCENT) : (d ? DARK_MUTED : LIGHT_MUTED),
                soundEnabled() ? (d ? DARK_ACCENT : LIGHT_ACCENT) : (d ? DARK_BODY : LIGHT_BODY)
        );

        backButton.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        footerLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);

        themeTogglePanel.applyTheme(d ? DARK_TOGGLE_BG : LIGHT_TOGGLE_BG, d ? DARK_TOGGLE_BDR : LIGHT_TOGGLE_BDR,
                d ? DARK_MUTED : LIGHT_MUTED, d ? DARK_ACCENT : LIGHT_ACCENT, d ? DARK_BG : LIGHT_BG);
        themeTogglePanel.setDarkSelected(d);

        bgPanel.repaint(); mainPanel.repaint();
    }

    // ═══════════════════════════════════════════════════════
    //  TABLE RENDERERS
    // ═══════════════════════════════════════════════════════

    private class ActHeaderRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
            boolean darkThemeEnabled = ThemeManager.isDark();
            l.setFont(FONT_TH);
            l.setForeground(darkThemeEnabled ? DARK_ACCENT : LIGHT_ACCENT);
            l.setBackground(darkThemeEnabled ? DARK_STAT_BG : LIGHT_STAT_BG);
            l.setOpaque(true);
            l.setBorder(new EmptyBorder(8, 12, 8, 12));
            return l;
        }
    }

    private class ActCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int row, int col) {
            String val = v.toString();
            boolean darkThemeEnabled = ThemeManager.isDark();
            JLabel l = new JLabel(val) {
                @Override protected void paintComponent(Graphics g) {
                    if (row == actHoveredRow) {
                        g.setColor(darkThemeEnabled ? DARK_ROW_HOVER : LIGHT_ROW_HOVER);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                    if (row < activityTable.getRowCount() - 1) {
                        g.setColor(darkThemeEnabled ? DARK_ROW_BORDER : LIGHT_ROW_BORDER);
                        g.fillRect(0, getHeight()-1, getWidth(), 1);
                    }
                    super.paintComponent(g);
                }
            };
            l.setFont(FONT_TD);
            l.setOpaque(false);
            l.setBorder(new EmptyBorder(0, 12, 0, 12));

            if (val.equals("WIN")) l.setForeground(darkThemeEnabled ? DARK_WIN : LIGHT_WIN);
            else if (val.equals("LOSS")) l.setForeground(darkThemeEnabled ? DARK_LOSS : LIGHT_LOSS);
            else l.setForeground(darkThemeEnabled ? DARK_BODY : LIGHT_BODY);

            return l;
        }
    }

    // ═══════════════════════════════════════════════════════
    //  CUSTOM COMPONENTS
    // ═══════════════════════════════════════════════════════

    private static class GradientBackgroundPanel extends JPanel {
        private boolean dark = true;
        void setDark(boolean d) { dark = d; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();
            g2.setColor(dark ? DARK_BG : LIGHT_BG); g2.fillRect(0, 0, w, h);
            Color gl1 = dark ? new Color(201,165,90,12) : new Color(139,105,20,8);
            g2.setPaint(new RadialGradientPaint(w*.2f, h*.15f, w*.6f, new float[]{0,1}, new Color[]{gl1, new Color(0,0,0,0)}));
            g2.fillRect(0,0,w,h);
            Color gl2 = dark ? new Color(201,165,90,6) : new Color(139,105,20,4);
            g2.setPaint(new RadialGradientPaint(w*.8f, h*.85f, w*.5f, new float[]{0,1}, new Color[]{gl2, new Color(0,0,0,0)}));
            g2.fillRect(0,0,w,h);
            g2.setColor(dark ? new Color(232,228,220,10) : new Color(26,26,26,10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50,50,50,120); g2.drawLine(50,50,120,50);
            g2.drawLine(w-50,h-50,w-50,h-120); g2.drawLine(w-50,h-50,w-120,h-50);
            g2.dispose();
        }
    }

    private static class GlassCard extends JPanel {
        private Color bg = new Color(255,255,255,8), border = new Color(255,255,255,15);
        GlassCard() { setOpaque(false); }
        void applyTheme(Color bg, Color b) { this.bg = bg; this.border = b; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape s = new RoundRectangle2D.Float(0,0,getWidth()-1,getHeight()-1,18,18);
            g2.setColor(bg); g2.fill(s); g2.setColor(border); g2.setStroke(new BasicStroke(1)); g2.draw(s);
            g2.dispose(); super.paintComponent(g);
        }
    }

    private static class AvatarPanel extends JPanel {
        private Color bg, border, icon;
        private String appearanceId = "bunny";
        AvatarPanel() { setOpaque(false); }
        void applyTheme(Color bg, Color b, Color ic) { this.bg = bg; this.border = b; this.icon = ic; repaint(); }
        void setAppearanceId(String appearanceId) { this.appearanceId = appearanceId; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int s = Math.min(getWidth(), getHeight());
            g2.setColor(bg != null ? bg : new Color(201,165,90,20));
            g2.fill(new Ellipse2D.Float(0,0,s,s));
            g2.setColor(border != null ? border : new Color(201,165,90,50));
            g2.setStroke(new BasicStroke(2)); g2.draw(new Ellipse2D.Float(1,1,s-2,s-2));
            drawAvatar(g2, s);
            g2.dispose();
        }

        private void drawAvatar(Graphics2D g2, int s) {
            PlayerIdentityUi.drawAvatar(g2, 0, 0, s, appearanceId, icon != null ? icon : new Color(201, 165, 90));
        }
    }

    private static class StatBox extends JPanel {
        private final String label, sub;
        private String value;
        private Color bg, border, labelC, valueC, subC;
        StatBox(String l, String v, String s) { label = l; value = v; sub = s; setOpaque(false); }
        void setValue(String value) { this.value = value; repaint(); }
        void applyTheme(Color bg, Color b, Color lbl, Color val, Color sub) {
            this.bg = bg; border = b; labelC = lbl; valueC = val; subC = sub; repaint();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bg); g2.fill(new RoundRectangle2D.Float(0,0,w,h,14,14));
            g2.setColor(border); g2.setStroke(new BasicStroke(1)); g2.draw(new RoundRectangle2D.Float(.5f,.5f,w-1,h-1,14,14));
            int x = 14;
            int y = 16;
            Font labelFont = FONT_STAT_LBL.deriveFont(11f);
            Font valueFont = FONT_STAT_VAL.deriveFont(22f);
            Font subFont = FONT_STAT_SUB.deriveFont(10f);
            g2.setFont(labelFont); g2.setColor(labelC); g2.drawString(label.toUpperCase(), x, y);
            y += 20;
            g2.setFont(valueFont); g2.setColor(valueC); g2.drawString(value, x, y);
            y += 12;
            g2.setFont(subFont); g2.setColor(subC);
            FontMetrics subMetrics = g2.getFontMetrics();
            String shortened = sub;
            while (subMetrics.stringWidth(shortened) > w - (x * 2) && shortened.length() > 4) {
                shortened = shortened.substring(0, shortened.length() - 4).trim() + "...";
            }
            g2.drawString(shortened, x, Math.min(h - 10, y + subMetrics.getAscent()));
            g2.dispose();
        }
    }

    private static class LevelBadgePanel extends JPanel {
        private final String label;
        private String value;
        private Color bg, border, labelC, valueC;
        LevelBadgePanel(String l, String v) { label = l; value = v; setOpaque(false); }
        void setValue(String value) { this.value = value; repaint(); }
        void applyTheme(Color bg, Color b, Color lbl, Color val) {
            this.bg = bg; border = b; labelC = lbl; valueC = val; repaint();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bg); g2.fill(new RoundRectangle2D.Float(0,0,w,h,14,14));
            g2.setColor(border); g2.setStroke(new BasicStroke(1)); g2.draw(new RoundRectangle2D.Float(.5f,.5f,w-1,h-1,14,14));
            g2.setFont(FONT_BADGE_LBL.deriveFont(11f)); g2.setColor(labelC); g2.drawString(label, 16, 20);
            g2.setFont(FONT_BADGE_VAL.deriveFont(24f)); g2.setColor(valueC); g2.drawString(value, 16, 44);
            g2.dispose();
        }
    }

    private static Player loadExistingPlayer(String username) {
        String safeUsername = requireUsername(username);
        SaveManager saveManager = new SaveManager(new FileStorageService());
        Player loaded = saveManager.loadPlayer(safeUsername);
        if (loaded != null) {
            return loaded;
        }
        throw new IllegalStateException("No saved player found for username: " + safeUsername);
    }

    private static String requireUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        return username.trim();
    }

    private void savePlayer() {
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
        saveStatusLabel.setForeground(ThemeManager.isDark() ? DARK_ACCENT : LIGHT_ACCENT);
        if (saveStatusTimer != null && saveStatusTimer.isRunning()) {
            saveStatusTimer.stop();
        }
        saveStatusTimer = new Timer(2200, e -> {
            saveStatusLabel.setText(" ");
            saveStatusLabel.setForeground(ThemeManager.isDark() ? DARK_MUTED : LIGHT_MUTED);
        });
        saveStatusTimer.setRepeats(false);
        saveStatusTimer.start();
    }

    private void openGameplayScreen(int level, boolean resumeFromCheckpoint) {
        try {
            new GameplayScreen(username, level, resumeFromCheckpoint);
            dispose();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "start gameplay", ex);
                return;
            }
            throw ex;
        }
    }

    private void openShopScreen() {
        try {
            new ShopScreen(username);
            dispose();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "open the shop", ex);
                return;
            }
            throw ex;
        }
    }

    private void openHighScoresScreen() {
        try {
            new HighScoresScreen(username);
            dispose();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "open the high score table", ex);
                return;
            }
            throw ex;
        }
    }

    private Statistics statisticsValue() {
        return player.getStatistics();
    }

    private boolean soundEnabled() {
        return progressValue().getSettings().isSoundOn();
    }

    private void toggleSoundSetting() {
        progressValue().getSettings().toggleSound();
        SoundManager.setEnabled(soundEnabled());
        savePlayer();
        applyTheme();
    }

    private void installButtonSound(AbstractButton button) {
        button.addActionListener(e -> SoundManager.playClick());
    }

    private Progress progressValue() {
        return player.getProgress();
    }

    private void normalizeProgressLevelState() {
        Progress progress = progressValue();
        int recommendedLevel = Math.max(1, Math.min(MAX_LEVEL, levelsCompletedValue() + 1));
        int persistedLevel = Math.max(1, Math.min(MAX_LEVEL, progress.getCurrentLevel()));
        if (persistedLevel < recommendedLevel) {
            progress.setCurrentLevel(recommendedLevel);
            savePlayer();
        }
    }

    private int currentLevelValue() {
        return Math.max(1, Math.min(MAX_LEVEL, progressValue().getCurrentLevel()));
    }

    private int levelsCompletedValue() {
        return Math.max(0, progressValue().getLevelsCompleted());
    }

    private int highestLevelReached() {
        return Math.max(1, Math.min(MAX_LEVEL, Math.max(currentLevelValue(), levelsCompletedValue())));
    }

    private float savedProgressValue() {
        return Math.max(0f, Math.min(1f, levelsCompletedValue() / (float) MAX_LEVEL));
    }

    private int highestSelectableLevel() {
        return Math.max(1, Math.min(MAX_LEVEL, Math.max(highestLevelReached(), levelsCompletedValue() + 1)));
    }

    private int totalPowerUpsOwned() {
        List<PowerUp> powerUps = player.getInventory().getAvailable();
        int total = 0;
        for (PowerUp powerUp : powerUps) {
            if (powerUp != null) {
                total += Math.max(0, powerUp.getCount());
            }
        }
        return total;
    }

    private String powerUpSummary() {
        int freeze = player.getInventory().getCount(ca.uwo.cs2212.demo.PowerUpType.FREEZE);
        int shield = player.getInventory().getCount(ca.uwo.cs2212.demo.PowerUpType.SHIELD);
        int bonus = player.getInventory().getCount(ca.uwo.cs2212.demo.PowerUpType.DOUBLE_SCORE);
        return "Freeze x" + freeze + "  Shield x" + shield + "  Double Score x" + bonus;
    }

    private String buildPlayerIdText() {
        return PlayerIdentityUi.buildPlayerIdText(player);
    }

    private String resolveAvatarAppearanceId(Player player) {
        ca.uwo.cs2212.demo.Character selectedCharacter = player.getSelectedCharacter();
        if (selectedCharacter == null || selectedCharacter.getAppearanceId() == null) {
            return AVATARS[0].getAppearanceId();
        }
        String appearanceId = selectedCharacter.getAppearanceId();
        for (Character avatar : AVATARS) {
            if (avatar.getAppearanceId().equals(appearanceId)) {
                return appearanceId;
            }
        }
        return AVATARS[0].getAppearanceId();
    }

    private String formatInteger(int value) {
        return String.format("%,d", value);
    }

    private String formatDecimal(double value) {
        return String.format("%.0f", value);
    }

    private String formatPercent(double value) {
        return String.format("%.0f%%", value);
    }

    private static class ProgressBarPanel extends JPanel {
        private float pct;
        private Color track, fill;
        ProgressBarPanel(float pct) { this.pct = pct; setOpaque(false); }
        void applyTheme(Color t, Color f) { track = t; fill = f; repaint(); }
        void setProgress(float pct) { this.pct = Math.max(0f, Math.min(1f, pct)); repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(track); g2.fillRoundRect(0, 0, w, h, h, h);
            int fw = (int)(w * pct);
            if (fill != null) {
                g2.setPaint(new GradientPaint(0, 0, fill, fw, 0,
                        new Color(Math.min(255,fill.getRed()+30), Math.min(255,fill.getGreen()+20), fill.getBlue())));
            }
            g2.fillRoundRect(0, 0, fw, h, h, h);
            g2.dispose();
        }
    }

    private static class GoldButton extends JButton {
        private Color bg, text;
        private boolean hovered;
        GoldButton(String t) {
            super(t); setFont(FONT_CONTINUE); setFocusPainted(false); setBorderPainted(false);
            setContentAreaFilled(false); setOpaque(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }
        void applyTheme(Color bg, Color text) { this.bg = bg; this.text = text; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color b = hovered ? new Color(Math.min(255,bg.getRed()+20),Math.min(255,bg.getGreen()+15),bg.getBlue()) : bg;
            g2.setColor(b); g2.fill(new RoundRectangle2D.Float(0,0,w,h,14,14));
            g2.setFont(getFont()); g2.setColor(text);
            FontMetrics fm = g2.getFontMetrics(); String s = getText();
            g2.drawString(s, (w-fm.stringWidth(s))/2, h/2+fm.getAscent()/2-1);
            g2.dispose();
        }
    }

    private static class SmallButton extends JButton {
        private Color border, text, hoverText;
        private boolean hovered;
        SmallButton(String t) {
            super(t); setFont(FONT_SMALL_BTN); setFocusPainted(false); setBorderPainted(false);
            setContentAreaFilled(false); setOpaque(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }
        void applyTheme(Color b, Color t, Color ht) { border = b; text = t; hoverText = ht; repaint(); }
        @Override public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            repaint();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), arc = Math.min(h, 20);
            boolean interactive = isEnabled();
            Color idleBorder = border != null ? border : Color.GRAY;
            Color idleText = text != null ? text : Color.WHITE;
            Color disabledBorder = new Color(idleBorder.getRed(), idleBorder.getGreen(), idleBorder.getBlue(), 70);
            Color disabledText = new Color(idleText.getRed(), idleText.getGreen(), idleText.getBlue(), 90);
            boolean showHover = interactive && hovered && hoverText != null;
            g2.setColor(showHover ? new Color(hoverText.getRed(),hoverText.getGreen(),hoverText.getBlue(),20) : new Color(0,0,0,0));
            g2.fill(new RoundRectangle2D.Float(0,0,w,h,arc,arc));
            g2.setColor(showHover ? hoverText : (interactive ? idleBorder : disabledBorder));
            g2.setStroke(new BasicStroke(1)); g2.draw(new RoundRectangle2D.Float(.5f,.5f,w-1,h-1,arc,arc));
            g2.setFont(getFont()); g2.setColor(showHover ? hoverText : (interactive ? idleText : disabledText));
            FontMetrics fm = g2.getFontMetrics(); String s = getText();
            g2.drawString(s, (w-fm.stringWidth(s))/2, h/2+fm.getAscent()/2-1);
            g2.dispose();
        }
    }

    private static class ActionButton extends JButton {
        private Color bg, border, text, hoverBg, hoverBorder;
        private boolean hovered, danger;
        private float hoverProg;
        private Timer hoverTimer;
        ActionButton(String t) {
            super(t); setFont(FONT_ACT_BTN); setFocusPainted(false); setBorderPainted(false);
            setContentAreaFilled(false); setOpaque(false); setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; anim(true); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; anim(false); }
            });
        }
        void setDanger(boolean d) { danger = d; }
        void applyTheme(Color bg, Color b, Color t, Color hbg, Color hb) {
            this.bg = bg; border = b; text = t; hoverBg = hbg; hoverBorder = hb; repaint();
        }
        private void anim(boolean enter) {
            if (hoverTimer != null) hoverTimer.stop();
            hoverTimer = new Timer(16, e -> {
                hoverProg = enter ? Math.min(1, hoverProg + .13f) : Math.max(0, hoverProg - .13f);
                repaint();
                if ((enter && hoverProg >= 1) || (!enter && hoverProg <= 0)) ((Timer)e.getSource()).stop();
            }); hoverTimer.start();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), arc = 16;
            Color cbg = interp(bg, hoverBg, hoverProg);
            Color cbd = interp(border, hoverBorder, hoverProg);
            g2.setColor(cbg); g2.fill(new RoundRectangle2D.Float(0,0,w,h,arc,arc));
            if (hoverProg > 0) {
                Color sweep = danger ? new Color(184,84,80,(int)(15*hoverProg)) : new Color(201,165,90,(int)(12*hoverProg));
                g2.setPaint(new GradientPaint(0,0,sweep,(int)(w*hoverProg),0,new Color(0,0,0,0)));
                g2.fill(new RoundRectangle2D.Float(0,0,w,h,arc,arc));
            }
            g2.setColor(cbd); g2.setStroke(new BasicStroke(1)); g2.draw(new RoundRectangle2D.Float(.5f,.5f,w-1,h-1,arc,arc));
            g2.setFont(getFont()); g2.setColor(text);
            FontMetrics fm = g2.getFontMetrics(); String s = getText();
            g2.drawString(s, (w-fm.stringWidth(s))/2, h/2+fm.getAscent()/2-1);
            g2.dispose();
        }
        private Color interp(Color a, Color b, float t) {
            if (a == null || b == null) return a != null ? a : b;
            return new Color(cl((int)(a.getRed()+(b.getRed()-a.getRed())*t)),cl((int)(a.getGreen()+(b.getGreen()-a.getGreen())*t)),
                    cl((int)(a.getBlue()+(b.getBlue()-a.getBlue())*t)),cl((int)(a.getAlpha()+(b.getAlpha()-a.getAlpha())*t)));
        }
        private int cl(int v) { return Math.max(0,Math.min(255,v)); }
    }

    private static class ThemeTogglePanel extends JPanel {
        private boolean darkSelected = true;
        private final JLabel lightLabel, darkLabel;
        private Color bgColor, borderColor, mutedColor, selectedColor, selectedTextColor;
        private java.util.function.Consumer<Boolean> listener;
        ThemeTogglePanel() {
            setOpaque(false); setPreferredSize(new Dimension(194,40));
            setMaximumSize(new Dimension(194,40)); setMinimumSize(new Dimension(194,40));
            setLayout(new GridLayout(1,2,0,0)); setCursor(new Cursor(Cursor.HAND_CURSOR));
            lightLabel = new JLabel("Light", SwingConstants.CENTER);
            darkLabel = new JLabel("Dark", SwingConstants.CENTER);
            lightLabel.setFont(FONT_TOGGLE); darkLabel.setFont(FONT_TOGGLE);
            lightLabel.setOpaque(false); darkLabel.setOpaque(false);
            add(lightLabel); add(darkLabel);
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    darkSelected = e.getX() >= getWidth()/2; repaint();
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), arc = h;
            g2.setColor(bgColor != null ? bgColor : new Color(255,255,255,10));
            g2.fillRoundRect(0,0,w,h,arc,arc);
            g2.setColor(borderColor != null ? borderColor : new Color(255,255,255,20));
            g2.setStroke(new BasicStroke(1)); g2.drawRoundRect(0,0,w-1,h-1,arc,arc);
            int pw = w/2, px = darkSelected ? pw-1 : 3;
            g2.setColor(selectedColor != null ? selectedColor : new Color(201,165,90));
            g2.fillRoundRect(px,3,pw-2,h-6,h-6,h-6); g2.dispose();
            Color sc = selectedTextColor != null ? selectedTextColor : Color.BLACK;
            Color uc = mutedColor != null ? mutedColor : new Color(200,200,200,120);
            lightLabel.setForeground(!darkSelected ? sc : uc);
            darkLabel.setForeground(darkSelected ? sc : uc);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");
            super.paintComponent(g);
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int arc;
        RoundedPanel(int arc) { this.arc = arc; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),arc,arc);
            if (getForeground() != null) { g2.setColor(getForeground()); g2.fillRoundRect(0,0,4,getHeight(),4,4); }
            g2.dispose(); super.paintComponent(g);
        }
    }
}
