package com.worldplay.mixin;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class Test {

    private EntityAttachment attachment = null;

    @Inject(method = "tick", at = @At("HEAD"))
    void init(CallbackInfo ci){
        if(this.attachment != null) return;

        var thisPlayer = (ServerPlayerEntity) (Object) this;

        var block = new BlockDisplayElement(Blocks.DIAMOND_BLOCK.getDefaultState());
        block.setOffset(new Vec3d(-0.5, 0, -0.5));
        block.setInterpolationDuration(20);
        block.startInterpolation();


        VirtualEntityUtils.addVirtualPassenger(thisPlayer, block.getEntityId());

        var holder = new ElementHolder();
        holder.addElement(block);


        this.attachment = new EntityAttachment(holder, thisPlayer, true);
        this.attachment.startWatching(thisPlayer);
        this.attachment.holder().notifyUpdate();

    }
}