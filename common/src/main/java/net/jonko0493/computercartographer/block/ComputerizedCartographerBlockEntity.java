package net.jonko0493.computercartographer.block;

import net.jonko0493.computercartographer.ComputerCartographer;
import net.jonko0493.computercartographer.peripheral.ComputerizedCartographerPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ComputerizedCartographerBlockEntity extends BlockEntity {
    private ComputerizedCartographerPeripheral peripheral;

    public ComputerizedCartographerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        peripheral = new ComputerizedCartographerPeripheral(this);
    }

    public ComputerizedCartographerBlockEntity(BlockPos pos, BlockState state) {
        this(ComputerCartographer.COMPUTERIZED_CARTOGRAPHER_BLOCK_ENTITY.get(), pos, state);
    }

    public ComputerizedCartographerPeripheral getPeripheral() {
        return peripheral;
    }
}
