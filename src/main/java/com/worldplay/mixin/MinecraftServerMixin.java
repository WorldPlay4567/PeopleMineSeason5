package com.worldplay.mixin;


import com.worldplay.utility.crafting.BluePrintList;
import com.worldplay.utility.villager.VillagerShopList;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    MinecraftServer minecraftServer = (MinecraftServer) (Object) this;

    @Inject(at = @At("HEAD"), method = "runAutosave")
    private void runAutosave(CallbackInfo ci) {
        VillagerShopList.save();
        BluePrintList.save(minecraftServer);
    }
}
