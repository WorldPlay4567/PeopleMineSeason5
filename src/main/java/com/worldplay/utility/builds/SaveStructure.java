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
import java.util.concurrent.CopyOnWriteArrayList;

public class SaveStructure extends PersistentState {
    private final CopyOnWriteArrayList<StructureData> structureData = new CopyOnWriteArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList nbtList = new NbtList();
        for(StructureData coord: structureData) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("x", (int) coord.posStructure.x);
            nbtCompound.putInt("y", (int) coord.posStructure.y);
            nbtCompound.putInt("z", (int) coord.posStructure.z);
            nbtCompound.putString("nameStructure", coord.nameStructure);
            nbtCompound.putBoolean("visibleLineBox", coord.visibleLineBox);
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
            Vec3d vec3d = new Vec3d(x,y,z);
            String string = coordTag.getString("nameStructure");
            boolean visible = coordTag.getBoolean("visibleLineBox");

            state.structureData.add(new StructureData(vec3d, string, visible));
        }
        return state;
    }

    public void addCoordinate(Vec3d pos, String name) {
        this.structureData.add(new StructureData(pos, name, false));
        markDirty();
    }

    public StructureData getStructureData(Vec3d vec3d) {
        for(StructureData coord: structureData) {
//            System.out.println("Data " +coord.posStructure + " Find " + vec3d);
            if(coord.posStructure.equals(vec3d)) {
                return coord;
            }
        }
        System.out.println("Не найдено :(");
        return null;
    }

    public List<StructureData> getStructureData(boolean visible) {
        List<StructureData> structureData1 = new ArrayList<>();
        for(StructureData coord: structureData) {
//            System.out.println("Data " +coord.posStructure + " Find " + vec3d);
            if(coord.visibleLineBox == visible) {
                structureData1.add(coord);
            }
        }

        return structureData1;
    }

    public void setStructureData(StructureData structureData1) {
        for(int i = 0; i < structureData.size(); i++) {
            if(structureData.get(i).posStructure == structureData1.posStructure) {
                structureData.set(i, structureData1);
                markDirty();
            }
        }
    }

    public void removeStructureData(StructureData structureData1) {
        for(int i = 0; i < structureData.size(); i++) {
            if(structureData.get(i).posStructure == structureData1.posStructure) {
                structureData.remove(i);
                markDirty();
            }
        }
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
