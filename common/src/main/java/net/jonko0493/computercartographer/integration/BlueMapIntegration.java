package net.jonko0493.computercartographer.integration;

import com.ibm.icu.util.Output;
import dan200.computercraft.api.lua.IArguments;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.Marker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import net.jonko0493.computercartographer.ComputerCartographer;
import org.apache.commons.io.FilenameUtils;

import java.io.OutputStream;
import java.net.URL;

public class BlueMapIntegration implements IMapIntegration {
    private final String name = "bluemap";
    private BlueMapAPI api;
    private BlueMapMap currentMap;
    private boolean enabled = false;

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
                        switch (icon.toLowerCase()) {
                            case "turtle":
                                break;
                            case "computer":
                                break;
                            case "house":
                                break;
                            case "minecart":
                                break;
                            case "portal":
                                break;
                            case "diamond":
                                break;
                            case "world":
                                break;
                            default:
                                String defaultName = "cc_" + icon.toLowerCase() + ".png";
                                if (!icon.equalsIgnoreCase("default") && currentMap.getAssetStorage().assetExists(defaultName)) {
                                    realIcon = currentMap.getAssetStorage().getAssetUrl(defaultName);
                                }
                        }
                    }
                    POIMarker marker;
                    if (realIcon.isEmpty()) {
                        marker = POIMarker.builder()
                                .defaultIcon()
                                .label(label)
                                .detail(detail)
                                .position(x, y, z)
                                .build();
                    } else {
                        marker = POIMarker.builder()
                                .icon(realIcon, IntegrationHelper.ICON_WIDTH / 2, IntegrationHelper.ICON_HEIGHT / 2)
                                .label(label)
                                .detail(detail)
                                .position(x, y, z)
                                .build();
                    }
                    currentMap.getMarkerSets().get(markerSet).put(id, marker);
                    return true;
                }
            } catch (Exception e) {
                ComputerCartographer.logException(e);
            }
        }
        return false;
    }

    @Override
    public boolean removePOIMarker(String markerSet, String id) {
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
