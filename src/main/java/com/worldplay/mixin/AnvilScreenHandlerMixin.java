package com.worldplay.mixin;

import com.worldplay.utility.villager.anvil.ICustomScreenState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ForgingScreenHandler.class)
public abstract class AnvilScreenHandlerMixin  {

    @Inject(method = "canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("RETURN"), cancellable = true)
    public void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ICustomScreenState screenState = (ICustomScreenState) player;

        if (screenState.isCustomScreenOpen()) {
            System.out.println("Open, state change");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onClosed", at = @At("HEAD"))
    public void onClosed(PlayerEntity player, CallbackInfo ci) {
        System.out.println("Close, state change");
        ICustomScreenState screenState = (ICustomScreenState) player;
        screenState.setCustomScreenOpen(false);
    }
}
