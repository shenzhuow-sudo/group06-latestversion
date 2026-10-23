package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.FileStorageService;
import ca.uwo.cs2212.demo.Player;
import ca.uwo.cs2212.demo.PowerUpType;
import ca.uwo.cs2212.demo.SaveManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * In-game shop screen for purchasing power-ups with player coins.
 */
public class ShopScreen extends JFrame {
    private static final String FOOTER_TEXT = "CS2212 \u00B7 Group 06 \u00B7 Winter 2026";

    // ── Dark palette ──
    private static final Color DARK_BG           = new Color(10, 10, 15);
    private static final Color DARK_TEXT          = new Color(232, 228, 220);
    private static final Color DARK_MUTED         = new Color(232, 228, 220, 115);
    private static final Color DARK_BODY          = new Color(232, 228, 220, 166);
    private static final Color DARK_ACCENT        = new Color(201, 165, 90);
    private static final Color DARK_ACCENT_DIM    = new Color(201, 165, 90, 128);
    private static final Color DARK_CARD_BG       = new Color(255, 255, 255, 6);
    private static final Color DARK_CARD_BORDER   = new Color(255, 255, 255, 15);
    private static final Color DARK_CARD_HOVER_B  = new Color(201, 165, 90, 50);
    private static final Color DARK_IMG_BG        = new Color(255, 255, 255, 10);
    private static final Color DARK_IMG_BORDER    = new Color(255, 255, 255, 15);
    private static final Color DARK_DIVIDER       = new Color(255, 255, 255, 10);
    private static final Color DARK_BAL_BG        = new Color(201, 165, 90, 15);
    private static final Color DARK_BAL_BORDER    = new Color(201, 165, 90, 46);
    private static final Color DARK_BUY_BORDER    = new Color(201, 165, 90, 90);
    private static final Color DARK_BUY_HOVER     = new Color(201, 165, 90, 30);
    private static final Color DARK_DISABLED_TEXT  = new Color(232, 228, 220, 50);
    private static final Color DARK_DISABLED_B     = new Color(255, 255, 255, 10);
    private static final Color DARK_INSUF          = new Color(184, 84, 80, 180);
    private static final Color DARK_ICON           = new Color(201, 165, 90, 64);
    private static final Color DARK_TOP_DIVIDER    = new Color(201, 165, 90, 60);
    private static final Color DARK_TOGGLE_BG      = new Color(255, 255, 255, 8);
    private static final Color DARK_TOGGLE_BDR     = new Color(255, 255, 255, 15);

    // ── Light palette ──
    private static final Color LIGHT_BG           = new Color(240, 236, 228);
    private static final Color LIGHT_TEXT          = new Color(26, 26, 26);
    private static final Color LIGHT_MUTED         = new Color(26, 26, 26, 102);
    private static final Color LIGHT_BODY          = new Color(26, 26, 26, 140);
    private static final Color LIGHT_ACCENT        = new Color(139, 105, 20);
    private static final Color LIGHT_ACCENT_DIM    = new Color(139, 105, 20, 102);
    private static final Color LIGHT_CARD_BG       = new Color(255, 255, 255, 128);
    private static final Color LIGHT_CARD_BORDER   = new Color(0, 0, 0, 15);
    private static final Color LIGHT_CARD_HOVER_B  = new Color(139, 105, 20, 55);
    private static final Color LIGHT_IMG_BG        = new Color(0, 0, 0, 8);
    private static final Color LIGHT_IMG_BORDER    = new Color(0, 0, 0, 15);
    private static final Color LIGHT_DIVIDER       = new Color(0, 0, 0, 12);
    private static final Color LIGHT_BAL_BG        = new Color(139, 105, 20, 12);
    private static final Color LIGHT_BAL_BORDER    = new Color(139, 105, 20, 46);
    private static final Color LIGHT_BUY_BORDER    = new Color(139, 105, 20, 90);
    private static final Color LIGHT_BUY_HOVER     = new Color(139, 105, 20, 20);
    private static final Color LIGHT_DISABLED_TEXT  = new Color(26, 26, 26, 46);
    private static final Color LIGHT_DISABLED_B     = new Color(0, 0, 0, 12);
    private static final Color LIGHT_INSUF          = new Color(158, 58, 55, 180);
    private static final Color LIGHT_ICON           = new Color(139, 105, 20, 50);
    private static final Color LIGHT_TOP_DIVIDER    = new Color(139, 105, 20, 80);
    private static final Color LIGHT_TOGGLE_BG      = new Color(0, 0, 0, 5);
    private static final Color LIGHT_TOGGLE_BDR     = new Color(0, 0, 0, 20);

