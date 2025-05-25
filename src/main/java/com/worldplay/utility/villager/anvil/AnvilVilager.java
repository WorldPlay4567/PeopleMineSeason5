package com.worldplay.utility.villager.anvil;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.SlotHolder;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.BaseSlotGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SimpleGuiBuilder;
import eu.pb4.sgui.virtual.SguiScreenHandlerFactory;
import eu.pb4.sgui.virtual.inventory.VirtualScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AnvilVilager extends SimpleGui {

    public AnvilVilager(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X6, player, true);
        openScreen();
    }

    public void openScreen(){

        setSlot(1, GuiElementBuilder.from(Items.GREEN_STAINED_GLASS_PANE.getDefaultStack())
                .setItemName(Text.of("Открыть магазин"))
                .setCallback(((index, clickType, actionType) -> {
                    if (clickType == ClickType.MOUSE_LEFT) {
                        close();
                        new VillagerShop(player);
                    }
                })));

        setSlot(2, GuiElementBuilder.from(Items.GREEN_STAINED_GLASS_PANE.getDefaultStack())
                .setItemName(Text.of("Открыть наковальню"))
                .setCallback(((index, clickType, actionType) -> {
                    if (clickType == ClickType.MOUSE_LEFT) {
                        close();
                        new Anvil(player);
                    }
                })));

        this.open();
    }

    private static class Anvil{

        public Anvil(ServerPlayerEntity player) {
            BlockPos blockPos = new BlockPos(0,-63,0);
            player.openHandledScreen( getOrCreate(player.getServerWorld()).createScreenHandlerFactory(player.getWorld(),blockPos ));
            player.incrementStat(Stats.INTERACT_WITH_ANVIL);
            ((ICustomScreenState) player).setCustomScreenOpen(true);
        }

        //Уже мои коментарий = Это костиль, чтобы не ебаться с кастомным окном наковальни
        public BlockState getOrCreate(ServerWorld serverWorld) {
            BlockPos blockPos = new BlockPos(0,-63,0);
            BlockState blockState = serverWorld.getBlockState(blockPos);

            if(blockState.getBlock() == Blocks.ANVIL){
                return blockState;
            }
            else {
                serverWorld.setBlockState(blockPos, Blocks.ANVIL.getDefaultState());
                return serverWorld.getBlockState(blockPos);
            }
        }
    }

    private static class VillagerShop extends SimpleGui{

        public VillagerShop(ServerPlayerEntity player) {
            super(ScreenHandlerType.MERCHANT, player, false);
            this.open();
        }
    }
}
