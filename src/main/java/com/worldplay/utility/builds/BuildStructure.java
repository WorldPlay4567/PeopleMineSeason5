package com.worldplay.utility.builds;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.*;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

public class BuildStructure {

    static ResourceManager resourceManager;

    public static void load(MinecraftServer server, ServerWorld world) {

        resourceManager = server.getResourceManager();
        System.out.println(getBuild("shop_stone"));
    }

    public static ArrayList<BuildEntity> getEntityBuild(String structureName) {
        NbtCompound nbt = getStructure(structureName);
        ArrayList<BuildEntity> buildEntities = new ArrayList<>();
        NbtList entitiesNbt = nbt.getList("entities", 10);

        for (int i = 0; i < entitiesNbt.size(); i++) {
            NbtCompound entityNbt = entitiesNbt.getCompound(i);


            String entityId =  entityNbt.getCompound("nbt").getString("id");

            Identifier identifier = Identifier.of(entityId);
            Optional<EntityType<?>> entityType = EntityType.get(identifier.toString());

            NbtList blockPosNbt = entityNbt.getList("pos", 6);

            Vec3d vec3d = new Vec3d(
                    blockPosNbt.getDouble(0),
                    blockPosNbt.getDouble(1),
                    blockPosNbt.getDouble(2));

            buildEntities.add(new BuildEntity(
                    vec3d,
                    entityNbt,
                    entityType
                    ));
        }

        return buildEntities;
    }

    public static ArrayList<BuildBlock> getBuild(String structureName) {
        NbtCompound nbt = getStructure(structureName);
        ArrayList<BuildBlock> blocks = new ArrayList<>();

        // load in blocks (list of blockPos and their palette index)
        NbtList blocksNbt = nbt.getList("blocks", 10);
        ArrayList<BlockState> palette = getBuildPalette(nbt);

        for(int i = 0; i < blocksNbt.size(); i++) {
            NbtCompound blockNbt = blocksNbt.getCompound(i);
            NbtList blockPosNbt = blockNbt.getList("pos", 3);

            blocks.add(new BuildBlock(
                    new BlockPos(
                            blockPosNbt.getInt(0),
                            blockPosNbt.getInt(1),
                            blockPosNbt.getInt(2)
                    ),
                    palette.get(blockNbt.getInt("state"))
            ));
        }
        return blocks;
    }

    public static NbtCompound getStructure(String structureName) {

        try {
        Identifier id = Identifier.of("peoplemineseason5", "structure/" + structureName + ".nbt");

        Optional<Resource> resource = resourceManager.getResource(id);

            NbtSizeTracker sizeTracker = new NbtSizeTracker(1_000_000L, 512);
        if (resource.isPresent()) {
            try (InputStream inputStream = resource.get().getInputStream()) {
                return NbtIo.readCompressed(inputStream,sizeTracker);
            }
        } else {
            System.err.println("Resource not found: " + id);
            return null;
        }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<BlockState> getBuildPalette(NbtCompound nbt) {
        ArrayList<BlockState> palette = new ArrayList<>();

        NbtList paletteNBT = nbt.getList("palette", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < paletteNBT.size(); i++) {
            NbtCompound blockStateNbt = paletteNBT.getCompound(i);

            int finalI = i;
            BlockState blockState = BlockState.CODEC.parse(NbtOps.INSTANCE, blockStateNbt)
                    .resultOrPartial(error -> System.err.println("Failed to parse BlockState: " + error))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid BlockState NBT at index " + finalI));

            palette.add(blockState);
        }

        return palette;
    }
    public static ArrayList<BlockState> getEntityPalette(NbtCompound nbt) {
        ArrayList<BlockState> palette = new ArrayList<>();

        NbtList paletteNBT = nbt.getList("palette", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < paletteNBT.size(); i++) {
            NbtCompound blockStateNbt = paletteNBT.getCompound(i);

            int finalI = i;
            BlockState blockState = BlockState.CODEC.parse(NbtOps.INSTANCE, blockStateNbt)
                    .resultOrPartial(error -> System.err.println("Failed to parse BlockState: " + error))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid BlockState NBT at index " + finalI));

            palette.add(blockState);
        }

        return palette;
    }



}
