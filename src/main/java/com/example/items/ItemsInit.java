package com.example.items;

import com.example.PeopleMineSeason5;
import com.example.blocks.BlockInit;

import com.example.blocks.DefaultItemBlock;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemsInit {
    public static final Item BLUE_PRINT = Items.register(Identifier.of(PeopleMineSeason5.MOD_ID, "blue_print"),new BluePrint(new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(64)));
    public static final Item TREMBLING_CRYSTAL = Items.register(Identifier.of(PeopleMineSeason5.MOD_ID, "trembling_crystal"),new TremblingCrystal(new Item.Settings().fireproof().rarity(Rarity.EPIC)));

    public static final Item CARROTS_SEEDS_ITEM = Items.register(Identifier.of(PeopleMineSeason5.MOD_ID, "carrots_seeds"),new DefaultItem(new Item.Settings().fireproof().rarity(Rarity.EPIC), Items.CARROT, "carrots_seeds"));
    public static final BlockItem CARROTS_SEEDS = new AliasedBlockItemPolymer(Blocks.CARROTS,new Item.Settings().component(DataComponentTypes.CUSTOM_NAME,Text.translatable("item.peoplemineseason5.carrots_seeds").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN))),CARROTS_SEEDS_ITEM);



    public static final Item CUSTOM_BLOCK_TEST = register(BlockInit.CUSTOM_BLOCK);
    public static final ItemGroup PEOPLEMINE = Registry.register(Registries.ITEM_GROUP, new Identifier(PeopleMineSeason5.MOD_ID,"peoplemine5"),
            PolymerItemGroupUtils.builder().displayName(Text.of("PeopleMine"))
                    .icon(()->new ItemStack(Items.DIAMOND)).entries(((displayContext, entries) -> {
                        entries.add(ItemsInit.BLUE_PRINT);
                        entries.add(ItemsInit.TREMBLING_CRYSTAL);

                    })).build());





    public static void init() {
        Registry.register(Registries.ITEM, new Identifier("peoplemineseason5", "carrots_seeds"), CARROTS_SEEDS);
    }

    public static Item register(Block block) {
        var id = Registries.BLOCK.getId(block);
        var path = id.getPath();

        return Registry.register(Registries.ITEM, id, new DefaultItemBlock(new Item.Settings(), block, "block/" + path));
    }

}
