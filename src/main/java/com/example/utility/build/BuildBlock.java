package com.example.utility.build;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
