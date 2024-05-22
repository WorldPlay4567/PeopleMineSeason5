package com.example.blocks;

import eu.pb4.polymer.blocks.api.BlockModelType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.minecraft.registry.Registries.BLOCK;

public class CustomBlockList {

//    public static final Block CustomBlockTest = Blocks.register(String.valueOf(Identifier.of(PeopleMineSeason5.MOD_ID, "custom_block_test"));

    public static Block register(BlockModelType type, String modelId) {
        var id = new Identifier("peoplemineseason5", modelId);
        var block = Registry.register(BLOCK, id,
                new CustomBlockTest(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK), type, modelId));


        Registry.register(Registries.ITEM, id, new CustomBlockItem(new Item.Settings(), block, modelId));
        return block;
    }

    public static final Block CUSTOM_BLOCK_ENTITY = register(BlockModelType.TRANSPARENT_BLOCK, "block/custom_block_test");

    public static void init() {


    }
}