    // ── Fonts ──
    private static final Font FONT_TITLE     = new Font("Serif", Font.BOLD, 26);
    private static final Font FONT_SUB       = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BAL_LBL   = new Font("SansSerif", Font.BOLD, 9);
    private static final Font FONT_BAL_VAL   = new Font("Monospaced", Font.BOLD, 20);
    private static final Font FONT_BAL_UNIT  = new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_ITEM_NAME = new Font("Serif", Font.BOLD, 19);
    private static final Font FONT_ITEM_DESC = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_PRICE     = new Font("Monospaced", Font.BOLD, 13);
    private static final Font FONT_PRICE_UNIT= new Font("SansSerif", Font.PLAIN, 10);
    private static final Font FONT_BUY       = new Font("Serif", Font.BOLD, 11);
    private static final Font FONT_INSUF     = new Font("SansSerif", Font.BOLD, 9);
    private static final Font FONT_ICON      = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONT_NAV       = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_TOGGLE    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_FOOTER    = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_BACK      = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_SECTION   = new Font("SansSerif", Font.BOLD, 10);

    // ── Item data ──
    private static final ItemSpec[] ITEMS = {
            new ItemSpec("Freeze", "Slows enemy movement for a short burst so the player has time to recover and clear the board.", 600, "\u2744", PowerUpType.FREEZE),
            new ItemSpec("Shield", "Absorbs one incoming enemy hit and prevents the next life loss during the round.", 500, "\u26E8", PowerUpType.SHIELD),
            new ItemSpec("Double Score", "Temporarily doubles points earned from defeated words while the boost is active.", 750, "\u00D7\u0032", PowerUpType.DOUBLE_SCORE),
    };

    private GradientBackgroundPanel bgPanel;
    private JPanel mainPanel;
    private JLabel navLabel;
    private JPanel navChipPanel, topBorderLine;
    private ThemeTogglePanel themeTogglePanel;
    private JLabel titleLabel, subLabel;
    private JLabel ownedLabel;
    private BalancePanel balancePanel;
    private JPanel dividerLine;
    private ShopItemCard[] itemCards;
    private JButton backButton;
    private JLabel footerLabel;
    private JLabel toastLabel;
    private Timer toastTimer;

    private final String username;
    private final Player player;

    /**
     * Prevents opening the shop without an existing player.
     */
    public ShopScreen() {
        throw new IllegalStateException("ShopScreen requires an existing player username.");
    }

