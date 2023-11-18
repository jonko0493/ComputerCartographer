package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.pl3x.map.core.Pl3xMap;

public class Pl3xMapIntegration implements IMapIntegration {
    private final String name = "pl3xmap";

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
    public boolean removePOIMarker(String markerSet, String id) {
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
