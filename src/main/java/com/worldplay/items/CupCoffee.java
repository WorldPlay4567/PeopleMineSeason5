package com.worldplay.items;

import com.worldplay.blocks.DefaultBlock;
import eu.pb4.polymer.core.api.block.PolymerHeadBlock;
import eu.pb4.polymer.core.api.item.PolymerHeadBlockItem;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;

public class CupCoffee extends Item implements PolymerItem {

    PacketContext context;

    public CupCoffee(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
//        if(EntityType.PLAYER == entity.getType()) {
//            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
////            System.out.println(serverPlayer);
//
//            stack.set(DataComponentTypes.ITEM_MODEL, Identifier.of("minecraft","mosin_rifle"));
//            ProfileComponent profileComponent1 = new ProfileComponent(serverPlayer.getGameProfile());
//            stack.set(DataComponentTypes.PROFILE, profileComponent1);
//            this.getPolymerItem(stack, context);
//        }
    }

    @Override
    public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context) {
        this.context = context;
        ProfileComponent profileComponent1 = new ProfileComponent(Objects.requireNonNull(context.getPlayer()).getGameProfile());
        out.set(DataComponentTypes.PROFILE, profileComponent1);

        out.set(DataComponentTypes.ITEM_MODEL, Identifier.of("minecraft","cup_coffee"));
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.PLAYER_HEAD;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return null;
    }
}
