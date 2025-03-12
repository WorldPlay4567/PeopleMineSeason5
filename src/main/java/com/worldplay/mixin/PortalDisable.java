package com.worldplay.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortal.class)
public class PortalDisable {
    @Shadow @Final private BlockPos lowerCorner;

    @Shadow @Final private int height;

    @Shadow @Final private Direction.Axis axis;

    @Shadow @Final private Direction negativeDir;

    @Shadow @Final private int width;

    @Inject(method = "createPortal", at = @At("TAIL"))
    private void $createPortal(WorldAccess world, CallbackInfo ci) {
        BlockState blockState = Blocks.AIR.getDefaultState();
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1))
                .forEach(pos -> world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
        ((ServerWorld) world).spawnParticles(ParticleTypes.PORTAL,this.lowerCorner.getX() + 1,this.lowerCorner.getY(),this.lowerCorner.getZ() + 1,20,2,2,2,2);
        ((ServerWorld) world).spawnParticles(ParticleTypes.ANGRY_VILLAGER,this.lowerCorner.getX() + 1,this.lowerCorner.getY(),this.lowerCorner.getZ() + 1,20,2,2,2,2);
        ((ServerWorld) world).playSound(null,this.lowerCorner, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS,1,1);
    }
}
