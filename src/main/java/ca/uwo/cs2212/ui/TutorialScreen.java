package ca.uwo.cs2212.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tutorial carousel that explains the main gameplay loop and controls.
 */
public class TutorialScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";
    private static final String[] SLIDE_IMAGE_PATHS = {
            "assets/tutorial/slide-1.png",
            "assets/tutorial/slide-2.png",
            "assets/tutorial/slide-3.png",
            "assets/tutorial/slide-4.png"
    };
    private static final String RESOURCE_ROOT = "src/main/resources";

    // ── Dark theme palette ──
    private static final Color DARK_BG           = new Color(10, 10, 15);
    private static final Color DARK_TEXT          = new Color(232, 228, 220);
    private static final Color DARK_MUTED         = new Color(232, 228, 220, 115);
    private static final Color DARK_ACCENT        = new Color(201, 165, 90);
    private static final Color DARK_ACCENT_DIM    = new Color(201, 165, 90, 128);
    private static final Color DARK_CARD_BG       = new Color(255, 255, 255, 8);
    private static final Color DARK_CARD_BORDER   = new Color(255, 255, 255, 15);
    private static final Color DARK_BODY          = new Color(232, 228, 220, 184);
    private static final Color DARK_ARROW_BG      = new Color(255, 255, 255, 10);
    private static final Color DARK_ARROW_BORDER  = new Color(255, 255, 255, 20);
    private static final Color DARK_ARROW_HOVER   = new Color(201, 165, 90, 20);
    private static final Color DARK_ARROW_HVBDR   = new Color(201, 165, 90, 64);
    private static final Color DARK_ARROW_COLOR   = new Color(232, 228, 220, 128);
    private static final Color DARK_ARROW_HVCOLOR = DARK_ACCENT;
    private static final Color DARK_DOT_ACTIVE    = DARK_ACCENT;
    private static final Color DARK_DOT_INACTIVE  = new Color(255, 255, 255, 38);
    private static final Color DARK_BADGE_BG      = new Color(201, 165, 90, 20);
    private static final Color DARK_BADGE_BORDER  = new Color(201, 165, 90, 38);
    private static final Color DARK_IMG_BG        = new Color(255, 255, 255, 10);
    private static final Color DARK_IMG_BORDER    = new Color(255, 255, 255, 20);
    private static final Color DARK_DIVIDER       = new Color(201, 165, 90, 60);
    private static final Color DARK_TOGGLE_BG     = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR    = new Color(255, 255, 255, 15);

    // ── Light theme palette ──
    private static final Color LIGHT_BG           = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT          = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED        = new Color(26, 26, 26, 102);
    private static final Color LIGHT_ACCENT       = new Color(139, 105, 20);
    private static final Color LIGHT_ACCENT_DIM   = new Color(139, 105, 20, 115);
    private static final Color LIGHT_CARD_BG      = new Color(255, 255, 255, 140);
    private static final Color LIGHT_CARD_BORDER  = new Color(0, 0, 0, 20);
    private static final Color LIGHT_BODY         = new Color(26, 26, 26, 166);
    private static final Color LIGHT_ARROW_BG     = new Color(0, 0, 0, 8);
    private static final Color LIGHT_ARROW_BORDER = new Color(0, 0, 0, 25);
    private static final Color LIGHT_ARROW_HOVER  = new Color(139, 105, 20, 15);
    private static final Color LIGHT_ARROW_HVBDR  = new Color(139, 105, 20, 77);
    private static final Color LIGHT_ARROW_COLOR  = new Color(26, 26, 26, 90);
    private static final Color LIGHT_ARROW_HVCOLOR= LIGHT_ACCENT;
    private static final Color LIGHT_DOT_ACTIVE   = LIGHT_ACCENT;
    private static final Color LIGHT_DOT_INACTIVE = new Color(0, 0, 0, 30);
    private static final Color LIGHT_BADGE_BG     = new Color(139, 105, 20, 15);
    private static final Color LIGHT_BADGE_BORDER = new Color(139, 105, 20, 38);
    private static final Color LIGHT_IMG_BG       = new Color(0, 0, 0, 8);
    private static final Color LIGHT_IMG_BORDER   = new Color(0, 0, 0, 20);
    private static final Color LIGHT_DIVIDER      = new Color(139, 105, 20, 80);
    private static final Color LIGHT_TOGGLE_BG    = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR   = new Color(0, 0, 0, 20);

    // ── Fonts ──
    private static final Font FONT_TITLE     = new Font("Serif", Font.BOLD, 42);
    private static final Font FONT_PAGE_IND  = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_BADGE     = new Font("Serif", Font.BOLD, 11);
    private static final Font FONT_CARD_TTL  = new Font("Serif", Font.BOLD, 20);
    private static final Font FONT_CARD_BODY = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_NAV       = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_FOOTER    = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_TOGGLE    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_BACK      = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_ARROW     = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_IMG_PLACE = new Font("SansSerif", Font.PLAIN, 11);

    // ── Tutorial data ──
    private static final String[][] SLIDES = {
            {"Step 1", "Type to Survive",
                    "Words appear above each enemy as they move down the screen. Type the full word correctly and press Enter to defeat that target. Clean, accurate typing matters most: mistakes cost score, and missed enemies cost lives."},

            {"Step 2", "Increasing Difficulty",
                    "Difficulty ramps up through faster drop speed, shorter spawn intervals, more enemies on screen, and longer target words. The enemy shapes change as levels rise, but they do not currently have unique gameplay abilities."},

            {"Step 3", "Power-ups & Shop",
                    "Earn coins, buy power-ups in the shop, then trigger them in-game with 1, 2, or 3. Completing each game awards 200 coins, so regular play steadily unlocks Freeze, Shield, and Double Score."},

            {"Step 4", "Special Enemies",
                    "Some enemies have unique abilities. Remembrance enemies freeze all enemies when defeated. Hunt enemies eliminate another random enemy with a beam. Abundance enemies restore health. Watch their icons and react quickly."}
    };

    private int currentSlide = 0;

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel;
    private JPanel topBorderLine;
    private ThemeTogglePanel themeTogglePanel;

    private JLabel titleLabel;
    private JLabel pageIndicator;
    private GlassCardPanel cardPanel;
    private JLabel badgeLabel;
    private JLabel cardTitleLabel;
    private JTextArea cardBodyArea;
    private ImagePlaceholderPanel imagePlaceholder;
    private ArrowButton prevButton;
    private ArrowButton nextButton;
    private DotPanel dotPanel;
    private JButton backButton;
    private JLabel footerLabel;

    /**
     * Creates the tutorial screen and opens it on the first slide.
     */
    public TutorialScreen() {
        setTitle("The Hunt - Tutorial");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        applyTheme();
        updateSlide();

        setContentPane(bgPanel);
        setVisible(true);
        installEscapeShortcut();

        // Keyboard navigation
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "prev");
        getRootPane().getActionMap().put("prev", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { navigate(-1); }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "next");
        getRootPane().getActionMap().put("next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { navigate(1); }
        });
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

        navLabel = new JLabel("THE HUNT / TUTORIAL");
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

        // Title
        titleLabel = new JLabel("TUTORIAL");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Page indicator
        pageIndicator = new JLabel("Page 1 of 3");
        pageIndicator.setFont(FONT_PAGE_IND);
        pageIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Carousel row: [prev] [card] [next]
        JPanel carouselRow = new JPanel();
        carouselRow.setOpaque(false);
        carouselRow.setLayout(new BoxLayout(carouselRow, BoxLayout.X_AXIS));
        carouselRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        carouselRow.setMaximumSize(new Dimension(980, 500));
        carouselRow.setBorder(new EmptyBorder(0, 32, 0, 32));

        prevButton = new ArrowButton("\u2039"); // ‹
        nextButton = new ArrowButton("\u203A"); // ›
        prevButton.addActionListener(e -> navigate(-1));
        nextButton.addActionListener(e -> navigate(1));
        prevButton.addActionListener(e -> SoundManager.playClick());
        nextButton.addActionListener(e -> SoundManager.playClick());

        // Card
        cardPanel = new GlassCardPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(34, 42, 32, 42));
        cardPanel.setPreferredSize(new Dimension(650, 430));
        cardPanel.setMaximumSize(new Dimension(650, 430));

        badgeLabel = new BadgeLabel("Step 1");
        badgeLabel.setFont(FONT_BADGE);
        badgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardTitleLabel = new JLabel("Type to Survive");
        cardTitleLabel.setFont(FONT_CARD_TTL);
        cardTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardBodyArea = new JTextArea();
        cardBodyArea.setFont(FONT_CARD_BODY);
        cardBodyArea.setLineWrap(true);
        cardBodyArea.setWrapStyleWord(true);
        cardBodyArea.setEditable(false);
        cardBodyArea.setFocusable(false);
        cardBodyArea.setOpaque(false);
        cardBodyArea.setBorder(new EmptyBorder(0, 8, 0, 8));
        cardBodyArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardBodyArea.setMaximumSize(new Dimension(620, 96));

        imagePlaceholder = new ImagePlaceholderPanel();
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePlaceholder.setMaximumSize(new Dimension(580, 220));
        imagePlaceholder.setPreferredSize(new Dimension(580, 220));

        cardPanel.add(badgeLabel);
        cardPanel.add(Box.createVerticalStrut(18));
        cardPanel.add(cardTitleLabel);
        cardPanel.add(Box.createVerticalStrut(16));
        cardPanel.add(imagePlaceholder);
        cardPanel.add(Box.createVerticalStrut(18));
        cardPanel.add(cardBodyArea);

        JPanel prevWrap = new JPanel(new GridBagLayout());
        prevWrap.setOpaque(false);
        prevWrap.setPreferredSize(new Dimension(72, 100));
        prevWrap.add(prevButton);

        JPanel nextWrap = new JPanel(new GridBagLayout());
        nextWrap.setOpaque(false);
        nextWrap.setPreferredSize(new Dimension(72, 100));
        nextWrap.add(nextButton);

        carouselRow.add(prevWrap);
        carouselRow.add(cardPanel);
        carouselRow.add(nextWrap);

        // Dots
        dotPanel = new DotPanel(SLIDES.length);
        dotPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dotPanel.setDotClickListener(this::goToSlide);

        // Back
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
        center.add(Box.createVerticalStrut(8));
        center.add(pageIndicator);
        center.add(Box.createVerticalStrut(22));
        center.add(carouselRow);
        center.add(Box.createVerticalStrut(18));
        center.add(dotPanel);
        center.add(Box.createVerticalStrut(16));
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

    // ═══════════════════════════════════════════════════════
    //  NAVIGATION
    // ═══════════════════════════════════════════════════════

    private void navigate(int dir) {
        int next = currentSlide + dir;
        if (next < 0 || next >= SLIDES.length) return;
        currentSlide = next;
        updateSlide();
    }

    private void goToSlide(int index) {
        if (index < 0 || index >= SLIDES.length) return;
        currentSlide = index;
        updateSlide();
    }

    private void updateSlide() {
        String[] s = SLIDES[currentSlide];
        badgeLabel.setText(s[0]);
        cardTitleLabel.setText(s[1]);
        cardBodyArea.setText(s[2]);
        imagePlaceholder.setSlideIndex(currentSlide);
        pageIndicator.setText("Page " + (currentSlide + 1) + " of " + SLIDES.length);

        prevButton.setEnabled(currentSlide > 0);
        nextButton.setEnabled(currentSlide < SLIDES.length - 1);
        dotPanel.setActive(currentSlide);

        cardPanel.repaint();
    }

    // ═══════════════════════════════════════════════════════
    //  THEME
    // ═══════════════════════════════════════════════════════

    private void applyTheme() {
        Theme theme = ThemeManager.getTheme();
        boolean darkThemeEnabled = theme == Theme.DARK;
        Color dividerColor;

        if (darkThemeEnabled) {
            dividerColor = DARK_DIVIDER;
            bgPanel.setDark(true);

            navLabel.setForeground(DARK_MUTED);
            navChipPanel.setBackground(new Color(255, 255, 255, 10));
            navChipPanel.setForeground(dividerColor);
            titleLabel.setForeground(DARK_TEXT);
            pageIndicator.setForeground(DARK_ACCENT_DIM);
            cardPanel.applyTheme(DARK_CARD_BG, DARK_CARD_BORDER);
            cardTitleLabel.setForeground(DARK_TEXT);
            cardBodyArea.setForeground(DARK_BODY);
            ((BadgeLabel) badgeLabel).applyTheme(DARK_ACCENT, DARK_BADGE_BG, DARK_BADGE_BORDER);
            imagePlaceholder.applyTheme(DARK_IMG_BG, DARK_IMG_BORDER, DARK_MUTED);
            prevButton.applyTheme(DARK_ARROW_BG, DARK_ARROW_BORDER, DARK_ARROW_COLOR, DARK_ARROW_HOVER, DARK_ARROW_HVBDR, DARK_ARROW_HVCOLOR);
            nextButton.applyTheme(DARK_ARROW_BG, DARK_ARROW_BORDER, DARK_ARROW_COLOR, DARK_ARROW_HOVER, DARK_ARROW_HVBDR, DARK_ARROW_HVCOLOR);
            dotPanel.applyTheme(DARK_DOT_ACTIVE, DARK_DOT_INACTIVE);
            backButton.setForeground(DARK_MUTED);
            footerLabel.setForeground(DARK_MUTED);
            themeTogglePanel.applyTheme(DARK_TOGGLE_BG, DARK_TOGGLE_BDR, DARK_MUTED, DARK_ACCENT, DARK_BG);
        } else {
            dividerColor = LIGHT_DIVIDER;
            bgPanel.setDark(false);

            navLabel.setForeground(LIGHT_MUTED);
            navChipPanel.setBackground(new Color(255, 255, 255, 145));
            navChipPanel.setForeground(dividerColor);
            titleLabel.setForeground(LIGHT_TEXT);
            pageIndicator.setForeground(LIGHT_ACCENT_DIM);
            cardPanel.applyTheme(LIGHT_CARD_BG, LIGHT_CARD_BORDER);
            cardTitleLabel.setForeground(LIGHT_TEXT);
            cardBodyArea.setForeground(LIGHT_BODY);
            ((BadgeLabel) badgeLabel).applyTheme(LIGHT_ACCENT, LIGHT_BADGE_BG, LIGHT_BADGE_BORDER);
            imagePlaceholder.applyTheme(LIGHT_IMG_BG, LIGHT_IMG_BORDER, LIGHT_MUTED);
            prevButton.applyTheme(LIGHT_ARROW_BG, LIGHT_ARROW_BORDER, LIGHT_ARROW_COLOR, LIGHT_ARROW_HOVER, LIGHT_ARROW_HVBDR, LIGHT_ARROW_HVCOLOR);
            nextButton.applyTheme(LIGHT_ARROW_BG, LIGHT_ARROW_BORDER, LIGHT_ARROW_COLOR, LIGHT_ARROW_HOVER, LIGHT_ARROW_HVBDR, LIGHT_ARROW_HVCOLOR);
            dotPanel.applyTheme(LIGHT_DOT_ACTIVE, LIGHT_DOT_INACTIVE);
            backButton.setForeground(LIGHT_MUTED);
            footerLabel.setForeground(LIGHT_MUTED);
            themeTogglePanel.applyTheme(LIGHT_TOGGLE_BG, LIGHT_TOGGLE_BDR, LIGHT_MUTED, LIGHT_ACCENT, LIGHT_BG);
        }

        topBorderLine.setBackground(dividerColor);
        themeTogglePanel.setDarkSelected(darkThemeEnabled);
        bgPanel.repaint();
        mainPanel.repaint();
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
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitTutorial");
        root.getActionMap().put("exitTutorial", new AbstractAction() {
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

    /** Background with radial glow — same as other screens. */
    private static class GradientBackgroundPanel extends JPanel {
        private boolean dark = true;

        void setDark(boolean dark) { this.dark = dark; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();

            g2.setColor(dark ? DARK_BG : LIGHT_BG);
            g2.fillRect(0, 0, w, h);

            Color glow1 = dark ? new Color(201, 165, 90, 12) : new Color(139, 105, 20, 8);
            g2.setPaint(new RadialGradientPaint(w * 0.2f, h * 0.15f, w * 0.6f,
                    new float[]{0f, 1f}, new Color[]{glow1, new Color(0, 0, 0, 0)}));
            g2.fillRect(0, 0, w, h);

            Color glow2 = dark ? new Color(201, 165, 90, 6) : new Color(139, 105, 20, 4);
            g2.setPaint(new RadialGradientPaint(w * 0.8f, h * 0.85f, w * 0.5f,
                    new float[]{0f, 1f}, new Color[]{glow2, new Color(0, 0, 0, 0)}));
            g2.fillRect(0, 0, w, h);

            g2.setColor(dark ? new Color(232, 228, 220, 10) : new Color(26, 26, 26, 10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50, 50, 50, 120); g2.drawLine(50, 50, 120, 50);
            g2.drawLine(w - 50, h - 50, w - 50, h - 120); g2.drawLine(w - 50, h - 50, w - 120, h - 50);
            g2.dispose();
        }
    }

    /** Glass card panel — same as LoginScreen. */
    private static class GlassCardPanel extends JPanel {
        private Color bgColor = new Color(255, 255, 255, 8);
        private Color borderColor = new Color(255, 255, 255, 15);

        GlassCardPanel() { setOpaque(false); }

        void applyTheme(Color bg, Color border) {
            this.bgColor = bg; this.borderColor = border; repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape s = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.setColor(bgColor); g2.fill(s);
            g2.setColor(borderColor); g2.setStroke(new BasicStroke(1f)); g2.draw(s);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Step badge label with rounded pill background. */
    private static class BadgeLabel extends JLabel {
        private Color textColor = DARK_ACCENT;
        private Color bgColor = DARK_BADGE_BG;
        private Color borderColor = DARK_BADGE_BORDER;

        BadgeLabel(String text) {
            super(text, CENTER);
            setOpaque(false);
            setMaximumSize(new Dimension(120, 30));
            setPreferredSize(new Dimension(120, 30));
        }

        void applyTheme(Color text, Color bg, Color border) {
            this.textColor = text; this.bgColor = bg; this.borderColor = border;
            setForeground(text); repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w, h, h, h);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Image placeholder panel. */
    private static class ImagePlaceholderPanel extends JPanel {
        private static final BufferedImage[] SLIDE_IMAGES = loadSlideImages();
        private Color bgColor = DARK_IMG_BG;
        private Color borderColor = DARK_IMG_BORDER;
        private Color textColor = DARK_MUTED;
        private int slideIndex;

        ImagePlaceholderPanel() { setOpaque(false); }

        void applyTheme(Color bg, Color border, Color text) {
            this.bgColor = bg; this.borderColor = border; this.textColor = text; repaint();
        }

        void setSlideIndex(int slideIndex) {
            this.slideIndex = slideIndex;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, 16, 16));

            int cx = w / 2, cy = h / 2;
            if (!drawSlideImage(g2, w, h)) {
                drawSlideArt(g2, w, h, cx, cy);
            }

            g2.dispose();
        }

        private boolean drawSlideImage(Graphics2D g2, int w, int h) {
            if (slideIndex < 0 || slideIndex >= SLIDE_IMAGES.length) return false;
            BufferedImage image = SLIDE_IMAGES[slideIndex];
            if (image == null) return false;

            int pad = 10;
            double scale = Math.min((w - pad * 2) / (double) image.getWidth(), (h - pad * 2) / (double) image.getHeight());
            int drawW = Math.max(1, (int) Math.round(image.getWidth() * scale));
            int drawH = Math.max(1, (int) Math.round(image.getHeight() * scale));
            int dx = (w - drawW) / 2;
            int dy = (h - drawH) / 2;
            g2.drawImage(image, dx, dy, drawW, drawH, null);
            return true;
        }

        private void drawSlideArt(Graphics2D g2, int w, int h, int cx, int cy) {
            g2.setColor(textColor);
            g2.setStroke(new BasicStroke(1.5f));

            if (slideIndex == 0) {
                drawGameplayMock(g2, w, h);
            } else if (slideIndex == 1) {
                drawDifficultyMock(g2, cx, cy);
            } else if (slideIndex == 2) {
                drawPowerupMock(g2, cx, cy);
            } else {
                drawSpecialEnemyMock(g2, cx, cy); // ← 新增
            }
        }

        private void drawGameplayMock(Graphics2D g2, int w, int h) {
            int sx = 54, sy = 14, sw = w - 108;
            Color softFill = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 10);
            Color paleFill = new Color(255, 255, 255, 120);
            Color enemyBlue = new Color(200, 211, 224, 180);
            Color enemyGreen = new Color(212, 228, 208, 180);
            Color enemyBlueStroke = new Color(176, 186, 200, 180);
            Color enemyGreenStroke = new Color(180, 203, 175, 180);

            g2.setColor(paleFill);
            g2.fill(new RoundRectangle2D.Float(sx, sy, sw, 96, 12, 12));
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(sx, sy, sw, 96, 12, 12));

            drawEnemyCard(g2, sx + 54, sy + 20, "Apple", enemyBlue, enemyBlueStroke, true);
            drawEnemyCard(g2, sx + 250, sy + 56, "Forest", enemyBlue, enemyBlueStroke, true);
            drawEnemyCard(g2, sx + 430, sy, "Viper", enemyGreen, enemyGreenStroke, false);

            int statsY = sy + 108;
            g2.setColor(softFill);
            g2.fillRect(sx, statsY, sw, 28);
            g2.setColor(borderColor);
            g2.drawRect(sx, statsY, sw, 28);
            g2.drawString("WPM", sx + 18, statsY + 18);
            g2.drawString("72", sx + 48, statsY + 18);
            g2.drawString("ACCURACY", sx + 92, statsY + 18);
            g2.drawString("94%", sx + 170, statsY + 18);
            g2.drawString("LIVES", sx + sw - 92, statsY + 18);
            g2.drawString("♥ ♥ ♡", sx + sw - 50, statsY + 18);

            int inputY = statsY + 36;
            g2.setColor(paleFill);
            g2.fill(new RoundRectangle2D.Float(sx, inputY, sw, 32, 8, 8));
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(sx, inputY, sw, 32, 8, 8));
            g2.drawString("TYPE WORD", sx + 16, inputY + 12);
            g2.drawString("apple", sx + 82, inputY + 22);
            drawMiniHint(g2, sx + sw - 118, inputY + 8, "Tab skip");
            drawMiniHint(g2, sx + sw - 56, inputY + 8, "Enter");

            int powerY = inputY + 40;
            drawPowerStrip(g2, sx, powerY, 146, "1", "Freeze", "x2");
            drawPowerStrip(g2, sx + 156, powerY, 146, "2", "Shield", "x1");
            drawPowerStrip(g2, sx + 312, powerY, 162, "3", "Double Score", "x1");
        }

        private void drawEnemyCard(Graphics2D g2, int x, int y, String word, Color fill, Color stroke, boolean happy) {
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(x, y, 64, 64, 14, 14));
            g2.setColor(stroke);
            g2.draw(new RoundRectangle2D.Float(x, y, 64, 64, 14, 14));
            g2.fill(new Ellipse2D.Float(x + 17, y + 23, 7, 7));
            g2.fill(new Ellipse2D.Float(x + 40, y + 23, 7, 7));
            if (happy) g2.drawArc(x + 21, y + 35, 22, 10, 200, 140);
            else g2.drawArc(x + 21, y + 37, 22, 10, 20, 140);
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fill(new RoundRectangle2D.Float(x - 3, y + 72, Math.max(70, word.length() * 8), 24, 6, 6));
            g2.setColor(textColor);
            g2.drawString(word, x + 12, y + 88);
        }

        private void drawMiniHint(Graphics2D g2, int x, int y, String label) {
            g2.setColor(new Color(255, 255, 255, 100));
            g2.fill(new RoundRectangle2D.Float(x, y, 48, 16, 5, 5));
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(x, y, 48, 16, 5, 5));
            g2.drawString(label, x + 4, y + 11);
        }

        private void drawPowerStrip(Graphics2D g2, int x, int y, int width, String key, String label, String count) {
            g2.setColor(new Color(255, 255, 255, 110));
            g2.fill(new RoundRectangle2D.Float(x, y, width, 22, 6, 6));
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(x, y, width, 22, 6, 6));
            g2.fill(new RoundRectangle2D.Float(x + 6, y + 4, 14, 14, 4, 4));
            g2.setColor(bgColor);
            g2.drawString(key, x + 10, y + 15);
            g2.setColor(textColor);
            g2.drawString(label, x + 28, y + 15);
            g2.drawString(count, x + width - 20, y + 15);
        }

        private void drawDifficultyMock(Graphics2D g2, int cx, int cy) {
            g2.drawLine(cx - 100, cy + 34, cx + 100, cy + 34);
            g2.drawLine(cx - 100, cy + 34, cx - 100, cy - 44);
            g2.drawPolyline(
                    new int[]{cx - 88, cx - 48, cx - 6, cx + 38, cx + 86},
                    new int[]{cy + 18, cy + 8, cy - 4, cy - 20, cy - 36}, 5);
            g2.drawString("speed", cx - 78, cy - 42);
            g2.drawString("spawn rate", cx + 28, cy - 42);
            g2.drawString("word size", cx - 24, cy + 60);
        }

        private void drawPowerupMock(Graphics2D g2, int cx, int cy) {
            int cardY = cy - 30;
            drawPowerCard(g2, cx - 108, cardY, "1", "Freeze");
            drawPowerCard(g2, cx - 24, cardY, "2", "Shield");
            drawPowerCard(g2, cx + 60, cardY, "3", "Score x2");
            g2.drawString("+200 coins each game", cx - 62, cy + 72);
        }

        private void drawPowerCard(Graphics2D g2, int x, int y, String key, String label) {
            g2.drawRoundRect(x, y, 68, 46, 12, 12);
            g2.drawRoundRect(x + 8, y + 8, 20, 20, 6, 6);
            g2.drawString(key, x + 15, y + 22);
            g2.drawString(label, x + 8, y + 39);
        }

        private static BufferedImage[] loadSlideImages() {
            BufferedImage[] images = new BufferedImage[SLIDE_IMAGE_PATHS.length];
            for (int i = 0; i < SLIDE_IMAGE_PATHS.length; i++) {
                try {
                    File resourceFile = new File(RESOURCE_ROOT, SLIDE_IMAGE_PATHS[i]);
                    if (resourceFile.exists()) {
                        images[i] = ImageIO.read(resourceFile);
                        continue;
                    }
                    File legacyFile = new File(SLIDE_IMAGE_PATHS[i]);
                    if (legacyFile.exists()) {
                        images[i] = ImageIO.read(legacyFile);
                        continue;
                    }
                    try (InputStream stream = TutorialScreen.class.getClassLoader().getResourceAsStream(SLIDE_IMAGE_PATHS[i])) {
                        images[i] = stream == null ? null : ImageIO.read(stream);
                    }
                    if (images[i] == null) {
                        System.err.println("Tutorial image missing: " + SLIDE_IMAGE_PATHS[i]);
                    }
                } catch (IOException ex) {
                    System.err.println("Failed to load tutorial image: " + SLIDE_IMAGE_PATHS[i]);
                    images[i] = null;
                }
            }
            return images;
        }

        private void drawSpecialEnemyMock(Graphics2D g2, int cx, int cy) {
            g2.drawString("Memory", cx - 120, cy);
            g2.drawString("Hunt", cx - 20, cy);
            g2.drawString("Abundance", cx + 80, cy);

            // 简单三图标
            g2.drawOval(cx - 120, cy - 30, 40, 40); // Memory
            g2.drawLine(cx - 20, cy - 30, cx + 20, cy + 10); // Hunt beam
            g2.drawOval(cx + 80, cy - 30, 40, 40); // Abundance

            g2.drawString("Freeze", cx - 120, cy + 40);
            g2.drawString("Beam", cx - 20, cy + 40);
            g2.drawString("Heal", cx + 80, cy + 40);
        }
    }

    /** Circular arrow button with hover animation. */
    private static class ArrowButton extends JButton {
        private Color bgColor, borderColor, arrowColor, hoverBg, hoverBorder, hoverArrowColor;
        private boolean hovered = false;
        private float hoverProgress = 0f;
        private Timer hoverTimer;

        ArrowButton(String arrow) {
            super(arrow);
            setPreferredSize(new Dimension(52, 52));
            setMaximumSize(new Dimension(52, 52));
            setMinimumSize(new Dimension(52, 52));
            setFont(FONT_ARROW);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hovered = true; animateHover(true); }
                @Override
                public void mouseExited(MouseEvent e) { hovered = false; animateHover(false); }
            });
        }

        void applyTheme(Color bg, Color border, Color arrow, Color hBg, Color hBdr, Color hArrow) {
            this.bgColor = bg; this.borderColor = border; this.arrowColor = arrow;
            this.hoverBg = hBg; this.hoverBorder = hBdr; this.hoverArrowColor = hArrow;
            repaint();
        }

        private void animateHover(boolean entering) {
            if (hoverTimer != null) hoverTimer.stop();
            hoverTimer = new Timer(16, e -> {
                hoverProgress = entering
                        ? Math.min(1f, hoverProgress + 0.15f)
                        : Math.max(0f, hoverProgress - 0.15f);
                repaint();
                if ((entering && hoverProgress >= 1f) || (!entering && hoverProgress <= 0f))
                    ((Timer) e.getSource()).stop();
            });
            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (!isEnabled()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(128, 128, 128, 15));
                g2.fillOval(0, 0, w, h);
                g2.setColor(new Color(128, 128, 128, 30));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, h / 2 + fm.getAscent() / 2 - 2);
                g2.dispose();
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            Color bg = interpolate(bgColor, hoverBg, hoverProgress);
            Color bdr = interpolate(borderColor, hoverBorder, hoverProgress);
            Color arr = interpolate(arrowColor, hoverArrowColor, hoverProgress);

            g2.setColor(bg); g2.fillOval(0, 0, w, h);
            g2.setColor(bdr); g2.setStroke(new BasicStroke(1f)); g2.drawOval(0, 0, w - 1, h - 1);
            g2.setColor(arr); g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, h / 2 + fm.getAscent() / 2 - 2);
            g2.dispose();
        }

        private Color interpolate(Color a, Color b, float t) {
            if (a == null || b == null) return a != null ? a : b;
            return new Color(
                    clamp((int)(a.getRed() + (b.getRed() - a.getRed()) * t)),
                    clamp((int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t)),
                    clamp((int)(a.getBlue() + (b.getBlue() - a.getBlue()) * t)),
                    clamp((int)(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t)));
        }
        private int clamp(int v) { return Math.max(0, Math.min(255, v)); }
    }

    /** Dot navigation indicator. */
    private static class DotPanel extends JPanel {
        private final int count;
        private int active = 0;
        private Color activeColor = DARK_DOT_ACTIVE;
        private Color inactiveColor = DARK_DOT_INACTIVE;
        private java.util.function.IntConsumer clickListener;

        DotPanel(int count) {
            this.count = count;
            setOpaque(false);
            setMaximumSize(new Dimension(200, 16));
            setPreferredSize(new Dimension(200, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (clickListener == null) return;
                    int dotSpacing = 18;
                    int totalWidth = (count - 1) * dotSpacing + 28; // active dot is wider
                    int startX = (getWidth() - totalWidth) / 2;
                    int x = e.getX() - startX;
                    int idx = x / dotSpacing;
                    if (idx >= 0 && idx < count) clickListener.accept(idx);
                }
            });
        }

        void setActive(int i) { this.active = i; repaint(); }
        void setDotClickListener(java.util.function.IntConsumer l) { this.clickListener = l; }
        void applyTheme(Color active, Color inactive) {
            this.activeColor = active; this.inactiveColor = inactive; repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int dotSize = 8, activeW = 28, gap = 10;
            int totalW = 0;
            for (int i = 0; i < count; i++) totalW += (i == active ? activeW : dotSize);
            totalW += (count - 1) * gap;
            int x = (getWidth() - totalW) / 2;
            int y = (getHeight() - dotSize) / 2;

            for (int i = 0; i < count; i++) {
                boolean isActive = i == active;
                int w = isActive ? activeW : dotSize;
                g2.setColor(isActive ? activeColor : inactiveColor);
                g2.fillRoundRect(x, y, w, dotSize, dotSize, dotSize);
                x += w + gap;
            }
            g2.dispose();
        }
    }

    /** Theme toggle — matches MainMenuScreen and LoginScreen. */
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            lightLabel = new JLabel("Light", SwingConstants.CENTER);
            darkLabel = new JLabel("Dark", SwingConstants.CENTER);
            lightLabel.setFont(FONT_TOGGLE); darkLabel.setFont(FONT_TOGGLE);
            lightLabel.setOpaque(false); darkLabel.setOpaque(false);
            add(lightLabel); add(darkLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    darkSelected = e.getX() >= getWidth() / 2;
                    repaint();
                    if (listener != null) listener.accept(darkSelected);
                }
            });
        }

        void setDarkSelected(boolean dark) { this.darkSelected = dark; repaint(); }
        void setToggleListener(java.util.function.Consumer<Boolean> l) { this.listener = l; }
        void applyTheme(Color bg, Color border, Color muted, Color selected, Color selText) {
            this.bgColor = bg; this.borderColor = border; this.mutedColor = muted;
            this.selectedColor = selected; this.selectedTextColor = selText; repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), arc = h;

            g2.setColor(bgColor != null ? bgColor : new Color(255, 255, 255, 10));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(borderColor != null ? borderColor : new Color(255, 255, 255, 20));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            int pillW = w / 2, pillX = darkSelected ? pillW - 1 : 3;
            g2.setColor(selectedColor != null ? selectedColor : new Color(201, 165, 90));
            g2.fillRoundRect(pillX, 3, pillW - 2, h - 6, h - 6, h - 6);
            g2.dispose();

            Color selC = selectedTextColor != null ? selectedTextColor : Color.BLACK;
            Color unselC = mutedColor != null ? mutedColor : new Color(200, 200, 200, 120);
            lightLabel.setForeground(!darkSelected ? selC : unselC);
            darkLabel.setForeground(darkSelected ? selC : unselC);
            lightLabel.setText(!darkSelected ? "\u2713  Light" : "Light");
            darkLabel.setText(darkSelected ? "\u2713  Dark" : "Dark");

            super.paintComponent(g);
        }
    }

    /** Rounded chip panel with left accent bar. */
    private static class RoundedPanel extends JPanel {
        private final int arc;
        RoundedPanel(int arc) { this.arc = arc; setOpaque(false); }

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
