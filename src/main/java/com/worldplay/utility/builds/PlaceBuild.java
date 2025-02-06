package com.worldplay.utility.builds;

import com.worldplay.utility.KillEntityBox;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class PlaceBuild {

    BlockPos structurePos;
    ArrayList<BuildBlock> blockList;
    ArrayList<BuildEntity> buildEntities;
    private final ServerWorld world;
    private int currentIndex = 0;
    private ServerPlayerEntity serverPlayer;
    private boolean isPlace = false;
    private int tick;
    private String name;
    public PlaceBuild(BlockPos blockPos, String name, ServerWorld world, ServerPlayerEntity serverPlayer){
        this.structurePos = blockPos;
         blockList = BuildStructure.getBuild(name);
         buildEntities = BuildStructure.getEntityBuild(name);
         this.name = name;
         this.world = world;
         this.serverPlayer = serverPlayer;
    }

    public PlaceBuild(BlockPos blockPos, String name, ServerWorld world){
        this.structurePos = blockPos;
        blockList = BuildStructure.getBuild(name);
        buildEntities = BuildStructure.getEntityBuild(name);
        this.name = name;
        this.world = world;
        this.serverPlayer = null;
    }


    public void place() {

        if(!isPlace) {
                if(currentIndex < blockList.size()) {

                   BuildBlock block = blockList.get(currentIndex);

                   BlockPos blockPos = new BlockPos(
                           structurePos.getX() + block.getBlockPos().getX(),
                           structurePos.getY() + block.getBlockPos().getY(),
                           structurePos.getZ() + block.getBlockPos().getZ()
                   );

                   this.placeBlock(blockPos, block.getBlockState());
                   currentIndex++;
                    if (currentIndex < blockList.size() && blockList.get(currentIndex).getBlockState() == Blocks.AIR.getDefaultState()) {
                        this.airSkip();
                    }
                } else {
                    for(BuildEntity entity: buildEntities) {
                        Optional<EntityType<?>> entityType = entity.getEntityType();
                        entityType.ifPresent(type -> {
                            Entity entity1 = type.create(world, SpawnReason.MOB_SUMMONED);
                            if (entity1 != null) {

                                Vec3d vec3d = new Vec3d(
                                        structurePos.getX() + entity.getEntityPos().x,
                                        structurePos.getY() + entity.getEntityPos().y,
                                        structurePos.getZ() + entity.getEntityPos().z
                                );
                                entity1.readNbt(entity.getNbtEntity().getCompound("nbt"));
                                entity1.setUuid(UUID.randomUUID());
                                entity1.setPosition(vec3d);
                                world.spawnEntity(entity1);
                            } else {
                                System.out.println("Ошибка спавна моба: \"NBT не найден\"");
                            }
                        });
                    }
                    isPlace = true;
                    this.isPlace(structurePos);
                }
        }
    }

    private void airSkip() {
        BuildBlock block = blockList.get(currentIndex);

        BlockPos blockPos = new BlockPos(
                structurePos.getX() + block.getBlockPos().getX(),
                structurePos.getY() + block.getBlockPos().getY(),
                structurePos.getZ() + block.getBlockPos().getZ()
        );

        this.placeBlock(blockPos, block.getBlockState());
        currentIndex++;

        if (currentIndex < blockList.size() && blockList.get(currentIndex).getBlockState() == Blocks.AIR.getDefaultState()) {
            this.airSkip();
        }
    }

    public void remove() {

        for(BuildBlock block: blockList) {
            BlockPos blockPos = new BlockPos(
                    structurePos.getX() + block.getBlockPos().getX(),
                    structurePos.getY() + block.getBlockPos().getY(),
                    structurePos.getZ() + block.getBlockPos().getZ()
            );
            this.removeBlock(blockPos, Blocks.AIR.getDefaultState());
        }

        for(BuildEntity entity: buildEntities) {
            Vec3d vec3d = new Vec3d(
                    structurePos.getX() + entity.getEntityPos().x,
                    structurePos.getY() + entity.getEntityPos().y,
                    structurePos.getZ() + entity.getEntityPos().z
            );
            KillEntityBox.killAll(world,vec3d);
        }
    }



    private void isPlace(BlockPos blockPos) {
        SaveStructure.getStructureState(world.getServer()).addCoordinate(Vec3d.of(structurePos),name);

        VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER,world);
        NbtCompound nbtCompound = new NbtCompound();
        villager.writeNbt(nbtCompound);
        nbtCompound.putBoolean("NoAI", true);
        villager.readNbt(nbtCompound);
        villager.addCommandTag("structureVillager");
        Vec3d var1 = Vec3d.of(structurePos);
        var1 = var1.add(0,1,0);

        villager.setPosition(var1);
        world.spawnEntity(villager);

        world.playSound(null, blockPos.getX(),blockPos.getY(),blockPos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 120f, 1f);
        serverPlayer.sendMessage(Text.literal("==========================\n Постройка завершена \n=========================="));
    }

    private void placeBlock(BlockPos blockPos, BlockState blockState) {
        world.setBlockState(blockPos,blockState);
        if(blockState.getBlock() != Blocks.AIR) {
            world.playSound(null, blockPos.getX(),blockPos.getY(),blockPos.getZ(), blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS);
            world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),blockPos.getX(),blockPos.getY(),blockPos.getZ(), 4, 0.1,0.1,0.1,0.1);
        }
    }

    private void removeBlock(BlockPos blockPos, BlockState blockState) {
        world.setBlockState(blockPos,blockState);
//        world.playSound(null, blockPos.getX(),blockPos.getY(),blockPos.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS);
        world.spawnParticles(ParticleTypes.CLOUD,blockPos.getX(),blockPos.getY(),blockPos.getZ(), 0, 0.1,0.1,0.1,0.1);

    }

    public boolean isPlace() {
        return isPlace;
    }
}

