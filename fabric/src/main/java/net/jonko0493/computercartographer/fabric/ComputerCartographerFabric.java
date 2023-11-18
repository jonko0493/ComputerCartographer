package net.jonko0493.computercartographer.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.api.ModInitializer;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;

public class ComputerCartographerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
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