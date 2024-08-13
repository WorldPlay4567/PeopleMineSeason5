package com.example;


import com.example.blocks.BlockInit;
import com.example.blocks.CustomBlockList;
import com.example.items.BluePrint;
import com.example.items.ItemsInit;
import com.mojang.brigadier.context.CommandContext;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;


public class PeopleMineSeason5 implements ModInitializer {

	public static final  String MOD_ID = "peoplemineseason5";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(PeopleMineSeason5.MOD_ID);
		PolymerResourcePackUtils.addModAssets("minecraft");

		LOGGER.info("=====================");
		LOGGER.info("PeopleMineSeason5");
		LOGGER.info("=====================");


//		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
//			if (hitResult == null) {
//				return ActionResult.PASS;
//			}
//
//            if (hitResult.getEntity() instanceof VillagerEntity) {
//				PeopleMineSeason5.test7((ServerPlayerEntity) player);
//				return ActionResult.FAIL;
//			}
//
//			return ActionResult.PASS;
//		});


//		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//			dispatcher.register(CommandManager.literal("test7")
//					.executes(PeopleMineSeason5::test7));});


		ServerTickEvents.START_SERVER_TICK.register((server)-> {

			Iterable<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
			for (ServerPlayerEntity player : players) {
				BluePrint.tick(player);
			}
		});

		CustomBlockList.init();
		ItemsInit.init();
		BlockInit.init();

	}


}




//world.spawnParticles(ParticleTypes.END_ROD, x + (double) i - 10, y, z, 1, 0, 0,0,0);






//		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//
//			BlockPos pos = hitResult.getBlockPos();
//			generateParticleSquare(world, pos.getX(), pos.getY(), pos.getZ(), 5);
//			return ActionResult.PASS;
//        });

//		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//			player.sendMessage(Text.of("YES"));
//			BlockPos pos = hitResult.getBlockPos();
//			Box box = new Box(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, pos.getX(), pos.getY(), pos.getZ());
//			List<ArmorStandEntity> markerEntities = world.getEntitiesByType(EntityType.ARMOR_STAND, box, entity -> true);
//			player.sendMessage(Text.of(markerEntities.toString()));
//
//			generateParticleSquare(world, pos.getX(), pos.getY(), pos.getZ(), 3);
//
//			if (markerEntities.isEmpty()) {
//				player.sendMessage(Text.of("YES_Spawn"));
//				world.spawnEntity(new ArmorStandEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
//				generateParticleSquare(world, pos.getX(), pos.getY(), pos.getZ(), 3);
//			}
//			return ActionResult.PASS;
//		});


