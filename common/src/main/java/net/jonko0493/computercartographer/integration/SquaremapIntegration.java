package net.jonko0493.computercartographer.integration;

import com.flowpowered.math.vector.Vector3d;
import net.jonko0493.computercartographer.ComputerCartographer;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import java.awt.*;
import java.util.ArrayList;

public class SquaremapIntegration implements IMapIntegration {
    private final String name = "squaremap";
    private Squaremap api;
    private boolean enabled = false;

    @Override
    public boolean init() {
        try {
            api = SquaremapProvider.get();
            enabled = true;
        } catch (Exception e) {
            ComputerCartographer.log("Squaremap is not loaded.");
            enabled = false;
        }
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
