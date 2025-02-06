package com.worldplay.blocks;

import com.worldplay.PeopleMineSeason5;
import com.worldplay.items.CupCoffee;
import eu.pb4.polymer.blocks.api.BlockModelType;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


import static net.minecraft.registry.Registries.BLOCK;

public class BlockInit {

    public static final Block CUSTOM_BLOCK = registerOld(BlockModelType.TRANSPARENT_BLOCK, "custom_block_test");



    public static final Block NEWS = register("news", new News(Block.Settings.copy(Blocks.OAK_PLANKS)
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PeopleMineSeason5.MOD_ID,"news")))
            .strength(1), BlockModelType.TRANSPARENT_BLOCK, "news"));

    public static final Block VOID_END = register("void_end" , new DefaultBlock(Block.Settings.copy(Blocks.DIAMOND_BLOCK)
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PeopleMineSeason5.MOD_ID,"void_end")))
            .luminance((state) -> {
        return 15;}), BlockModelType.TRANSPARENT_BLOCK,"void_end"));

    public static final Block VOID_FIRE = register("void_fire" , new DefaultBlock(Block.Settings.copy(Blocks.DIAMOND_BLOCK)
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PeopleMineSeason5.MOD_ID,"void_fire")))
            ,BlockModelType.TRANSPARENT_BLOCK,"void_fire"));

    public static final Block CAT = register("cat" , new DefaultBlock(Block.Settings
            .copy(Blocks.DIAMOND_BLOCK)
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PeopleMineSeason5.MOD_ID,"cat"))),
            BlockModelType.TRANSPARENT_BLOCK,"cat"));

//    public static final Item CUP_COFFEE = Items.register(Blocks.PLAYER_HEAD, (block, settings) -> {
//        return new CupCoffee(block, Blocks.PLAYER_WALL_HEAD, settings);
//    });


//    public static final Block ANVIL_BLOCK = Registry.register(BLOCK, Identifier.of("minecraft","anvil"),new AnvilBlockRemake(Block.Settings.copy(Blocks.ANVIL).registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of("minecraft","anvil")))));

    public static Block registerOld(BlockModelType type, String modelId) {
        var id = Identifier.of("peoplemineseason5", modelId);
        var block = Registry.register(BLOCK, id,
                new CustomBlockTest(Block.Settings.copy(Blocks.DIAMOND_BLOCK)
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(PeopleMineSeason5.MOD_ID,modelId)))
                        , type,"block/" + modelId));

        Registry.register(Registries.ITEM, id, new DefaultItemBlock(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeopleMineSeason5.MOD_ID,modelId))), block, modelId));

        return block;
    }

    public static <T extends Block> T register(String path, T item) {
        var id = Identifier.of(PeopleMineSeason5.MOD_ID, path);

        var block = Registry.register(Registries.BLOCK, id, item);

        Registry.register(Registries.ITEM, id, new DefaultItemBlock(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PeopleMineSeason5.MOD_ID,path)))
                ,block, path));
        return block;
    }



    public static void init( ){}

}
