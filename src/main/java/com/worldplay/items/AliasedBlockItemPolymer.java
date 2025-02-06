package com.worldplay.items;

import com.worldplay.PeopleMineSeason5;
import com.worldplay.api.RPFast;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class AliasedBlockItemPolymer extends PolymerBlockItem {

    public Identifier polymerModel;
    public AliasedBlockItemPolymer(Block block, Settings settings, Item virtualItem, String modelId) {
        super(block, settings, virtualItem);
        this.polymerModel = RPFast.getItemModel(modelId);
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return this.polymerModel;
    }

}
