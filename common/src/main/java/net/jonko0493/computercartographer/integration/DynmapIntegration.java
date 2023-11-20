package net.jonko0493.computercartographer.integration;

import com.flowpowered.math.vector.Vector3d;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.MinecraftServerGetter;
import net.minecraft.util.WorldSavePath;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class DynmapIntegration implements IMapIntegration {
    private final String name = "dynmap";
    private static DynmapCommonAPI dynmapAPI;
    private static MarkerAPI markerAPI;
    private boolean enabled = false;
    private String currentMap;

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
        if (enabled) {
            markerAPI = dynmapAPI.getMarkerAPI();
            if (currentMap == null) {
                String[] availableMaps = getAvailableMaps();
                currentMap = availableMaps[availableMaps.length - 1];
            }
        }
        return enabled;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAvailableMaps() {
        Path savePath = MinecraftServerGetter.getServerInstance().getSavePath(WorldSavePath.ROOT);
        savePath = savePath.getParent();

        return IntegrationHelper.getDirectoriesWithRegionDirectory(savePath.toFile()).toArray(String[]::new);
    }

    @Override
    public String getCurrentMap() {
        if (enabled) {
            return currentMap;
        }
        return null;
    }

    @Override
    public boolean setCurrentMap(String world) {
        if (enabled && Arrays.stream(getAvailableMaps()).anyMatch(m -> m.equalsIgnoreCase(world))) {
            currentMap = world;
            return true;
        }
        return false;
    }

    @Override
    public boolean addMarkerSet(String setName, String label) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                markerAPI.createMarkerSet(setName, label, null, false);
                return markerAPI.getMarkerSet(setName) != null;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean removeMarkerSet(String setName) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                markerAPI.getMarkerSet(setName).deleteMarkerSet();
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public String[] getMarkerSets() {
        if (enabled) {
            try {
                return markerAPI.getMarkerSets().stream()
                        .map(MarkerSet::getMarkerSetID)
                        .filter(s -> s.startsWith("cc_"))
                        .toArray(String[]::new);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public boolean clearMarkerSet(String setName) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                markerAPI.getMarkerSet(setName).getMarkers().clear();
                return markerAPI.getMarkerSet(setName).getMarkers().isEmpty();
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addPOIMarker(String markerSet, String id, String label, String detail, String icon, double x, double y, double z) {
        if (enabled) {

        }
        return false;
    }

    @Override
    public boolean addLineMarker(String markerSet, String id, String label, String detail, Color color, int width, ArrayList<Vector3d> points) {
        return false;
    }

    @Override
    public boolean addCircleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x, double z, double radius) {
        return false;
    }

    @Override
    public boolean addRectangleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x1, double z1, double x2, double z2) {
        return false;
    }

    @Override
    public boolean addAreaMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, ArrayList<Vector3d> points) {
        return false;
    }

    @Override
    public boolean removeMarker(String markerSet, String id) {
        return false;
    }
}
