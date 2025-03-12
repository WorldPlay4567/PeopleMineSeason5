package com.worldplay.blocks;

import com.worldplay.utility.MinimalSidedInventory;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
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

public class CustomBlockEntity extends BlockEntity implements MinimalSidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(25, ItemStack.EMPTY);
    private final int[] SLOTS = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
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

        ServerWorld serverWorld = (ServerWorld) world;
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
        return true;
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


            this.open();

        }



    }

}
