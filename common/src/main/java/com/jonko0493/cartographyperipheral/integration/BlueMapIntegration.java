package com.jonko0493.cartographyperipheral.integration;

import com.jonko0493.cartographyperipheral.CartographyPeripheral;
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
            CartographyPeripheral.Log.atInfo().log("BlueMap is not loaded.");
            enabled = false;
        }
        return enabled;
    }
}
