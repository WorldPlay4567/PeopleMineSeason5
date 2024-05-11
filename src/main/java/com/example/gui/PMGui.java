package com.example.gui;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PMGui {

    public static void open(ServerPlayerEntity serverPlayerEntity) {
        SimpleGui simpleGui = new SimpleGui(ScreenHandlerType.GENERIC_9X3,serverPlayerEntity,false)
        {
                @Override
                public boolean onClick(int index, ClickType type, SlotActionType action, GuiElementInterface element) {
                    if (index == 5) {
                        onClickItem(player, index, type, action, element);
                    }
                    return super.onClick(index,type,action,element);
                }
        };

        simpleGui.setSlot(5, new ItemStack(Items.STONE_SWORD));
        simpleGui.setTitle(Text.literal("Test"));
        simpleGui.open();
    }

    private static void onClickItem(ServerPlayerEntity player, int index, ClickType type, SlotActionType action, GuiElementInterface element) {

        player.sendMessage(Text.literal("ПОЗДРАВЛЯЮ ВЫ НАЖАЛИ НА КНОПКУ").formatted(Formatting.AQUA));


    }


}
