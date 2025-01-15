package com.worldplay.utility.builds;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BuildBlock {
    private BlockPos blockPos;
    private BlockState blockState;

    public BuildBlock(BlockPos blockPos, BlockState blockState) {
        this.blockPos = blockPos;
        this.blockState = blockState;
    }

    public BlockPos getBlockPos() { return blockPos; }
    public BlockState getBlockState() { return blockState; }

}
