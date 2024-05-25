package com.example.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomBlockEntity extends BlockEntity {


    public CustomBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    public static void tick(World world, BlockPos pos, BlockState blockState) {
        ServerWorld serverWorld = world.getServer().getOverworld();
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY()+ 2, pos.getZ()+ 0.5, 5, 0, 0.1,0,0.01);
        System.out.println(pos);
    }
}
