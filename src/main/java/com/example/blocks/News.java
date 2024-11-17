package com.example.blocks;

import com.example.PeopleMineSeason5;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import xyz.nucleoid.packettweaker.PacketContext;

public class News extends Block implements PolymerBlock, PolymerTexturedBlock, BlockWithElementHolder {

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public ItemStack model;


    BlockState polymerBlockState;

    public News(Settings copy, BlockModelType type, String modelId) {
        super(copy);
        polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                type,
                PolymerBlockModel.of(Identifier.of(PeopleMineSeason5.MOD_ID, "block/" + modelId)));


//        this.setDefaultState((BlockState)this.getDefaultState().with(FACING, 0));
//        this.model = BaseItemProvider.requestModel(BaseItemProvider.requestModel(), Identifier.of(PeopleMineSeason5.MOD_ID, "block/" + modelId));
    }

//    @Override
//    public BlockState getPolymerBreakEventBlockState(BlockState state, ServerPlayerEntity player) {
//        return this.getDefaultState();
//    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
        gui(serverPlayerEntity);

        return ActionResult.SUCCESS_SERVER;
    }




    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return new Model(initialBlockState);
    }


    private static void gui(ServerPlayerEntity player) {
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6,player,false);

        Text message = Text.literal("")

                .append(Text.translatable("space.-86").styled(style -> style.withFont(Identifier.of("space","default")))
                .append(Text.literal("\u0006").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))))

                .append(Text.translatable("space.-2").styled(style -> style.withFont(Identifier.of("space","default")))
                .append(Text.literal("\u0007").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));
        gui.setTitle(message);
        gui.open();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        var dir = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, dir);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState blockState, PacketContext packetContext) {
        return Blocks.ACACIA_BUTTON.getDefaultState().with(FACING, blockState.get(FACING));
    }

    public final class Model extends BlockModel {
        private final ItemDisplayElement main;

        public Model(BlockState state) {
            this.main = ItemDisplayElementUtil.createSimple(Identifier.of(PeopleMineSeason5.MOD_ID,"block/news"));
            this.main.setDisplaySize(1, 1);
            this.main.setScale(new Vector3f(2));
            this.main.setYaw(state.get(FACING).asRotation());
            this.addElement(this.main);
        }

        public float rotate(Direction direction) {

            if(direction == Direction.NORTH) {
                return 180f;
            }
            if(direction == Direction.SOUTH) {
                return 0f;
            }
            if(direction == Direction.EAST) {
                return -90f;
            }
            if(direction == Direction.WEST) {
                return 90f;
            }
            return 0;
        }

    }
}
