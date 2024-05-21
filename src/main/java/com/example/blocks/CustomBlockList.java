package com.example.blocks;

import com.example.PeopleMineSeason5;
import com.example.items.BluePrint;
import eu.pb4.polymer.blocks.api.BlockModelType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static net.minecraft.registry.Registries.BLOCK;

public class CustomBlockList {

//    public static final Block CustomBlockTest = Blocks.register(String.valueOf(Identifier.of(PeopleMineSeason5.MOD_ID, "custom_block_test"));

    public static void register(BlockModelType type, String modelId) {
        var id = new Identifier("peoplemineseason5", modelId);
        var block = Registry.register(BLOCK, id,
                new CustomBlockTest(FabricBlockSettings.copy(Blocks.DIAMOND_BLOCK), type, modelId));

    }




    public static void init() {
        register(BlockModelType.TRANSPARENT_BLOCK, "block/custom_block_test");
    }
}
