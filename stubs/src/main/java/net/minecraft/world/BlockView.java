package net.minecraft.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface BlockView {
    BlockState getBlockState(BlockPos pos);
}
