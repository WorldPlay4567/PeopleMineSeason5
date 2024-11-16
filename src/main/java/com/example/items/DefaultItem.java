
package com.example.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;


public class DefaultItem extends Item implements PolymerItem {

    private final Identifier polymerModel;
    Item item;

    public DefaultItem(Settings settings, Item item, String path) {
        super(settings);
        this.item = item;
        this.polymerModel = PolymerResourcePackUtils.getBridgedModelId(Identifier.of("blocktest", path));
//        final PolymerModelData modelData = PolymerResourcePackUtils.requestModel(item, new Identifier(PeopleMineSeason5.MOD_ID, "item/" + path));

    }


    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return item;
    }
    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return this.polymerModel;
    }
}



