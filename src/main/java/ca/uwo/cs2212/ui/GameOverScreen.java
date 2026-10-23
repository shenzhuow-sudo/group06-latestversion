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

/**
 * Post-game summary screen that displays the completed round outcome and next actions.
 */
public class GameOverScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final int MAX_LEVEL = 8;

    private static final Color DARK_BG = new Color(10, 10, 15);
    private static final Color DARK_TEXT = new Color(232, 228, 220);
    private static final Color DARK_MUTED = new Color(232, 228, 220, 115);
    private static final Color DARK_LABEL = new Color(232, 228, 220, 128);
    private static final Color DARK_ACCENT = new Color(201, 165, 90);
    private static final Color DARK_WIN = new Color(118, 206, 138);
    private static final Color DARK_LOSS = new Color(232, 108, 108);
    private static final Color DARK_CARD_BG = new Color(255, 255, 255, 10);
    private static final Color DARK_CARD_BORDER = new Color(255, 255, 255, 20);
    private static final Color DARK_BTN_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_BTN_BORDER = new Color(255, 255, 255, 15);
    private static final Color DARK_BTN_HOVER_BG = new Color(201, 165, 90, 15);
    private static final Color DARK_BTN_HOVER_BDR = new Color(201, 165, 90, 64);
    private static final Color DARK_TOGGLE_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR = new Color(255, 255, 255, 15);

    private static final Color LIGHT_BG = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED = new Color(26, 26, 26, 102);
    private static final Color LIGHT_LABEL = new Color(26, 26, 26, 115);
    private static final Color LIGHT_ACCENT = new Color(139, 105, 20);
    private static final Color LIGHT_WIN = new Color(54, 150, 74);
    private static final Color LIGHT_LOSS = new Color(196, 68, 68);
    private static final Color LIGHT_CARD_BG = new Color(255, 255, 255, 145);
    private static final Color LIGHT_CARD_BORDER = new Color(0, 0, 0, 18);
    private static final Color LIGHT_BTN_BG = new Color(0, 0, 0, 6);
    private static final Color LIGHT_BTN_BORDER = new Color(0, 0, 0, 25);
    private static final Color LIGHT_BTN_HOVER_BG = new Color(139, 105, 20, 12);
    private static final Color LIGHT_BTN_HOVER_BDR = new Color(139, 105, 20, 64);
    private static final Color LIGHT_TOGGLE_BG = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR = new Color(0, 0, 0, 20);

    private static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 54);
    private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_SECTION = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_STAT_LBL = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FONT_STAT_VAL = new Font("Monospaced", Font.BOLD, 24);
    private static final Font FONT_STAT_SUB = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_ACTION = new Font("Serif", Font.BOLD, 13);

    private final String username;
    private final int score;
    private final int elapsed;
    private final String wpm;
    private final String accuracy;
    private final int currentLevel;
    private final boolean won;

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private ThemeTogglePanel themeTogglePanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel dividerLine;
    private GlassCardPanel statsCard;
    private GlassCardPanel actionCard;
    private JLabel footerLabel;
    private StatBox[] statBoxes;
    private JButton restartLevelButton;
    private JButton nextLevelButton;
    private JButton dashboardButton;

    /**
     * Creates a game-over screen with default level and loss-state assumptions.
     *
     * @param username player username
     * @param score final score
     * @param elapsed elapsed time in seconds
     * @param wpm formatted words-per-minute value
     * @param accuracy formatted accuracy value
     */
    public GameOverScreen(String username, int score, int elapsed, String wpm, String accuracy) {
        this(username, score, elapsed, wpm, accuracy, 1, false);
    }

    /**
     * Creates a game-over screen for a specific level with a default loss-state assumption.
     *
     * @param username player username
     * @param score final score
     * @param elapsed elapsed time in seconds
     * @param wpm formatted words-per-minute value
     * @param accuracy formatted accuracy value
     * @param currentLevel level that was played
     */
    public GameOverScreen(String username, int score, int elapsed, String wpm, String accuracy, int currentLevel) {
        this(username, score, elapsed, wpm, accuracy, currentLevel, false);
    }

    /**
     * Creates a game-over screen for the supplied completed round.
     *
     * @param username player username
     * @param score final score
     * @param elapsed elapsed time in seconds
     * @param wpm formatted words-per-minute value
     * @param accuracy formatted accuracy value
     * @param currentLevel level that was played
     * @param won whether the round ended in a win
     */
    public GameOverScreen(String username, int score, int elapsed, String wpm, String accuracy, int currentLevel, boolean won) {
        this.username = username == null || username.isBlank() ? "Player1" : username.trim();
        this.score = score;
        this.elapsed = elapsed;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.currentLevel = Math.max(1, Math.min(MAX_LEVEL, currentLevel));
        this.won = won;
        syncSoundPreference();

        setTitle("The Hunt - Game Over");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 820);
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

        navLabel = new JLabel("THE HUNT / GAME OVER");
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
            syncThemePreference(selectedTheme);
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
        boolean campaignComplete = won && currentLevel >= MAX_LEVEL;
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(18, 0, 10, 0));

        titleLabel = new JLabel(campaignComplete ? "CAMPAIGN COMPLETE" : (won ? "LEVEL CLEARED" : "GAME OVER"));
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel(campaignComplete
                ? "WIN • " + username.toUpperCase() + " COMPLETED THE FINAL LEVEL"
                : (won
                ? "WIN \u2022 " + username.toUpperCase() + " COMPLETED LEVEL " + currentLevel
                : "LOSS \u2022 " + username.toUpperCase() + " DID NOT CLEAR LEVEL " + currentLevel));
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dividerLine = new JPanel();
        dividerLine.setOpaque(true);
        dividerLine.setMaximumSize(new Dimension(84, 2));
        dividerLine.setPreferredSize(new Dimension(84, 2));
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        statsCard = new GlassCardPanel();
        statsCard.setLayout(new BoxLayout(statsCard, BoxLayout.Y_AXIS));
        statsCard.setBorder(new EmptyBorder(26, 28, 24, 28));
        statsCard.setMaximumSize(new Dimension(760, 260));
        statsCard.setPreferredSize(new Dimension(760, 260));
        statsCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel section = new JLabel("FINAL RUN SUMMARY");
        section.setFont(FONT_SECTION);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsCard.add(section);
        statsCard.add(Box.createVerticalStrut(14));

        JPanel statGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        statGrid.setOpaque(false);
        statGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[][] stats = {
                {"Final Score", String.format("%,d", score), "Points earned this run"},
                {"Average WPM", wpm, "Typing speed during session"},
                {"Accuracy", accuracy, "Words correctly typed"},
                {"Time Survived", formatTime(elapsed), "Session duration"}
        };
        statBoxes = new StatBox[4];
        for (int i = 0; i < 4; i++) {
            statBoxes[i] = new StatBox(stats[i][0], stats[i][1], stats[i][2]);
            statGrid.add(statBoxes[i]);
        }
        statsCard.add(statGrid);

        actionCard = new GlassCardPanel();
        actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));
        actionCard.setBorder(new EmptyBorder(24, 28, 24, 28));
        actionCard.setMaximumSize(new Dimension(760, 130));
        actionCard.setPreferredSize(new Dimension(760, 130));
        actionCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel actionSection = new JLabel("NEXT MOVE");
        actionSection.setFont(FONT_SECTION);
        actionSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionCard.add(actionSection);
        actionCard.add(Box.createVerticalStrut(14));

        JPanel buttons = new JPanel(new GridLayout(1, campaignComplete ? 2 : 3, 12, 0));
        buttons.setOpaque(false);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        restartLevelButton = new ActionButton(campaignComplete ? "REPLAY FINAL LEVEL" : "RESTART CURRENT LEVEL");
        nextLevelButton = new ActionButton("START NEXT LEVEL");
        dashboardButton = new ActionButton("DASHBOARD");

        restartLevelButton.addActionListener(e -> {
            openGameplayScreen(currentLevel);
        });
        restartLevelButton.addActionListener(e -> SoundManager.playClick());
        nextLevelButton.addActionListener(e -> {
            if (!won) {
                SoundManager.playWarning();
                JOptionPane.showMessageDialog(
                        this,
                        "Pass the current level before starting the next one.",
                        "Level Locked",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            openGameplayScreen(Math.min(MAX_LEVEL, currentLevel + 1));
        });
        nextLevelButton.addActionListener(e -> {
            if (won) {
                SoundManager.playClick();
            }
        });
        nextLevelButton.setEnabled(won);
        dashboardButton.addActionListener(e -> {
            openDashboard();
        });
        dashboardButton.addActionListener(e -> SoundManager.playClick());

        buttons.add(restartLevelButton);
        if (!campaignComplete) {
            buttons.add(nextLevelButton);
        }
        buttons.add(dashboardButton);
        actionCard.add(buttons);

        center.add(Box.createVerticalGlue());
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(12));
        center.add(subtitleLabel);
        center.add(Box.createVerticalStrut(18));
        center.add(dividerLine);
        center.add(Box.createVerticalStrut(28));
        center.add(statsCard);
        center.add(Box.createVerticalStrut(18));
        center.add(actionCard);
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

    private void syncSoundPreference() {
        try {
            Player player = new SaveManager(new FileStorageService()).loadPlayer(username);
            if (player != null) {
                ThemeManager.setTheme(UiRuntimeGuard.toUiTheme(player.getProgress().getSettings().getTheme()));
                SoundManager.setEnabled(player.getProgress().getSettings().isSoundOn());
            }
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "load player settings", ex);
                return;
            }
            throw ex;
        }
    }

    private void syncThemePreference(Theme selectedTheme) {
        try {
            Player player = new SaveManager(new FileStorageService()).loadPlayer(username);
            if (player == null) {
                return;
            }
            player.getProgress().getSettings().setTheme(UiRuntimeGuard.toModelTheme(selectedTheme));
            new SaveManager(new FileStorageService()).savePlayer(player);
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "save player settings", ex);
                return;
            }
            throw ex;
        }
    }

    private void openGameplayScreen(int level) {
        try {
            new GameplayScreen(username, level, false);
            dispose();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "start gameplay", ex);
                return;
            }
            throw ex;
        }
    }

    private void openDashboard() {
        try {
            DashboardScreen.fromLastGame(username);
            dispose();
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "open the dashboard", ex);
                return;
            }
            throw ex;
        }
    }

    private void installEscapeShortcut() {
        JRootPane root = getRootPane();
        if (root == null) {
            return;
        }
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitGameOver");
        root.getActionMap().put("exitGameOver", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openDashboard();
            }
        });
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
        Color buttonBg = darkThemeEnabled ? DARK_BTN_BG : LIGHT_BTN_BG;
        Color buttonBorder = darkThemeEnabled ? DARK_BTN_BORDER : LIGHT_BTN_BORDER;
        Color buttonHoverBg = darkThemeEnabled ? DARK_BTN_HOVER_BG : LIGHT_BTN_HOVER_BG;
        Color buttonHoverBorder = darkThemeEnabled ? DARK_BTN_HOVER_BDR : LIGHT_BTN_HOVER_BDR;
        Color bg = darkThemeEnabled ? DARK_BG : LIGHT_BG;

        bgPanel.setDark(darkThemeEnabled);
        navLabel.setForeground(muted);
        navChipPanel.setBackground(darkThemeEnabled ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 145));
        navChipPanel.setForeground(accent);
        topBorderLine.setBackground(accent);
        titleLabel.setForeground(text);
        subtitleLabel.setForeground(won
                ? (darkThemeEnabled ? DARK_WIN : LIGHT_WIN)
                : (darkThemeEnabled ? DARK_LOSS : LIGHT_LOSS));
        dividerLine.setBackground(accent);
        footerLabel.setForeground(muted);

        statsCard.applyTheme(cardBg, cardBorder);
        actionCard.applyTheme(cardBg, cardBorder);
        ((JLabel) statsCard.getComponent(0)).setForeground(muted);
        ((JLabel) actionCard.getComponent(0)).setForeground(muted);
        for (StatBox box : statBoxes) {
            box.applyTheme(cardBg, cardBorder, label, accent, muted);
        }

        ((ActionButton) restartLevelButton).applyTheme(buttonBg, buttonBorder, text, buttonHoverBg, buttonHoverBorder);
        ((ActionButton) nextLevelButton).applyTheme(buttonBg, buttonBorder, text, buttonHoverBg, buttonHoverBorder);
        ((ActionButton) dashboardButton).applyTheme(buttonBg, buttonBorder, text, buttonHoverBg, buttonHoverBorder);

        themeTogglePanel.applyTheme(
                darkThemeEnabled ? DARK_TOGGLE_BG : LIGHT_TOGGLE_BG,
                darkThemeEnabled ? DARK_TOGGLE_BDR : LIGHT_TOGGLE_BDR,
                muted, accent, bg
        );
        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        repaint();
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
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
            g2.draw(shape);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class StatBox extends JPanel {
        private final String label;
        private final String value;
        private final String subtitle;
        private Color bgColor;
        private Color borderColor;
        private Color labelColor;
        private Color valueColor;
        private Color subtitleColor;

        StatBox(String label, String value, String subtitle) {
            this.label = label;
            this.value = value;
            this.subtitle = subtitle;
            setOpaque(false);
        }

        void applyTheme(Color bg, Color border, Color labelColor, Color valueColor, Color subtitleColor) {
            this.bgColor = bg;
            this.borderColor = border;
            this.labelColor = labelColor;
            this.valueColor = valueColor;
            this.subtitleColor = subtitleColor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 16, 16));
            g2.setFont(FONT_STAT_LBL);
            g2.setColor(labelColor);
            g2.drawString(label.toUpperCase(), 14, 20);
            g2.setFont(FONT_STAT_VAL);
            g2.setColor(valueColor);
            g2.drawString(value, 14, 48);
            g2.setFont(FONT_STAT_SUB);
            g2.setColor(subtitleColor);
            g2.drawString(subtitle, 14, 66);
            g2.dispose();
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
