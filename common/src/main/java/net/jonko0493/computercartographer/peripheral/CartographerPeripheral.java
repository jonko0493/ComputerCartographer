package net.jonko0493.computercartographer.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.integration.IMapIntegration;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class CartographerPeripheral implements IPeripheral {
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
        if (ComputerCartographer.integrations.size() == 1) {
            currentIntegration = ComputerCartographer.integrations.get(0);
        }
    }

    @LuaFunction
    public String[] getAvailableIntegrations() {
        return ComputerCartographer.integrations.stream().map(IMapIntegration::getName).toArray(String[]::new);
    }

    @LuaFunction
    public String getCurrentIntegration() {
        return currentIntegration == null ? null : currentIntegration.getName();
    }

    @LuaFunction
    public boolean setCurrentIntegration(String integrationName) {
        AtomicBoolean success = new AtomicBoolean(false);
        ComputerCartographer.integrations.stream()
                .filter(i -> i.getName().equalsIgnoreCase(integrationName))
                .findFirst()
                .ifPresent(integration -> {
                    String currentWorld = "";
                    if (currentIntegration != null) {
                        currentWorld = currentIntegration.getCurrentWorld();
                    }
                    currentIntegration = integration;
                    success.set(true);
                    if (!currentWorld.isEmpty()) {
                        integration.setCurrentWorld(currentWorld);
                    }
                });
        return success.get();
    }

    @LuaFunction
    public String getCurrentWorld() {
        if (currentIntegration == null) {
            return null;
        }
        return currentIntegration.getCurrentWorld();
    }

    @LuaFunction
    public boolean setCurrentWorld(String world) {
        if (currentIntegration == null) {
            return false;
        }
        return currentIntegration.setCurrentWorld(world);
    }
}
