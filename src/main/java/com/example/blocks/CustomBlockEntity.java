package com.example.blocks;

import com.example.utility.MinimalSidedInventory;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class CustomBlockEntity extends BlockEntity implements MinimalSidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(25, ItemStack.EMPTY);
    private static final int[] SLOTS = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
    public int time = 0;


    public CustomBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        Inventories.writeNbt(nbt, this.items, lookup);
        nbt.putInt("time", time);
        super.writeNbt(nbt, lookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        Inventories.readNbt(nbt, items, lookup);
        this.time = nbt.getInt("time");
        super.readNbt(nbt, lookup);
    }

    public void createGui(ServerPlayerEntity player) {
        new Gui(player);
    }
    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {

        var self = (CustomBlockEntity) t;

        self.time ++;
        self.markDirty();

        ServerWorld serverWorld = world.getServer().getOverworld();
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY()+ 2, pos.getZ()+ 0.5, 5, 0, 0.1,0,0.01);
    }

    @Override
    public DefaultedList<ItemStack> getStacks() {
        return this.items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }


    private class Gui extends SimpleGui {

        public Gui(ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X3,player,false);
            //this.setSlot(5, new ItemStack(Items.STONE_SWORD));
            this.setTitle(Text.literal("Test"));

            this.setSlotRedirect(0, new Slot(CustomBlockEntity.this,0 ,0,0) );
            this.setSlotRedirect(1, new Slot(CustomBlockEntity.this,1 ,1,0) );
            this.setSlotRedirect(2, new Slot(CustomBlockEntity.this,2 ,2,0) );
            this.setSlotRedirect(3, new Slot(CustomBlockEntity.this,3 ,3,0) );
            this.setSlotRedirect(4, new Slot(CustomBlockEntity.this,4 ,4,0) );
            this.setSlotRedirect(5, new Slot(CustomBlockEntity.this,5 ,5,0) );
            this.setSlotRedirect(6, new Slot(CustomBlockEntity.this,6 ,6,0) );
            this.setSlotRedirect(7, new Slot(CustomBlockEntity.this,7 ,7,0) );
            this.setSlotRedirect(8, new Slot(CustomBlockEntity.this,8 ,8,0) );
            this.setSlotRedirect(9, new Slot(CustomBlockEntity.this,9 ,9,0) );
            this.setSlotRedirect(10, new Slot(CustomBlockEntity.this,10 ,10,0) );
            this.setSlotRedirect(11, new Slot(CustomBlockEntity.this,11 ,11,0) );
            this.setSlotRedirect(12, new Slot(CustomBlockEntity.this,12 ,12,0) );
            this.setSlotRedirect(13, new Slot(CustomBlockEntity.this,13 ,13,0) );
            this.setSlotRedirect(14, new Slot(CustomBlockEntity.this,14 ,14,0) );
            this.setSlotRedirect(15, new Slot(CustomBlockEntity.this,15 ,15,0) );
            this.setSlotRedirect(16, new Slot(CustomBlockEntity.this,16 ,16,0) );
            this.setSlotRedirect(17, new Slot(CustomBlockEntity.this,17 ,17,0) );
            this.setSlotRedirect(18, new Slot(CustomBlockEntity.this,18 ,18,0) );
            this.setSlotRedirect(19, new Slot(CustomBlockEntity.this,19 ,19,0) );
            this.setSlotRedirect(20, new Slot(CustomBlockEntity.this,20 ,20,0) );
            this.setSlotRedirect(21, new Slot(CustomBlockEntity.this,21 ,21,0) );
            this.setSlotRedirect(22, new Slot(CustomBlockEntity.this,22 ,22,0) );
            this.setSlotRedirect(23, new Slot(CustomBlockEntity.this,23 ,23,0) );
            this.setSlotRedirect(24, new Slot(CustomBlockEntity.this,24 ,24,0) );




            this.open();

        }



    }

}
