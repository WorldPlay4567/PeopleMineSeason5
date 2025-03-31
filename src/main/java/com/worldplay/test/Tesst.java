package com.worldplay.test;

import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;


import java.util.*;

public class Tesst {

    public static void init(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//            dark(player);
//            start(player);
        }
    }

    public static void dark(ServerPlayerEntity player) {
        if(player.getPos().y < 0) {
            if(player.interactionManager.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            StatusEffectInstance s = new StatusEffectInstance(StatusEffects.BLINDNESS,40,1,true,false);
            player.addStatusEffect(s);
        }
    }

    public static void start( ServerPlayerEntity player) {
        var advancementId =
                Objects.requireNonNull(Identifier.of("mymod", "notification_example"));

// Build the advancement itself
        var advancement =
                Advancement.Builder.create()
                        .display(Items.AXOLOTL_BUCKET, Text.literal("Hiiii test test"),
                                Text.literal("This is a test lol"), null, AdvancementFrame.GOAL,
                                true, true, true)
                        .criterion("custom_criterion",
                                Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                        .build(advancementId);

// Set up the progress for the advancement we're going to send
        var progress = new AdvancementProgress();
        progress.init(AdvancementRequirements.allOf(Set.of("custom_criterion")));
        progress.obtain("custom_criterion");

// Send the advancement, along with the player's "progress" on the advancement
// to the client
        player.networkHandler.sendPacket(
                new AdvancementUpdateS2CPacket(false, Set.of(advancement),
                        Collections.emptySet(), Map.of(advancementId, progress),true));

// Immediately remove the advancement so it doesn't appear in the "Advancements"
// screen (optional)
        player.networkHandler.sendPacket(new AdvancementUpdateS2CPacket(false,
                Collections.emptySet(), Set.of(advancementId), Collections.emptyMap(),true));
    }
//    private void updatePlayerTabName(ServerPlayerEntity player) {
//        Formatting color = getColorForDimension(player);
//        Text coloredName = Text.literal(String.valueOf(player.getPlayerListName())).formatted(color);
//        PlayerListS2CPacket.Entry entry = new PlayerListS2CPacket.Entry(
//                player.getUuid(),
//                player.getGameProfile(), // или используйте player.getLatency(), если доступно
//                player.interactionManager.getGameMode(), // или player.getGameMode() при наличии
//                coloredName
//        );
//    }
//
//    private Formatting getColorForDimension(ServerPlayerEntity player) {
//        World world = player.getWorld();
//        if (world.getRegistryKey().equals(World.OVERWORLD)) {
//            return Formatting.GREEN;
//        } else if (world.getRegistryKey().equals(World.NETHER)) {
//            return Formatting.RED;
//        } else if (world.getRegistryKey().equals(World.END)) {
//            return Formatting.DARK_PURPLE;
//        } else {
//            return Formatting.BLUE;
//        }
//    }

}
