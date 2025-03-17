package com.worldplay.utility.crafting;

import com.worldplay.items.BluePrint;
import com.worldplay.items.ItemsInit;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class VillagerBluePrint extends SimpleGui {
    SimpleInventory simpleInventory = new SimpleInventory(1);
    boolean isItemList = false;
    BluePrintRequirement bluePrintCorrect;

    public VillagerBluePrint(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);

        screenList();
        open();
    }

    @Override
    public void onClose() {
        super.onClose();
            ItemEntity item = new ItemEntity(player.getWorld(),player.getX(),player.getY() + 1,player.getZ(),simpleInventory.getStack(0));
            player.getWorld().spawnEntity(item);

    }

    @Override
    public void onTick() {
        super.onTick();

        if(isItemList) {

            if(!bluePrintCorrect.is_allComplete()) {
                Identifier identifier = Registries.ITEM.getId(simpleInventory.getStack(0).getItem());

                for(BluePrintRequirement.Items items : bluePrintCorrect.listRequirement) {

                    if(items.item.toString().equals(identifier.toString())) {
                        player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(), SoundCategory.PLAYERS,1,1);

                        items.count -= simpleInventory.getStack(0).getCount();

                        if(items.count <= 0) {
                            items.is_compile = true;
                        }

                        simpleInventory.setStack(0, Items.AIR.getDefaultStack());
                        itemList(bluePrintCorrect);
                    }
                }
            } else {
                if(!bluePrintCorrect.is_complete) {
                    ItemStack itemStack = new ItemStack(ItemsInit.BLUE_PRINT, 1);
                    BluePrint.setStructure(itemStack, bluePrintCorrect.nameBluePrint);

                    simpleInventory.setStack(0, itemStack);
                    bluePrintCorrect.is_complete = true;
                }
            }
        }

    }

    public void screenList() {
        Text message = Text.literal("")

                .append(Text.translatable("space.-49").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal("\u0008").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));

        this.setTitle(message);

        
        for(int i = 0; i < BluePrintList.BLUE_PRINT.size(); i++) {

            BluePrintRequirement blue_print = BluePrintList.BLUE_PRINT.get(i);

            setSlot(18 + i, new GuiElementBuilder(ItemsInit.BLUE_PRINT)
                    .setName(Text.literal("Цель: ")
                            .styled(style -> {return style.withColor(Formatting.GOLD);})
                            .append(Text.translatable(blue_print.nameBluePrint))
                    )
                    .addLoreLine(Text.literal("Нажмите чтобы выбрать цель"))
                    .setCallback(((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT) {
                            player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.PLAYERS,1,1);
                            itemList(blue_print);
                        }
                    })));
        }
    }

    public void itemList(BluePrintRequirement bluePrint) {
        this.isItemList = true;
        this.bluePrintCorrect = bluePrint;

        for(int i = 0; i< 26 ; i++) {
            clearSlot(i);
        }

        Text message = Text.literal("")
                .append(Text.translatable("space.-49").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal("\u0009").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));
        setTitle(message);

        this.setSlotRedirect(13,new Slot(simpleInventory,0,0,0));

        for(int i = 0; i < bluePrint.listRequirement.size(); i++) {
            if(i >= 8) {
                break;
            }

            BluePrintRequirement.Items items = bluePrint.getListRequirement().get(i);

            if(!items.is_compile) {
                this.setSlot(18+i, new GuiElementBuilder(Registries.ITEM.get(items.item))
                        .setCount(items.count)
                        .setName(Text.literal("Нужнее предметы: ").styled(style -> {
                                    return style.withColor(Formatting.RED);
                                }).append(Registries.ITEM.get(items.item).getName())
                                .append(Text.literal(" " + items.count))));
            }
            else {
                this.setSlot(18+i, new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                        .glow(true)
                        .setCount(1)
                        .setName(Text.literal("Выполнено ").styled(style -> {
                            return style.withColor(Formatting.GREEN);
                        })));
            }
        }
    }
}
