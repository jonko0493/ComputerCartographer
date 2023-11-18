package net.jonko0493.computercartographer.peripheral;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.jonko0493.computercartographer.integration.IMapIntegration;
import org.jetbrains.annotations.Nullable;

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
    public final boolean removePOIMarker(String markerSet, String markerId) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.removePOIMarker(markerSet, markerId);
    }
}
