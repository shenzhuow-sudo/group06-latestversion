package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.ParentLinkStore;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.SaveManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Registration screen for creating a new player profile, optionally linked to a parent account.
 */
public class CreatePlayerScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final String USERNAME_HINT = "Enter new player's username";
    private static final String PASSWORD_HINT = "Enter password";
    private static final String CONFIRM_HINT = "Confirm password";

    private static final Color DARK_BG = new Color(10, 10, 15);
    private static final Color DARK_TEXT = new Color(232, 228, 220);
    private static final Color DARK_MUTED = new Color(232, 228, 220, 115);
    private static final Color DARK_LABEL = new Color(232, 228, 220, 160);
    private static final Color DARK_ACCENT = new Color(201, 165, 90);
    private static final Color DARK_CARD_BG = new Color(255, 255, 255, 10);
    private static final Color DARK_CARD_BORDER = new Color(255, 255, 255, 20);
    private static final Color DARK_INPUT_BG = new Color(255, 255, 255, 12);
    private static final Color DARK_INPUT_BORDER = new Color(255, 255, 255, 25);
    private static final Color DARK_INPUT_FOCUS = new Color(201, 165, 90, 80);
    private static final Color DARK_DANGER = new Color(184, 84, 80);
    private static final Color DARK_BTN_TEXT = new Color(10, 10, 15);
    private static final Color DARK_TOGGLE_BG = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR = new Color(255, 255, 255, 15);

    private static final Color LIGHT_BG = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED = new Color(26, 26, 26, 102);
    private static final Color LIGHT_LABEL = new Color(26, 26, 26, 140);
    private static final Color LIGHT_ACCENT = new Color(139, 105, 20);
    private static final Color LIGHT_CARD_BG = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER = new Color(0, 0, 0, 30);
    private static final Color LIGHT_INPUT_BG = new Color(255, 255, 255, 180);
    private static final Color LIGHT_INPUT_BORDER = new Color(0, 0, 0, 40);
    private static final Color LIGHT_INPUT_FOCUS = new Color(139, 105, 20, 100);
    private static final Color LIGHT_DANGER = new Color(184, 84, 80);
    private static final Color LIGHT_BTN_TEXT = new Color(255, 255, 255);
    private static final Color LIGHT_TOGGLE_BG = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR = new Color(0, 0, 0, 20);

    private static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 48);
    private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 11);
    private static final Font FONT_INPUT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Serif", Font.BOLD, 15);
    private static final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_BACK = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_ERROR = new Font("SansSerif", Font.BOLD, 12);

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private ThemeTogglePanel themeTogglePanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel dividerLine;
    private GlassCardPanel formCard;
    private JLabel usernameLabel;
    private HintTextField usernameField;
    private JLabel passwordLabel;
    private HintPasswordField passwordField;
    private JLabel confirmLabel;
    private HintPasswordField confirmField;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel errorLabel;
    private JLabel footerLabel;
    private final String parentUsername;
    private final SaveManager saveManager = new SaveManager(new FileStorageService());
    private final ParentLinkStore parentLinkStore = new ParentLinkStore(new FileStorageService());

    /**
     * Creates an unlinked player-creation screen.
     */
    public CreatePlayerScreen() {
        this(null);
    }

    /**
     * Creates a player-creation screen that can attach the new profile to a parent account.
     *
     * @param parentUsername parent username to link after creation, or {@code null}
     */
    public CreatePlayerScreen(String parentUsername) {
        this.parentUsername = parentUsername;
        setTitle("The Hunt - Create Player");
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

        navLabel = new JLabel("THE HUNT / CREATE PLAYER");
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
        center.setBorder(new EmptyBorder(10, 0, 20, 0));

        titleLabel = new JLabel("CREATE PLAYER ACCOUNT");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitleLabel = new JLabel("ADD A NEW PLAYER PROFILE");
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dividerLine = new JPanel();
        dividerLine.setOpaque(true);
        dividerLine.setMaximumSize(new Dimension(84, 2));
        dividerLine.setPreferredSize(new Dimension(84, 2));
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        formCard = new GlassCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(32, 32, 28, 32));
        formCard.setMaximumSize(new Dimension(460, 440));
        formCard.setPreferredSize(new Dimension(460, 440));
        formCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(FONT_LABEL);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new HintTextField(USERNAME_HINT);
        usernameField.setFont(FONT_INPUT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        usernameField.setPreferredSize(new Dimension(380, 46));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(FONT_LABEL);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new HintPasswordField(PASSWORD_HINT);
        passwordField.setFont(FONT_INPUT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        passwordField.setPreferredSize(new Dimension(380, 46));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmLabel = new JLabel("CONFIRM PASSWORD");
        confirmLabel.setFont(FONT_LABEL);
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmField = new HintPasswordField(CONFIRM_HINT);
        confirmField.setFont(FONT_INPUT);
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        confirmField.setPreferredSize(new Dimension(380, 46));
        confirmField.setAlignmentX(Component.CENTER_ALIGNMENT);

        createButton = new GoldButton("CREATE");
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        createButton.setPreferredSize(new Dimension(380, 48));
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.addActionListener(e -> handleCreate());
        createButton.addActionListener(e -> SoundManager.playClick());

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(FONT_BACK);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> openReturnScreen());
        cancelButton.addActionListener(e -> SoundManager.playClick());

        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(FONT_ERROR);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setMaximumSize(new Dimension(380, 36));
        errorLabel.setPreferredSize(new Dimension(380, 36));
        errorLabel.setForeground(new Color(0, 0, 0, 0));

        formCard.add(usernameLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(18));
        formCard.add(passwordLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(18));
        formCard.add(confirmLabel);
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(confirmField);
        formCard.add(Box.createVerticalStrut(22));
        formCard.add(createButton);
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(cancelButton);
        formCard.add(Box.createVerticalStrut(12));
        formCard.add(errorLabel);

        center.add(Box.createVerticalGlue());
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitleLabel);
        center.add(Box.createVerticalStrut(22));
        center.add(dividerLine);
        center.add(Box.createVerticalStrut(28));
        center.add(formCard);
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

    private void handleCreate() {
        String username = usernameField.readValue(USERNAME_HINT);
        String password = passwordField.readValue(PASSWORD_HINT);
        String confirm = confirmField.readValue(CONFIRM_HINT);

        String validationError = AuthLogic.validateCreatePlayerInput(username, password, confirm);
        if (validationError != null) {
            showError(validationError);
            return;
        }
        try {
            if (saveManager.loadPlayer(username) != null) {
                showError("Username already exists");
                return;
            }

            Player player = new Player(username, password);
            saveManager.savePlayer(player);
            if (parentUsername != null && !parentUsername.isBlank()) {
                parentLinkStore.linkPlayer(parentUsername, username);
            }
        } catch (RuntimeException ex) {
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "create a player account", ex);
                showError("Player account could not be created");
                return;
            }
            throw ex;
        }
        JOptionPane.showMessageDialog(this,
                "Player account \"" + username + "\" created.",
                "Create Player", JOptionPane.INFORMATION_MESSAGE);
        openReturnScreen();
    }

    private void showError(String message) {
        SoundManager.playWarning();
        errorLabel.setText(message);
        errorLabel.setForeground(ThemeManager.isDark() ? DARK_DANGER : LIGHT_DANGER);
    }

    /**
     * Returns to the screen that launched player creation.
     * <p>
     * Parent-linked flows return to the parent dashboard; standalone flows fall back
     * to the main menu instead of opening a synthetic guardian dashboard.
     * </p>
     */
    private void openReturnScreen() {
        dispose();
        if (parentUsername != null && !parentUsername.isBlank()) {
            new ParentControlsScreen(parentUsername);
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
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitCreatePlayer");
        root.getActionMap().put("exitCreatePlayer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openReturnScreen();
            }
        });
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        Color dividerColor;
        if (darkThemeEnabled) {
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
            confirmLabel.setForeground(DARK_LABEL);
            usernameField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);
            passwordField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);
            confirmField.applyTheme(DARK_INPUT_BG, DARK_INPUT_BORDER, DARK_INPUT_FOCUS, DARK_TEXT, DARK_MUTED);
            formCard.applyTheme(DARK_CARD_BG, DARK_CARD_BORDER);
            createButton.setForeground(DARK_BTN_TEXT);
            cancelButton.setForeground(DARK_MUTED);
            footerLabel.setForeground(DARK_MUTED);
            themeTogglePanel.applyTheme(DARK_TOGGLE_BG, DARK_TOGGLE_BDR, DARK_MUTED, DARK_ACCENT, DARK_BG);
        } else {
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
            confirmLabel.setForeground(LIGHT_LABEL);
            usernameField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);
            passwordField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);
            confirmField.applyTheme(LIGHT_INPUT_BG, LIGHT_INPUT_BORDER, LIGHT_INPUT_FOCUS, LIGHT_TEXT, LIGHT_MUTED);
            formCard.applyTheme(LIGHT_CARD_BG, LIGHT_CARD_BORDER);
            createButton.setForeground(LIGHT_BTN_TEXT);
            cancelButton.setForeground(LIGHT_MUTED);
            footerLabel.setForeground(LIGHT_MUTED);
            themeTogglePanel.applyTheme(LIGHT_TOGGLE_BG, LIGHT_TOGGLE_BDR, LIGHT_MUTED, LIGHT_ACCENT, LIGHT_BG);
        }

        topBorderLine.setBackground(dividerColor);
        if (errorLabel.getForeground().getAlpha() > 0) {
            errorLabel.setForeground(darkThemeEnabled ? DARK_DANGER : LIGHT_DANGER);
        }
        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        bgPanel.repaint();
        mainPanel.repaint();
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

    private static class GoldButton extends JButton {
        private boolean hovered;

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

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = hovered ? new Color(220, 184, 108) : new Color(201, 165, 90);
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, getHeight() / 2 + fm.getAscent() / 2 - 1);
            g2.dispose();
        }
    }

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
                    if (showingHint) {
                        setText("");
                        showingHint = false;
                    }
                    setForeground(textColor);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(hint);
                        showingHint = true;
                        setForeground(hintColor);
                    }
                }
            });
        }

        void applyTheme(Color bg, Color border, Color focusBorder, Color text, Color hint) {
            bgColor = bg;
            borderColor = border;
            focusBorderColor = focusBorder;
            textColor = text;
            hintColor = hint;
            setForeground(showingHint ? hint : text);
            setCaretColor(text);
            repaint();
        }

        String readValue(String hintValue) {
            String value = getText() == null ? "" : getText().trim();
            return hintValue.equals(value) ? "" : value;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));
            g2.setColor(hasFocus() ? focusBorderColor : borderColor);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 14, 14));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class HintPasswordField extends JPasswordField {
        private final String hint;
        private boolean showingHint = true;
        private final char defaultEcho;
        private Color bgColor, borderColor, focusBorderColor, textColor, hintColor;

        HintPasswordField(String hint) {
            this.hint = hint;
            defaultEcho = getEchoChar();
            setText(hint);
            setEchoChar((char) 0);
            setOpaque(false);
            setBorder(new EmptyBorder(0, 16, 0, 16));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingHint) {
                        setText("");
                        showingHint = false;
                        setEchoChar(defaultEcho);
                    }
                    setForeground(textColor);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getPassword().length == 0) {
                        setText(hint);
                        showingHint = true;
                        setEchoChar((char) 0);
                        setForeground(hintColor);
                    }
                }
            });
        }

        void applyTheme(Color bg, Color border, Color focusBorder, Color text, Color hint) {
            bgColor = bg;
            borderColor = border;
            focusBorderColor = focusBorder;
            textColor = text;
            hintColor = hint;
            setForeground(showingHint ? hint : text);
            setCaretColor(text);
            repaint();
        }

        String readValue(String hintValue) {
            String value = new String(getPassword()).trim();
            return hintValue.equals(value) ? "" : value;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));
            g2.setColor(hasFocus() ? focusBorderColor : borderColor);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 14, 14));
            g2.dispose();
            super.paintComponent(g);
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
