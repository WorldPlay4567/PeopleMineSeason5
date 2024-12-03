package com.example.mixin;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin{

//    @Unique
//    private static final BooleanProperty BUY = BooleanProperty.of("buy");
//
//    public AnvilBlockMixin(Settings settings) {
//        super(settings);
//    }
//
//    @Inject(at = @At("TAIL"), method = "appendProperties(Lnet/minecraft/state/StateManager$Builder;)V")
//    public void appendProperties(StateManager.Builder<Block, BlockState> manager, CallbackInfo ci)
//    {
//        manager.add(new Property[]{BUY});
//    }
//    @Inject(method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", at = @At(value = "TAIL"))
//    private void tailConstructor(AbstractBlock.Settings settings, CallbackInfo ci) {
//        this.setDefaultState(this.getDefaultState().with(BUY, false));
//    }












//
//    public AnvilBlockMixin(Settings settings) {
//        super(settings);
//        this.setDefaultState(this.getStateManager().getDefaultState().with(BUY,false));
//    }
//
//    @Override
//    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(BUY);
//    }

//    @Override
//    public BlockState getDefaultState() {
//        return this.stateManager.getDefaultState().with(BUY, false);
//
//    }


    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        // Замените этот код на вашу собственную логику
        if (world.isClient()) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            player.sendMessage(Text.literal("Вы использовали наковальню"), true);

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            SimpleInventory simpleInventory = new SimpleInventory(1);
            final int[] SLOTS = new int[]{0};
            SimpleGui simpleGui = new SimpleGui(ScreenHandlerType.GENERIC_9X3,serverPlayer,false){
                @Override
                public void onClose() {
                    super.onClose();

                    ItemEntity item = new ItemEntity(world,pos.getX(),pos.getY() + 1,pos.getZ(),simpleInventory.getStack(0));
                    
                    world.spawnEntity(item);
                }
            };


            for(int i = 0; i <= 26; i++) {
                if(i==13) {
                    continue;
                }

                simpleGui.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                        .setName(Text.literal("")));
            }


            simpleGui.setSlotRedirect(13, new Slot(simpleInventory,0,0,0));

            simpleGui.setSlot(15,new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                    .setName(Text.literal("Подтвердить покупку").styled(style -> style.withColor(Formatting.GREEN)))
                    .setCallback(((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT) {

                            if(simpleGui.getSlotRedirect(13) == null) {
                                player.sendMessage(Text.literal("Не верная оплата"),false);
                                simpleGui.close();
                            }else {
                                System.out.print(simpleGui);
                                System.out.print(simpleGui.getSlotRedirect(13).getStack());

                                ItemStack itemStack = simpleGui.getSlotRedirect(13).getStack();

                                if(itemStack.getItem() == Items.EMERALD && itemStack.getCount() == 32) {
                                    simpleInventory.setStack(0,new ItemStack(Items.AIR));
                                    simpleGui.close();

                                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                                    player.incrementStat(Stats.INTERACT_WITH_ANVIL);
                                }
                            }
                        }
                    })));
            cir.setReturnValue(ActionResult.FAIL);
            simpleGui.open();

            simpleGui.setTitle(Text.literal("Цена: ").append(Text.literal("32 Изумруда").styled(style -> style.withColor(Formatting.DARK_GREEN))));



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
