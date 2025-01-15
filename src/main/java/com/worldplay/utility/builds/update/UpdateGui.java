package com.worldplay.utility.builds.update;

import com.worldplay.items.ItemsInit;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

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
                        if(clickType.isLeft && index == 26) {
                            visibleLineBox = true;
                        }
                    }));
        } else {
            Style style = Style.EMPTY.withColor(Formatting.RED);
            this.setSlot(26, new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE,1)
                    .setName(Text.literal("Показать зону для постройки?").setStyle(Style.EMPTY))
                    .addLoreLine(Text.literal("Сейчас: ").append("[Выключено]").setStyle(style))
                    .setCallback((index, clickType, actionType) -> {
                        if(clickType.isLeft && index == 26) {
                            visibleLineBox = false;
                        }
                    }));
        }
        /////////////

        this.setSlot(13, new GuiElementBuilder(ItemsInit.BLUE_PRINT,1)
                .setName(Text.literal("Сейчас стоит постройка: ").append(nameStructure)));
        this.open();

    }

    @Override
    public void onClose() {
        super.onClose();
        this.setNBT();
    }

    private void getNBT() {
        nbt = new NbtCompound();
        villager.writeNbt(nbt);

        nameStructure = nbt.getString("structure");
        posStructure = villager.getPos();
        visibleLineBox = nbt.getBoolean("visibleLineBox");
    }

    private void setNBT() {

        NbtList nbtList = new NbtList();

        NbtCompound nbtCompound1 = new NbtCompound();
        nbtCompound1.putString("structure", this.nameStructure);
        nbtCompound1.putBoolean("visibleLineBox", this.visibleLineBox);
        
        nbtList.add(nbtCompound1);

        nbt.put("setting_pm",nbtList);

        villager.readNbt(nbt);
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
