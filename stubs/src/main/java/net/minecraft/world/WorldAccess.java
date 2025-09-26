package net.minecraft.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface WorldAccess extends WorldView {
    int getEmittedRedstonePower(BlockPos pos, Direction direction);
}
