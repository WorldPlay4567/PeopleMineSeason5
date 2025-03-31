package com.worldplay.utility.builds.update;

import com.worldplay.PeopleMineSeason5;
import com.worldplay.items.ItemsInit;
import com.worldplay.utility.builds.PlaceBuild;
import com.worldplay.utility.builds.SaveStructure;
import com.worldplay.utility.builds.StructureData;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class UpdateGui extends SimpleGui {
    boolean visibleLineBox = false;
    String nameStructure = "";
    Vec3d posStructure;
    NbtCompound nbt;
    VillagerEntity villager;

    public UpdateGui(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots) {
        super(type, player, manipulatePlayerSlots);
    }
    public UpdateGui(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X3, player, true);
    }
    public UpdateGui(ServerPlayerEntity player, VillagerEntity villager) {
        super(ScreenHandlerType.GENERIC_9X3, player, true);
        this.villager = villager;
        getNBT();
    }

    public void openUpdateGui() {
        /////////////
        if(visibleLineBox) {
            Style style = Style.EMPTY.withColor(Formatting.GREEN);
            this.setSlot(26, new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE,1)
                    .setName(Text.literal("Показать зону для постройки?").setStyle(Style.EMPTY))
                    .addLoreLine(Text.literal("Сейчас: ").append("[Включено]").setStyle(style))
                    .setCallback((index, clickType, actionType) -> {
                        if(clickType.isLeft) {
                            visibleLineBox = false;
                            openUpdateGui();
                        }
                    }));
        } else {
            Style style = Style.EMPTY.withColor(Formatting.RED);
            this.setSlot(26, new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE,1)
                    .setName(Text.literal("Показать зону для постройки?").setStyle(Style.EMPTY))
                    .addLoreLine(Text.literal("Сейчас: ").append("[Выключено]").setStyle(style))
                    .setCallback((index, clickType, actionType) -> {
                        if(clickType.isLeft) {
                            visibleLineBox = true;
                            openUpdateGui();
                        }
                    }));
        }
        /////////////

        this.setSlot(13, new GuiElementBuilder(ItemsInit.BLUE_PRINT,1)
                .setName(Text.literal("Сейчас стоит постройка: ").append(nameStructure)));

        this.setSlot(17, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE,1)
                .setName(Text.literal("Забрать постройку"))
                .addLoreLine(Text.literal("Может случайно задеть постройки"))
                .setCallback((index, clickType, actionType) -> {
                    if(clickType.isLeft) {
                        BlockPos pos = BlockPos.ofFloored(posStructure);
                        PlaceBuild placeBuild = new PlaceBuild(pos,nameStructure,villager.getServer().getOverworld());
                        placeBuild.remove();
                        villager.kill(villager.getServer().getOverworld());
                        SaveStructure saveStructure = SaveStructure.getStructureState(Objects.requireNonNull(villager.getServer()));
                        Vec3d var = villager.getPos();
                        var = var.add(0,-1,0);
                        StructureData structureData = saveStructure.getStructureData(var);
                        saveStructure.removeStructureData(structureData);

                        ItemEntity item = new ItemEntity(EntityType.ITEM,villager.getServer().getOverworld());


                        ItemStack itemStack = new ItemStack(ItemsInit.BLUE_PRINT,1);
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putString("structure",PeopleMineSeason5.MOD_ID +":" +nameStructure);
                        itemStack.set(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(nbtCompound));

                        item.setStack(itemStack);
                        item.addVelocity(0,0.6,0);
                        item.setPosition(posStructure.add(0,1,0));
                        villager.getServer().getOverworld().spawnEntity(item);
                        this.close();
                    }
                }));
        this.open();

    }

    @Override
    public void onClose() {
        super.onClose();
        this.setNBT();
    }

    private void getNBT() {
        SaveStructure saveStructure = SaveStructure.getStructureState(Objects.requireNonNull(villager.getServer()));
        Vec3d var = villager.getPos();
        var = var.add(0,-1,0);
        StructureData structureData = saveStructure.getStructureData(var);

        nameStructure = structureData.nameStructure;
        posStructure = structureData.posStructure;
        visibleLineBox = structureData.visibleLineBox;
    }

    private void setNBT() {
        SaveStructure saveStructure = SaveStructure.getStructureState(Objects.requireNonNull(villager.getServer()));
        Vec3d var = villager.getPos();
        var = var.add(0,-1,0);
        StructureData structureData = saveStructure.getStructureData(var);

        structureData.nameStructure = nameStructure;
        structureData.posStructure = posStructure;
        structureData.visibleLineBox = visibleLineBox;

//        saveStructure.setStructureData(structureData);
    }

    public void setNbt(NbtCompound nbt) {
        this.nbt = nbt;
    }

    public void updateNameStructure( String name) {
        this.nameStructure = name;
    }

    public void setPosStructure(Vec3d vec3d) {
        this.posStructure = vec3d;
    }
}
