package ca.uwo.cs2212.ui;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

/**
 * Utility for sizing Swing top-level windows so they fit on the primary display without
 * overlapping taskbars or system UI.
 * <p>
 * The effective size is the smaller of the caller's preferred dimensions and the
 * screen's usable bounds (screen size minus insets, with a small margin). The minimum
 * clamped dimension is 320 pixels per axis. The frame is always marked non-resizable.
 * </p>
 */
final class WindowSizing {
    /**
     * Extra pixels subtracted from each horizontal edge when computing usable width/height,
     * so the window does not sit flush against monitor edges.
     */
    private static final int EDGE_MARGIN = 24;

    /**
     * Prevents instantiation of this utility class.
     */
    private WindowSizing() {
    }

    /**
     * Sets the frame's size to fit within the current screen's usable area and disables resizing.
     * <p>
     * Uses the default screen device, global screen size, and screen insets for the default
     * {@link GraphicsConfiguration} to compute how much space is available. The final width and
     * height are each {@code min(preferred, usable)} with a floor of 320 on each axis.
     * </p>
     *
     * @param frame           target window; must not be {@code null}
     * @param preferredWidth  desired width in pixels before clamping
     * @param preferredHeight desired height in pixels before clamping
     * @throws NullPointerException if {@code frame} is {@code null}
     */
    static void applyFixedScreenSafeSize(JFrame frame, int preferredWidth, int preferredHeight) {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = device.getDefaultConfiguration();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(configuration);

        int usableWidth = Math.max(320, screenSize.width - insets.left - insets.right - EDGE_MARGIN * 2);
        int usableHeight = Math.max(320, screenSize.height - insets.top - insets.bottom - EDGE_MARGIN * 2);

        frame.setSize(Math.min(preferredWidth, usableWidth), Math.min(preferredHeight, usableHeight));
        frame.setResizable(false);
    }
}
