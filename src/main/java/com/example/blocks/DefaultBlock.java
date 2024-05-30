package com.example.blocks;

import com.example.PeopleMineSeason5;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public class DefaultBlock extends Block implements PolymerBlock, PolymerTexturedBlock {
    private final BlockState polymerBlockState;
    public DefaultBlock(Settings settings, BlockModelType type, String modelId) {
        super(settings);
        polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                type,
                PolymerBlockModel.of(new Identifier(PeopleMineSeason5.MOD_ID, "block/" + modelId)));
    }


    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return polymerBlockState;
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) { return true; }

}
