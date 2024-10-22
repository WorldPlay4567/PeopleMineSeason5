package com.example.mixin;

import com.example.utility.MinimalSidedInventory;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        // Замените этот код на вашу собственную логику
        if (world.isClient()) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            // Ваша кастомная логика здесь
            // Например, открытие вашего собственного интерфейса или выполнение действия:
            player.sendMessage(Text.literal("Вы использовали наковальню"), true);

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            final int[] SLOTS = new int[]{0};
            SimpleGui simpleGui = new SimpleGui(ScreenHandlerType.GENERIC_9X3,serverPlayer,false);


            for(int i = 0; i <= 26; i++) {
                if(i==13) {
                    continue;
                }

                simpleGui.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE));
            }


            simpleGui.setSlotRedirect(13, new Slot(new SimpleInventory(1),0,0,0));

            simpleGui.setSlot(15,new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                    .setCallback(((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT) {

                            if(simpleGui.getSlot(13) == null) {
                                player.sendMessage(Text.literal("Не верная оплата"));
                                simpleGui.close();
                            }else {
                                System.out.print(simpleGui);
                                System.out.print(simpleGui.getSlot(13).getItemStack());

                                ItemStack itemStack = simpleGui.getSlot(13).getItemStack();

                                if(itemStack.getItem() == Items.EMERALD) {

                                    simpleGui.close();

                                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                                    player.incrementStat(Stats.INTERACT_WITH_ANVIL);
                                }
                            }
                        }
                    })));
            cir.setReturnValue(ActionResult.FAIL);
            simpleGui.open();

            simpleGui.setTitle(Text.literal("Потвердите покупку:"));



        }
    }
}

//class Gui extends SimpleGui implements MinimalSidedInventory {
//    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
//    private static final int[] SLOTS = new int[]{0};
//
//
//    public Gui(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots) {
//        super(type, player, manipulatePlayerSlots);
//
//    }
//
//    @Override
//    public DefaultedList<ItemStack> getStacks() {
//        return this.items;
//    }
//
//    @Override
//    public int[] getAvailableSlots(Direction side) {
//        return new int[0];
//    }
//
//    @Override
//    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
//        return false;
//    }
//
//    @Override
//    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
//        return true;
//    }
//
//    @Override
//    public void markDirty() {
//
//    }
//}
//
