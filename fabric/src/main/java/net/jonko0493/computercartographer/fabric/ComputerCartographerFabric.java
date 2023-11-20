package net.jonko0493.computercartographer.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.minecraft.server.MinecraftServer;

public class ComputerCartographerFabric implements ModInitializer {
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(s -> server = s);

        ComputerCartographer.init();
        registerPeripherals();
    }

    public static void registerPeripherals() {
        PeripheralLookup.get().registerFallback(((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof ComputerizedCartographerBlockEntity) {
                return ((ComputerizedCartographerBlockEntity)blockEntity).getPeripheral();
            }
            return null;
        }));
    }
}