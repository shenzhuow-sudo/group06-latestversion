package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SaveManager;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Login and registration screen for player accounts.
 */
public class LoginScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final String USERNAME_HINT = "Enter your username";
    private static final String PASSWORD_HINT = "Enter your password";
    private static final String CONFIRM_PASSWORD_HINT = "Confirm your password";

    // ── Dark theme palette (identical to MainMenuScreen) ──
    private static final Color DARK_BG           = new Color(10, 10, 15);
    private static final Color DARK_TEXT          = new Color(232, 228, 220);
    private static final Color DARK_MUTED         = new Color(232, 228, 220, 115);
    private static final Color DARK_LABEL         = new Color(232, 228, 220, 160);
    private static final Color DARK_ACCENT        = new Color(201, 165, 90);
    private static final Color DARK_ACCENT_GLOW   = new Color(201, 165, 90, 45);
    private static final Color DARK_CARD_BG       = new Color(255, 255, 255, 10);
    private static final Color DARK_CARD_BORDER   = new Color(255, 255, 255, 20);
    private static final Color DARK_INPUT_BG      = new Color(255, 255, 255, 12);
    private static final Color DARK_INPUT_BORDER  = new Color(255, 255, 255, 25);
    private static final Color DARK_INPUT_FOCUS   = new Color(201, 165, 90, 80);
    private static final Color DARK_DANGER        = new Color(184, 84, 80);
    private static final Color DARK_BTN_TEXT      = new Color(10, 10, 15);
    private static final Color DARK_TOGGLE_BG     = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR    = new Color(255, 255, 255, 15);

    // ── Light theme palette (identical to MainMenuScreen) ──
    private static final Color LIGHT_BG           = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT          = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED        = new Color(26, 26, 26, 102);
    private static final Color LIGHT_LABEL        = new Color(26, 26, 26, 140);
    private static final Color LIGHT_ACCENT       = new Color(139, 105, 20);
    private static final Color LIGHT_ACCENT_GLOW  = new Color(139, 105, 20, 25);
    private static final Color LIGHT_CARD_BG      = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER  = new Color(0, 0, 0, 30);
    private static final Color LIGHT_INPUT_BG     = new Color(255, 255, 255, 180);
    private static final Color LIGHT_INPUT_BORDER = new Color(0, 0, 0, 40);
    private static final Color LIGHT_INPUT_FOCUS  = new Color(139, 105, 20, 100);
    private static final Color LIGHT_DANGER       = new Color(184, 84, 80);
    private static final Color LIGHT_BTN_TEXT     = new Color(255, 255, 255);
    private static final Color LIGHT_TOGGLE_BG    = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR   = new Color(0, 0, 0, 20);

    // ── Fonts ──
    private static final Font FONT_TITLE    = new Font("Serif", Font.BOLD, 52);
    private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_LABEL    = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_INPUT    = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BUTTON   = new Font("Serif", Font.BOLD, 15);
    private static final Font FONT_NAV      = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER   = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE   = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_BACK     = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_ERROR    = new Font("SansSerif", Font.BOLD, 12);

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;

    private JLabel navLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel dividerLine;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private HintTextField usernameField;
    private HintPasswordField passwordField;
    private HintPasswordField confirmPasswordField;
    private GoldButton loginButton;
    private JButton switchModeButton;
    private JButton backButton;
    private JLabel errorLabel;
    private JLabel footerLabel;

    private GlassCardPanel formCard;
    private ThemeTogglePanel themeTogglePanel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;

    private boolean registerMode;

    /**
     * Creates the player login screen.
     */
    public LoginScreen() {
        setTitle("The Hunt - Login");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        applyTheme();

        setContentPane(bgPanel);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
        installEscapeShortcut();
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
        topBar.setBorder(new EmptyBorder(20, 32, 16, 32));

        navLabel = new JLabel("THE HUNT / LOGIN");
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
        center.setBorder(new EmptyBorder(4, 0, 10, 0));

        // Title
        titleLabel = new JLabel("THE HUNT");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        subtitleLabel = new JLabel("ENTER THE PURSUIT");
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        dividerLine = new JPanel();
        dividerLine.setOpaque(true);
        dividerLine.setMaximumSize(new Dimension(84, 2));
        dividerLine.setPreferredSize(new Dimension(84, 2));
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form card
        formCard = new GlassCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(22, 28, 20, 28));
        formCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(FONT_LABEL);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new HintTextField(USERNAME_HINT);
        usernameField.setFont(FONT_INPUT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setPreferredSize(new Dimension(356, 42));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(FONT_LABEL);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new HintPasswordField(PASSWORD_HINT);
        passwordField.setFont(FONT_INPUT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setPreferredSize(new Dimension(356, 42));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPasswordLabel = new JLabel("CONFIRM PASSWORD");
        confirmPasswordLabel.setFont(FONT_LABEL);
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPasswordField = new HintPasswordField(CONFIRM_PASSWORD_HINT);
        confirmPasswordField.setFont(FONT_INPUT);
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmPasswordField.setPreferredSize(new Dimension(356, 42));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton = new GoldButton("LOGIN");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginButton.setPreferredSize(new Dimension(356, 44));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handlePrimaryAction());
        loginButton.addActionListener(e -> SoundManager.playClick());

        switchModeButton = new JButton("Register account");
        switchModeButton.setFont(FONT_BACK);
        switchModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setBorderPainted(false);
        switchModeButton.setFocusPainted(false);
        switchModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchModeButton.addActionListener(e -> toggleAuthMode());
        switchModeButton.addActionListener(e -> SoundManager.playClick());

        errorLabel = new JLabel("Invalid username or password", SwingConstants.CENTER);
        errorLabel.setFont(FONT_ERROR);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setMaximumSize(new Dimension(356, 28));
        errorLabel.setPreferredSize(new Dimension(356, 28));
        errorLabel.setMinimumSize(new Dimension(356, 28));
        // Start transparent but always occupy space
        errorLabel.setForeground(new Color(0, 0, 0, 0));

        formCard.add(usernameLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(passwordLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(confirmPasswordLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(confirmPasswordField);
        formCard.add(Box.createVerticalStrut(18));
        formCard.add(loginButton);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(switchModeButton);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(errorLabel);

        // Back button
        backButton = new JButton("\u2190  Back to Main Menu");
        backButton.setFont(FONT_BACK);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> openMainMenu());
        backButton.addActionListener(e -> SoundManager.playClick());

        center.add(Box.createVerticalGlue());
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(6));
        center.add(subtitleLabel);
        center.add(Box.createVerticalStrut(12));
        center.add(dividerLine);
        center.add(Box.createVerticalStrut(16));
        center.add(formCard);
        center.add(Box.createVerticalStrut(12));
        center.add(backButton);
        center.add(Box.createVerticalGlue());

        mainPanel.add(center, BorderLayout.CENTER);
        updateAuthModeUI();
    }

    private void buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 32, 12));
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
        Color accent, dividerColor;

        if (darkThemeEnabled) {
            accent = DARK_ACCENT;
            dividerColor = new Color(201, 165, 90, 60);

            bgPanel.setDark(true);

            navLabel.setForeground(DARK_MUTED);
            navChipPanel.setBackground(new Color(255, 255, 255, 10));
            navChipPanel.setForeground(dividerColor);
            titleLabel.setForeground(DARK_TEXT);
            subtitleLabel.setForeground(DARK_ACCENT);
            dividerLine.setBackground(dividerColor);

            usernameLabel.setForeground(DARK_LABEL);
            passwordLabel.setForeground(DARK_LABEL);
            confirmPasswordLabel.setForeground(DARK_LABEL);
            usernameField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);
            passwordField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);
            confirmPasswordField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);

            formCard.applyTheme(DARK_CARD_BG, DARK_CARD_BORDER);
            loginButton.applyTheme(DARK_ACCENT, DARK_BTN_TEXT);
            switchModeButton.setForeground(DARK_ACCENT);
            backButton.setForeground(DARK_MUTED);
            footerLabel.setForeground(DARK_MUTED);

            themeTogglePanel.applyTheme(DARK_TOGGLE_BG, DARK_TOGGLE_BDR, DARK_MUTED, DARK_ACCENT, DARK_BG);
        } else {
            accent = LIGHT_ACCENT;
            dividerColor = new Color(139, 105, 20, 80);

            bgPanel.setDark(false);

            navLabel.setForeground(LIGHT_MUTED);
            navChipPanel.setBackground(new Color(255, 255, 255, 145));
            navChipPanel.setForeground(dividerColor);
            titleLabel.setForeground(LIGHT_TEXT);
            subtitleLabel.setForeground(LIGHT_ACCENT);
            dividerLine.setBackground(dividerColor);

            usernameLabel.setForeground(LIGHT_LABEL);
            passwordLabel.setForeground(LIGHT_LABEL);
            confirmPasswordLabel.setForeground(LIGHT_LABEL);
            usernameField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);
            passwordField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);
            confirmPasswordField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);

            formCard.applyTheme(LIGHT_CARD_BG, LIGHT_CARD_BORDER);
            loginButton.applyTheme(LIGHT_ACCENT, LIGHT_BTN_TEXT);
            switchModeButton.setForeground(LIGHT_ACCENT);
            backButton.setForeground(LIGHT_MUTED);
            footerLabel.setForeground(LIGHT_MUTED);

            themeTogglePanel.applyTheme(LIGHT_TOGGLE_BG, LIGHT_TOGGLE_BDR, LIGHT_MUTED, LIGHT_ACCENT, LIGHT_BG);
        }

        topBorderLine.setBackground(dividerColor);

        // Keep error color if already shown, else keep transparent
        if (errorLabel.getForeground().getAlpha() > 0) {
            errorLabel.setForeground(resolveStatusColor(!errorLabel.getText().startsWith("Account created")));
        }

        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        bgPanel.repaint();
        mainPanel.repaint();
    }

    private void handlePrimaryAction() {
        if (registerMode) {
            handleRegister();
            return;
        }

        handleLogin();
    }

    private void handleLogin() {
        String username = readUsername();
        String password = readPassword(passwordField, PASSWORD_HINT);
        String validationError = AuthLogic.validateLoginInput(username, password);
        if (validationError != null) {
            showStatus(validationError, true);
            return;
        }
        Player player;
        try {
            player = loadPlayer(username);
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "load player data", ex);
                showStatus("Player data could not be loaded", true);
                return;
            }
            throw ex;
        }
        if (player == null || !player.verifyPassword(password)) {
            showStatus("Invalid username or password", true);
            return;
        }

        dispose();
        new DashboardScreen(player);
    }

    private void handleRegister() {
        String username = readUsername();
        String password = readPassword(passwordField, PASSWORD_HINT);
        String confirmPassword = readPassword(confirmPasswordField, CONFIRM_PASSWORD_HINT);

        String validationError = AuthLogic.validateRegistrationInput(username, password, confirmPassword);
        if (validationError != null) {
            showStatus(validationError, true);
            return;
        }
        try {
            if (loadPlayer(username) != null) {
                showStatus("That username already exists", true);
                return;
            }

            Player player = new Player(username, password);
            savePlayer(player);
            showStatus("Account created. Entering dashboard...", false);
            dispose();
            new DashboardScreen(player);
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "save player data", ex);
                showStatus("Account could not be created", true);
                return;
            }
            throw ex;
        }
    }

    private Player loadPlayer(String username) {
        return createSaveManager().loadPlayer(username);
    }

    private void savePlayer(Player player) {
        createSaveManager().savePlayer(player);
    }

    private SaveManager createSaveManager() {
        return new SaveManager(new FileStorageService());
    }

    private void toggleAuthMode() {
        registerMode = !registerMode;
        clearForm();
        updateAuthModeUI();
    }

    private void updateAuthModeUI() {
        navLabel.setText(registerMode ? "THE HUNT / REGISTER" : "THE HUNT / LOGIN");
        subtitleLabel.setText(registerMode ? "CREATE YOUR ACCOUNT" : "ENTER THE PURSUIT");
        loginButton.setText(registerMode ? "CREATE ACCOUNT" : "LOGIN");
        switchModeButton.setText(registerMode ? "Back to login" : "Register account");
        confirmPasswordLabel.setVisible(registerMode);
        confirmPasswordField.setVisible(registerMode);
        int cardHeight = registerMode ? 398 : 312;
        formCard.setMaximumSize(new Dimension(420, cardHeight));
        formCard.setPreferredSize(new Dimension(420, cardHeight));
        showStatus("", true);
        formCard.revalidate();
        formCard.repaint();
    }

    private void clearForm() {
        usernameField.resetHint();
        passwordField.resetHint();
        confirmPasswordField.resetHint();
    }

    private String readUsername() {
        String username = usernameField.getText();
        if (username == null) return "";
        username = username.trim();
        return USERNAME_HINT.equals(username) ? "" : username;
    }

    private String readPassword(HintPasswordField field, String hint) {
        String value = new String(field.getPassword()).trim();
        return hint.equals(value) ? "" : value;
    }

    private void showStatus(String message, boolean error) {
        if (error && message != null && !message.isBlank()) {
            SoundManager.playWarning();
        }
        errorLabel.setText(message == null || message.isBlank() ? " " : message);
        errorLabel.setForeground(message == null || message.isBlank()
                ? new Color(0, 0, 0, 0)
                : resolveStatusColor(error));
    }

    private Color resolveStatusColor(boolean error) {
        return error
                ? (ThemeManager.isDark() ? DARK_DANGER : LIGHT_DANGER)
                : (ThemeManager.isDark() ? DARK_ACCENT : LIGHT_ACCENT);
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
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitScreen");
        root.getActionMap().put("exitScreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openMainMenu();
            }
        });
    }

    // ═══════════════════════════════════════════════════════
    //  CUSTOM COMPONENTS
    // ═══════════════════════════════════════════════════════

    /**
     * Background with radial glow — same as MainMenuScreen.
     */
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

            // Radial glow top-left
            Color glow1 = dark ? new Color(201, 165, 90, 12) : new Color(139, 105, 20, 8);
            g2.setPaint(new RadialGradientPaint(
                    w * 0.2f, h * 0.15f, w * 0.6f,
                    new float[]{0f, 1f},
                    new Color[]{glow1, new Color(0, 0, 0, 0)}
            ));
            g2.fillRect(0, 0, w, h);

            // Radial glow bottom-right
            Color glow2 = dark ? new Color(201, 165, 90, 6) : new Color(139, 105, 20, 4);
            g2.setPaint(new RadialGradientPaint(
                    w * 0.8f, h * 0.85f, w * 0.5f,
                    new float[]{0f, 1f},
                    new Color[]{glow2, new Color(0, 0, 0, 0)}
            ));
            g2.fillRect(0, 0, w, h);

            // Corner crosshairs
            g2.setColor(dark ? new Color(232, 228, 220, 10) : new Color(26, 26, 26, 10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50, 50, 50, 120);
            g2.drawLine(50, 50, 120, 50);
            g2.drawLine(w - 50, h - 50, w - 50, h - 120);
            g2.drawLine(w - 50, h - 50, w - 120, h - 50);

            g2.dispose();
        }
    }

    /**
     * Glass-like card with rounded corners.
     */
    private static class GlassCardPanel extends JPanel {
        private Color bgColor = new Color(255, 255, 255, 10);
        private Color borderColor = new Color(255, 255, 255, 20);

        GlassCardPanel() {
            setOpaque(false);
        }

        void applyTheme(Color bg, Color border) {
            this.bgColor = bg;
            this.borderColor = border;
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

    /**
     * Gold accent button with hover effect.
     */
    private static class GoldButton extends JButton {
        private Color bgColor = DARK_ACCENT;
        private Color textColor = DARK_BG;
        private boolean hovered = false;

        GoldButton(String text) {
            super(text);
            setFont(FONT_BUTTON);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        void applyTheme(Color bg, Color text) {
            this.bgColor = bg;
            this.textColor = text;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int arc = 20;

            // Background
            Color bg = hovered
                    ? new Color(
                    Math.min(255, bgColor.getRed() + 20),
                    Math.min(255, bgColor.getGreen() + 15),
                    Math.min(255, bgColor.getBlue() + 10))
                    : bgColor;
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // Hover shadow
            if (hovered) {
                g2.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 50));
                g2.fill(new RoundRectangle2D.Float(0, 2, w, h, arc, arc));
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            }

            // Text
            g2.setFont(getFont());
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            String t = getText();
            g2.drawString(t, (w - fm.stringWidth(t)) / 2, h / 2 + fm.getAscent() / 2 - 1);

            g2.dispose();
        }
    }

    /**
     * Text field with placeholder hint.
     */
    private static class HintTextField extends JTextField {
        private final String hint;
        private boolean showingHint = true;
        private Color bgColor, borderColor, focusBorderColor, textColor, hintColor;

        HintTextField(String hint) {
            this.hint = hint;
            setText(hint);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 16, 0, 16));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingHint) { setText(""); showingHint = false; }
                    setForeground(textColor);
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) { setText(hint); showingHint = true; setForeground(hintColor); }
                    repaint();
                }
            });
        }

        void applyTheme(Color bg, Color border, Color focusBorder, Color text, Color hint) {
            this.bgColor = bg;
            this.borderColor = border;
            this.focusBorderColor = focusBorder;
            this.textColor = text;
            this.hintColor = hint;
            setForeground(showingHint ? hint : text);
            setCaretColor(text);
            repaint();
        }

        void resetHint() {
            setText(hint);
            showingHint = true;
            setForeground(hintColor);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = 14;

            // Background
            g2.setColor(bgColor != null ? bgColor : new Color(255, 255, 255, 10));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // Border
            Color bdr = hasFocus() && focusBorderColor != null ? focusBorderColor : (borderColor != null ? borderColor : new Color(255, 255, 255, 20));
            g2.setColor(bdr);
            g2.setStroke(new BasicStroke(hasFocus() ? 1.5f : 1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, arc, arc));

            // Focus glow
            if (hasFocus() && focusBorderColor != null) {
                g2.setColor(new Color(focusBorderColor.getRed(), focusBorderColor.getGreen(), focusBorderColor.getBlue(), 20));
                g2.setStroke(new BasicStroke(4f));
                g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc + 2, arc + 2));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Password field with placeholder hint.
     */
    private static class HintPasswordField extends JPasswordField {
        private final String hint;
        private boolean showingHint = true;
        private final char defaultEcho;
        private Color bgColor, borderColor, focusBorderColor, textColor, hintColor;

        HintPasswordField(String hint) {
            this.hint = hint;
            this.defaultEcho = getEchoChar();
            setText(hint);
            setEchoChar((char) 0);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 16, 0, 16));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingHint) { setText(""); showingHint = false; setEchoChar(defaultEcho); }
                    setForeground(textColor);
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getPassword().length == 0) {
                        setText(hint); showingHint = true; setEchoChar((char) 0); setForeground(hintColor);
                    }
                    repaint();
                }
            });
        }

        void applyTheme(Color bg, Color border, Color focusBorder, Color text, Color hintClr) {
            this.bgColor = bg;
            this.borderColor = border;
            this.focusBorderColor = focusBorder;
            this.textColor = text;
            this.hintColor = hintClr;
            setForeground(showingHint ? hintClr : text);
            setCaretColor(text);
            repaint();
        }

        void resetHint() {
            setText(hint);
            showingHint = true;
            setEchoChar((char) 0);
            setForeground(hintColor);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = 14;

            g2.setColor(bgColor != null ? bgColor : new Color(255, 255, 255, 10));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            Color bdr = hasFocus() && focusBorderColor != null ? focusBorderColor : (borderColor != null ? borderColor : new Color(255, 255, 255, 20));
            g2.setColor(bdr);
            g2.setStroke(new BasicStroke(hasFocus() ? 1.5f : 1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, arc, arc));

            if (hasFocus() && focusBorderColor != null) {
                g2.setColor(new Color(focusBorderColor.getRed(), focusBorderColor.getGreen(), focusBorderColor.getBlue(), 20));
                g2.setStroke(new BasicStroke(4f));
                g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc + 2, arc + 2));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Rounded pill theme toggle — same as MainMenuScreen.
     */
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
            setMinimumSize(new Dimension(194, 40));
            setLayout(new GridLayout(1, 2, 0, 0));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            lightLabel = new JLabel("Light", SwingConstants.CENTER);
            darkLabel = new JLabel("Dark", SwingConstants.CENTER);
            lightLabel.setFont(FONT_TOGGLE);
            darkLabel.setFont(FONT_TOGGLE);
            lightLabel.setOpaque(false);
            darkLabel.setOpaque(false);

            add(lightLabel);
            add(darkLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean clickedLeft = e.getX() < getWidth() / 2;
                    darkSelected = !clickedLeft; // left = Light, right = Dark
                    repaint();
                    if (listener != null) listener.accept(darkSelected);
                }
            });
        }

        void setDarkSelected(boolean dark) {
            this.darkSelected = dark;
            repaint();
        }

        void setToggleListener(java.util.function.Consumer<Boolean> l) {
            this.listener = l;
        }

        void applyTheme(Color bg, Color border, Color muted, Color selected, Color selText) {
            this.bgColor = bg;
            this.borderColor = border;
            this.mutedColor = muted;
            this.selectedColor = selected;
            this.selectedTextColor = selText;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = h;

            // Background pill
            g2.setColor(bgColor != null ? bgColor : new Color(255, 255, 255, 10));
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Border
            g2.setColor(borderColor != null ? borderColor : new Color(255, 255, 255, 20));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            // Selected indicator pill: Light on left, Dark on right
            int pillW = w / 2;
            int pillX = darkSelected ? pillW - 1 : 3;
            g2.setColor(selectedColor != null ? selectedColor : new Color(201, 165, 90));
            g2.fillRoundRect(pillX, 3, pillW - 2, h - 6, h - 6, h - 6);

            g2.dispose();

            // Update label colors and checkmarks
            Color selC = selectedTextColor != null ? selectedTextColor : Color.BLACK;
            Color unselC = mutedColor != null ? mutedColor : new Color(200, 200, 200, 120);
            lightLabel.setForeground(!darkSelected ? selC : unselC);
            darkLabel.setForeground(darkSelected ? selC : unselC);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");

            super.paintComponent(g);
        }
    }

    /**
     * Rounded chip panel with left accent bar — same as MainMenuScreen.
     */
    private static class RoundedPanel extends JPanel {
        private final int arc;

        private RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            if (getForeground() != null) {
                g2.setColor(getForeground());
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
