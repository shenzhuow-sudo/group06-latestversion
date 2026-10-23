package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.ParentLinkStore;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SaveManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent dashboard for reviewing linked players and managing restricted account actions.
 */
public class ParentControlsScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";

    private static final Color DARK_BG = new Color(10, 10, 15);
    private static final Color DARK_TEXT = new Color(232, 228, 220);
    private static final Color DARK_MUTED = new Color(232, 228, 220, 115);
    private static final Color DARK_LABEL = new Color(232, 228, 220, 128);
    private static final Color DARK_ACCENT = new Color(201, 165, 90);
    private static final Color DARK_CARD_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_CARD_BORDER = new Color(255, 255, 255, 15);
    private static final Color DARK_BTN_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_BTN_BORDER = new Color(255, 255, 255, 15);
    private static final Color DARK_BTN_HOVER_BG = new Color(201, 165, 90, 15);
    private static final Color DARK_BTN_HOVER_BDR = new Color(201, 165, 90, 64);
    private static final Color DARK_TOGGLE_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR = new Color(255, 255, 255, 15);
    private static final Color DARK_DANGER = new Color(184, 84, 80);
    private static final Color DARK_DANGER_BDR = new Color(184, 84, 80, 64);
    private static final Color DARK_TRACK = new Color(255, 255, 255, 15);
    private static final Color DARK_SLIDER = new Color(201, 165, 90, 70);

    private static final Color LIGHT_BG = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED = new Color(26, 26, 26, 102);
    private static final Color LIGHT_LABEL = new Color(26, 26, 26, 115);
    private static final Color LIGHT_ACCENT = new Color(139, 105, 20);
    private static final Color LIGHT_CARD_BG = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER = new Color(0, 0, 0, 18);
    private static final Color LIGHT_BTN_BG = new Color(0, 0, 0, 6);
    private static final Color LIGHT_BTN_BORDER = new Color(0, 0, 0, 25);
    private static final Color LIGHT_BTN_HOVER_BG = new Color(139, 105, 20, 12);
    private static final Color LIGHT_BTN_HOVER_BDR = new Color(139, 105, 20, 64);
    private static final Color LIGHT_TOGGLE_BG = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR = new Color(0, 0, 0, 20);
    private static final Color LIGHT_DANGER = new Color(158, 58, 55);
    private static final Color LIGHT_DANGER_BDR = new Color(158, 58, 55, 64);
    private static final Color LIGHT_TRACK = new Color(0, 0, 0, 18);
    private static final Color LIGHT_SLIDER = new Color(139, 105, 20, 70);

    private static final Font FONT_SECTION = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_TOGGLE = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_FOOTER = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_PLAYER = new Font("Serif", Font.BOLD, 18);
    private static final Font FONT_ID = new Font("Monospaced", Font.PLAIN, 11);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_VALUE = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT_STAT_LBL = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_STAT_VAL = new Font("Monospaced", Font.BOLD, 24);
    private static final Font FONT_STAT_SUB = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_ACTION = new Font("Serif", Font.BOLD, 12);
    private static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TABLE_TH = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_TABLE_TD = new Font("Monospaced", Font.PLAIN, 12);
    private static final String[] ACTIVITY_COLS = {"#", "Date", "Action", "Status"};

    private final String parentUsername;
    private final SaveManager saveManager = new SaveManager(new FileStorageService());
    private final ParentLinkStore parentLinkStore = new ParentLinkStore(new FileStorageService());
    private final List<PlayerProfile> linkedPlayers = new ArrayList<>();
    private int selectedPlayerIndex;

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private ThemeTogglePanel themeTogglePanel;

    private GlassCard adminCard;
    private GlassCard perfCard;
    private GlassCard accountCard;
    private GlassCard activityCard;
    private JLabel adminSectionLabel;
    private JLabel perfSectionLabel;
    private JLabel accountSectionLabel;
    private JLabel activitySectionLabel;
    private JLabel playerSelectTitleLabel;
    private JLabel linkTitleLabel;
    private JLabel perfPlayerContextLabel;

    private JLabel[] adminInfoLabels;
    private JLabel[] adminInfoValues;
    private JLabel adminNameLabel;
    private JLabel adminIdLabel;
    private AvatarPanel avatarPanel;

    private StatBox[] statBoxes;
    private JLabel highestLevelLabel;
    private JLabel highestLevelValue;
    private JComboBox<String> playerSelector;
    private JComboBox<String> availablePlayerSelector;
    private JLabel selectedPlayerLabel;
    private JButton linkAccountButton;
    private JButton unlinkAccountButton;
    private JButton createPlayerButton;
    private JButton resetPasswordButton;
    private JButton resetStatsButton;
    private JButton resetHighScoresButton;
    private JButton openHighScoresButton;
    private JButton playerDashboardButton;
    private JButton logoutButton;
    private JTable activityTable;
    private DefaultTableModel activityTableModel;
    private JLabel footerLabel;

    /**
     * Creates the parent dashboard using the default administrator label.
     */
    public ParentControlsScreen() {
        this("Guardian Admin");
    }

    /**
     * Creates the parent dashboard for the supplied parent account.
     *
     * @param parentUsername parent username shown in the dashboard context
     */
    public ParentControlsScreen(String parentUsername) {
        this.parentUsername = (parentUsername == null || parentUsername.isBlank()) ? "Guardian Admin" : parentUsername.trim();
        SoundManager.setEnabled(true);
        setTitle("The Hunt - Parent Dashboard");
        WindowSizing.applyFixedScreenSafeSize(this, 1240, 930);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        reloadLinkedPlayers();
        buildUI();
        updateSelectedPlayerUI();
        applyTheme();
        setContentPane(bgPanel);
        setVisible(true);
        installEscapeShortcut();
    }

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

        navLabel = new JLabel("THE HUNT / PARENT DASHBOARD");
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
            ThemeManager.setTheme(d ? Theme.DARK : Theme.LIGHT);
            applyTheme();
        });

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrap.setOpaque(false);
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

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel topRow = new JPanel(new GridLayout(1, 2, 16, 0));
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1000, 250));

        adminCard = new GlassCard();
        adminCard.setLayout(new BoxLayout(adminCard, BoxLayout.Y_AXIS));
        adminCard.setBorder(new EmptyBorder(16, 22, 16, 22));
        buildAdminCard();

        perfCard = new GlassCard();
        perfCard.setLayout(new BoxLayout(perfCard, BoxLayout.Y_AXIS));
        perfCard.setBorder(new EmptyBorder(16, 22, 16, 22));
        buildPerfCard();

        topRow.add(adminCard);
        topRow.add(perfCard);

        JPanel midRow = new JPanel(new GridLayout(1, 2, 16, 0));
        midRow.setOpaque(false);
        midRow.setPreferredSize(new Dimension(1000, 244));

        accountCard = new GlassCard();
        accountCard.setLayout(new BoxLayout(accountCard, BoxLayout.Y_AXIS));
        accountCard.setBorder(new EmptyBorder(16, 22, 14, 22));
        buildAccountCard();

        activityCard = new GlassCard();
        activityCard.setLayout(new BoxLayout(activityCard, BoxLayout.Y_AXIS));
        activityCard.setBorder(new EmptyBorder(16, 22, 12, 22));
        buildActivityCard();

        midRow.add(accountCard);
        midRow.add(activityCard);

        JPanel actionsRow = new JPanel(new GridLayout(1, 2, 12, 0));
        actionsRow.setOpaque(false);
        actionsRow.setPreferredSize(new Dimension(1000, 36));

        playerDashboardButton = new ActionButton("SWITCH TO PLAYER DASHBOARD");
        logoutButton = new ActionButton("LOG OUT");

        playerDashboardButton.addActionListener(e -> openParentPlayerDashboard());
        playerDashboardButton.addActionListener(e -> SoundManager.playClick());
        logoutButton.addActionListener(e -> {
            dispose();
            new MainMenuScreen();
        });
        logoutButton.addActionListener(e -> SoundManager.playClick());

        actionsRow.add(playerDashboardButton);
        actionsRow.add(logoutButton);

        gbc.gridy = 0;
        gbc.weighty = 0.52;
        gbc.insets = new Insets(0, 0, 12, 0);
        center.add(topRow, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.48;
        gbc.insets = new Insets(0, 0, 14, 0);
        center.add(midRow, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        center.add(actionsRow, gbc);

        outer.add(center, BorderLayout.CENTER);
        mainPanel.add(outer, BorderLayout.CENTER);
    }

    private void buildAdminCard() {
        adminSectionLabel = new JLabel("PARENT / TEACHER ADMIN");
        adminSectionLabel.setFont(FONT_SECTION);
        adminSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        avatarPanel = new AvatarPanel();
        avatarPanel.setPreferredSize(new Dimension(48, 48));

        JPanel nameCol = new JPanel();
        nameCol.setOpaque(false);
        nameCol.setLayout(new BoxLayout(nameCol, BoxLayout.Y_AXIS));
        adminNameLabel = new JLabel(parentUsername);
        adminNameLabel.setFont(FONT_PLAYER);
        adminIdLabel = new JLabel("ID: #PRT-" + String.format("%04d", Math.abs(parentUsername.hashCode()) % 10000));
        adminIdLabel.setFont(FONT_ID);
        nameCol.add(adminNameLabel);
        nameCol.add(Box.createVerticalStrut(2));
        nameCol.add(adminIdLabel);

        header.add(avatarPanel, BorderLayout.WEST);
        header.add(nameCol, BorderLayout.CENTER);

        String[] labels = {"Account Role", "Managed Players", "Linked Status"};
        String[] values = {"Parent / Teacher", String.valueOf(linkedPlayers.size()), linkedPlayers.isEmpty() ? "No linked players" : "Active"};
        adminInfoLabels = new JLabel[3];
        adminInfoValues = new JLabel[3];

        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 3; i++) {
            adminInfoLabels[i] = new JLabel(labels[i]);
            adminInfoLabels[i].setFont(FONT_LABEL);
            adminInfoValues[i] = new JLabel(values[i]);
            adminInfoValues[i].setFont(FONT_VALUE);

            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
            row.setBorder(new EmptyBorder(3, 0, 3, 0));
            row.add(adminInfoLabels[i], BorderLayout.WEST);
            row.add(adminInfoValues[i], BorderLayout.EAST);
            rows.add(row);
        }

        adminCard.add(adminSectionLabel);
        adminCard.add(Box.createVerticalStrut(8));
        adminCard.add(header);
        adminCard.add(Box.createVerticalStrut(4));
        adminCard.add(rows);
    }

    private void buildPerfCard() {
        perfSectionLabel = new JLabel("PLAYER MONITORING OVERVIEW");
        perfSectionLabel.setFont(FONT_SECTION);
        perfSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerRow.add(perfSectionLabel, BorderLayout.WEST);

        perfPlayerContextLabel = new JLabel();
        perfPlayerContextLabel.setFont(FONT_LABEL);
        perfPlayerContextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statRows = new JPanel();
        statRows.setOpaque(false);
        statRows.setLayout(new BoxLayout(statRows, BoxLayout.Y_AXIS));
        statRows.setAlignmentX(Component.LEFT_ALIGNMENT);
        statRows.setMaximumSize(new Dimension(Integer.MAX_VALUE, 188));

        JPanel statTopRow = new JPanel(new GridLayout(1, 4, 10, 0));
        statTopRow.setOpaque(false);
        statTopRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statTopRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JPanel statBottomRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statBottomRow.setOpaque(false);
        statBottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statBottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        String[][] stats = {
                {"Best Score", "0", "All time personal best"},
                {"Average WPM", "0", "Across all sessions"},
                {"Best WPM", "0", "Single session peak"},
                {"Avg Accuracy", "0%", "Correctness across sessions"},
                {"Play Time", "0m 00s", "Total saved game time"},
                {"Correct Words", "0", "Completed words saved"},
                {"Errors", "0", "Total incorrect keystrokes"}
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

        JPanel highestLevelPanel = new JPanel(new BorderLayout());
        highestLevelPanel.setOpaque(false);
        highestLevelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        highestLevelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        highestLevelLabel = new JLabel("HIGHEST LEVEL REACHED");
        highestLevelLabel.setFont(FONT_LABEL);
        highestLevelValue = new JLabel("LEVEL 0");
        highestLevelValue.setFont(FONT_VALUE);
        highestLevelPanel.add(highestLevelLabel, BorderLayout.WEST);
        highestLevelPanel.add(highestLevelValue, BorderLayout.EAST);

        JPanel perfActions = new JPanel(new GridLayout(1, 3, 10, 0));
        perfActions.setOpaque(false);
        perfActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        perfActions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        openHighScoresButton = new ActionButton("VIEW HIGH SCORES");
        resetHighScoresButton = new ActionButton("RESET HIGH SCORES");
        resetStatsButton = new ActionButton("RESET PLAYER STATS");
        openHighScoresButton.addActionListener(e -> openHighScoresTable());
        resetHighScoresButton.addActionListener(e -> resetHighScoreTable());
        resetStatsButton.addActionListener(e -> openResetStatisticsModal());
        openHighScoresButton.addActionListener(e -> SoundManager.playClick());
        resetHighScoresButton.addActionListener(e -> SoundManager.playClick());
        resetStatsButton.addActionListener(e -> SoundManager.playClick());
        perfActions.add(openHighScoresButton);
        perfActions.add(resetHighScoresButton);
        perfActions.add(resetStatsButton);

        perfCard.add(headerRow);
        perfCard.add(Box.createVerticalStrut(6));
        perfCard.add(perfPlayerContextLabel);
        perfCard.add(Box.createVerticalStrut(8));
        perfCard.add(statRows);
        perfCard.add(Box.createVerticalStrut(8));
        perfCard.add(highestLevelPanel);
        perfCard.add(Box.createVerticalStrut(10));
        perfCard.add(perfActions);
    }

    private void buildAccountCard() {
        accountSectionLabel = new JLabel("ACCOUNT MANAGEMENT");
        accountSectionLabel.setFont(FONT_SECTION);
        accountSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        playerSelectTitleLabel = new JLabel("SELECT PLAYER TO VIEW");
        playerSelectTitleLabel.setFont(FONT_LABEL);
        playerSelectTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        playerSelector = new JComboBox<>(playerNames());
        playerSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        playerSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerSelector.setRenderer(new SelectorRenderer(false));
        playerSelector.addActionListener(e -> {
            selectedPlayerIndex = playerSelector.getSelectedIndex();
            updateSelectedPlayerUI();
        });

        linkTitleLabel = new JLabel("LINK EXISTING PLAYER");
        linkTitleLabel.setFont(FONT_LABEL);
        linkTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        availablePlayerSelector = new JComboBox<>(allPlayerNames());
        availablePlayerSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        availablePlayerSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        availablePlayerSelector.setRenderer(new SelectorRenderer(true));

        linkAccountButton = new ActionButton("LINK ACCOUNT");
        unlinkAccountButton = new ActionButton("UNLINK SELECTED");
        resetPasswordButton = new ActionButton("RESET PASSWORD");
        linkAccountButton.addActionListener(e -> linkChildAccount());
        unlinkAccountButton.addActionListener(e -> unlinkSelectedAccount());
        resetPasswordButton.addActionListener(e -> openResetPasswordModal());
        linkAccountButton.addActionListener(e -> SoundManager.playClick());
        unlinkAccountButton.addActionListener(e -> SoundManager.playClick());
        resetPasswordButton.addActionListener(e -> SoundManager.playClick());

        createPlayerButton = new ActionButton("CREATE NEW PLAYER ACCOUNT");

        createPlayerButton.addActionListener(e -> createNewPlayer());
        createPlayerButton.addActionListener(e -> SoundManager.playClick());

        JPanel selectors = new JPanel(new GridLayout(1, 2, 12, 0));
        selectors.setOpaque(false);
        selectors.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectors.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel linkedColumn = new JPanel();
        linkedColumn.setOpaque(false);
        linkedColumn.setLayout(new BoxLayout(linkedColumn, BoxLayout.Y_AXIS));
        linkedColumn.add(playerSelectTitleLabel);
        linkedColumn.add(Box.createVerticalStrut(6));
        linkedColumn.add(playerSelector);

        JPanel availableColumn = new JPanel();
        availableColumn.setOpaque(false);
        availableColumn.setLayout(new BoxLayout(availableColumn, BoxLayout.Y_AXIS));
        availableColumn.add(linkTitleLabel);
        availableColumn.add(Box.createVerticalStrut(6));
        availableColumn.add(availablePlayerSelector);

        selectors.add(linkedColumn);
        selectors.add(availableColumn);

        JPanel actionGrid = new JPanel(new GridLayout(1, 2, 10, 0));
        actionGrid.setOpaque(false);
        actionGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        actionGrid.add(linkAccountButton);
        actionGrid.add(unlinkAccountButton);

        JPanel playerActions = new JPanel(new GridLayout(1, 2, 10, 0));
        playerActions.setOpaque(false);
        playerActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerActions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        playerActions.add(createPlayerButton);
        playerActions.add(resetPasswordButton);

        accountCard.add(accountSectionLabel);
        accountCard.add(Box.createVerticalStrut(8));
        accountCard.add(selectors);
        accountCard.add(Box.createVerticalStrut(8));
        accountCard.add(actionGrid);
        accountCard.add(Box.createVerticalStrut(8));
        accountCard.add(playerActions);
    }

    private void buildActivityCard() {
        activitySectionLabel = new JLabel("RECENT ACTIVITY");
        activitySectionLabel.setFont(FONT_SECTION);
        activitySectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        activityTableModel = new DefaultTableModel(ACTIVITY_COLS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        activityTable = new JTable(activityTableModel);
        activityTable.setOpaque(false);
        activityTable.setRowHeight(40);
        activityTable.setShowGrid(false);
        activityTable.setFocusable(false);
        activityTable.setRowSelectionAllowed(false);
        activityTable.setIntercellSpacing(new Dimension(0, 0));
        activityTable.setFont(FONT_TABLE_TD);

        JTableHeader header = activityTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setFont(FONT_TABLE_TH);

        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 148));
        scrollPane.setPreferredSize(new Dimension(440, 148));

        activityCard.add(activitySectionLabel);
        activityCard.add(Box.createVerticalStrut(8));
        activityCard.add(scrollPane);
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 12));
        footer.setOpaque(false);

        footerLabel = new JLabel(FOOTER_TEXT);
        footerLabel.setFont(FONT_FOOTER);
        footer.add(footerLabel);

        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    private void updateSelectedPlayerUI() {
        PlayerProfile player = currentPlayer();
        Color playerNameColor = ThemeManager.isDark() ? DARK_ACCENT : LIGHT_ACCENT;
        String playerHex = String.format("#%02x%02x%02x",
                playerNameColor.getRed(), playerNameColor.getGreen(), playerNameColor.getBlue());
        perfPlayerContextLabel.setText("<html>Viewing statistics for <b><span style='color:" + playerHex + ";'>" + player.name + "</span></b></html>");
        statBoxes[0].setValue(player.bestScore);
        statBoxes[1].setValue(player.averageWpm);
        statBoxes[2].setValue(player.bestWpm);
        statBoxes[3].setValue(player.avgAccuracy);
        statBoxes[4].setValue(player.totalPlayTime);
        statBoxes[5].setValue(player.correctWords);
        statBoxes[6].setValue(player.errorCount);
        highestLevelValue.setText(player.highestLevel);
        refreshActivityTable(player.activityRows);
        adminInfoValues[1].setText(String.valueOf(linkedPlayers.size()));
        adminInfoValues[2].setText(linkedPlayers.isEmpty() ? "No linked players" : "Active");
        playerSelector.setModel(new DefaultComboBoxModel<>(playerNames()));
        availablePlayerSelector.setModel(new DefaultComboBoxModel<>(allPlayerNames()));
        if (!linkedPlayers.isEmpty()) {
            playerSelector.setSelectedIndex(Math.min(selectedPlayerIndex, linkedPlayers.size() - 1));
        }
    }

    private void refreshActivityTable(Object[][] rows) {
        activityTableModel.setRowCount(0);
        for (Object[] row : rows) {
            activityTableModel.addRow(row);
        }
    }

    private void createNewPlayer() {
        dispose();
        new CreatePlayerScreen(parentUsername);
    }

    private void openParentPlayerDashboard() {
        Player actualPlayer = loadPlayerSafely(parentUsername, "load the parent dashboard");
        if (actualPlayer == null) {
            JOptionPane.showMessageDialog(this,
                    "No player dashboard exists for parent account \"" + parentUsername + "\".",
                    "Player Dashboard", JOptionPane.ERROR_MESSAGE);
            SoundManager.playWarning();
            return;
        }

        dispose();
        new DashboardScreen(actualPlayer, parentUsername);
    }

    private void openMainMenu() {
        dispose();
        new MainMenuScreen();
    }

    private void installEscapeShortcut() {
        JRootPane root = getRootPane();
        if (root == null) {
            return;
        }
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitParentControls");
        root.getActionMap().put("exitParentControls", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openMainMenu();
            }
        });
    }

    private void openResetPasswordModal() {
        PlayerProfile player = currentPlayer();

        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Reset Password for " + player.name));
        panel.add(Box.createVerticalStrut(12));
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showOptionDialog(
                this,
                panel,
                "Reset Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Confirm", "Cancel"},
                "Confirm"
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        String validationError = ParentControlsLogic.validateResetPasswordInput(newPassword, confirmPassword);
        if (validationError != null) {
            JOptionPane.showMessageDialog(this,
                    validationError,
                    "Reset Password", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player actualPlayer = loadPlayerSafely(player.name, "load player data");
        if (actualPlayer == null) {
            JOptionPane.showMessageDialog(this,
                    "Player account could not be loaded.",
                    "Reset Password", JOptionPane.ERROR_MESSAGE);
            SoundManager.playWarning();
            return;
        }

        Player updated = ParentControlsLogic.rebuildPlayerWithPassword(actualPlayer, newPassword);
        if (!savePlayerSafely(updated, "save the updated password")) {
            return;
        }
        reloadLinkedPlayers();
        updateSelectedPlayerUI();
        JOptionPane.showMessageDialog(this,
                "Password reset for " + player.name + ".",
                "Reset Password", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openResetStatisticsModal() {
        PlayerProfile player = currentPlayer();
        String message = """
                Are you sure you want to reset statistics for %s?

                This will clear:
                - High Score
                - WPM
                - Accuracy
                - Game History
                """.formatted(player.name);

        int result = JOptionPane.showOptionDialog(
                this,
                message,
                "Confirm Reset",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Confirm Reset", "Cancel"},
                "Cancel"
        );

        if (result == JOptionPane.OK_OPTION) {
            resetCurrentPlayerStats();
        }
    }

    private void resetCurrentPlayerStats() {
        PlayerProfile player = currentPlayer();
        Player actualPlayer = loadPlayerSafely(player.name, "load player statistics");
        if (actualPlayer != null) {
            ParentControlsLogic.resetPlayerStatistics(actualPlayer);
            if (!savePlayerSafely(actualPlayer, "save reset statistics")) {
                return;
            }
            reloadLinkedPlayers();
        }
        updateSelectedPlayerUI();
        JOptionPane.showMessageDialog(this,
                player.name + " statistics reset.",
                "Parent Dashboard", JOptionPane.INFORMATION_MESSAGE);
    }

    private PlayerProfile currentPlayer() {
        if (linkedPlayers.isEmpty()) {
            return PlayerProfile.empty();
        }
        int safeIndex = Math.max(0, Math.min(selectedPlayerIndex, linkedPlayers.size() - 1));
        return linkedPlayers.get(safeIndex);
    }

    private String[] playerNames() {
        if (linkedPlayers.isEmpty()) {
            return new String[]{"No linked players"};
        }
        String[] names = new String[linkedPlayers.size()];
        for (int i = 0; i < linkedPlayers.size(); i++) {
            names[i] = linkedPlayers.get(i).name;
        }
        return names;
    }

    private void linkChildAccount() {
        String childUsername = availablePlayerSelector.getSelectedItem() == null ? "" : availablePlayerSelector.getSelectedItem().toString().trim();
        if (childUsername.isEmpty() || "No players found".equals(childUsername)) {
            JOptionPane.showMessageDialog(this, "Select a player to link.", "Link Account", JOptionPane.ERROR_MESSAGE);
            SoundManager.playWarning();
            return;
        }
        Player player = loadPlayerSafely(childUsername, "load child account data");
        if (player == null) {
            JOptionPane.showMessageDialog(this, "No player found with username " + childUsername + ".", "Link Account", JOptionPane.ERROR_MESSAGE);
            SoundManager.playWarning();
            return;
        }
        if (!parentLinkStore.linkPlayer(parentUsername, childUsername)) {
            JOptionPane.showMessageDialog(this, childUsername + " is already linked.", "Link Account", JOptionPane.INFORMATION_MESSAGE);
            SoundManager.playWarning();
            return;
        }
        reloadLinkedPlayers();
        updateSelectedPlayerUI();
        JOptionPane.showMessageDialog(this, "Linked " + childUsername + ".", "Link Account", JOptionPane.INFORMATION_MESSAGE);
    }

    private void unlinkSelectedAccount() {
        String selected = playerSelector.getSelectedItem() == null ? "" : playerSelector.getSelectedItem().toString();
        if (selected == null || selected.isBlank()) {
            JOptionPane.showMessageDialog(this, "Select a linked account first.", "Unlink Account", JOptionPane.ERROR_MESSAGE);
            SoundManager.playWarning();
            return;
        }
        parentLinkStore.unlinkPlayer(parentUsername, selected);
        reloadLinkedPlayers();
        selectedPlayerIndex = 0;
        updateSelectedPlayerUI();
        JOptionPane.showMessageDialog(this, "Unlinked " + selected + ".", "Unlink Account", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reloadLinkedPlayers() {
        linkedPlayers.clear();
        List<String> linkedUsernames = parentLinkStore.getLinkedPlayers(parentUsername);
        for (String username : linkedUsernames) {
            Player player = loadPlayerSafely(username, "load linked player data");
            if (player == null) {
                continue;
            }
            linkedPlayers.add(PlayerProfile.fromPlayer(player));
        }
    }

    private String[] allPlayerNames() {
        return ParentControlsLogic.allPlayerNames(loadAllPlayersSafely("load the player list"));
    }

    private void openHighScoresTable() {
        HighScoresScreen.fromParentDashboard(parentUsername);
    }

    private void resetHighScoreTable() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Reset the high score table for all players?",
                "Reset High Scores",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        List<Player> allPlayers = loadAllPlayersSafely("load the high score table");
        ParentControlsLogic.resetHighScores(allPlayers);
        for (Player player : allPlayers) {
            if (player != null && !savePlayerSafely(player, "save reset high scores")) {
                return;
            }
        }
        reloadLinkedPlayers();
        updateSelectedPlayerUI();
        JOptionPane.showMessageDialog(this, "High score table reset.", "Reset High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        Color text = darkThemeEnabled ? DARK_TEXT : LIGHT_TEXT;
        Color muted = darkThemeEnabled ? DARK_MUTED : LIGHT_MUTED;
        Color label = darkThemeEnabled ? DARK_LABEL : LIGHT_LABEL;
        Color accent = darkThemeEnabled ? DARK_ACCENT : LIGHT_ACCENT;
        Color cardBg = darkThemeEnabled ? DARK_CARD_BG : LIGHT_CARD_BG;
        Color cardBorder = darkThemeEnabled ? DARK_CARD_BORDER : LIGHT_CARD_BORDER;
        Color divider = darkThemeEnabled ? DARK_ACCENT : LIGHT_ACCENT;

        bgPanel.setDark(darkThemeEnabled);
        navLabel.setForeground(muted);
        navChipPanel.setBackground(darkThemeEnabled ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 145));
        navChipPanel.setForeground(new Color(divider.getRed(), divider.getGreen(), divider.getBlue(), darkThemeEnabled ? 60 : 80));
        topBorderLine.setBackground(new Color(divider.getRed(), divider.getGreen(), divider.getBlue(), darkThemeEnabled ? 60 : 80));
        footerLabel.setForeground(muted);

        adminCard.applyTheme(cardBg, cardBorder);
        perfCard.applyTheme(cardBg, cardBorder);
        accountCard.applyTheme(cardBg, cardBorder);
        activityCard.applyTheme(cardBg, cardBorder);

        adminSectionLabel.setForeground(muted);
        perfSectionLabel.setForeground(muted);
        accountSectionLabel.setForeground(muted);
        activitySectionLabel.setForeground(muted);
        adminNameLabel.setForeground(text);
        adminIdLabel.setForeground(muted);
        highestLevelLabel.setForeground(label);
        highestLevelValue.setForeground(accent);
        playerSelectTitleLabel.setForeground(text);
        linkTitleLabel.setForeground(darkThemeEnabled ? new Color(232, 228, 220, 88) : new Color(26, 26, 26, 92));
        perfPlayerContextLabel.setForeground(label);

        for (JLabel item : adminInfoLabels) item.setForeground(label);
        for (JLabel item : adminInfoValues) item.setForeground(text);
        for (StatBox box : statBoxes) box.applyTheme(darkThemeEnabled);

        playerSelector.setBackground(darkThemeEnabled ? new Color(22, 22, 28) : Color.WHITE);
        playerSelector.setForeground(text);
        availablePlayerSelector.setBackground(darkThemeEnabled ? new Color(20, 20, 24) : new Color(248, 248, 246));
        availablePlayerSelector.setForeground(darkThemeEnabled ? new Color(232, 228, 220, 150) : new Color(26, 26, 26, 150));

        activityTable.setForeground(text);
        activityTable.setBackground(new Color(0, 0, 0, 0));
        activityTable.getTableHeader().setForeground(muted);
        activityTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));

        applyActionTheme((ActionButton) createPlayerButton, text, false);
        applyActionTheme((ActionButton) linkAccountButton, text, false);
        applyActionTheme((ActionButton) unlinkAccountButton, text, false);
        applyActionTheme((ActionButton) openHighScoresButton, text, false);
        applyActionTheme((ActionButton) resetPasswordButton, text, false);
        applyActionTheme((ActionButton) resetStatsButton, darkThemeEnabled ? DARK_DANGER : LIGHT_DANGER, true);
        applyActionTheme((ActionButton) resetHighScoresButton, darkThemeEnabled ? DARK_DANGER : LIGHT_DANGER, true);
        applyActionTheme((ActionButton) playerDashboardButton, text, false);
        applyActionTheme((ActionButton) logoutButton, darkThemeEnabled ? DARK_DANGER : LIGHT_DANGER, true);

        themeTogglePanel.applyTheme(
                darkThemeEnabled ? DARK_TOGGLE_BG : LIGHT_TOGGLE_BG,
                darkThemeEnabled ? DARK_TOGGLE_BDR : LIGHT_TOGGLE_BDR,
                muted, accent, darkThemeEnabled ? DARK_BG : LIGHT_BG
        );
        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        repaint();
    }

    private Player loadPlayerSafely(String username, String action) {
        try {
            return saveManager.loadPlayer(username);
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, action, ex);
                return null;
            }
            throw ex;
        }
    }

    private List<Player> loadAllPlayersSafely(String action) {
        try {
            return saveManager.loadAllPlayers();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, action, ex);
                return new ArrayList<>();
            }
            throw ex;
        }
    }

    private boolean savePlayerSafely(Player player, String action) {
        try {
            saveManager.savePlayer(player);
            return true;
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, action, ex);
                return false;
            }
            throw ex;
        }
    }

    private void applyActionTheme(ActionButton button, Color textColor, boolean danger) {
        boolean darkThemeEnabled = ThemeManager.isDark();
        if (danger) {
            button.applyTheme(
                    darkThemeEnabled ? DARK_BTN_BG : LIGHT_BTN_BG,
                    darkThemeEnabled ? DARK_DANGER_BDR : LIGHT_DANGER_BDR,
                    textColor,
                    darkThemeEnabled ? new Color(DARK_DANGER.getRed(), DARK_DANGER.getGreen(), DARK_DANGER.getBlue(), 24)
                            : new Color(LIGHT_DANGER.getRed(), LIGHT_DANGER.getGreen(), LIGHT_DANGER.getBlue(), 18),
                    darkThemeEnabled ? DARK_DANGER_BDR : LIGHT_DANGER_BDR
            );
            return;
        }
        button.applyTheme(
                darkThemeEnabled ? DARK_BTN_BG : LIGHT_BTN_BG,
                darkThemeEnabled ? DARK_BTN_BORDER : LIGHT_BTN_BORDER,
                textColor,
                darkThemeEnabled ? DARK_BTN_HOVER_BG : LIGHT_BTN_HOVER_BG,
                darkThemeEnabled ? DARK_BTN_HOVER_BDR : LIGHT_BTN_HOVER_BDR
        );
    }

    private static class PlayerProfile {
        private final String name;
        private String bestScore;
        private String averageWpm;
        private String bestWpm;
        private String avgAccuracy;
        private String totalPlayTime;
        private String correctWords;
        private String errorCount;
        private String highestLevel;
        private Object[][] activityRows;

        private PlayerProfile(String name, String bestScore, String averageWpm, String bestWpm, String avgAccuracy,
                              String totalPlayTime, String correctWords, String errorCount, String highestLevel, Object[][] activityRows) {
            this.name = name;
            this.bestScore = bestScore;
            this.averageWpm = averageWpm;
            this.bestWpm = bestWpm;
            this.avgAccuracy = avgAccuracy;
            this.totalPlayTime = totalPlayTime;
            this.correctWords = correctWords;
            this.errorCount = errorCount;
            this.highestLevel = highestLevel;
            this.activityRows = activityRows;
        }

        private static PlayerProfile fromPlayer(Player player) {
            Object[][] rows;
            List<ca.uwo.cs2212.demo.SessionResult> recent = player.getRecentActivity();
            if (recent.isEmpty()) {
                rows = new Object[][]{{"-", "--", "No recent sessions", "--"}};
            } else {
                int limit = Math.min(3, recent.size());
                rows = new Object[limit][4];
                for (int i = 0; i < limit; i++) {
                    ca.uwo.cs2212.demo.SessionResult result = recent.get(i);
                    rows[i][0] = String.valueOf(i + 1);
                    rows[i][1] = result.getCompletedAt();
                    rows[i][2] = "Played session";
                    rows[i][3] = result.isWon() ? "WIN" : "LOSS";
                }
            }
            return new PlayerProfile(
                    player.getUsername(),
                    String.format("%,d", player.getStatistics().getHighScore()),
                    String.format("%.0f", player.getStatistics().getAvgWPM()),
                    String.format("%.0f", player.getStatistics().getPeakWPM()),
                    String.format("%.0f%%", player.getStatistics().getAccuracy()),
                    PlayerIdentityUi.formatPlayTime(player.getStatistics().getTotalTime()),
                    String.format("%,d", player.getStatistics().getWordsTyped()),
                    String.format("%,d", player.getStatistics().getErrorCount()),
                    "LEVEL " + Math.max(1, player.getProgress().getCurrentLevel()),
                    rows
            );
        }

        private static PlayerProfile empty() {
            return new PlayerProfile(
                    "No linked player",
                    "0",
                    "0",
                    "0",
                    "0%",
                    "0m 00s",
                    "0",
                    "0",
                    "LEVEL 0",
                    new Object[][]{{"-", "--", "Link a child account", "PENDING"}}
            );
        }
    }

    private static class SelectorRenderer extends DefaultListCellRenderer {
        private final boolean secondary;

        SelectorRenderer(boolean secondary) {
            this.secondary = secondary;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            boolean darkThemeEnabled = ThemeManager.isDark();
            Color baseBg = secondary
                    ? (darkThemeEnabled ? new Color(20, 20, 24) : new Color(248, 248, 246))
                    : (darkThemeEnabled ? new Color(22, 22, 28) : Color.WHITE);
            Color baseText = secondary
                    ? (darkThemeEnabled ? new Color(220, 216, 208) : new Color(70, 70, 70))
                    : (darkThemeEnabled ? DARK_TEXT : LIGHT_TEXT);
            Color selectedBg = darkThemeEnabled ? new Color(54, 54, 68) : new Color(230, 226, 214);
            Color selectedText = darkThemeEnabled ? DARK_TEXT : LIGHT_TEXT;

            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            label.setBackground(isSelected ? selectedBg : baseBg);
            label.setForeground(isSelected ? selectedText : baseText);
            return label;
        }
    }

    private class GradientBackgroundPanel extends JPanel {
        private boolean dark = true;

        void setDark(boolean dark) {
            this.dark = dark;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();
            g2.setColor(dark ? DARK_BG : LIGHT_BG);
            g2.fillRect(0, 0, w, h);
            Color glow1 = dark ? new Color(201, 165, 90, 12) : new Color(139, 105, 20, 8);
            g2.setPaint(new RadialGradientPaint(
                    w * 0.2f, h * 0.15f, w * 0.6f,
                    new float[]{0f, 1f},
                    new Color[]{glow1, new Color(0, 0, 0, 0)}
            ));
            g2.fillRect(0, 0, w, h);
            Color glow2 = dark ? new Color(201, 165, 90, 6) : new Color(139, 105, 20, 4);
            g2.setPaint(new RadialGradientPaint(
                    w * 0.8f, h * 0.85f, w * 0.5f,
                    new float[]{0f, 1f},
                    new Color[]{glow2, new Color(0, 0, 0, 0)}
            ));
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    private static class GlassCard extends JPanel {
        private Color bgColor = new Color(255, 255, 255, 10);
        private Color borderColor = new Color(255, 255, 255, 20);

        GlassCard() {
            setOpaque(false);
        }

        void applyTheme(Color bg, Color border) {
            bgColor = bg;
            borderColor = border;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            g2.setColor(bgColor);
            g2.fill(shape);
            g2.setColor(borderColor);
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class AvatarPanel extends JPanel {
        AvatarPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(201, 165, 90, 36));
            g2.fill(new Ellipse2D.Float(0, 0, getWidth() - 1, getHeight() - 1));
            g2.setColor(new Color(201, 165, 90, 90));
            g2.draw(new Ellipse2D.Float(0.5f, 0.5f, getWidth() - 2, getHeight() - 2));
            g2.setColor(new Color(201, 165, 90));
            g2.setFont(new Font("Serif", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("A", (getWidth() - fm.stringWidth("A")) / 2, getHeight() / 2 + fm.getAscent() / 2 - 2);
            g2.dispose();
        }
    }

    private static class StatBox extends JPanel {
        private final JLabel label;
        private final JLabel value;
        private final JLabel detail;

        StatBox(String labelText, String valueText, String detailText) {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(14, 14, 14, 14));
            label = new JLabel(labelText);
            label.setFont(FONT_STAT_LBL);
            value = new JLabel(valueText);
            value.setFont(FONT_STAT_VAL);
            detail = new JLabel(detailText);
            detail.setFont(FONT_STAT_SUB);
            add(label);
            add(Box.createVerticalStrut(6));
            add(value);
            add(Box.createVerticalStrut(4));
            add(detail);
        }

        void setValue(String text) {
            value.setText(text);
        }

        void applyTheme(boolean dark) {
            label.setForeground(dark ? DARK_LABEL : LIGHT_LABEL);
            value.setForeground(dark ? DARK_ACCENT : LIGHT_ACCENT);
            detail.setForeground(dark ? DARK_MUTED : LIGHT_MUTED);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 8));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ActionButton extends JButton {
        private Color bgColor = new Color(255, 255, 255, 10);
        private Color borderColor = new Color(255, 255, 255, 20);
        private Color textColor = Color.WHITE;
        private Color hoverBg = new Color(255, 255, 255, 20);
        private Color hoverBorder = new Color(255, 255, 255, 30);
        private boolean hovered;

        ActionButton(String text) {
            super(text);
            setFont(FONT_ACTION);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        void applyTheme(Color bg, Color border, Color text, Color hoverBg, Color hoverBorder) {
            bgColor = bg;
            borderColor = border;
            textColor = text;
            this.hoverBg = hoverBg;
            this.hoverBorder = hoverBorder;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? hoverBg : bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            g2.setColor(hovered ? hoverBorder : borderColor);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 20, 20));
            g2.setColor(textColor);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, getHeight() / 2 + fm.getAscent() / 2 - 1);
            g2.dispose();
        }
    }

    private static class ThemeTogglePanel extends JPanel {
        private boolean darkSelected = true;
        private final JLabel lightLabel;
        private final JLabel darkLabel;
        private Color bgColor, borderColor, mutedColor, selectedColor, selectedTextColor;
        private java.util.function.Consumer<Boolean> listener;

        ThemeTogglePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(194, 40));
            setMaximumSize(new Dimension(194, 40));
            setLayout(new GridLayout(1, 2, 0, 0));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            lightLabel = new JLabel("Light", SwingConstants.CENTER);
            darkLabel = new JLabel("Dark", SwingConstants.CENTER);
            lightLabel.setFont(FONT_TOGGLE);
            darkLabel.setFont(FONT_TOGGLE);
            add(lightLabel);
            add(darkLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    darkSelected = !(e.getX() < getWidth() / 2);
                    repaint();
                    if (listener != null) listener.accept(darkSelected);
                }
            });
        }

        void setDarkSelected(boolean dark) {
            darkSelected = dark;
            repaint();
        }

        void setToggleListener(java.util.function.Consumer<Boolean> listener) {
            this.listener = listener;
        }

        void applyTheme(Color bg, Color border, Color muted, Color selected, Color selectedText) {
            bgColor = bg;
            borderColor = border;
            mutedColor = muted;
            selectedColor = selected;
            selectedTextColor = selectedText;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w, h, h, h);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            int pillW = w / 2;
            int pillX = darkSelected ? pillW - 1 : 3;
            g2.setColor(selectedColor);
            g2.fillRoundRect(pillX, 3, pillW - 2, h - 6, h - 6, h - 6);
            g2.dispose();

            lightLabel.setForeground(!darkSelected ? selectedTextColor : mutedColor);
            darkLabel.setForeground(darkSelected ? selectedTextColor : mutedColor);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");
            super.paintComponent(g);
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int arc;

        RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setColor(getForeground());
            g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
