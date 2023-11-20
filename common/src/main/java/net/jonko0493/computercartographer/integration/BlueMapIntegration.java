package net.jonko0493.computercartographer.integration;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.*;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Line;
import de.bluecolored.bluemap.api.math.Shape;
import net.jonko0493.computercartographer.ComputerCartographer;
import org.apache.commons.io.FilenameUtils;

import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class BlueMapIntegration implements IMapIntegration {
    private final String name = "bluemap";
    private BlueMapAPI api;
    private BlueMapMap currentMap;
    private boolean enabled = false;

    private static Color convertFromAwt(java.awt.Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0f);
    }

    @Override
    public boolean init() {
        try {
            BlueMapAPI.getInstance().ifPresent(api -> {
                this.api = api;
                api.getMap("overworld").ifPresent(map -> currentMap = map);
                if (currentMap == null) {
                    api.getMaps().stream().findFirst().ifPresent(map -> currentMap = map);
                }
                enabled = true;
                if (currentMap == null) {
                    ComputerCartographer.logWarning("No BlueMap maps found! Disabling BlueMap integration");
                    enabled = false;
                }
            });
        } catch (NoClassDefFoundError | IllegalStateException ignore) {
            ComputerCartographer.log("BlueMap is not loaded.");
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
        if (enabled) {
            try {
                return api.getMaps().stream().map(BlueMapMap::getName).toArray(String[]::new);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public String getCurrentMap() {
        if (enabled) {
            try {
                return currentMap.getId();
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public boolean setCurrentMap(String world) {
        if (enabled) {
            try {
                api.getMap(world.toLowerCase()).ifPresent(map -> currentMap = map);
                return currentMap.getId().equalsIgnoreCase(world);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addMarkerSet(String setName, String label) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                currentMap.getMarkerSets().put(setName, new MarkerSet(label, true, false));
                return currentMap.getMarkerSets().containsKey(setName);
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
                currentMap.getMarkerSets().remove(setName);
                return !currentMap.getMarkerSets().containsKey(setName);
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
                return currentMap.getMarkerSets().keySet().stream()
                        .filter(s -> s.startsWith("cc_"))
                        .map(s -> s.substring(3))
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
                if (currentMap.getMarkerSets().containsKey(setName)) {
                    currentMap.getMarkerSets().get(setName).getMarkers().clear();
                    return currentMap.getMarkerSets().get(setName).getMarkers().isEmpty();
                } else {
                    ComputerCartographer.logWarning("Attempted to clear markers from non-existent set '" + setName + "'");
                }
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
                if (currentMap.getMarkerSets().containsKey(markerSet)) {
                    String realIcon = "";
                    if (icon.startsWith("http")) {
                        try {
                            URL url = new URL(icon);
                            String iconFileName = "cc_" + FilenameUtils.removeExtension(FilenameUtils.getBaseName(url.getFile().toLowerCase())) + ".png";
                            if (currentMap.getAssetStorage().assetExists(iconFileName)) {
                                realIcon = currentMap.getAssetStorage().getAssetUrl(iconFileName);
                            } else {
                                try (OutputStream out = currentMap.getAssetStorage().writeAsset(iconFileName)) {
                                    if (!IntegrationHelper.downloadAndResizeIcon(url, out)) {
                                        ComputerCartographer.logWarning(("Failed to download icon for POI marker!"));
                                        return false;
                                    }
                                    realIcon = currentMap.getAssetStorage().getAssetUrl(iconFileName);
                                } catch (Exception e) {
                                    ComputerCartographer.logException(e);
                                    return false;
                                }
                            }
                        } catch (Exception e) {
                            ComputerCartographer.logException(e);
                            return false;
                        }
                    }
                    else {
                        String iconName = "cc_" + icon.toLowerCase() + ".png";
                        if (currentMap.getAssetStorage().assetExists(iconName)) {
                            realIcon = currentMap.getAssetStorage().getAssetUrl(iconName);
                        } else {
                            ComputerCartographer.logWarning("Attempted to use icon " + icon + " which does not exist.");
                        }
                    }
                    POIMarker.Builder markerBuilder = POIMarker.builder();
                    if (realIcon.isEmpty()) {
                        markerBuilder.defaultIcon();
                    } else {
                        markerBuilder.icon(realIcon, IntegrationHelper.ICON_WIDTH / 2, IntegrationHelper.ICON_HEIGHT / 2);
                    }
                    POIMarker marker = markerBuilder
                            .label(label)
                            .detail(detail)
                            .position(x, y, z)
                            .build();
                    currentMap.getMarkerSets().get(markerSet).put(id, marker);
                    return true;
                } else {
                    ComputerCartographer.logWarning("Attempted to add POI marker to non-existent set '" + markerSet + "'");
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addLineMarker(String markerSet, String id, String label, String detail, java.awt.Color color, int width, ArrayList<org.joml.Vector3d> points) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                if (currentMap.getMarkerSets().containsKey(markerSet)) {
                    Line.Builder lineBuilder = Line.builder();
                    for (org.joml.Vector3d point : points) {
                        lineBuilder.addPoint(new Vector3d(point.x, point.y, point.z));
                    }
                    LineMarker lineMarker = LineMarker.builder()
                            .line(lineBuilder.build())
                            .centerPosition()
                            .label(label)
                            .detail(detail)
                            .lineColor(convertFromAwt(color))
                            .lineWidth(width)
                            .depthTestEnabled(false)
                            .build();
                    currentMap.getMarkerSets().get(markerSet).put(id, lineMarker);
                    return true;
                } else {
                    ComputerCartographer.logWarning("Attempted to remove marker from non-existent set '" + markerSet + "'");
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addCircleMarker(String markerSet, String id, String label, String detail, java.awt.Color lineColor, java.awt.Color fillColor, int lineWidth, double x, double y, double z, double radius) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                if (currentMap.getMarkerSets().containsKey(markerSet)) {
                    Shape circle = Shape.createCircle(x, z, radius, (int)Math.max(10, Math.min(100, radius)));
                    ShapeMarker circleMarker = ShapeMarker.builder()
                            .shape(circle, (float)y)
                            .centerPosition()
                            .label(label)
                            .detail(detail)
                            .lineColor(convertFromAwt(lineColor))
                            .fillColor(convertFromAwt(fillColor))
                            .lineWidth(lineWidth)
                            .depthTestEnabled(false)
                            .build();
                    currentMap.getMarkerSets().get(markerSet).put(id, circleMarker);
                    return true;
                } else {
                    ComputerCartographer.logWarning("Attempted to add circle marker to non-existent set '" + markerSet + "'");
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addRectangleMarker(String markerSet, String id, String label, String detail, java.awt.Color lineColor, java.awt.Color fillColor, int lineWidth, double x1, double z1, double x2, double z2) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                if (currentMap.getMarkerSets().containsKey(markerSet)) {
                    Shape rectangle = Shape.createRect(x1, z1, x2, z2);
                    ShapeMarker rectangleMarker = ShapeMarker.builder()
                            .shape(rectangle, 63)
                            .centerPosition()
                            .label(label)
                            .detail(detail)
                            .lineColor(convertFromAwt(lineColor))
                            .fillColor(convertFromAwt(fillColor))
                            .lineWidth(lineWidth)
                            .depthTestEnabled(false)
                            .build();
                    currentMap.getMarkerSets().get(markerSet).put(id, rectangleMarker);
                    return true;
                } else {
                    ComputerCartographer.logWarning("Attempted to add rectangle marker to non-existent set '" + markerSet + "'");
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean addAreaMarker(String markerSet, String id, String label, String detail, java.awt.Color lineColor, java.awt.Color fillColor, int lineWidth, ArrayList<org.joml.Vector3d> points) {
        if (enabled) {
            try {
                markerSet = "cc_" + markerSet;
                if (currentMap.getMarkerSets().containsKey(markerSet)) {
                    Shape.Builder shapeBuilder = Shape.builder();
                    for (org.joml.Vector3d point : points) {
                        shapeBuilder.addPoint(new Vector2d(point.x, point.z));
                    }
                    // This is the only map plugin that can make a 3D shape, so we're going to
                    // advantage of it. If all the y's are the same, we'll make a shape marker,
                    // but if they differ at all it will be an extrude marker!
                    if (points.stream().allMatch(p -> p.y == points.get(0).y)) {
                        ShapeMarker shapeMarker = ShapeMarker.builder()
                                .shape(shapeBuilder.build(), (float) points.get(0).y)
                                .centerPosition()
                                .label(label)
                                .detail(detail)
                                .lineColor(convertFromAwt(lineColor))
                                .fillColor(convertFromAwt(fillColor))
                                .lineWidth(lineWidth)
                                .depthTestEnabled(false)
                                .build();
                        currentMap.getMarkerSets().get(markerSet).put(id, shapeMarker);
                        return true;
                    } else {
                        ExtrudeMarker extrudeMarker = ExtrudeMarker.builder()
                                .shape(shapeBuilder.build(),
                                        points.stream().map(v -> v.y).min(Double::compare).get().floatValue(),
                                        points.stream().map(v -> v.y).max(Double::compare).get().floatValue())
                                .centerPosition()
                                .label(label)
                                .detail(detail)
                                .lineColor(convertFromAwt(lineColor))
                                .fillColor(convertFromAwt(fillColor))
                                .lineWidth(lineWidth)
                                .depthTestEnabled(false)
                                .build();
                        currentMap.getMarkerSets().get(markerSet).put(id, extrudeMarker);
                        return true;
                    }
                } else {
                    ComputerCartographer.logWarning("Attempted to add area marker to non-existent set '" + markerSet + "'");
                }
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
                return currentMap.getMarkerSets().get(markerSet).remove(id) != null;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }
}
