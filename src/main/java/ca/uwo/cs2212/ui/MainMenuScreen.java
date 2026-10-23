package ca.uwo.cs2212.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Landing screen that routes players to the main sections of the application.
 */
public class MainMenuScreen extends JFrame {
    private static final String FOOTER_TEXT_1 = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final String FOOTER_TEXT_2 = "Kaiwen Li \u00B7 Robert Niu \u00B7 Yangyue Qin \u00B7 Shenzhuo Wei \u00B7 Qixuan Zhu";

    // ── Dark theme palette ──
    private static final Color DARK_BG           = new Color(10, 10, 15);
    private static final Color DARK_TEXT          = new Color(232, 228, 220);
    private static final Color DARK_MUTED         = new Color(232, 228, 220, 115);
    private static final Color DARK_ACCENT        = new Color(201, 165, 90);
    private static final Color DARK_ACCENT_HOVER  = new Color(224, 190, 114);
    private static final Color DARK_BTN_BG        = new Color(255, 255, 255, 10);
    private static final Color DARK_BTN_BORDER    = new Color(255, 255, 255, 20);
    private static final Color DARK_BTN_HOVER_BG  = new Color(201, 165, 90, 20);
    private static final Color DARK_BTN_HOVER_BDR = new Color(201, 165, 90, 64);
    private static final Color DARK_DIVIDER       = new Color(201, 165, 90, 60);
    private static final Color DARK_EXIT          = new Color(184, 84, 80);
    private static final Color DARK_EXIT_HOVER_BG = new Color(184, 84, 80, 38);
    private static final Color DARK_EXIT_BORDER   = new Color(184, 84, 80, 38);
    private static final Color DARK_EXIT_HVBDR    = new Color(184, 84, 80, 90);
    private static final Color DARK_TOGGLE_BG     = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR    = new Color(255, 255, 255, 15);
    private static final Color DARK_TOGGLE_SEL    = DARK_ACCENT;

    // ── Light theme palette ──
    private static final Color LIGHT_BG           = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT          = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED        = new Color(26, 26, 26, 102);
    private static final Color LIGHT_ACCENT       = new Color(139, 105, 20);
    private static final Color LIGHT_ACCENT_HOVER = new Color(163, 124, 26);
    private static final Color LIGHT_BTN_BG       = new Color(0, 0, 0, 8);
    private static final Color LIGHT_BTN_BORDER   = new Color(0, 0, 0, 26);
    private static final Color LIGHT_BTN_HOVER_BG = new Color(139, 105, 20, 15);
    private static final Color LIGHT_BTN_HOVER_BDR= new Color(139, 105, 20, 77);
    private static final Color LIGHT_DIVIDER      = new Color(139, 105, 20, 80);
    private static final Color LIGHT_EXIT         = new Color(158, 58, 55);
    private static final Color LIGHT_EXIT_HOVER_BG= new Color(158, 58, 55, 30);
    private static final Color LIGHT_EXIT_BORDER  = new Color(158, 58, 55, 38);
    private static final Color LIGHT_EXIT_HVBDR   = new Color(158, 58, 55, 90);
    private static final Color LIGHT_TOGGLE_BG    = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR   = new Color(0, 0, 0, 20);
    private static final Color LIGHT_TOGGLE_SEL   = LIGHT_ACCENT;

    // ── Fonts ──
    private static final Font FONT_TITLE    = new Font("Serif", Font.BOLD, 58);
    private static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_BUTTON   = new Font("Serif", Font.BOLD, 14);
    private static final Font FONT_NAV      = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER   = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE   = new Font("SansSerif", Font.PLAIN, 12);

    private JPanel mainPanel;
    private GradientBackgroundPanel bgPanel;
    private JPanel navChipPanel;
    private JLabel navLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel footerLabel1;
    private JLabel footerLabel2;
    private JLabel interactionHintLabel;
    private JPanel dividerLine;

    private HuntMenuButton loginButton;
    private HuntMenuButton tutorialButton;
    private HuntMenuButton highScoresButton;
    private HuntMenuButton parentControlsButton;
    private HuntMenuButton exitButton;
    private HuntMenuButton[] menuButtons;
    private int selectedMenuIndex;

    private ThemeTogglePanel themeTogglePanel;

    /**
     * Creates the main menu window.
     */
    public MainMenuScreen() {
        setTitle("The Hunt - Main Menu");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        applyTheme();

        setContentPane(bgPanel);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
        installKeyboardShortcut();
    }

