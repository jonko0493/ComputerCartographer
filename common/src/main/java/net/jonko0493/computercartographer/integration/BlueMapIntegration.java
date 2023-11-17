package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import de.bluecolored.bluemap.api.BlueMapAPI;

public class BlueMapIntegration implements IMapIntegration {
    private BlueMapAPI api;
    private boolean enabled = false;

    @Override
    public boolean init() {
        try {
            BlueMapAPI.getInstance().ifPresent(api -> this.api = api);
            enabled = true;
        } catch (NoClassDefFoundError | IllegalStateException ignore) {
            ComputerCartographer.log("BlueMap is not loaded.");
            enabled = false;
        }
        return enabled;
    }
}
