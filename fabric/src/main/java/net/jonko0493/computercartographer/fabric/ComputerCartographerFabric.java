package net.jonko0493.computercartographer.fabric;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.jonko0493.computercartographer.ComputerCartographer;
import net.fabricmc.api.ModInitializer;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlock;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.jonko0493.computercartographer.peripheral.ComputerizedCartographerPeripheral;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ComputerCartographerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ComputerCartographer.init();
        registerPeripherals();
    }

    public static void registerPeripherals() {
        PeripheralLookup.get().registerForBlocks((world, pos, state, blockEntity, context) -> {
           if (blockEntity instanceof ComputerizedCartographerBlockEntity) {
               return ((ComputerizedCartographerBlockEntity)blockEntity).getPeripheral();
           } else {
               return null;
           }
        });
    }
}