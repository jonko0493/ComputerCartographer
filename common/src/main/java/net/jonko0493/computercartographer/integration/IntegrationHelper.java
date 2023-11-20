package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import org.joml.Vector3d;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class IntegrationHelper {
    public static final int ICON_WIDTH = 24;
    public static final int ICON_HEIGHT = 24;

    private static BufferedImage downloadAndResizeIconPrivate(URL url) {
        try {
            BufferedImage icon = ImageIO.read(url);
            if (icon == null) {
                return null;
            }
            BufferedImage resizedIcon = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedIcon.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(icon, 0, 0, ICON_WIDTH, ICON_HEIGHT, null);
            return resizedIcon;
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return null;
    }

    public static InputStream downloadAndResizeIcon(URL url) {
        BufferedImage resizedIcon = downloadAndResizeIconPrivate(url);
        if (resizedIcon != null) {
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                ImageIO.write(resizedIcon, "png", outStream);
                return new ByteArrayInputStream(outStream.toByteArray());
            } catch (IOException e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    public static boolean downloadAndResizeIcon(URL url, OutputStream output) {
        try {
            BufferedImage resizedIcon = downloadAndResizeIconPrivate(url);
            if (resizedIcon != null) {
                ImageIO.write(resizedIcon, "png", output);
                output.flush();
                output.close();
                return true;
            }
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return false;
    }

    public static ArrayList<String> getDirectoriesWithRegionDirectory(File dir) {
        ArrayList<String> matches = new ArrayList<>();
        return getDirectoriesWithRegionDirectory(dir, matches);
    }

    public static ArrayList<String> getDirectoriesWithRegionDirectory(File dir, ArrayList<String> matches) {
        File[] ls = dir.listFiles();
        for (File file : Objects.requireNonNull(ls)) {
            if (file.isDirectory() && file.getName().equals("region")) {
                matches.add(file.getParentFile().getName());
            } else if (file.isDirectory()) {
                getDirectoriesWithRegionDirectory(file, matches);
            }
        }
        return matches;
    }

    public static Vector3d parsePoint(Map<?, ?> table) {
        if (table == null || !table.containsKey("x") || !table.containsKey("y") || !table.containsKey("z")) {
            return null;
        }

        return new Vector3d(
                ((Number) table.get("x")).intValue(),
                ((Number) table.get("y")).intValue(),
                ((Number) table.get("z")).intValue()
        );
    }

    public static ArrayList<Vector3d> parsePoints(Map<?, ?> table) {
        ArrayList<Vector3d> points = new ArrayList<>();
        // Yes, double :) (it's a Lua thing)
        for (double i = 1; table.containsKey(i); i++) {
            Vector3d point = parsePoint((Map<?, ?>) table.get(i));
            if (point != null) {
                points.add(point);
            }
        }
        return points;
    }
}
