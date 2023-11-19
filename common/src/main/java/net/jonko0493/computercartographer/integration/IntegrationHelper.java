package net.jonko0493.computercartographer.integration;

import com.flowpowered.math.vector.Vector3d;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.jonko0493.computercartographer.ComputerCartographer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class IntegrationHelper {
    public static final int ICON_WIDTH = 24;
    public static final int ICON_HEIGHT = 24;

    public static boolean downloadAndResizeIcon(URL url, OutputStream output) {
        try {
            BufferedImage icon = ImageIO.read(url);
            if (icon == null) {
                return false;
            }
            BufferedImage resizedIcon = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedIcon.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(icon, 0, 0, ICON_WIDTH, ICON_HEIGHT, null);
            ImageIO.write(resizedIcon, "png", output);
            output.flush();
            output.close();
            return true;
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return false;
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
