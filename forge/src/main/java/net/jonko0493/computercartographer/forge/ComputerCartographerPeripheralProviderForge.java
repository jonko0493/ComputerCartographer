package net.jonko0493.computercartographer.forge;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class ComputerCartographerPeripheralProviderForge implements IPeripheralProvider {
    @Override
    public LazyOptional<IPeripheral> getPeripheral(World world, BlockPos pos, Direction side) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ComputerizedCartographerBlockEntity) {
            return LazyOptional.of(() -> ((ComputerizedCartographerBlockEntity)blockEntity).getPeripheral());
        } else {
            return LazyOptional.empty();
        }
    }
}
