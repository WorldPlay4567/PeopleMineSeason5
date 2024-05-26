package com.example.blocks;

import com.example.gui.PMGui;
import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.block.entity.SculkSpreadManager.MAX_CHARGE;


public class CustomBlockTest extends BlockWithEntity implements PolymerTexturedBlock,BlockEntityProvider, BlockWithElementHolder, PolymerBlock, PolymerClientDecoded {
    private final BlockState polymerBlockState;

    public CustomBlockTest(Settings settings, BlockModelType type, String modelId) {
        super(settings);

        this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                type,
                PolymerBlockModel.of(new Identifier("peoplemineseason5", modelId)));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        PMGui.open((ServerPlayerEntity) player);
        return ActionResult.success(true);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

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
        return this.polymerBlockState;
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
            spawnRandomParticle(random, world, pos);
            spawnRandomParticle(random, world, pos);
            spawnRandomParticle(random, world, pos);
    }

    private void spawnRandomParticle(Random random, World world, BlockPos pos) {

        double offsetX = random.nextTriangular(0.5, 0.25);
        double offsetZ = random.nextTriangular(0.5, 0.25);

        world.addParticle(ParticleTypes.SOUL, pos.getX()+offsetX, pos.getY()+0.75, pos.getZ()+offsetZ, 0, 0.2*random.nextDouble(), 0);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomBlockEntity(CustomBlockList.CUSTOM_BLOCK_ENTITY,pos,state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : validateTicker(type, CustomBlockList.CUSTOM_BLOCK_ENTITY, (CustomBlockEntity::tick));
    }


}
