package com.example.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomBlockEntity extends BlockEntity {

    public int time = 0;


    public CustomBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        nbt.putInt("time", time);
        super.writeNbt(nbt, lookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        this.time = nbt.getInt("time");
        super.readNbt(nbt, lookup);
    }


    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {

        var self = (CustomBlockEntity) t;

        self.time ++;
        self.markDirty();

        ServerWorld serverWorld = world.getServer().getOverworld();
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY()+ 2, pos.getZ()+ 0.5, 5, 0, 0.1,0,0.01);
        System.out.println(pos);
    }

}
