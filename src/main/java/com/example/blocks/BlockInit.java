package com.example.blocks;

import com.example.PeopleMineSeason5;
import com.example.items.ItemsInit;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;


import static net.minecraft.registry.Registries.BLOCK;

public class BlockInit {

    public static final Block CUSTOM_BLOCK = registerOld(BlockModelType.TRANSPARENT_BLOCK, "custom_block_test");

    public static final Block NEWS = register("news", new News(FabricBlockSettings.copy(Blocks.OAK_PLANKS).strength(1), BlockModelType.TRANSPARENT_BLOCK, "news"));

    public static final Block VOID_END = register("void_end" , new DefaultBlock(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK).luminance((state) -> {
        return 15;}), BlockModelType.TRANSPARENT_BLOCK,"void_end"));
    public static final Block VOID_FIRE = register("void_fire" , new DefaultBlock(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK), BlockModelType.TRANSPARENT_BLOCK,"void_fire"));

    public static final Block CAT = register("cat" , new DefaultBlock(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK), BlockModelType.TRANSPARENT_BLOCK,"cat"));


    public static Block registerOld(BlockModelType type, String modelId) {
        var id = new Identifier("peoplemineseason5", modelId);
        var block = Registry.register(BLOCK, id,
                new CustomBlockTest(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK), type,"block/" + modelId));

        Registry.register(Registries.ITEM, id, new DefaultItem(new Item.Settings(), block, "block/" + modelId));

        return block;
    }

    public static <T extends Block> T register(String path, T item) {
        var id = new Identifier(PeopleMineSeason5.MOD_ID, path);

        var block = Registry.register(Registries.BLOCK, id, item);

        Registry.register(Registries.ITEM, id, new DefaultItem(new Item.Settings(), block, "block/" + path));
        return block;
    }



    public static void init( ){}

}
