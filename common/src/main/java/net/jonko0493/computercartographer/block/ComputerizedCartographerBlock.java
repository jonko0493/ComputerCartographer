package net.jonko0493.computercartographer.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ComputerizedCartographerBlock extends BlockWithEntity {
    public ComputerizedCartographerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerizedCartographerBlockEntity(pos, state);
    }

//    @Nullable
//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
//        if (direction == Direction.DOWN || direction == Direction.UP) {
//            direction = Direction.NORTH;
//        }
//        return this.getDefaultState().with(FacingBlock.FACING, direction);
//    }
}
