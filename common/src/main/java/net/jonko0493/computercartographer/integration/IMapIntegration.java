package net.jonko0493.computercartographer.integration;

import org.joml.Vector3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public interface IMapIntegration {
    boolean init();
    String getName();
    String[] getAvailableMaps();
    String getCurrentMap();
    boolean setCurrentMap(String world);
    boolean addMarkerSet(String setName, String label);
    boolean removeMarkerSet(String setName);
    String[] getMarkerSets();
    Map<?, ?> getMarkers(String setName, boolean cartographerCreated);
    boolean clearMarkerSet(String setName);
    boolean addPOIMarker(String markerSet, String id, String label, String detail, String icon, double x, double y, double z);
    boolean addLineMarker(String markerSet, String id, String label, String detail, Color color, int width, ArrayList<Vector3d> points);
    boolean addCircleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x, double y, double z, double radius);
    boolean addRectangleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x1, double z1, double x2, double z2);
    boolean addAreaMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, ArrayList<Vector3d> points);
    boolean removeMarker(String markerSet, String id);
}
