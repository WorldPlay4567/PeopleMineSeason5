//package com.worldplay.blocks;
//
//import com.worldplay.PeopleMineSeason5;
//import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
//
//import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
//
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.util.Identifier;
//
//import static com.worldplay.blocks.BlockInit.CUSTOM_BLOCK;
//
//public class CustomBlockList {
//
////    public static final Block CustomBlockTest = Blocks.register(String.valueOf(Identifier.of(PeopleMineSeason5.MOD_ID, "custom_block_test"));
//
//    public static BlockEntityType<CustomBlockEntity> CUSTOM_BLOCK_ENTITY;
//
//    public static void init() {
//
//        CUSTOM_BLOCK_ENTITY =
//                Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(PeopleMineSeason5.MOD_ID, "custom_block_test"),
//                        FabricBlockEntityTypeBuilder.create((pos, state) -> new CustomBlockEntity(CUSTOM_BLOCK_ENTITY,pos, state),
//                                CUSTOM_BLOCK).build(null));
//
//        PolymerBlockUtils.registerBlockEntity(CUSTOM_BLOCK_ENTITY);
//
//    }
//}