    /**
     * Creates a shop screen for the given saved player.
     *
     * @param username player username to load
     */
    public ShopScreen(String username) {
        this.username = requireUsername(username);
        this.player = loadExistingPlayer(this.username);
        ThemeManager.setTheme(UiRuntimeGuard.toUiTheme(player.getProgress().getSettings().getTheme()));
        SoundManager.setEnabled(player.getProgress().getSettings().isSoundOn());
        setTitle("The Hunt - Shop");
        WindowSizing.applyFixedScreenSafeSize(this, 1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        refreshShopUI();
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

        navLabel = new JLabel("THE HUNT / SHOP");
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
            player.getProgress().getSettings().setTheme(UiRuntimeGuard.toModelTheme(selectedTheme));
            try {
                savePlayer();
            } catch (RuntimeException ex) {
                if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                    UiRuntimeGuard.showDataAccessError(this, "save player settings", ex);
                    return;
                }
                throw ex;
            }
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
        outer.setBorder(new EmptyBorder(10, 48, 8, 48));

        JPanel shell = new JPanel(new GridBagLayout());
        shell.setOpaque(false);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setMaximumSize(new Dimension(1020, Integer.MAX_VALUE));
        center.setPreferredSize(new Dimension(1020, 560));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header row: title + balance
        JPanel headerRow = new JPanel(new BorderLayout(20, 0));
        headerRow.setOpaque(false);
        headerRow.setMinimumSize(new Dimension(1020, 70));
        headerRow.setPreferredSize(new Dimension(1020, 70));

        JPanel titleCol = new JPanel();
        titleCol.setOpaque(false);
        titleCol.setLayout(new BoxLayout(titleCol, BoxLayout.Y_AXIS));
        titleLabel = new JLabel("AVAILABLE POWERUPS");
        titleLabel.setFont(FONT_TITLE);
        subLabel = new JLabel("Purchase powerups using in-game currency");
        subLabel.setFont(FONT_SUB);
        ownedLabel = new JLabel();
        ownedLabel.setFont(FONT_SUB);
        titleCol.add(titleLabel);
        titleCol.add(Box.createVerticalStrut(4));
        titleCol.add(subLabel);
        titleCol.add(Box.createVerticalStrut(4));
        titleCol.add(ownedLabel);

        balancePanel = new BalancePanel();
        balancePanel.setPreferredSize(new Dimension(180, 60));

        headerRow.add(titleCol, BorderLayout.CENTER);
        headerRow.add(balancePanel, BorderLayout.EAST);

        // Divider
        dividerLine = new JPanel();
        dividerLine.setOpaque(true);
        dividerLine.setPreferredSize(new Dimension(1020, 1));

        // Items grid 1x3
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setOpaque(false);
        grid.setMinimumSize(new Dimension(1020, 420));
        grid.setPreferredSize(new Dimension(1020, 430));

        itemCards = new ShopItemCard[ITEMS.length];
        for (int i = 0; i < ITEMS.length; i++) {
            itemCards[i] = new ShopItemCard(ITEMS[i]);
            grid.add(itemCards[i]);
        }

        // Back
        backButton = new JButton("\u2190  Back to Dashboard");
        backButton.setFont(FONT_BACK);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> { dispose(); new DashboardScreen(username); });
        backButton.addActionListener(e -> SoundManager.playClick());

        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 16, 0);
        center.add(headerRow, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        center.add(dividerLine, gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 22, 0);
        center.add(grid, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        center.add(backButton, gbc);

        GridBagConstraints shellGbc = new GridBagConstraints();
        shellGbc.gridx = 0;
        shellGbc.gridy = 0;
        shellGbc.weightx = 1.0;
        shellGbc.weighty = 1.0;
        shellGbc.anchor = GridBagConstraints.CENTER;
        shellGbc.fill = GridBagConstraints.BOTH;
        shell.add(center, shellGbc);

        outer.add(shell, BorderLayout.CENTER);
        toastLabel = new JLabel("", SwingConstants.CENTER);
        toastLabel.setFont(FONT_SUB);
        toastLabel.setVisible(false);
        outer.add(toastLabel, BorderLayout.SOUTH);
        mainPanel.add(outer, BorderLayout.CENTER);
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
        bgPanel.setDark(d);

        Color divider = d ? DARK_TOP_DIVIDER : LIGHT_TOP_DIVIDER;
        navLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        navChipPanel.setBackground(d ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 145));
        navChipPanel.setForeground(divider);
        topBorderLine.setBackground(divider);

        titleLabel.setForeground(d ? DARK_TEXT : LIGHT_TEXT);
        subLabel.setForeground(d ? DARK_BODY : LIGHT_BODY);
        ownedLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        dividerLine.setBackground(d ? DARK_ACCENT_DIM : LIGHT_ACCENT_DIM);

        balancePanel.applyTheme(
                d ? DARK_BAL_BG : LIGHT_BAL_BG,
                d ? DARK_BAL_BORDER : LIGHT_BAL_BORDER,
                d ? DARK_ACCENT : LIGHT_ACCENT,
                d ? DARK_TEXT : LIGHT_TEXT,
                d ? DARK_MUTED : LIGHT_MUTED);

        for (ShopItemCard card : itemCards) {
            card.applyTheme(
                    d ? DARK_CARD_BG : LIGHT_CARD_BG,
                    d ? DARK_CARD_BORDER : LIGHT_CARD_BORDER,
                    d ? DARK_CARD_HOVER_B : LIGHT_CARD_HOVER_B,
                    d ? DARK_IMG_BG : LIGHT_IMG_BG,
                    d ? DARK_IMG_BORDER : LIGHT_IMG_BORDER,
                    d ? DARK_TEXT : LIGHT_TEXT,
                    d ? DARK_BODY : LIGHT_BODY,
                    d ? DARK_ACCENT : LIGHT_ACCENT,
                    d ? DARK_MUTED : LIGHT_MUTED,
                    d ? DARK_BUY_BORDER : LIGHT_BUY_BORDER,
                    d ? DARK_BUY_HOVER : LIGHT_BUY_HOVER,
                    d ? DARK_DIVIDER : LIGHT_DIVIDER,
                    d ? DARK_ICON : LIGHT_ICON,
                    d ? DARK_DISABLED_TEXT : LIGHT_DISABLED_TEXT,
                    d ? DARK_DISABLED_B : LIGHT_DISABLED_B,
                    d ? DARK_INSUF : LIGHT_INSUF);
        }

        backButton.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        footerLabel.setForeground(d ? DARK_MUTED : LIGHT_MUTED);
        if (toastLabel != null) {
            toastLabel.setForeground(d ? DARK_ACCENT : LIGHT_ACCENT);
        }

        themeTogglePanel.applyTheme(d ? DARK_TOGGLE_BG : LIGHT_TOGGLE_BG, d ? DARK_TOGGLE_BDR : LIGHT_TOGGLE_BDR,
                d ? DARK_MUTED : LIGHT_MUTED, d ? DARK_ACCENT : LIGHT_ACCENT, d ? DARK_BG : LIGHT_BG);
        themeTogglePanel.setDarkSelected(d);

        bgPanel.repaint();
        mainPanel.repaint();
    }

    private void refreshShopUI() {
        balancePanel.setBalance(player.getCoins());
        ownedLabel.setText("Owned: " + buildOwnedPowerupSummary());
        for (ShopItemCard card : itemCards) {
            card.refreshState();
        }
        repaint();
    }

    private String buildOwnedPowerupSummary() {
        int freeze = player.getInventory().getCount(PowerUpType.FREEZE);
        int shield = player.getInventory().getCount(PowerUpType.SHIELD);
        int bonus = player.getInventory().getCount(PowerUpType.DOUBLE_SCORE);
        return "Freeze x" + freeze + "  Shield x" + shield + "  Double Score x" + bonus;
    }

    private void purchaseItem(ItemSpec spec) {
        if (spec == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Buy " + spec.name() + " for " + spec.price() + " coins?",
                "Confirm Purchase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        ShopLogic.PurchaseResult result = ShopLogic.purchase(
                player,
                spec.name(),
                spec.type,
                spec.price(),
                confirm == JOptionPane.OK_OPTION,
                this::savePlayer);
        if (result.status() == ShopLogic.PurchaseStatus.CANCELLED) {
            SoundManager.playWarning();
            showToast(result.message());
            return;
        }
        if (result.status() == ShopLogic.PurchaseStatus.INSUFFICIENT_FUNDS) {
            Toolkit.getDefaultToolkit().beep();
            SoundManager.playWarning();
            showToast(result.message());
            return;
        }
        if (result.status() == ShopLogic.PurchaseStatus.SAVE_FAILED) {
            RuntimeException ex = result.error();
            if (UiRuntimeGuard.isDataAccessFailure(ex)) {
                UiRuntimeGuard.showDataAccessError(this, "save shop purchases", ex);
            } else {
                throw ex;
            }
            SoundManager.playWarning();
            showToast(result.message());
            return;
        }
        refreshShopUI();
        SoundManager.playClick();
        showToast(result.message());
    }

    private void savePlayer() {
        new SaveManager(new FileStorageService()).savePlayer(player);
    }

    private void openDashboard() {
        dispose();
        new DashboardScreen(username);
    }

    private void showToast(String message) {
        if (toastLabel == null) {
            return;
        }
        toastLabel.setText(message);
        toastLabel.setVisible(true);
        if (toastTimer != null && toastTimer.isRunning()) {
            toastTimer.stop();
        }
        toastTimer = new Timer(1800, e -> toastLabel.setVisible(false));
        toastTimer.setRepeats(false);
        toastTimer.start();
    }

    private void installEscapeShortcut() {
        JRootPane root = getRootPane();
        if (root == null) {
            return;
        }
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitShop");
        root.getActionMap().put("exitShop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.playClick();
                openDashboard();
            }
        });
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

    // ═══════════════════════════════════════════════════════
    //  CUSTOM COMPONENTS
    // ═══════════════════════════════════════════════════════

    /** Balance display panel. */
    private static class BalancePanel extends JPanel {
        private int balance = 3000;
        private Color bg, border, labelC, valueC, unitC;

        BalancePanel() { setOpaque(false); }

        void setBalance(int balance) {
            this.balance = Math.max(0, balance);
            repaint();
        }

        void applyTheme(Color bg, Color b, Color lbl, Color val, Color unit) {
            this.bg = bg; border = b; labelC = lbl; valueC = val; unitC = unit; repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(bg != null ? bg : new Color(201, 165, 90, 15));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));
            g2.setColor(border != null ? border : new Color(201, 165, 90, 46));
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(.5f, .5f, w - 1, h - 1, 16, 16));

            g2.setFont(FONT_BAL_LBL);
            g2.setColor(labelC != null ? labelC : new Color(201, 165, 90));
            FontMetrics fm1 = g2.getFontMetrics();
            String lbl = "YOUR BALANCE";
            g2.drawString(lbl, w - 20 - fm1.stringWidth(lbl), 22);

            g2.setFont(FONT_BAL_VAL);
            g2.setColor(valueC != null ? valueC : Color.WHITE);
            FontMetrics fm2 = g2.getFontMetrics();
            String val = String.format("%,d", balance);
            String unit = " Coins";
            int valW = fm2.stringWidth(val);

            g2.setFont(FONT_BAL_UNIT);
            FontMetrics fm3 = g2.getFontMetrics();
            int unitW = fm3.stringWidth(unit);
            int totalW = valW + unitW + 2;
            int startX = w - 20 - totalW;

            g2.setFont(FONT_BAL_VAL);
            g2.setColor(valueC != null ? valueC : Color.WHITE);
            g2.drawString(val, startX, 48);

            g2.setFont(FONT_BAL_UNIT);
            g2.setColor(unitC != null ? unitC : new Color(200, 200, 200, 120));
            g2.drawString(unit, startX + valW + 2, 48);

            g2.dispose();
        }
    }

    /** Shop item card with icon area, name, description, price, buy button. */
    private class ShopItemCard extends JPanel {
        private final ItemSpec spec;
        private boolean hovered = false;
        private boolean buyHovered = false;
        private int ownedCount;
        private boolean affordable;

        private Color cardBg, cardBorder, cardHoverBorder;
        private Color imgBg, imgBorder;
        private Color textC, bodyC, accentC, mutedC;
        private Color buyBorder, buyHover, dividerC, iconC;
        private Color disabledText, disabledBorder, insufC;

        ShopItemCard(ItemSpec spec) {
            this.spec = spec;
            refreshState();
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (affordable) { hovered = true; repaint(); }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false; buyHovered = false; repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!affordable) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    int bx = getWidth() - 20 - 72, by = getHeight() - 44, bw = 72, bh = 30;
                    boolean onBuy = e.getX() >= bx && e.getX() <= bx + bw && e.getY() >= by && e.getY() <= by + bh;
                    if (onBuy) {
                        purchaseItem(spec);
                    }
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (!affordable) return;
                    // Check if over buy button area
                    int bx = getWidth() - 20 - 72, by = getHeight() - 44, bw = 72, bh = 30;
                    boolean over = e.getX() >= bx && e.getX() <= bx + bw && e.getY() >= by && e.getY() <= by + bh;
                    if (over != buyHovered) { buyHovered = over; repaint(); }
                }
            });
        }

        void refreshState() {
            this.ownedCount = player.getInventory().getCount(spec.type);
            this.affordable = player.getCoins() >= spec.price;
            repaint();
        }

        void applyTheme(Color cardBg, Color cardBorder, Color cardHoverBorder,
                        Color imgBg, Color imgBorder, Color textC, Color bodyC,
                        Color accentC, Color mutedC, Color buyBorder, Color buyHover,
                        Color dividerC, Color iconC, Color disabledText, Color disabledBorder, Color insufC) {
            this.cardBg = cardBg; this.cardBorder = cardBorder; this.cardHoverBorder = cardHoverBorder;
            this.imgBg = imgBg; this.imgBorder = imgBorder; this.textC = textC; this.bodyC = bodyC;
            this.accentC = accentC; this.mutedC = mutedC; this.buyBorder = buyBorder; this.buyHover = buyHover;
            this.dividerC = dividerC; this.iconC = iconC; this.disabledText = disabledText;
            this.disabledBorder = disabledBorder; this.insufC = insufC;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), arc = 18;
            float alpha = affordable ? 1f : 0.4f;
            Composite orig = g2.getComposite();
            if (!affordable) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Card background
            g2.setColor(cardBg);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

            // Card border
            g2.setColor(hovered ? cardHoverBorder : cardBorder);
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(.5f, .5f, w - 1, h - 1, arc, arc));

            // Image area
            int imgH = 156;
            g2.setColor(imgBg);
            g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, imgH, arc - 2, arc - 2));
            // bottom clip for image area
            g2.setColor(imgBorder);
            g2.drawLine(1, imgH, w - 1, imgH);

            drawIllustration(g2, w, imgH);

            // Content area
            int cy = imgH + 26;

            // Name
            g2.setFont(FONT_ITEM_NAME);
            g2.setColor(affordable ? textC : disabledText);
            g2.drawString(spec.name, 18, cy + 2);
            cy += 28;

            g2.setFont(FONT_SECTION);
            g2.setColor(affordable ? accentC : disabledText);
            g2.drawString("Owned x" + ownedCount, 18, cy);
            cy += 18;

            // Description (word wrap)
            g2.setFont(FONT_ITEM_DESC);
            g2.setColor(affordable ? bodyC : disabledText);
            FontMetrics descFm = g2.getFontMetrics();
            int maxW = w - 36;
            String[] words = spec.desc.split(" ");
            StringBuilder line = new StringBuilder();
            int lineCount = 0;
            for (String word : words) {
                String test = line.isEmpty() ? word : line + " " + word;
                if (descFm.stringWidth(test) > maxW) {
                    g2.drawString(line.toString(), 18, cy);
                    cy += descFm.getHeight();
                    line = new StringBuilder(word);
                    lineCount++;
                    if (lineCount >= 3) { line.append("..."); break; }
                } else {
                    line = new StringBuilder(test);
                }
            }
            if (!line.isEmpty()) {
                g2.drawString(line.toString(), 18, cy);
                cy += descFm.getHeight();
            }

            // Footer divider
            int footerY = h - 58;
            g2.setColor(dividerC);
            g2.drawLine(18, footerY, w - 18, footerY);

            // Price
            int priceY = h - 28;
            g2.setFont(FONT_PRICE);
            Color priceColor = affordable ? accentC : disabledText;
            g2.setColor(priceColor);
            String priceStr = String.valueOf(spec.price);
            if (!affordable) {
                // Strikethrough
                FontMetrics pFm = g2.getFontMetrics();
                int pW = pFm.stringWidth(priceStr);
                g2.drawString(priceStr, 18, priceY);
                g2.drawLine(18, priceY - pFm.getAscent() / 2 + 2, 18 + pW, priceY - pFm.getAscent() / 2 + 2);
            } else {
                g2.drawString(priceStr, 18, priceY);
            }

            // Coins unit
            FontMetrics priceFm = g2.getFontMetrics();
            int afterPrice = 18 + priceFm.stringWidth(priceStr) + 4;
            g2.setFont(FONT_PRICE_UNIT);
            g2.setColor(affordable ? mutedC : disabledText);
            g2.drawString("Coins", afterPrice, priceY);

            // Insufficient funds
            if (!affordable) {
                g2.setFont(FONT_INSUF);
                g2.setColor(insufC);
                g2.drawString("INSUFFICIENT FUNDS", 18, h - 8);
            }

            // Buy button
            int bw = 72, bh = 30;
            int bx = w - 18 - bw, by = h - 48;
            g2.setColor(buyHovered && affordable ? buyHover : new Color(0, 0, 0, 0));
            g2.fill(new RoundRectangle2D.Float(bx, by, bw, bh, 10, 10));
            g2.setColor(affordable ? (buyHovered ? accentC : buyBorder) : disabledBorder);
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(bx + .5f, by + .5f, bw - 1, bh - 1, 10, 10));
            g2.setFont(FONT_BUY);
            g2.setColor(affordable ? (buyHovered ? accentC : accentC) : disabledText);
            FontMetrics buyFm = g2.getFontMetrics();
            String buyText = "BUY";
            g2.drawString(buyText, bx + (bw - buyFm.stringWidth(buyText)) / 2, by + bh / 2 + buyFm.getAscent() / 2 - 1);

            if (!affordable) g2.setComposite(orig);
            g2.dispose();
        }

        private void drawIllustration(Graphics2D g2, int w, int imgH) {
            int cx = w / 2;
            int cy = imgH / 2 + 2;

            if (hovered) {
                g2.setColor(new Color(accentC.getRed(), accentC.getGreen(), accentC.getBlue(), 18));
                g2.fillOval(cx - 42, cy - 34, 84, 68);
            }

            if ("Freeze".equals(spec.name)) {
                g2.setColor(new Color(iconC.getRed(), iconC.getGreen(), iconC.getBlue(), 28));
                g2.fillRoundRect(cx - 50, cy - 20, 100, 42, 20, 20);
                g2.setColor(iconC);
                g2.setStroke(new BasicStroke(2f));
                for (int i = 0; i < 6; i++) {
                    double angle = Math.PI / 3 * i;
                    int x2 = cx + (int) (Math.cos(angle) * 18);
                    int y2 = cy + (int) (Math.sin(angle) * 18);
                    g2.drawLine(cx, cy, x2, y2);
                }
                g2.drawLine(cx - 10, cy - 10, cx + 10, cy + 10);
                g2.drawLine(cx - 10, cy + 10, cx + 10, cy - 10);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new Ellipse2D.Float(cx - 34, cy - 10, 18, 18));
                g2.draw(new Ellipse2D.Float(cx + 16, cy - 10, 18, 18));
            } else if ("Shield".equals(spec.name)) {
                g2.setColor(new Color(iconC.getRed(), iconC.getGreen(), iconC.getBlue(), 22));
                g2.fillOval(cx - 46, cy - 30, 92, 60);
                g2.setColor(iconC);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - 34, cy - 22, 68, 44);
                g2.drawOval(cx - 24, cy - 14, 48, 28);
                Polygon shield = new Polygon(
                        new int[]{cx, cx + 18, cx + 12, cx, cx - 12, cx - 18},
                        new int[]{cy - 18, cy - 8, cy + 16, cy + 24, cy + 16, cy - 8}, 6);
                g2.fillPolygon(shield);
                g2.setColor(cardBg);
                g2.fill(new Ellipse2D.Float(cx - 3, cy - 2, 6, 6));
            } else {
                g2.setColor(new Color(iconC.getRed(), iconC.getGreen(), iconC.getBlue(), 22));
                g2.fillOval(cx - 52, cy - 28, 104, 56);
                g2.setColor(iconC);
                g2.setStroke(new BasicStroke(2f));
                for (int i = 0; i < 10; i++) {
                    double angle = (Math.PI * 2 * i) / 10.0;
                    int x2 = cx + (int) (Math.cos(angle) * 26);
                    int y2 = cy + (int) (Math.sin(angle) * 26);
                    g2.drawLine(cx, cy, x2, y2);
                }
                g2.setFont(FONT_ICON);
                FontMetrics iconFm = g2.getFontMetrics();
                int iconW = iconFm.stringWidth(spec.icon());
                g2.drawString(spec.icon(), cx - iconW / 2, cy + iconFm.getAscent() / 2 - 3);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new Ellipse2D.Float(cx - 40, cy - 8, 12, 12));
                g2.draw(new Ellipse2D.Float(cx + 28, cy + 6, 10, 10));
            }
        }
    }

    private record ItemSpec(String name, String desc, int price, String icon, PowerUpType type) {}

    // ── Shared components (same as other screens) ──

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
            g2.setPaint(new RadialGradientPaint(w*.2f,h*.15f,w*.6f,new float[]{0,1},new Color[]{gl1,new Color(0,0,0,0)}));
            g2.fillRect(0,0,w,h);
            Color gl2 = dark ? new Color(201,165,90,6) : new Color(139,105,20,4);
            g2.setPaint(new RadialGradientPaint(w*.8f,h*.85f,w*.5f,new float[]{0,1},new Color[]{gl2,new Color(0,0,0,0)}));
            g2.fillRect(0,0,w,h);
            g2.setColor(dark ? new Color(232,228,220,10) : new Color(26,26,26,10));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(50,50,50,120); g2.drawLine(50,50,120,50);
            g2.drawLine(w-50,h-50,w-50,h-120); g2.drawLine(w-50,h-50,w-120,h-50);
            g2.dispose();
        }
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
            bgColor=bg; borderColor=b; mutedColor=m; selectedColor=s; selectedTextColor=st; repaint();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight(),arc=h;
            g2.setColor(bgColor!=null?bgColor:new Color(255,255,255,10)); g2.fillRoundRect(0,0,w,h,arc,arc);
            g2.setColor(borderColor!=null?borderColor:new Color(255,255,255,20));
            g2.setStroke(new BasicStroke(1)); g2.drawRoundRect(0,0,w-1,h-1,arc,arc);
            int pw=w/2,px=darkSelected?pw-1:3;
            g2.setColor(selectedColor!=null?selectedColor:new Color(201,165,90));
            g2.fillRoundRect(px,3,pw-2,h-6,h-6,h-6); g2.dispose();
            Color sc=selectedTextColor!=null?selectedTextColor:Color.BLACK;
            Color uc=mutedColor!=null?mutedColor:new Color(200,200,200,120);
            lightLabel.setForeground(!darkSelected?sc:uc); darkLabel.setForeground(darkSelected?sc:uc);
            lightLabel.setText(!darkSelected?"\u2713  Light":"Light"); darkLabel.setText(darkSelected?"\u2713  Dark":"Dark");
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
            if (getForeground()!=null) { g2.setColor(getForeground()); g2.fillRoundRect(0,0,4,getHeight(),4,4); }
            g2.dispose(); super.paintComponent(g);
        }
    }
}
