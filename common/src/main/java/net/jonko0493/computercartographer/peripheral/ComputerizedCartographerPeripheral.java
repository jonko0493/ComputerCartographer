package net.jonko0493.computercartographer.peripheral;

import com.flowpowered.math.vector.Vector3d;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.jonko0493.computercartographer.integration.IMapIntegration;
import net.jonko0493.computercartographer.integration.IntegrationHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComputerizedCartographerPeripheral implements IPeripheral {
    private final ComputerizedCartographerBlockEntity blockEntity;

    public ComputerizedCartographerPeripheral(ComputerizedCartographerBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Nullable
    private IMapIntegration currentIntegration;

    @Override
    public String getType() {
        return "cartographer";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }

    @Override
    public void attach(IComputerAccess computer) {
        // If there is only one integration available, we should default to using it
        if (ComputerCartographer.integrations.isEmpty()) {
            ComputerCartographer.initIntegrations();
        }
        if (ComputerCartographer.integrations.size() == 1) {
            currentIntegration = ComputerCartographer.integrations.get(0);
        }
    }

    @LuaFunction
    public final String[] getAvailableIntegrations() {
        return ComputerCartographer.integrations.stream().map(IMapIntegration::getName).toArray(String[]::new);
    }

    @LuaFunction
    public final String getCurrentIntegration() {
        return currentIntegration == null ? null : currentIntegration.getName();
    }

    @LuaFunction
    public final boolean setCurrentIntegration(String integrationName) {
        AtomicBoolean success = new AtomicBoolean(false);
        ComputerCartographer.integrations.stream()
                .filter(i -> i.getName().equalsIgnoreCase(integrationName))
                .findFirst()
                .ifPresent(integration -> {
                    String currentWorld = "";
                    if (currentIntegration != null) {
                        currentWorld = currentIntegration.getCurrentMap();
                    }
                    currentIntegration = integration;
                    success.set(true);
                    if (!currentWorld.isEmpty()) {
                        integration.setCurrentMap(currentWorld);
                    }
                });
        return success.get();
    }

    @LuaFunction
    public final String[] getAvailableMaps() {
        if (currentIntegration == null) {
            return null;
        }
        return currentIntegration.getAvailableMaps();
    }

    @LuaFunction
    public final String getCurrentMap() {
        if (currentIntegration == null) {
            return null;
        }
        return currentIntegration.getCurrentMap();
    }

    @LuaFunction
    public final boolean setCurrentMap(String world) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.setCurrentMap(world);
    }

    @LuaFunction
    public final boolean addMarkerSet(String setName, String label) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.addMarkerSet(setName, label);
    }

    @LuaFunction
    public final boolean removeMarkerSet(String setName) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.removeMarkerSet(setName);
    }

    @LuaFunction
    public final String[] getMarkerSets() {
        if (currentIntegration == null) {
            return null;
        }
        return currentIntegration.getMarkerSets();
    }

    @LuaFunction
    public final boolean clearMarkerSet(String setName) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.clearMarkerSet(setName);
    }

    // Arguments:
    // 0: markerSet (String)
    // 1: markerId (String)
    // 2: label (String)
    // 3: detail (String)
    // 4: x (double)
    // 5: y (double)
    // 6: z (double)
    // 7: icon (optional)
    @LuaFunction
    public final boolean addPOIMarker(IArguments arguments) {
        if (currentIntegration == null) {
            return false;
        }
        try {
            String markerSet = arguments.getString(0);
            String markerId = arguments.getString(1);
            String label = arguments.getString(2);
            String detail = arguments.getString(3);
            double x = arguments.getDouble(4);
            double y = arguments.getDouble(5);
            double z = arguments.getDouble(6);
            String icon = arguments.optString(7, "");
            return currentIntegration.addPOIMarker(markerSet, markerId, label, detail, icon, x, y, z);
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return false;
    }

    @LuaFunction
    public final boolean removeMarker(String markerSet, String markerId) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.removeMarker(markerSet, markerId);
    }

    // Arguments:
    // 0: markerSet (String)
    // 1: markerId (String)
    // 2: label (String)
    // 3: detail (String)
    // 4: color (String, RGBA hex code)
    // 5: color alpha (double, 0.0-1.0)
    // 6: width (int)
    // 7: points (table, interpreted as ArrayList<Vector3d>)
    @LuaFunction
    public final boolean addLineMarker(IArguments arguments) {
        if (currentIntegration == null) {
            return false;
        }
        try {
            String markerSet = arguments.getString(0);
            String markerId = arguments.getString(1);
            String label = arguments.getString(2);
            String detail = arguments.getString(3);
            Color color = Color.decode(arguments.getString(4));
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(arguments.getDouble(5) * 255));
            int width = arguments.getInt(6);
            ArrayList<Vector3d> points = IntegrationHelper.parsePoints(arguments.getTable(7));

            return currentIntegration.addLineMarker(markerSet, markerId, label, detail, color, width, points);
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return false;
    }

    @LuaFunction
    public final boolean addCircleMarker(String markerSet, String markerId, String label, String detail, String lineColorStr, double lineAlpha, String fillColorStr, double fillAlpha, int lineWidth, double x, double z, double radius) {
        if (currentIntegration == null) {
            return false;
        }
        Color lineColor = Color.decode(lineColorStr);
        lineColor = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)(lineAlpha * 255));
        Color fillColor = Color.decode(fillColorStr);
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)(fillAlpha * 255));
        return currentIntegration.addCircleMarker(markerSet, markerId, label, detail, lineColor, fillColor, lineWidth, x, z, radius);
    }

    @LuaFunction
    public final boolean addRectangleMarker(String markerSet, String markerId, String label, String detail, String lineColorStr, double lineAlpha, String fillColorStr, double fillAlpha, int lineWidth, double x1, double z1, double x2, double z2) {
        if (currentIntegration == null) {
            return false;
        }
        Color lineColor = Color.decode(lineColorStr);
        lineColor = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)(lineAlpha * 255));
        Color fillColor = Color.decode(fillColorStr);
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)(fillAlpha * 255));
        return currentIntegration.addRectangleMarker(markerSet, markerId, label, detail, lineColor, fillColor, lineWidth, x1, z1, x2, z2);
    }

    // Arguments:
    // 0: markerSet (String)
    // 1: markerId (String)
    // 2: label (String)
    // 3: detail (String)
    // 4: lineColor (String, RGBA hex code)
    // 5: lineAlpha (double, 0.0-1.0)
    // 6: fillColor (String, RGBA hex code)
    // 7: fillAlpha (double, 0.0-1.0)
    // 8: lineWidth (int)
    // 9: points (table, interpreted as ArrayList<Vector3d>)
    @LuaFunction
    public final boolean addAreaMarker(IArguments arguments) {
        if (currentIntegration == null) {
            return false;
        }
        try {
            String markerSet = arguments.getString(0);
            String markerId = arguments.getString(1);
            String label = arguments.getString(2);
            String detail = arguments.getString(3);
            Color lineColor = Color.decode(arguments.getString(4));
            lineColor = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)(arguments.getDouble(5) * 255));
            Color fillColor = Color.decode(arguments.getString(6));
            fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)(arguments.getDouble(7) * 255));
            int lineWidth = arguments.getInt(8);
            ArrayList<Vector3d> points = IntegrationHelper.parsePoints(arguments.getTable(9));

            return currentIntegration.addAreaMarker(markerSet, markerId, label, detail, lineColor, fillColor, lineWidth, points);
        } catch (Exception e) {
            ComputerCartographer.logException(e);
        }
        return false;
    }
}
