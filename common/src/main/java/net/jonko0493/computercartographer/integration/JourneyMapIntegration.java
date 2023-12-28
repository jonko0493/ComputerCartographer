package net.jonko0493.computercartographer.integration;

import org.joml.Vector3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class JourneyMapIntegration implements IMapIntegration {

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getAvailableMaps() {
        return new String[0];
    }

    @Override
    public String getCurrentMap() {
        return null;
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
    public Map<?, ?> getMarkers(String setName, boolean cartographerCreated) {
        return null;
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
    public boolean addCircleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x, double y, double z, double radius) {
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
