package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

import java.util.Arrays;

public class DynmapIntegration implements IMapIntegration {
    private final String name = "dynmap";
    private static DynmapCommonAPI dynmapAPI;
    private boolean enabled = false;

    static {
        try {
            DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
                @Override
                public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
                    DynmapIntegration.dynmapAPI = dynmapCommonAPI;
                }
            });
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
    }

    @Override
    public boolean init() {
        enabled = dynmapAPI != null;
        return enabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCurrentWorld() {
        return "";
    }

    @Override
    public boolean setCurrentWorld(String world) {
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
