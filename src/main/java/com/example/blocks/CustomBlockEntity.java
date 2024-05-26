package com.example.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickManager;
import net.minecraft.world.tick.TickScheduler;

public class CustomBlockEntity extends BlockEntity {


    public CustomBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirty(this.world, this.pos, this.getCachedState());
        }

    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        nbt.putInt("Test", 1);
        super.writeNbt(nbt, lookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {

        super.readNbt(nbt, lookup);
    }


    public static void tick(World world, BlockPos pos, BlockState blockState) {
        ServerWorld serverWorld = world.getServer().getOverworld();
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY()+ 2, pos.getZ()+ 0.5, 5, 0, 0.1,0,0.01);
        System.out.println(pos);
    }

}
