package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.MinecraftServerGetter;
import net.minecraft.util.WorldSavePath;
import org.apache.commons.io.FilenameUtils;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.*;
import org.joml.Vector3d;

import java.awt.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

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
                        .map(m -> m.substring(3))
                        .toArray(String[]::new);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public Map<?, ?> getMarkers(String setName, boolean cartographerCreated) {
        if (enabled) {
            try {
                if (cartographerCreated) {
                    setName = "cc_" + setName;
                }
                Marker[] markerSet = markerAPI.getMarkerSet(setName).getMarkers().toArray(Marker[]::new);
                Map<Integer, Object> table = new HashMap<>();

                for (int i = 0; i < markerSet.length; i++) {
                    Map<String, Object> markerTable = new HashMap<>();
                    Marker marker = markerSet[i];

                    markerTable.put("id", marker.getMarkerID());
                    markerTable.put("type", "DynmapMarker");
                    markerTable.put("label", marker.getLabel());
                    Map<String, Double> position = new HashMap<>();
                    position.put("x", marker.getX());
                    position.put("y", marker.getY());
                    position.put("z", marker.getZ());
                    markerTable.put("position", position);

                    table.put(i + 1, markerTable);
                }

                return table;
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
                MarkerSet markerSet = markerAPI.getMarkerSet(setName);
                for (Marker marker : markerSet.getMarkers()) {
                    marker.deleteMarker();
                }
                for (PolyLineMarker lineMarker : markerSet.getPolyLineMarkers()) {
                    lineMarker.deleteMarker();
                }
                for (CircleMarker circleMarker : markerSet.getCircleMarkers()) {
                    circleMarker.deleteMarker();
                }
                for (AreaMarker areaMarker : markerSet.getAreaMarkers()) {
                    areaMarker.deleteMarker();
                }
                return markerSet.getMarkers().isEmpty() && markerSet.getPolyLineMarkers().isEmpty() && markerSet.getCircleMarkers().isEmpty() && markerSet.getAreaMarkers().isEmpty();
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addPOIMarker(String markerSet, String id, String label, String detail, String icon, double x, double y, double z) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                String realIcon = "";
                if (!icon.isEmpty()) {
                    URL iconUrl = null;
                    if (icon.startsWith("http")) {
                        iconUrl = new URL(icon);
                        realIcon = "cc_" + FilenameUtils.removeExtension(FilenameUtils.getBaseName(iconUrl.getFile().toLowerCase()));
                    }
                    final String iconCompare = realIcon;
                    if (markerAPI.getMarkerIcons().stream().noneMatch(i -> i.getMarkerIconID().equals(iconCompare))) {
                        if (iconUrl != null) {
                            markerAPI.createMarkerIcon(realIcon, realIcon, IntegrationHelper.downloadAndResizeIcon(iconUrl));
                        } else if (markerAPI.getMarkerIcon(icon) != null) {
                            realIcon = icon;
                        } else if (markerAPI.getMarkerIcon("cc_" + icon) != null) {
                            realIcon = "cc_" + icon;
                        } else {
                            realIcon = "";
                        }
                    }
                }
                String htmlLabel = "<div>" + label + "</div>" + detail;
                MarkerIcon markerIcon = realIcon.isEmpty() ? null : markerAPI.getMarkerIcon(realIcon);
                markerAPI.getMarkerSet(markerSet).createMarker(id, htmlLabel, true, currentMap, x, y, z, markerIcon, false);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addLineMarker(String markerSet, String id, String label, String detail, Color color, int width, ArrayList<Vector3d> points) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                String htmlLabel = "<div>" + label + "</div>" + detail;
                PolyLineMarker lineMarker = markerAPI.getMarkerSet(markerSet).createPolyLineMarker(id, htmlLabel, true, currentMap, getXCoords(points), getYCoords(points), getZCoords(points), false);
                lineMarker.setLineStyle(width, color.getAlpha() / 255.0, color.getRGB() & 0xFFFFFF);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addCircleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x, double y, double z, double radius) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                String htmlLabel = "<div>" + label + "</div>" + detail;
                CircleMarker circleMarker = markerAPI.getMarkerSet(markerSet).createCircleMarker(id, htmlLabel, true, currentMap, x, y, z, radius, radius, false);
                circleMarker.setLineStyle(lineWidth, lineColor.getAlpha() / 255.0, lineColor.getRGB() & 0xFFFFFF);
                circleMarker.setFillStyle(fillColor.getAlpha() / 255.0, fillColor.getRGB() & 0xFFFFFF);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addRectangleMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, double x1, double z1, double x2, double z2) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                String htmlLabel = "<div>" + label + "</div>" + detail;
                double[] xCoords = new double[] { x1, x2 };
                double[] zCoords = new double[] { z1, z2 };
                AreaMarker rectangleMarker = markerAPI.getMarkerSet(markerSet).createAreaMarker(id, htmlLabel, true, currentMap, xCoords, zCoords, false);
                rectangleMarker.setLineStyle(lineWidth, lineColor.getAlpha() / 255.0, lineColor.getRGB() & 0xFFFFFF);
                rectangleMarker.setFillStyle(fillColor.getAlpha() / 255.0, fillColor.getRGB() & 0xFFFFFF);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addAreaMarker(String markerSet, String id, String label, String detail, Color lineColor, Color fillColor, int lineWidth, ArrayList<Vector3d> points) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                String htmlLabel = "<div>" + label + "</div>" + detail;
                AreaMarker areaMarker = markerAPI.getMarkerSet(markerSet).createAreaMarker(id, htmlLabel, true, currentMap, getXCoords(points), getZCoords(points), false);
                areaMarker.setLineStyle(lineWidth, lineColor.getAlpha() / 255.0, lineColor.getRGB() & 0xFFFFFF);
                areaMarker.setFillStyle(fillColor.getAlpha() / 255.0, fillColor.getRGB() & 0xFFFFFF);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean removeMarker(String markerSet, String id) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                MarkerSet markers = markerAPI.getMarkerSet(markerSet);
                Marker marker = markers.findMarker(id);
                if (marker != null) {
                    marker.deleteMarker();
                    return true;
                }
                PolyLineMarker lineMarker = markers.findPolyLineMarker(id);
                if (lineMarker != null) {
                    lineMarker.deleteMarker();
                    return true;
                }
                CircleMarker circleMarker = markers.findCircleMarker(id);
                if (circleMarker != null) {
                    circleMarker.deleteMarker();
                    return true;
                }
                AreaMarker areaMarker = markers.findAreaMarker(id);
                if (areaMarker != null) {
                    areaMarker.deleteMarker();
                    return true;
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    private static double[] getXCoords(ArrayList<Vector3d> points) {
        double[] xArray = new double[points.size()];
        for (int i = 0; i < xArray.length; i++) {
            xArray[i] = points.get(i).x;
        }
        return xArray;
    }
    private static double[] getYCoords(ArrayList<Vector3d> points) {
        double[] yArray = new double[points.size()];
        for (int i = 0; i < yArray.length; i++) {
            yArray[i] = points.get(i).y;
        }
        return yArray;
    }
    private static double[] getZCoords(ArrayList<Vector3d> points) {
        double[] zArray = new double[points.size()];
        for (int i = 0; i < zArray.length; i++) {
            zArray[i] = points.get(i).z;
        }
        return zArray;
    }
}
