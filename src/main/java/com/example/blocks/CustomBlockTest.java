package com.example.blocks;

import com.example.PeopleMineSeason5;
import com.example.items.ItemsInit;
import com.ibm.icu.impl.ICUService;
import com.mojang.serialization.MapCodec;
import eu.pb4.factorytools.api.block.FactoryBlock;
import eu.pb4.factorytools.api.resourcepack.BaseItemProvider;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.ItemDisplayElementUtil;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockAwareAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.TextDisplayElement;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import static eu.pb4.polymer.common.impl.CommonImplUtils.id;
import static net.minecraft.block.RedstoneOreBlock.LIT;


public class CustomBlockTest extends BlockWithEntity implements FactoryBlock,BlockEntityProvider, BlockWithElementHolder, PolymerBlock, PolymerClientDecoded {




    public CustomBlockTest(Settings settings, BlockModelType type, String modelId) {
        super(settings);
    }

//    @Override
//    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//        Model.holder.destroy();
//        return state;
//    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CustomBlockEntity be) {
            be.createGui((ServerPlayerEntity) player);
        }
        return ActionResult.success(true);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    }

    @Override
    public @Nullable ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        ElementHolder model = new ElementHolder();
        var element = new TextDisplayElement();
        element.setText(Text.literal("Hello world"));
        element.setOffset(new Vec3d(0, 0.7, 0));
        model.addElement(element);
        return model;
    }

    @Override
    public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
        return true;
    }
//    @Override
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        double d = (double)pos.getX() + 0.55 - (double)(random.nextFloat() * 0.1F);
//        double e = (double)pos.getY() + 0.55 - (double)(random.nextFloat() * 0.1F);
//        double f = (double)pos.getZ() + 0.55 - (double)(random.nextFloat() * 0.1F);
//        if (random.nextInt(5) == 0) {
//            ServerWorld serverWorld = world.getServer().getOverworld();
//            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, d, e, f,1, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, 0.01);
//        }
//        System.out.println(d);
//        System.out.println(e);
//        System.out.println(f);
//        super.scheduledTick(state, world, pos, random);
//    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.BARRIER.getDefaultState();

    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomBlockEntity(CustomBlockList.CUSTOM_BLOCK_ENTITY,pos,state);
    }

    @Override
    public ElementHolder createMovingElementHolder(ServerWorld world, BlockPos blockPos, BlockState blockState, @Nullable ElementHolder oldStaticElementHolder) {
        return oldStaticElementHolder;
    }

    @Override
    public ElementHolder createStaticElementHolder(ServerWorld world, BlockPos blockPos, BlockState blockState, @Nullable ElementHolder oldMovingElementHolder) {
        return oldMovingElementHolder;
    }


    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : validateTicker(type, CustomBlockList.CUSTOM_BLOCK_ENTITY, (CustomBlockEntity::tick));
    }}

//    public static final class Model extends BlockModel {
//        public static final ItemStack UNLIT = BaseItemProvider.requestModel(id("block/custom_block_test"));
//        public static  final ItemStack TEST = new ItemStack(ItemsInit.CUSTOM_BLOCK_TEST);
//        private final ItemDisplayElement main;
//
//        static ElementHolder holder = new ElementHolder();
//
//
//
//
////        static {
////            TEST.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(PolymerResourcePackUtils.requestModel(Items.STONE, new Identifier(PeopleMineSeason5.MOD_ID, "block/custom_block_test")).value()));
////        }
//
//        private Model(BlockState state, BlockPos pos, World world) {
//            super();
//
//            this.main = createSimple(TEST);
//            this.main.setScale(new Vector3f(2));
//
//            var offset = new Vec3d(
//                   0,
//                    0,
//                     0
//            );
//            this.main.setOffset(offset);
//            this.main.setDisplayWidth(3);
//
//            new ChunkAttachment(holder, world.getWorldChunk(pos), pos.toCenterPos(), true);
//            holder.addElement(main);
//        }
//
//        public static ItemDisplayElement createSimple(ItemStack model) {
//            var element = createSimple();
//            element.setItem(model);
//            return element;
//        }
//
//
//        public static ItemDisplayElement createSimple() {
//            var element = new ItemDisplayElement();
//            element.setDisplaySize(2, 2);
//            element.setViewRange(0.8f);
//            element.setModelTransformation(ModelTransformationMode.FIXED);
//            element.setTeleportDuration(1);
//            element.setInvisible(true);
//
//            return element;
//        }
//        @Override
//        public void notifyUpdate(HolderAttachment.UpdateType updateType) {
//            if (updateType == BlockAwareAttachment.BLOCK_STATE_UPDATE) {
//                var state = this.blockState();
//                this.main.setBrightness(state.get(LIT) ? new Brightness(15, 15) : null);
//                this.main.setItem(state.get(LIT) ? ItemDisplayElementUtil.getModel(state.getBlock().asItem()) : UNLIT);
//
//                this.tick();
//            }
//        }
//
//    }
//
//}
