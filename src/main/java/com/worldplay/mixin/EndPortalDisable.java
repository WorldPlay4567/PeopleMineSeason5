package com.worldplay.mixin;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EndPortalDisable {
    @Unique
    private static final GameRules.Key<GameRules.BooleanRule> PORTAL_DISABLE = GameRuleRegistry.register("endPortalDisable", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));


    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void mixin$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ServerWorld world = (ServerWorld) context.getWorld();
        BlockPos pos = context.getBlockPos();

        if(world.getGameRules().getBoolean(PORTAL_DISABLE)) {
            if(world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME){
                world.playSound(null,pos, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS,1,1);
                world.spawnParticles(ParticleTypes.ANGRY_VILLAGER,pos.getX() + 0.5,pos.getY(),pos.getZ() + 0.5,50,0.2,0.2,0.2,2);
                context.getPlayer().sendMessage(Text.literal("Мир не даёт вставить глаз в портал").setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }
}
