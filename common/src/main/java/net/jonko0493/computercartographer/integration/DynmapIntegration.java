package net.jonko0493.computercartographer.integration;

import com.flowpowered.math.vector.Vector3d;
import net.jonko0493.computercartographer.ComputerCartographer;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;

import java.awt.*;
import java.util.ArrayList;
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
    public String[] getAvailableMaps() {
        return new String[0];
    }

    @Override
    public String getCurrentMap() {
        return "";
    }

    @Override
    public boolean setCurrentMap(String world) {
        return false;
    }

    @Override
    public boolean addMarkerSet(String setName, String label) {
        return false;
    }

    @Override
    public boolean removeMarkerSet(String setName) {
        return false;
    }

    @Override
    public String[] getMarkerSets() {
        return new String[0];
    }

    @Override
    public boolean clearMarkerSet(String setName) {
        return false;
    }

    @Override
    public boolean addPOIMarker(String markerSet, String id, String label, String detail, String icon, double x, double y, double z) {
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
