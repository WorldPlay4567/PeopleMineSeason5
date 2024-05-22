package com.example.blocks;

import com.example.gui.PMGui;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class CustomBlockTest extends Block implements PolymerTexturedBlock {
    private final BlockState polymerBlockState;

    SpawnBlock spawnBlock = new SpawnBlock();

    Random random = Random.create();
    public CustomBlockTest(Settings settings, BlockModelType type, String modelId) {
        super(settings);

        this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(
                type,
                PolymerBlockModel.of(new Identifier("peoplemineseason5", modelId)));


    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        spawnBlock.destroy();
        return state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        PMGui.open((ServerPlayerEntity) player);
        return ActionResult.success(true);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        spawnBlock.onPlace(world,pos);
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

    public static final class SpawnBlock {
        private static ElementHolder holder;

        public SpawnBlock (){

        }

        public static void onPlace(World world, BlockPos pos){
            holder = new ElementHolder(){
                @Override
                public void tick() {
                    super.tick();
                }
            };

            var blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
            new ChunkAttachment(holder, world.getWorldChunk(blockPos), pos.toCenterPos(), true);
            BlockDisplayElement blockElement = new BlockDisplayElement(Blocks.DIAMOND_BLOCK.getDefaultState());
            holder.addElement(blockElement);
            System.out.println("MiddleGenerator created" + pos);
        }

        public static void destroy() {
            holder.destroy();
        }




    }

}