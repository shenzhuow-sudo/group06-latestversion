package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SaveManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Leaderboard screen that displays the highest player scores across saved profiles.
 */
public class HighScoresScreen extends JFrame {
    private enum BackTarget { MAIN_MENU, DASHBOARD, PARENT_DASHBOARD }

    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";

    private static final Color DARK_BG = new Color(10, 10, 15);
    private static final Color DARK_TEXT = new Color(232, 228, 220);
    private static final Color DARK_MUTED = new Color(232, 228, 220, 115);
    private static final Color DARK_BODY = new Color(232, 228, 220, 184);
    private static final Color DARK_ACCENT = new Color(201, 165, 90);
    private static final Color DARK_CARD_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_CARD_BORDER = new Color(255, 255, 255, 15);
    private static final Color DARK_ROW_BG = new Color(255, 255, 255, 10);
    private static final Color DARK_ROW_BORDER = new Color(255, 255, 255, 18);
    private static final Color DARK_TOGGLE_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR = new Color(255, 255, 255, 15);

    private static final Color LIGHT_BG = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED = new Color(26, 26, 26, 102);
    private static final Color LIGHT_BODY = new Color(26, 26, 26, 166);
    private static final Color LIGHT_ACCENT = new Color(139, 105, 20);
    private static final Color LIGHT_CARD_BG = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER = new Color(0, 0, 0, 20);
    private static final Color LIGHT_ROW_BG = new Color(255, 255, 255, 160);
    private static final Color LIGHT_ROW_BORDER = new Color(0, 0, 0, 18);
    private static final Color LIGHT_TOGGLE_BG = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR = new Color(0, 0, 0, 20);

