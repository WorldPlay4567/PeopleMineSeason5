package com.worldplay.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class CustomTabColor extends PlayerEntity {

    public CustomTabColor(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getPlayerListName", at = @At("TAIL"), cancellable = true)
    private void styledNicknames$replacePlayerListName(CallbackInfoReturnable<Text> cir) {
        try {
            cir.setReturnValue(Team.decorateName(this.getScoreboardTeam(), Text.literal( getDisplayName().getString()).setStyle(Style.EMPTY.withColor(getColorForDimension(getWorld())))));
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Unique
        private Formatting getColorForDimension(World world) {

        if (world.getRegistryKey().equals(World.OVERWORLD)) {
            return Formatting.GREEN;
        } else if (world.getRegistryKey().equals(World.NETHER)) {
            return Formatting.RED;
        } else if (world.getRegistryKey().equals(World.END)) {
            return Formatting.DARK_PURPLE;
        } else {
            return Formatting.BLUE;
        }
    }
}
