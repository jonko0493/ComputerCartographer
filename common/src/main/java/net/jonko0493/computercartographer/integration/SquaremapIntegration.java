package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

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