    private static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 42);
    private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_BACK = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);
    private static final Font FONT_ROW = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_RANK = new Font("Serif", Font.BOLD, 20);

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private JPanel dividerLine;
    private GlassCardPanel boardPanel;
    private ThemeTogglePanel themeTogglePanel;

    private JLabel navLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel footerLabel;
    private JButton backButton;
    private JLabel[] tableLabels;
    private ScoreRowPanel[] scoreRows;

    private final BackTarget backTarget;
    private final String username;
    private final SaveManager saveManager = new SaveManager(new FileStorageService());

    /**
     * Creates a leaderboard screen that returns to the main menu.
     */
    public HighScoresScreen() {
        this(BackTarget.MAIN_MENU, null);
    }

    /**
     * Creates a leaderboard screen that returns to the given player's dashboard.
     *
     * @param username player username used for back navigation context
     */
    public HighScoresScreen(String username) {
        this(BackTarget.DASHBOARD, username);
    }

    /**
     * Creates a leaderboard screen configured to return to a parent dashboard.
     *
     * @param parentUsername parent dashboard username used for back navigation context
     * @return configured high-scores screen instance
     */
    public static HighScoresScreen fromParentDashboard(String parentUsername) {
        return new HighScoresScreen(BackTarget.PARENT_DASHBOARD, parentUsername);
    }

    private HighScoresScreen(BackTarget backTarget, String username) {
        this.backTarget = backTarget;
        this.username = username;

        setTitle("The Hunt - High Scores");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
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
        topBar.setBorder(new EmptyBorder(20, 32, 16, 32));

        navLabel = new JLabel("THE HUNT / HIGH SCORES");
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
        themeTogglePanel.setToggleListener(dark -> {
            ThemeManager.setTheme(dark ? Theme.DARK : Theme.LIGHT);
            applyTheme();
        });

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrap.setOpaque(false);
        rightWrap.add(themeTogglePanel);

        topBar.add(leftWrap, BorderLayout.WEST);
        topBar.add(rightWrap, BorderLayout.EAST);

        JPanel topBarContainer = new JPanel(new BorderLayout());
        topBarContainer.setOpaque(false);
        topBarContainer.add(topBar, BorderLayout.CENTER);

        topBorderLine = new JPanel();
        topBorderLine.setPreferredSize(new Dimension(1, 2));
        topBorderLine.setOpaque(true);
        topBarContainer.add(topBorderLine, BorderLayout.SOUTH);

        mainPanel.add(topBarContainer, BorderLayout.NORTH);
    }

    private void buildCenter() {
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(16, 0, 10, 0));

        titleLabel = new JLabel("HIGH SCORES");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel("FASTEST HUNTERS THIS SEASON");
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dividerLine = new JPanel();
        dividerLine.setOpaque(true);
        dividerLine.setMaximumSize(new Dimension(84, 2));
        dividerLine.setPreferredSize(new Dimension(84, 2));
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        boardPanel = new GlassCardPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
        boardPanel.setBorder(new EmptyBorder(26, 28, 22, 28));
        boardPanel.setMaximumSize(new Dimension(760, 480));
        boardPanel.setPreferredSize(new Dimension(760, 480));
        boardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel headerRow = new JPanel(new GridLayout(1, 5, 16, 0));
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] headers = {"RANK", "PLAYER", "SCORE", "LEVEL", "ACCURACY"};
        tableLabels = new JLabel[headers.length];
        for (int i = 0; i < headers.length; i++) {
            tableLabels[i] = new JLabel(headers[i], i == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
            tableLabels[i].setFont(FONT_HEADER);
            headerRow.add(tableLabels[i]);
        }

        boardPanel.add(headerRow);
        boardPanel.add(Box.createVerticalStrut(16));

        String[][] scoreData = buildScoreData();
        scoreRows = new ScoreRowPanel[scoreData.length];
        for (int i = 0; i < scoreData.length; i++) {
            scoreRows[i] = new ScoreRowPanel(scoreData[i]);
            boardPanel.add(scoreRows[i]);
            if (i < scoreData.length - 1) {
                boardPanel.add(Box.createVerticalStrut(12));
            }
        }

        String backLabel = switch (backTarget) {
            case DASHBOARD -> "\u2190  Back to Dashboard";
            case PARENT_DASHBOARD -> "\u2190  Back to Parent Dashboard";
            default -> "\u2190  Back to Main Menu";
        };
        backButton = new JButton(backLabel);
        backButton.setFont(FONT_BACK);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> openBackTarget());
        backButton.addActionListener(e -> SoundManager.playClick());

        center.add(Box.createVerticalGlue());
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(14));
        center.add(subtitleLabel);
        center.add(Box.createVerticalStrut(18));
        center.add(dividerLine);
        center.add(Box.createVerticalStrut(28));
        center.add(boardPanel);
        center.add(Box.createVerticalStrut(20));
        center.add(backButton);
        center.add(Box.createVerticalGlue());

        mainPanel.add(center, BorderLayout.CENTER);
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 12));
        footer.setOpaque(false);

        footerLabel = new JLabel(FOOTER_TEXT);
        footerLabel.setFont(FONT_FOOTER);
        footer.add(footerLabel);

        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    private String[][] buildScoreData() {
        List<Player> players;
        try {
            players = new ArrayList<>(saveManager.loadAllPlayers());
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "load high scores", ex);
                return new String[][]{{"-", "Scores Unavailable", "0", "Level 0", "0%"}};
            }
            throw ex;
        }
        players.sort(Comparator.comparingInt((Player p) -> p.getStatistics().getHighScore()).reversed());
        players.removeIf(player -> player == null || player.getStatistics().getHighScore() <= 0);
        if (players.isEmpty()) {
            return new String[][]{{"-", "No Scores Yet", "0", "Level 0", "0%"}};
        }
        int limit = Math.min(5, players.size());
        String[][] rows = new String[limit][5];
        for (int i = 0; i < limit; i++) {
            Player player = players.get(i);
            rows[i][0] = String.valueOf(i + 1);
            rows[i][1] = player.getUsername();
            rows[i][2] = String.format("%,d", player.getStatistics().getHighScore());
            rows[i][3] = "Level " + Math.max(1, player.getProgress().getCurrentLevel());
            rows[i][4] = String.format("%.0f%%", player.getStatistics().getAccuracy());
        }
        return rows;
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        Color text;
        Color muted;
        Color accent;
        Color body;
        Color rowBg;
        Color rowBorder;

        if (darkThemeEnabled) {
            bgPanel.setDark(true);
            text = DARK_TEXT;
            muted = DARK_MUTED;
            accent = DARK_ACCENT;
            body = DARK_BODY;
            rowBg = DARK_ROW_BG;
            rowBorder = DARK_ROW_BORDER;

            navChipPanel.setBackground(new Color(255, 255, 255, 10));
            navChipPanel.setForeground(accent);
            boardPanel.applyTheme(DARK_CARD_BG, DARK_CARD_BORDER);
            themeTogglePanel.applyTheme(DARK_TOGGLE_BG, DARK_TOGGLE_BDR, muted, accent, DARK_BG);
        } else {
            bgPanel.setDark(false);
            text = LIGHT_TEXT;
            muted = LIGHT_MUTED;
            accent = LIGHT_ACCENT;
            body = LIGHT_BODY;
            rowBg = LIGHT_ROW_BG;
            rowBorder = LIGHT_ROW_BORDER;

            navChipPanel.setBackground(new Color(255, 255, 255, 145));
            navChipPanel.setForeground(accent);
            boardPanel.applyTheme(LIGHT_CARD_BG, LIGHT_CARD_BORDER);
            themeTogglePanel.applyTheme(LIGHT_TOGGLE_BG, LIGHT_TOGGLE_BDR, muted, accent, LIGHT_BG);
        }

        navLabel.setForeground(muted);
        titleLabel.setForeground(text);
        subtitleLabel.setForeground(accent);
        dividerLine.setBackground(accent);
        topBorderLine.setBackground(accent);
        footerLabel.setForeground(muted);
        backButton.setForeground(muted);

        for (JLabel tableLabel : tableLabels) {
            tableLabel.setForeground(muted);
        }

        for (int i = 0; i < scoreRows.length; i++) {
            scoreRows[i].applyTheme(rowBg, rowBorder, i == 0 ? accent : text, body);
        }

        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        bgPanel.repaint();
        mainPanel.repaint();
    }

    private void openBackTarget() {
        dispose();
        if (backTarget == BackTarget.DASHBOARD) {
            new DashboardScreen(username);
            return;
        }
        if (backTarget == BackTarget.PARENT_DASHBOARD) {
            new ParentControlsScreen(username);
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
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitHighScores");
        root.getActionMap().put("exitHighScores", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openBackTarget();
            }
        });
    }

    private static class GradientBackgroundPanel extends JPanel {
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

            int w = getWidth();
            int h = getHeight();

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

            g2.setColor(dark ? new Color(232, 228, 220, 10) : new Color(26, 26, 26, 10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50, 50, 50, 120);
            g2.drawLine(50, 50, 120, 50);
            g2.drawLine(w - 50, h - 50, w - 50, h - 120);
            g2.drawLine(w - 50, h - 50, w - 120, h - 50);

            g2.dispose();
        }
    }

    private static class GlassCardPanel extends JPanel {
        private Color bgColor = new Color(255, 255, 255, 10);
        private Color borderColor = new Color(255, 255, 255, 20);

        GlassCardPanel() {
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
            Shape shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.setColor(bgColor);
            g2.fill(shape);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ScoreRowPanel extends JPanel {
        private final JLabel rankLabel;
        private final JLabel playerLabel;
        private final JLabel scoreLabel;
        private final JLabel levelLabel;
        private final JLabel accuracyLabel;
        private Color bgColor = new Color(255, 255, 255, 10);
        private Color borderColor = new Color(255, 255, 255, 20);

        ScoreRowPanel(String[] data) {
            setOpaque(false);
            setLayout(new GridLayout(1, 5, 16, 0));
            setBorder(new EmptyBorder(14, 18, 14, 18));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            rankLabel = new JLabel(data[0], SwingConstants.CENTER);
            rankLabel.setFont(FONT_RANK);
            playerLabel = new JLabel(data[1], SwingConstants.LEFT);
            playerLabel.setFont(FONT_ROW);
            scoreLabel = new JLabel(data[2], SwingConstants.CENTER);
            scoreLabel.setFont(FONT_ROW);
            levelLabel = new JLabel(data[3], SwingConstants.CENTER);
            levelLabel.setFont(FONT_ROW);
            accuracyLabel = new JLabel(data[4], SwingConstants.CENTER);
            accuracyLabel.setFont(FONT_ROW);

            add(rankLabel);
            add(playerLabel);
            add(scoreLabel);
            add(levelLabel);
            add(accuracyLabel);
        }

        void applyTheme(Color bg, Color border, Color rankColor, Color bodyColor) {
            bgColor = bg;
            borderColor = border;
            rankLabel.setForeground(rankColor);
            playerLabel.setForeground(bodyColor);
            scoreLabel.setForeground(bodyColor);
            levelLabel.setForeground(bodyColor);
            accuracyLabel.setForeground(bodyColor);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(bgColor);
            g2.fill(shape);
            g2.setColor(borderColor);
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ThemeTogglePanel extends JPanel {
        private boolean darkSelected = true;
        private final JLabel darkLabel;
        private final JLabel lightLabel;
        private Color bgColor;
        private Color borderColor;
        private Color mutedColor;
        private Color selectedColor;
        private Color selectedTextColor;
        private java.util.function.Consumer<Boolean> listener;

        ThemeTogglePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(194, 40));
            setMaximumSize(new Dimension(194, 40));
            setMinimumSize(new Dimension(194, 40));
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
                    boolean clickedLeft = e.getX() < getWidth() / 2;
                    darkSelected = !clickedLeft;
                    repaint();
                    if (listener != null) {
                        listener.accept(darkSelected);
                    }
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

        void applyTheme(Color bg, Color border, Color muted, Color selected, Color selText) {
            bgColor = bg;
            borderColor = border;
            mutedColor = muted;
            selectedColor = selected;
            selectedTextColor = selText;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = h;
            int pillW = w / 2;
            int pillX = darkSelected ? pillW - 1 : 3;

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
            g2.setColor(selectedColor);
            g2.fillRoundRect(pillX, 3, pillW - 2, h - 6, h - 6, h - 6);
            g2.dispose();

            Color selected = selectedTextColor;
            Color unselected = mutedColor;
            lightLabel.setForeground(darkSelected ? unselected : selected);
            darkLabel.setForeground(darkSelected ? selected : unselected);
            lightLabel.setText(darkSelected ? "Light" : "\u2713  Light");
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
