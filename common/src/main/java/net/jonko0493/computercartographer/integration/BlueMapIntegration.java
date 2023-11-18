package net.jonko0493.computercartographer.integration;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import net.jonko0493.computercartographer.ComputerCartographer;

public class BlueMapIntegration implements IMapIntegration {
    private final String name = "bluemap";
    private BlueMapAPI api;
    private BlueMapMap currentMap;
    private boolean enabled = false;

    @Override
    public boolean init() {
        try {
            BlueMapAPI.getInstance().ifPresent(api -> {
                this.api = api;
                api.getMap("overworld").ifPresent(map -> currentMap = map);
                if (currentMap == null) {
                    api.getMaps().stream().findFirst().ifPresent(map -> currentMap = map);
                }
                enabled = true;
                if (currentMap == null) {
                    ComputerCartographer.logWarning("No BlueMap maps found! Disabling BlueMap integration");
                    enabled = false;
                }
            });
        } catch (NoClassDefFoundError | IllegalStateException ignore) {
            ComputerCartographer.log("BlueMap is not loaded.");
            enabled = false;
        }
        return enabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCurrentWorld() {
        if (enabled) {
            try {
                return currentMap.getId();
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public boolean setCurrentWorld(String world) {
        if (enabled) {
            try {
                api.getMap(world.toLowerCase()).ifPresent(map -> currentMap = map);
                return currentMap.getId().equalsIgnoreCase(world);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addMarkerSet() {
        return false;
    }

    @Override
    public boolean removeMarkerSet() {
        return false;
    }

    @Override
    public String[] getMarkerSets() {
        return new String[0];
    }

    @Override
    public boolean clearMarkerSet() {
        return false;
    }

    @Override
    public boolean addPOIMarker() {
        return false;
    }

    @Override
    public boolean removePOIMarker() {
        return false;
    }

    @Override
    public boolean editPOIMarker() {
        return false;
    }

    @Override
    public boolean addAreaMarker() {
        return false;
    }

    @Override
    public boolean removeAreaMarker() {
        return false;
    }

    @Override
    public boolean editAreaMarker() {
        return false;
    }

    @Override
    public boolean addLineMarker() {
        return false;
    }

    @Override
    public boolean editLineMarker() {
        return false;
    }

    @Override
    public boolean removeLineMarker() {
        return false;
    }
}
