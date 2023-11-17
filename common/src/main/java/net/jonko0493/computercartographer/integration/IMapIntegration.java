package net.jonko0493.computercartographer.integration;

public interface IMapIntegration {
    boolean init();
    String getName();
    String getCurrentWorld();
    boolean setCurrentWorld(String world);
    boolean addMarkerSet();
    boolean removeMarkerSet();
    String[] getMarkerSets();
    boolean clearMarkerSet();
    boolean addPOIMarker();
    boolean removePOIMarker();
    boolean editPOIMarker();
    boolean addAreaMarker();
    boolean removeAreaMarker();
    boolean editAreaMarker();
    boolean addLineMarker();
    boolean editLineMarker();
    boolean removeLineMarker();
}
