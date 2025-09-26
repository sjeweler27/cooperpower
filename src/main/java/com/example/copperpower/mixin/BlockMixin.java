package com.example.copperpower.mixin;

import com.example.copperpower.CopperNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "emitsRedstonePower", at = @At("HEAD"), cancellable = true)
    private void copperpower$emitsRedstonePower(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (CopperNetwork.isConductiveCopper(state)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getWeakRedstonePower", at = @At("HEAD"), cancellable = true)
    private void copperpower$getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (CopperNetwork.isConductiveCopper(state)) {
            cir.setReturnValue(CopperNetwork.computePower(state, world, pos));
        }
    }

    @Inject(method = "getStrongRedstonePower", at = @At("HEAD"), cancellable = true)
    private void copperpower$getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (CopperNetwork.isConductiveCopper(state)) {
            cir.setReturnValue(CopperNetwork.computePower(state, world, pos));
        }
    }
}
