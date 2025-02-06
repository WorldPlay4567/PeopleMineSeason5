
package com.worldplay.items;

import com.worldplay.PeopleMineSeason5;
import com.worldplay.api.RPFast;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;


public class DefaultItem extends Item implements PolymerItem {
    public Identifier polymerModel;
    Item item;

    public DefaultItem(Settings settings, Item item, String path) {
        super(settings);
        this.item = item;
        polymerModel = RPFast.getItemModel(path);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return item;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return polymerModel;
    }
}



