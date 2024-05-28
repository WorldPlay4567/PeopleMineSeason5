package com.example.items;

import com.example.PeopleMineSeason5;
import com.example.blocks.CustomBlockList;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemsInit {
    public static final Item BLUE_PRINT = Items.register(Identifier.of(PeopleMineSeason5.MOD_ID, "blue_print"),new BluePrint(new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(64)));
    public static final Item TREMBLING_CRYSTAL = Items.register(Identifier.of(PeopleMineSeason5.MOD_ID, "trembling_crystal"),new TremblingCrystal(new Item.Settings().fireproof().rarity(Rarity.EPIC)));

    public static final ItemGroup PEOPLEMINE = Registry.register(Registries.ITEM_GROUP, new Identifier(PeopleMineSeason5.MOD_ID,"peoplemine5"),
            PolymerItemGroupUtils.builder().displayName(Text.of("PeopleMine"))
                    .icon(()->new ItemStack(Items.DIAMOND)).entries(((displayContext, entries) -> {
                        entries.add(ItemsInit.BLUE_PRINT);
                        entries.add(ItemsInit.TREMBLING_CRYSTAL);
                    })).build());

    public static void init() {

    }

}
