package com.worldplay.test;

import net.minecraft.advancement.*;
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
            dark(player);
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
