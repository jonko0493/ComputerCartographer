package net.jonko0493.computercartographer.integration;

import java.io.InputStream;
import java.io.OutputStream;

public interface IMapIntegration {
    boolean init();
    String getName();
    String[] getAvailableMaps();
    String getCurrentMap();
    boolean setCurrentMap(String world);
    boolean addMarkerSet(String setName, String label);
    boolean removeMarkerSet(String setName);
    String[] getMarkerSets();
    boolean clearMarkerSet(String setName);
    boolean addPOIMarker(String markerSet, String id, String label, String detail, String icon, double x, double y, double z);
    boolean removePOIMarker(String markerSet, String id);
    boolean editPOIMarker();
    boolean addAreaMarker();
    boolean removeAreaMarker();
    boolean editAreaMarker();
    boolean addLineMarker();
    boolean editLineMarker();
    boolean removeLineMarker();
}
