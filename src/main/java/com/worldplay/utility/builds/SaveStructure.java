package com.worldplay.utility.builds;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveStructure extends PersistentState {
    private final List<StructureData> structureData = new ArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList nbtList = new NbtList();
        for(StructureData coord: structureData) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("x", coord.x);
            nbtCompound.putInt("y", coord.y);
            nbtCompound.putInt("z", coord.z);
            nbtList.add(nbtCompound);
        }
        nbt.put("structure", nbtList);
        return nbt;
    }

    public static SaveStructure fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        SaveStructure state = new SaveStructure();
        NbtList nbtList = nbt.getList("structure", 10); // 10 = тип TAG_Compound
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound coordTag = nbtList.getCompound(i);
            int x = coordTag.getInt("x");
            int y = coordTag.getInt("y");
            int z = coordTag.getInt("z");
            state.coordinateList.add(new Coordinate(x, y, z));
        }
        return state;
    }

    public void addCoordinate(int x, int y, int z) {
        this.coordinateList.add(new Coordinate(x,y,z));
        markDirty();
    }
    public void addCoordinate(Vec3d pos) {
        this.coordinateList.add(new Coordinate((int) pos.x, (int) pos.y, (int) pos.z));
        markDirty();
    }



    private static Type<SaveStructure> type = new Type<>(
            SaveStructure::new,
            SaveStructure::fromNbt,
            null);


    public static SaveStructure getStructureState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();
        SaveStructure saveStructure = persistentStateManager.getOrCreate(type, "pos_structure");
        saveStructure.markDirty();
        return saveStructure;
    }

//    public List<String> getAllString() {
//
//        List<String> strings = new ArrayList<>();
//        for(Coordinate coordinate : coordinateList) {
//            strings.add(coordinate.get());
//        }
//        return strings;
//    }

//    static class Coordinate {
//        public int x;
//        public int y;
//        public int z;
//
//        public Coordinate(int x, int y, int z) {
//            this.x = x;
//            this.y = y;
//            this.z = z;
//        }
//
//        public String get() {
//            return ("x:" + x + " y: " + y + " z: " + z );
//        }
//    }
}
