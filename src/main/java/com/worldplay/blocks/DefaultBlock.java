package com.worldplay.blocks;

import com.worldplay.PeopleMineSeason5;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class DefaultBlock extends Block implements PolymerBlock, PolymerTexturedBlock {
    private final BlockState polymerBlockState;
    public DefaultBlock(Settings settings, BlockModelType type, String modelId) {
        super(settings);
        polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                type,
                PolymerBlockModel.of(Identifier.of(PeopleMineSeason5.MOD_ID, "block/" + modelId)));
    }

    public DefaultBlock(Settings aNull) {
        super(aNull);
        polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                null,
                PolymerBlockModel.of(Identifier.of(PeopleMineSeason5.MOD_ID, "block/" + null)));
    }


    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return polymerBlockState;
    }

    @Override
    public boolean forceLightUpdates(BlockState blockState) { return true; }

}
