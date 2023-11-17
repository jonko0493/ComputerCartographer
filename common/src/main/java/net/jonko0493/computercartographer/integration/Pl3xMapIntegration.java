package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.pl3x.map.core.Pl3xMap;

public class Pl3xMapIntegration implements IMapIntegration {

    private Pl3xMap pl3xMapApi;
    private boolean enabled;

    @Override
    public boolean init() {
        try {
            pl3xMapApi = Pl3xMap.api();
            enabled = true;
        } catch (Exception e) {
            ComputerCartographer.log("Pl3xMap is not loaded");
            enabled = false;
        }
        return enabled;
    }
}
