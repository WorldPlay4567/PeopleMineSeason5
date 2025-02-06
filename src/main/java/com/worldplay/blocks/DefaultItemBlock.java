package com.worldplay.blocks;

import com.worldplay.PeopleMineSeason5;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class DefaultItemBlock extends BlockItem implements PolymerItem {

    private final Identifier polymerModel;

    public DefaultItemBlock(Item.Settings settings, Block block, String modelId) {
        super(block, settings);

        this.polymerModel = Identifier.of(PeopleMineSeason5.MOD_ID, "block/" + modelId);
        System.out.print(polymerModel + " | ");

    }
    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return polymerModel;
    }

//    @Override
//    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player, PacketContext packetContext) {
//        return this.polymerModel;
//    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.BARRIER;
    }
}
