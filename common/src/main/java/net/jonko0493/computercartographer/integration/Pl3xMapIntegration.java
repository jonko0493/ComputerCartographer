package net.jonko0493.computercartographer.integration;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.image.IconImage;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.layer.SimpleLayer;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Rectangle;
import net.pl3x.map.core.markers.marker.*;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.world.World;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.awt.*;
import java.net.URL;
import java.util.*;

public class Pl3xMapIntegration implements IMapIntegration {
    private final String name = "pl3xmap";

    private Pl3xMap pl3xMapApi;
    private boolean enabled;
    private World currentMap;
    private final Map<String, CCLayer> layers = new HashMap<>();

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
        if (enabled) {
            try {
                ArrayList<String> mapNames = new ArrayList<>();
                for (World world : pl3xMapApi.getWorldRegistry()) {
                    mapNames.add(world.getName());
                }
                return mapNames.toArray(String[]::new);
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return null;
    }

    @Override
    public String getCurrentMap() {
        if (enabled) {
            return currentMap.getName();
        }
        return null;
    }

    @Override
    public boolean setCurrentMap(String world) {
        if (Arrays.stream(getAvailableMaps()).anyMatch(m -> m.equalsIgnoreCase(world))) {
            currentMap = pl3xMapApi.getWorldRegistry().get(world);
            return true;
        }
        return false;
    }

    @Override
    public boolean addMarkerSet(String setName, String label) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                CCLayer layer = new CCLayer(setName, label, currentMap);
                currentMap.getLayerRegistry().register(setName, layer);
                layers.put(setName, layer);
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
                currentMap.getLayerRegistry().unregister(setName);
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
    public Map<?, ?> getMarkers(String setName, boolean cartographerCreated) {
        if (enabled) {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("Integration unsupported", "Pl3xMap's API architecture is not conducive to retrieving the markers in a set. Please " +
                    "keep track of the markers you create in your own program.");
            return errorMessage;
        }
        return null;
    }

    @Override
    public boolean clearMarkerSet(String setName) {
        if (enabled) {
            try {
                setName = "cc_" + setName;
                layers.get(setName).markers.clear();
                return true;
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
                    for (IconImage iconImage : pl3xMapApi.getIconRegistry()) {
                        iconNames.add(iconImage.getKey());
                    }
                    if (iconNames.stream().noneMatch(i -> i.equals(iconCompare))) {
                        if (iconUrl != null) {
                            pl3xMapApi.getIconRegistry().register(realIcon, new IconImage(realIcon, Objects.requireNonNull(IntegrationHelper.downloadAndResizeIconBufferedImage(iconUrl)), "png"));
                        } else if (pl3xMapApi.getIconRegistry().get(icon) != null) {
                            realIcon = icon;
                        } else if (pl3xMapApi.getIconRegistry().get("cc_" + icon) != null) {
                            realIcon = "cc_" + icon;
                        } else {
                            realIcon = "";
                        }
                    }
                    Icon iconMarker = new Icon(id, x, z, realIcon);
                    iconMarker.setOptions(new Options.Builder()
                                    .popupContent("<div>" + label + "</div>" + detail)
                                    .tooltipContent(label)
                                    .build());
                    layers.get(markerSet).markers.add(iconMarker);
                    return true;
                }
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
                Collection<Point> pl3xPoints = points.stream().map(p -> new Point((int)p.x, (int)p.z)).toList();
                Polyline lineMarker = new Polyline(id, pl3xPoints);
                lineMarker.setOptions(new Options.Builder()
                        .popupContent("<div>" + label + "</div>" + detail)
                        .tooltipContent(label)
                        .strokeColor(color.getRGB())
                        .strokeWeight(width)
                        .build());
                layers.get(markerSet).markers.add(lineMarker);
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
                Circle circleMarker = new Circle(id, x, z, radius);
                circleMarker.setOptions(new Options.Builder()
                        .popupContent("<div>" + label + "</div>" + detail)
                        .tooltipContent(label)
                        .strokeColor(lineColor.getRGB())
                        .strokeWeight(lineWidth)
                        .fillColor(fillColor.getRGB())
                        .build());
                layers.get(markerSet).markers.add(circleMarker);
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
                Rectangle rectMarker = new Rectangle(id, x1, z1, x2, z2);
                rectMarker.setOptions(new Options.Builder()
                        .popupContent("<div>" + label + "</div>" + detail)
                        .tooltipContent(label)
                        .strokeColor(lineColor.getRGB())
                        .strokeWeight(lineWidth)
                        .fillColor(fillColor.getRGB())
                        .build());
                layers.get(markerSet).markers.add(rectMarker);
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
                Collection<Point> pl3xPoints = points.stream().map(p -> new Point((int)p.x, (int)p.z)).toList();
                Polyline line = new Polyline(id, pl3xPoints);
                Polygon polygonMarker = new Polygon(id, line);
                polygonMarker.setOptions(new Options.Builder()
                        .popupContent("<div>" + label + "</div>" + detail)
                        .tooltipContent(label)
                        .strokeColor(lineColor.getRGB())
                        .strokeWeight(lineWidth)
                        .fillColor(fillColor.getRGB())
                        .build());
                layers.get(markerSet).markers.add(polygonMarker);
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
                layers.get(markerSet).markers.removeIf(m -> m.getKey().equals(id));
                return true;
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    public static class CCLayer extends SimpleLayer {
        public ArrayList<Marker<?>> markers = new ArrayList<>();
        public final World mapWorld;

        public CCLayer(String key, String label, World world) {
            super(key, () -> label);
            mapWorld = world;
        }

        @Override
        @NotNull
        public Collection<Marker<?>> getMarkers() {
            return new LinkedList<>(markers);
        }
    }
}
