package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import org.apache.commons.io.FilenameUtils;
import org.joml.Vector3d;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.*;
import xyz.jpenilla.squaremap.api.marker.Polygon;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SquaremapIntegration implements IMapIntegration {
    private final String name = "squaremap";
    private Squaremap api;
    private boolean enabled = false;
    private MapWorld currentMap;
    private Map<String, SimpleLayerProvider> layers = new HashMap<>();

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
        if (enabled) {
            try {
                return api.mapWorlds().stream().map(m -> m.identifier().asString()).toArray(String[]::new);
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
                return currentMap.identifier().asString();
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
                if (Arrays.asList(getAvailableMaps()).contains(world)) {
                    api.mapWorlds().stream().filter(m -> m.identifier().asString().equals(world)).findFirst().ifPresent(map -> currentMap = map);
                    return currentMap.identifier().asString().equals(world);
                }
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
                SimpleLayerProvider provider = SimpleLayerProvider.builder(label)
                        .showControls(true)
                        .defaultHidden(false)
                        .layerPriority(5)
                        .zIndex(250)
                        .build();
                Key setNameKey = Key.of(setName);
                currentMap.layerRegistry().register(setNameKey, provider);
                layers.put(setName, provider);
                return true;
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
                currentMap.layerRegistry().unregister(Key.of(setName));
                layers.remove(setName);
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
                return layers.keySet().stream().map(k -> k.substring(3)).toArray(String[]::new);
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
                layers.get(setName).clearMarkers();
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
                    ArrayList<String> iconNames = new ArrayList<>();
                    for (Pair<Key, BufferedImage> iconRegister : api.iconRegistry().entries()) {
                        iconNames.add(iconRegister.left().getKey());
                    }
                    if (iconNames.stream().noneMatch(i -> i.equals(iconCompare))) {
                        if (iconUrl != null) {
                            api.iconRegistry().register(Key.of(realIcon), Objects.requireNonNull(IntegrationHelper.downloadAndResizeIconBufferedImage(iconUrl)));
                        } else if (api.iconRegistry().hasEntry(Key.of(icon))) {
                            realIcon = icon;
                        } else if (api.iconRegistry().hasEntry(Key.of("cc_" + icon))) {
                            realIcon = "cc_" + icon;
                        } else {
                            realIcon = "";
                        }
                    }
                }
                Icon poiMarker = Marker.icon(Point.point(x, z), Key.of(realIcon), IntegrationHelper.ICON_WIDTH, IntegrationHelper.ICON_HEIGHT);
                layers.get(markerSet).addMarker(Key.of(id), poiMarker);
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
                List<Point> squarePoints = points.stream().map(p -> Point.point(p.x, p.z)).toList();
                Polyline lineMarker = Polyline.polyline(squarePoints);
                lineMarker.markerOptions(
                        MarkerOptions.builder()
                        .clickTooltip("<div>" + label + "</div>" + detail)
                        .stroke(true)
                        .strokeColor(color)
                        .strokeWeight(width)
                        .build());
                layers.get(markerSet).addMarker(Key.of(id), lineMarker);
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
                Circle circleMarker = Circle.circle(Point.point(x, z), radius);
                circleMarker.markerOptions(
                        MarkerOptions.builder()
                                .clickTooltip("<div>" + label + "</div>" + detail)
                                .stroke(true)
                                .strokeColor(lineColor)
                                .strokeWeight(lineWidth)
                                .fill(true)
                                .fillColor(fillColor)
                                .build());
                layers.get(markerSet).addMarker(Key.of(id), circleMarker);
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
                Rectangle rectangleMarker = Rectangle.rectangle(Point.point(x1, z1), Point.point(x2, z2));
                rectangleMarker.markerOptions(
                        MarkerOptions.builder()
                                .clickTooltip("<div>" + label + "</div>" + detail)
                                .stroke(true)
                                .strokeColor(lineColor)
                                .strokeWeight(lineWidth)
                                .fill(true)
                                .fillColor(fillColor)
                                .build());
                layers.get(markerSet).addMarker(Key.of(id), rectangleMarker);
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
                List<Point> squarePoints = points.stream().map(p -> Point.point(p.x, p.z)).toList();
                Polygon areaMarker = Polygon.polygon(squarePoints);
                areaMarker.markerOptions(
                        MarkerOptions.builder()
                                .clickTooltip("<div>" + label + "</div>" + detail)
                                .stroke(true)
                                .strokeColor(lineColor)
                                .strokeWeight(lineWidth)
                                .fill(true)
                                .fillColor(fillColor)
                                .build());
                layers.get(markerSet).addMarker(Key.of(id), areaMarker);
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean removeMarker(String markerSet, String id) {
        layers.get(markerSet).removeMarker(Key.of(id));
        return false;
    }
}
