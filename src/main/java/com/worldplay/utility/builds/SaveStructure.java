package com.worldplay.utility.builds;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldplay.PeopleMineSeason5;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SaveStructure extends PersistentState  {
    private CopyOnWriteArrayList<StructureData> structureData = new CopyOnWriteArrayList<>();

    // Конструктор по умолчанию – создаёт пустой список
    public SaveStructure() {
        this.structureData = new CopyOnWriteArrayList<>();
    }

    // Конструктор для десериализации Codec (принимает список структур)
    public SaveStructure(List<StructureData> structureDataList) {
        this.structureData = new CopyOnWriteArrayList<>(structureDataList);
    }

    // Codec для сериализации/десериализации StructureData
    public static final Codec<StructureData> STRUCTURE_DATA_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(sd -> sd.posStructure.x),
                    Codec.DOUBLE.fieldOf("y").forGetter(sd -> sd.posStructure.y),
                    Codec.DOUBLE.fieldOf("z").forGetter(sd -> sd.posStructure.z),
                    Codec.STRING.fieldOf("nameStructure").forGetter(sd -> sd.nameStructure),
                    Codec.BOOL.fieldOf("visibleLineBox").forGetter(sd -> sd.visibleLineBox)
            ).apply(instance, (x, y, z, name, visible) ->
                    new StructureData(new Vec3d(x, y, z), name, visible)
            )
    );

    // Codec для сериализации/десериализации SaveStructure (список структур)
    public static final Codec<SaveStructure> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(STRUCTURE_DATA_CODEC).fieldOf("structureData")
                            .forGetter(save -> new ArrayList<>(save.structureData))
            ).apply(instance, SaveStructure::new)
    );


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
        Optional<NbtList> nbtList = nbt.getList("structure"); // 10 = тип TAG_Compound
        for (int i = 0; i < nbtList.get().size(); i++) {
            Optional<NbtCompound> coordTag = nbtList.get().getCompound(i);
            Optional<Integer> x = coordTag.get().getInt("x");
            Optional<Integer> y = coordTag.get().getInt("y");
            Optional<Integer> z = coordTag.get().getInt("z");
            Vec3d vec3d = new Vec3d(x.get(),y.get(),z.get());
            String string = String.valueOf(coordTag.get().getString("nameStructure"));
            Optional<Boolean> visible = coordTag.get().getBoolean("visibleLineBox");

            state.structureData.add(new StructureData(vec3d, string, visible.get()));
        }
        return state;
    }

    public void addCoordinate(Vec3d pos, String name) {
        this.structureData.add(new StructureData(pos, name, false));
        markDirty();
    }

    // Поиск структуры по координате
    public StructureData getStructureData(Vec3d pos) {
        for (StructureData data : structureData) {
            if (data.posStructure.equals(pos)) {
                return data;
            }
        }
        System.out.println("Структура по координате " + pos + " не найдена.");
        return null;
    }

    // Получение списка структур по параметру видимости
    public List<StructureData> getStructureData(boolean visible) {
        List<StructureData> list = new ArrayList<>();
        for (StructureData data : structureData) {
            if (data.visibleLineBox == visible) {
                list.add(data);
            }
        }
        return list;
    }

    // Обновление данных структуры
    public void updateStructureData(StructureData newData) {
        for (int i = 0; i < structureData.size(); i++) {
            StructureData data = structureData.get(i);
            if (data.posStructure.equals(newData.posStructure)) {
                structureData.set(i, newData);
                markDirty();
                break;
            }
        }
    }

    // Удаление структуры
    public void removeStructureData(StructureData dataToRemove) {
        if (structureData.removeIf(data -> data.posStructure.equals(dataToRemove.posStructure))) {
            markDirty();
        }
    }

    // Регистрация типа persistent state с использованием Codec
    private static final PersistentStateType<SaveStructure> TYPE =
            new PersistentStateType<>(PeopleMineSeason5.MOD_ID, SaveStructure::new, CODEC, null);

    // Метод для получения состояния сохранения структуры из мира (используется Overworld)
    public static SaveStructure getStructureState(MinecraftServer server) {
        PersistentStateManager persistentStateManager =
                Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();
        SaveStructure state = persistentStateManager.getOrCreate(TYPE);
        state.markDirty();
        return state;
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
