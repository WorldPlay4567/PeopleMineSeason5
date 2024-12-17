package com.example.blocks;

import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import xyz.nucleoid.packettweaker.PacketContext;

public class AnvilBlockRemake extends AnvilBlock implements PolymerBlock {
    public AnvilBlockRemake(Settings settings) {
        super(settings);
    }

    public static final BooleanProperty BUY = BooleanProperty.of("buy");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
        builder.add(new Property[]{BUY});
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.ANVIL.getDefaultState();
    }
}