    // ═══════════════════════════════════════════════════════
    //  BUILD UI
    // ═══════════════════════════════════════════════════════

    private void buildUI() {
        bgPanel = new GradientBackgroundPanel();
        bgPanel.setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        buildTopBar();
        buildCenter();
        buildFooter();

        bgPanel.add(mainPanel, BorderLayout.CENTER);
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 32, 16, 32));

        navLabel = new JLabel("THE HUNT / MAIN MENU");
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

        JPanel topBorder = new JPanel();
        topBorder.setPreferredSize(new Dimension(1, 2));
        topBorder.setOpaque(true);
        topBarContainer.add(topBorder, BorderLayout.SOUTH);
        topBarContainer.putClientProperty("topBorder", topBorder);

        mainPanel.add(topBarContainer, BorderLayout.NORTH);
    }

    private void buildCenter() {
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Title
        titleLabel = new JLabel("THE HUNT");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        subtitleLabel = new JLabel("TYPE TO SURVIVE");
        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        dividerLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // painted via applyTheme
            }
        };
        dividerLine.setOpaque(false);
        dividerLine.setMaximumSize(new Dimension(48, 1));
        dividerLine.setPreferredSize(new Dimension(48, 1));
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menu buttons
        loginButton          = new HuntMenuButton("LOGIN",            "\u2192"); // →
        tutorialButton       = new HuntMenuButton("TUTORIAL",         "\u25C8"); // ◈
        highScoresButton     = new HuntMenuButton("HIGH SCORES",      "\u2605"); // ★
        parentControlsButton = new HuntMenuButton("PARENT CONTROLS",  "\u2699"); // ⚙
        exitButton           = new HuntMenuButton("EXIT",             "\u2715"); // ✕
        exitButton.setExitStyle(true);

        loginButton.addActionListener(e -> openLoginScreen());
        tutorialButton.addActionListener(e -> openTutorialScreen());
        highScoresButton.addActionListener(e -> openHighScoresScreen());
        parentControlsButton.addActionListener(e -> openParentControlLoginScreen());
        exitButton.addActionListener(e -> System.exit(0));
        loginButton.addActionListener(e -> SoundManager.playClick());
        tutorialButton.addActionListener(e -> SoundManager.playClick());
        highScoresButton.addActionListener(e -> SoundManager.playClick());
        parentControlsButton.addActionListener(e -> SoundManager.playClick());
        exitButton.addActionListener(e -> SoundManager.playClick());
        menuButtons = new HuntMenuButton[]{loginButton, tutorialButton, highScoresButton, parentControlsButton, exitButton};
        selectedMenuIndex = 0;

        center.add(Box.createVerticalGlue());
        center.add(titleLabel);
        center.add(Box.createVerticalStrut(14));
        center.add(subtitleLabel);
        center.add(Box.createVerticalStrut(18));
        center.add(dividerLine);
        center.add(Box.createVerticalStrut(32));
        center.add(loginButton);
        center.add(Box.createVerticalStrut(12));
        center.add(tutorialButton);
        center.add(Box.createVerticalStrut(12));
        center.add(highScoresButton);
        center.add(Box.createVerticalStrut(12));
        center.add(parentControlsButton);
        center.add(Box.createVerticalStrut(12));
        center.add(exitButton);
        center.add(Box.createVerticalStrut(18));
        interactionHintLabel = new JLabel("Keyboard: Up/Down select  Enter open  L login  T tutorial  H high scores  P parent controls  Esc exit");
        interactionHintLabel.setFont(FONT_FOOTER);
        interactionHintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(interactionHintLabel);
        center.add(Box.createVerticalGlue());

        mainPanel.add(center, BorderLayout.CENTER);
        updateKeyboardSelection();
    }

    private void buildFooter() {
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(8, 0, 24, 0));

        footerLabel1 = new JLabel(FOOTER_TEXT_1);
        footerLabel2 = new JLabel(FOOTER_TEXT_2);
        footerLabel1.setFont(FONT_FOOTER);
        footerLabel2.setFont(FONT_FOOTER);
        footerLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(footerLabel1);
        footer.add(Box.createVerticalStrut(4));
        footer.add(footerLabel2);

        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    private void installKeyboardShortcut() {
        JRootPane root = getRootPane();
        if (root == null) {
            return;
        }
        InputMap inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = root.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "activateSelected");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectPrevious");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectNext");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "openLogin");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "openTutorial");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "openHighScores");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "openParentControls");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitApplication");
        actionMap.put("activateSelected", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activateSelectedMenuAction();
            }
        });
        actionMap.put("openLogin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMenuIndex = 0;
                updateKeyboardSelection();
                activateSelectedMenuAction();
            }
        });
        actionMap.put("selectPrevious", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveKeyboardSelection(-1);
            }
        });
        actionMap.put("selectNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveKeyboardSelection(1);
            }
        });
        actionMap.put("openTutorial", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMenuIndex = 1;
                updateKeyboardSelection();
                activateSelectedMenuAction();
            }
        });
        actionMap.put("openHighScores", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMenuIndex = 2;
                updateKeyboardSelection();
                activateSelectedMenuAction();
            }
        });
        actionMap.put("openParentControls", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMenuIndex = 3;
                updateKeyboardSelection();
                activateSelectedMenuAction();
            }
        });
        actionMap.put("exitApplication", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMenuIndex = 4;
                updateKeyboardSelection();
                activateSelectedMenuAction();
            }
        });
    }

    private void moveKeyboardSelection(int delta) {
        if (menuButtons == null || menuButtons.length == 0) {
            return;
        }
        selectedMenuIndex = (selectedMenuIndex + delta + menuButtons.length) % menuButtons.length;
        updateKeyboardSelection();
    }

    private void updateKeyboardSelection() {
        if (menuButtons == null || menuButtons.length == 0) {
            return;
        }
        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i].setKeyboardSelected(i == selectedMenuIndex);
        }
        getRootPane().setDefaultButton(menuButtons[selectedMenuIndex]);
        menuButtons[selectedMenuIndex].requestFocusInWindow();
    }

    private void activateSelectedMenuAction() {
        if (menuButtons == null || menuButtons.length == 0) {
            return;
        }
        menuButtons[selectedMenuIndex].doClick(0);
    }

    // ═══════════════════════════════════════════════════════
    //  THEME APPLICATION
    // ═══════════════════════════════════════════════════════

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        Color bg, text, muted, accent, divider;

        if (darkThemeEnabled) {
            bg      = DARK_BG;
            text    = DARK_TEXT;
            muted   = DARK_MUTED;
            accent  = DARK_ACCENT;
            divider = DARK_DIVIDER;

            bgPanel.setTheme(true);
            loginButton.applyTheme(DARK_BTN_BG, DARK_BTN_BORDER, text, DARK_BTN_HOVER_BG, DARK_BTN_HOVER_BDR);
            tutorialButton.applyTheme(DARK_BTN_BG, DARK_BTN_BORDER, text, DARK_BTN_HOVER_BG, DARK_BTN_HOVER_BDR);
            highScoresButton.applyTheme(DARK_BTN_BG, DARK_BTN_BORDER, text, DARK_BTN_HOVER_BG, DARK_BTN_HOVER_BDR);
            parentControlsButton.applyTheme(DARK_BTN_BG, DARK_BTN_BORDER, text, DARK_BTN_HOVER_BG, DARK_BTN_HOVER_BDR);
            exitButton.applyTheme(DARK_EXIT_HOVER_BG, DARK_EXIT_BORDER, DARK_EXIT, DARK_EXIT_HOVER_BG, DARK_EXIT_HVBDR);
            themeTogglePanel.applyTheme(DARK_TOGGLE_BG, DARK_TOGGLE_BDR, muted, DARK_TOGGLE_SEL, bg);
        } else {
            bg      = LIGHT_BG;
            text    = LIGHT_TEXT;
            muted   = LIGHT_MUTED;
            accent  = LIGHT_ACCENT;
            divider = LIGHT_DIVIDER;

            bgPanel.setTheme(false);
            loginButton.applyTheme(LIGHT_BTN_BG, LIGHT_BTN_BORDER, text, LIGHT_BTN_HOVER_BG, LIGHT_BTN_HOVER_BDR);
            tutorialButton.applyTheme(LIGHT_BTN_BG, LIGHT_BTN_BORDER, text, LIGHT_BTN_HOVER_BG, LIGHT_BTN_HOVER_BDR);
            highScoresButton.applyTheme(LIGHT_BTN_BG, LIGHT_BTN_BORDER, text, LIGHT_BTN_HOVER_BG, LIGHT_BTN_HOVER_BDR);
            parentControlsButton.applyTheme(LIGHT_BTN_BG, LIGHT_BTN_BORDER, text, LIGHT_BTN_HOVER_BG, LIGHT_BTN_HOVER_BDR);
            exitButton.applyTheme(LIGHT_EXIT_HOVER_BG, LIGHT_EXIT_BORDER, LIGHT_EXIT, LIGHT_EXIT_HOVER_BG, LIGHT_EXIT_HVBDR);
            themeTogglePanel.applyTheme(LIGHT_TOGGLE_BG, LIGHT_TOGGLE_BDR, muted, LIGHT_TOGGLE_SEL, bg);
        }

        navLabel.setForeground(muted);
        titleLabel.setForeground(text);
        subtitleLabel.setForeground(accent);
        footerLabel1.setForeground(muted);
        footerLabel2.setForeground(muted);
        if (interactionHintLabel != null) {
            interactionHintLabel.setForeground(muted);
        }

        dividerLine.setBackground(divider);
        dividerLine.setOpaque(true);
        dividerLine.setMaximumSize(new Dimension(84, 2));
        dividerLine.setPreferredSize(new Dimension(84, 2));

        navChipPanel.setBackground(darkThemeEnabled ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 145));
        navChipPanel.setForeground(divider);
        navLabel.setText(darkThemeEnabled ? "  THE HUNT / MAIN MENU" : "  THE HUNT / MAIN MENU");

        JPanel topBarContainer = (JPanel) mainPanel.getComponent(0);
        JPanel topBorder = (JPanel) topBarContainer.getClientProperty("topBorder");
        if (topBorder != null) {
            topBorder.setBackground(divider);
        }

        bgPanel.repaint();
        mainPanel.repaint();
    }

    private void openLoginScreen() {
        dispose();
        new LoginScreen();
    }

    private void openTutorialScreen() {
        dispose();
        new TutorialScreen();
    }

    private void openHighScoresScreen() {
        dispose();
        new HighScoresScreen();
    }

    private void openParentControlLoginScreen() {
        dispose();
        new ParentControlLoginScreen();
    }

    // ═══════════════════════════════════════════════════════
    //  CUSTOM COMPONENTS
    // ═══════════════════════════════════════════════════════

    /**
     * Background panel with subtle radial glow effect.
     */
    private static class GradientBackgroundPanel extends JPanel {
        private boolean dark = true;

        void setTheme(boolean dark) {
            this.dark = dark;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int w = getWidth(), h = getHeight();

            // Solid background
            g2.setColor(dark ? DARK_BG : LIGHT_BG);
            g2.fillRect(0, 0, w, h);

            // Subtle radial glow top-left
            Color glowColor = dark
                    ? new Color(201, 165, 90, 12)
                    : new Color(139, 105, 20, 8);
            RadialGradientPaint glow1 = new RadialGradientPaint(
                    w * 0.2f, h * 0.15f, w * 0.6f,
                    new float[]{0f, 1f},
                    new Color[]{glowColor, new Color(0, 0, 0, 0)}
            );
            g2.setPaint(glow1);
            g2.fillRect(0, 0, w, h);

            // Subtle radial glow bottom-right
            Color glowColor2 = dark
                    ? new Color(201, 165, 90, 6)
                    : new Color(139, 105, 20, 4);
            RadialGradientPaint glow2 = new RadialGradientPaint(
                    w * 0.8f, h * 0.85f, w * 0.5f,
                    new float[]{0f, 1f},
                    new Color[]{glowColor2, new Color(0, 0, 0, 0)}
            );
            g2.setPaint(glow2);
            g2.fillRect(0, 0, w, h);

            // Corner accents (crosshair decoration)
            g2.setColor(dark ? new Color(232, 228, 220, 10) : new Color(26, 26, 26, 10));
            g2.setStroke(new BasicStroke(1));
            // Top-left
            g2.drawLine(50, 50, 50, 120);
            g2.drawLine(50, 50, 120, 50);
            // Bottom-right
            g2.drawLine(w - 50, h - 50, w - 50, h - 120);
            g2.drawLine(w - 50, h - 50, w - 120, h - 50);

            g2.dispose();
        }
    }

    /**
     * Custom rounded menu button with hover animation.
     */
    private static class HuntMenuButton extends JButton {
        private Color bgColor, borderColor, textColor, hoverBg, hoverBorder;
        private boolean hovered = false;
        private boolean keyboardSelected = false;
        private boolean exitStyle = false;
        private final String iconChar;
        private float hoverProgress = 0f;
        private Timer hoverTimer;

        HuntMenuButton(String text, String icon) {
            super(text);
            this.iconChar = icon;
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(380, 52));
            setMaximumSize(new Dimension(380, 52));
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
                    animateHover(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    animateHover(false);
                }
            });
        }

        void setExitStyle(boolean exit) {
            this.exitStyle = exit;
        }

        void setKeyboardSelected(boolean selected) {
            this.keyboardSelected = selected;
            repaint();
        }

        void applyTheme(Color bg, Color border, Color text, Color hBg, Color hBdr) {
            this.bgColor = bg;
            this.borderColor = border;
            this.textColor = text;
            this.hoverBg = hBg;
            this.hoverBorder = hBdr;
            repaint();
        }

        private void animateHover(boolean entering) {
            if (hoverTimer != null) hoverTimer.stop();
            hoverTimer = new Timer(16, e -> {
                if (entering) {
                    hoverProgress = Math.min(1f, hoverProgress + 0.12f);
                } else {
                    hoverProgress = Math.max(0f, hoverProgress - 0.12f);
                }
                repaint();
                if ((entering && hoverProgress >= 1f) || (!entering && hoverProgress <= 0f)) {
                    ((Timer) e.getSource()).stop();
                }
            });
            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int arc = 24;

            // Interpolate colors
            float visualProgress = keyboardSelected ? Math.max(hoverProgress, 0.9f) : hoverProgress;
            Color bg = interpolateColor(bgColor, hoverBg, visualProgress);
            Color bdr = interpolateColor(borderColor, hoverBorder, visualProgress);

            // Background
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // Gold sweep on hover
            if (visualProgress > 0) {
                Color sweepColor = exitStyle
                        ? new Color(184, 84, 80, (int)(20 * visualProgress))
                        : new Color(201, 165, 90, (int)(15 * visualProgress));
                GradientPaint sweep = new GradientPaint(
                        0, 0, sweepColor,
                        (int)(w * visualProgress), 0, new Color(0, 0, 0, 0)
                );
                g2.setPaint(sweep);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            }

            // Border
            g2.setColor(bdr);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, arc, arc));

            // Icon (left side, fades in on hover)
            if (visualProgress > 0.1f) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.setColor(new Color(
                        textColor.getRed(), textColor.getGreen(), textColor.getBlue(),
                        (int)(120 * visualProgress)
                ));
                FontMetrics iconFm = g2.getFontMetrics();
                g2.drawString(iconChar, 24, h / 2 + iconFm.getAscent() / 2 - 1);
            }

            // Text (centered)
            g2.setFont(getFont());
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(getText());
            int textH = fm.getAscent();
            g2.drawString(getText(), (w - textW) / 2, h / 2 + textH / 2 - 1);

            g2.dispose();
        }

        private Color interpolateColor(Color a, Color b, float t) {
            if (a == null || b == null) return a != null ? a : b;
            return new Color(
                    clamp((int)(a.getRed()   + (b.getRed()   - a.getRed())   * t)),
                    clamp((int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t)),
                    clamp((int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t)),
                    clamp((int)(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t))
            );
        }

        private int clamp(int v) { return Math.max(0, Math.min(255, v)); }
    }

    /**
     * Rounded pill theme toggle (Dark / Light).
     */
    private static class ThemeTogglePanel extends JPanel {
        private boolean darkSelected = true;
        private final JLabel darkLabel;
        private final JLabel lightLabel;
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
            darkLabel.setFont(FONT_TOGGLE);
            lightLabel.setFont(FONT_TOGGLE);
            darkLabel.setOpaque(false);
            lightLabel.setOpaque(false);

            add(lightLabel);
            add(darkLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean clickedLeft = e.getX() < getWidth() / 2;
                    darkSelected = !clickedLeft;
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

            // Selected indicator pill
            int pillW = w / 2;
            int pillX = darkSelected ? pillW - 1 : 3;
            g2.setColor(selectedColor != null ? selectedColor : new Color(201, 165, 90));
            g2.fillRoundRect(pillX, 3, pillW - 2, h - 6, h - 6, h - 6);

            g2.dispose();

            // Update label colors
            Color selC = selectedTextColor != null ? selectedTextColor : Color.BLACK;
            Color unselC = mutedColor != null ? mutedColor : new Color(200, 200, 200, 120);
            lightLabel.setForeground(!darkSelected ? selC : unselC);
            darkLabel.setForeground(darkSelected ? selC : unselC);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");

            super.paintComponent(g);
        }
    }

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
