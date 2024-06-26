package com.example;


import com.example.blocks.BlockInit;
import com.example.blocks.CustomBlockList;
import com.example.items.ItemsInit;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeopleMineSeason5 implements ModInitializer {

	public static final  String MOD_ID = "peoplemineseason5";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//trembling_crystal



	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(PeopleMineSeason5.MOD_ID);
		PolymerResourcePackUtils.addModAssets("minecraft");

		LOGGER.info("=====================");
		LOGGER.info("PeopleMineSeason5");
		LOGGER.info("=====================");


		ServerTickEvents.START_SERVER_TICK.register((server)->{

			Iterable<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
			for (ServerPlayerEntity player : players) {
				// Perform actions for each player on each tick
				if (player.getInventory().getMainHandStack().getItem() == ItemsInit.BLUE_PRINT)
					spawnBox(player);
			}
		});

		CustomBlockList.init();
		ItemsInit.init();
		BlockInit.init();
	}




	public static void spawnBox(ServerPlayerEntity player) {
		generateParticleSquare(player.getServerWorld(),player.getPos().x,player.getPos().y,player.getPos().z,10);
	}

//world.spawnParticles(ParticleTypes.END_ROD, x + (double) i - 10, y, z, 1, 0, 0,0,0);
	public static void generateParticleSquare(ServerWorld world, double pos_x, double pos_y, double pos_z, int size) {

		BlockPos corner1 = new BlockPos((int) pos_x, (int) pos_y, (int) pos_z);
		BlockPos corner2 = new BlockPos(corner1.getX() + size, corner1.getY() + size, corner1.getZ() + size);

		// Генерация партиклов на рёбрах кубоида
		for (int x = corner1.getX(); x <= corner2.getX(); x++) {
			for (int y = corner1.getY(); y <= corner2.getY(); y += (corner2.getY() - corner1.getY())) {
				for (int z = corner1.getZ(); z <= corner2.getZ(); z += (corner2.getZ() - corner1.getZ())) {
					Vec3d pos = new Vec3d(x, y, z);
					world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 0, 0, 0,0,0);
				}
			}
		}

		for (int y = corner1.getY(); y <= corner2.getY(); y++) {
			for (int x = corner1.getX(); x <= corner2.getX(); x += (corner2.getX() - corner1.getX())) {
				for (int z = corner1.getZ(); z <= corner2.getZ(); z += (corner2.getZ() - corner1.getZ())) {
					Vec3d pos = new Vec3d(x, y, z);
					world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 0, 0, 0,0,0);
				}
			}
		}

		for (int z = corner1.getZ(); z <= corner2.getZ(); z++) {
			for (int x = corner1.getX(); x <= corner2.getX(); x += (corner2.getX() - corner1.getX())) {
				for (int y = corner1.getY(); y <= corner2.getY(); y += (corner2.getY() - corner1.getY())) {
					Vec3d pos = new Vec3d(x, y, z);
					world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 0, 0, 0,0,0);
				}
			}
		}
	}





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

}
