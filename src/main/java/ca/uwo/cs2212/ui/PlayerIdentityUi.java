package ca.uwo.cs2212.ui;

import ca.uwo.cs2212.demo.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

/**
 * Shared player identity rendering helpers for Swing screens.
 */
final class PlayerIdentityUi {
    private PlayerIdentityUi() {
    }

    static String buildPlayerIdText(Player player) {
        if (player == null || player.getUsername() == null) {
            return "ID: #00000";
        }
        return "ID: #" + String.format("%05d", Math.abs(player.getUsername().hashCode()) % 100000);
    }

    static String resolveAvatarAppearanceId(Player player, String fallbackAppearanceId) {
        if (player == null || player.getSelectedCharacter() == null) {
            return fallbackAppearanceId;
        }
        String appearanceId = player.getSelectedCharacter().getAppearanceId();
        if (appearanceId == null || appearanceId.isBlank()) {
            return fallbackAppearanceId;
        }
        return appearanceId;
    }

    static String formatPlayTime(int totalSeconds) {
        int safeSeconds = Math.max(0, totalSeconds);
        int hours = safeSeconds / 3600;
        int minutes = (safeSeconds % 3600) / 60;
        int seconds = safeSeconds % 60;
        if (hours > 0) {
            return String.format("%dh %02dm %02ds", hours, minutes, seconds);
        }
        return String.format("%dm %02ds", minutes, seconds);
    }

    static void drawAvatar(Graphics2D g2, int x, int y, int size, String appearanceId, Color accent) {
        Graphics2D avatarGraphics = (Graphics2D) g2.create();
        avatarGraphics.translate(x, y);
        switch (appearanceId) {
            case "bear" -> drawBear(avatarGraphics, size, accent);
            case "fox" -> drawFox(avatarGraphics, size, accent);
            case "cat" -> drawCat(avatarGraphics, size, accent);
            default -> drawBunny(avatarGraphics, size, accent);
        }
        avatarGraphics.dispose();
    }

    private static void drawBunny(Graphics2D g2, int size, Color accent) {
        g2.setColor(withAlpha(accent, 130));
        g2.fillRoundRect(size / 2 - 10, 6, 8, 18, 8, 8);
        g2.fillRoundRect(size / 2 + 2, 6, 8, 18, 8, 8);
        g2.fill(new Ellipse2D.Float(size / 2f - 12, size / 2f - 6, 24, 20));
        drawFace(g2, size, accent);
    }

    private static void drawBear(Graphics2D g2, int size, Color accent) {
        g2.setColor(withAlpha(accent, 125));
        g2.fill(new Ellipse2D.Float(size / 2f - 16, 10, 10, 10));
        g2.fill(new Ellipse2D.Float(size / 2f + 6, 10, 10, 10));
        g2.fill(new Ellipse2D.Float(size / 2f - 14, size / 2f - 6, 28, 24));
        drawFace(g2, size, accent);
    }

    private static void drawFox(Graphics2D g2, int size, Color accent) {
        g2.setColor(withAlpha(accent, 135));
        Polygon leftEar = new Polygon(new int[]{size / 2 - 14, size / 2 - 6, size / 2 - 16}, new int[]{18, 8, 6}, 3);
        Polygon rightEar = new Polygon(new int[]{size / 2 + 14, size / 2 + 6, size / 2 + 16}, new int[]{18, 8, 6}, 3);
        g2.fillPolygon(leftEar);
        g2.fillPolygon(rightEar);
        g2.fill(new Ellipse2D.Float(size / 2f - 14, size / 2f - 4, 28, 22));
        drawFace(g2, size, accent);
    }

    private static void drawCat(Graphics2D g2, int size, Color accent) {
        g2.setColor(withAlpha(accent, 130));
        Polygon leftEar = new Polygon(new int[]{size / 2 - 14, size / 2 - 6, size / 2 - 18}, new int[]{18, 8, 10}, 3);
        Polygon rightEar = new Polygon(new int[]{size / 2 + 14, size / 2 + 6, size / 2 + 18}, new int[]{18, 8, 10}, 3);
        g2.fillPolygon(leftEar);
        g2.fillPolygon(rightEar);
        g2.fill(new Ellipse2D.Float(size / 2f - 14, size / 2f - 4, 28, 22));
        drawFace(g2, size, accent);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(size / 2 - 18, size / 2 + 8, size / 2 - 8, size / 2 + 6);
        g2.drawLine(size / 2 + 18, size / 2 + 8, size / 2 + 8, size / 2 + 6);
    }

    private static void drawFace(Graphics2D g2, int size, Color accent) {
        g2.setColor(new Color(255, 255, 255, 185));
        g2.fill(new Ellipse2D.Float(size / 2f - 9, size / 2f + 2, 18, 12));
        g2.setColor(withAlpha(accent, 220));
        g2.fill(new Ellipse2D.Float(size / 2f - 7, size / 2f - 1, 3, 3));
        g2.fill(new Ellipse2D.Float(size / 2f + 4, size / 2f - 1, 3, 3));
        g2.drawArc(size / 2 - 4, size / 2 + 4, 8, 6, 200, 140);
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
