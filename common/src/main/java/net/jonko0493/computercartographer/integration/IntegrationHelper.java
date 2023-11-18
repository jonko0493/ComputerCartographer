package net.jonko0493.computercartographer.integration;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.jonko0493.computercartographer.ComputerCartographer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class IntegrationHelper {
    public static final int ICON_WIDTH = 24;
    public static final int ICON_HEIGHT = 24;

    public static boolean downloadAndResizeIcon(URL url, OutputStream output) {
        try {
            BufferedImage icon = ImageIO.read(url);
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
}
