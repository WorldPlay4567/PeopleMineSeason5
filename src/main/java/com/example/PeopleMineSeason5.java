package com.example;


import com.example.blocks.BlockInit;
import com.example.blocks.CustomBlockList;
import com.example.items.BluePrint;
import com.example.items.ItemsInit;
import com.example.utility.ConfigVillager;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




public class PeopleMineSeason5 implements ModInitializer {

	public static final  String MOD_ID = "peoplemineseason5";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private final Map<UUID, ArmorStandEntity> playerArmorStands = new HashMap<>();





	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(PeopleMineSeason5.MOD_ID);
		PolymerResourcePackUtils.addModAssets("minecraft");

		LOGGER.info("=====================");
		LOGGER.info("PeopleMineSeason5");
		LOGGER.info("=====================");

		ConfigVillager.loadOrCreateConfig();


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
//		ServerWorldEvents.LOAD.register((test,test2)-> {
//			ConfigVillager.getItem("stone");
//		} );

//		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//			dispatcher.register(CommandManager.literal("test7")
//					.executes(PeopleMineSeason5::test7));});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				World world = player.getWorld();
				UUID playerId = player.getUuid();

				// Проверяем наличие алмаза в инвентаре
				boolean hasDiamond = player.getInventory().contains(Items.DIAMOND.getDefaultStack());

				if (hasDiamond) {
					// Если у игрока есть алмаз, создаем или двигаем Armor Stand
					if (!playerArmorStands.containsKey(playerId)) {
						// Спавним Armor Stand
						ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
						NbtCompound nbt = new NbtCompound();
						nbt.putBoolean("Small", true);  // Устанавливаем флаг маленького размера
						armorStand.readNbt(nbt);
						armorStand.setNoGravity(true);  // Отключаем гравитацию
						armorStand.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0, 0);
						world.spawnEntity(armorStand);
						playerArmorStands.put(playerId, armorStand);
					}

					// Плавно двигаем Armor Stand к плечу игрока с помощью телепортации
					ArmorStandEntity armorStand = playerArmorStands.get(playerId);

					// Вычисляем позицию плеча
					Vec3d shoulderPos = getPlayerShoulderPosition(player);

					// Вычисляем текущую позицию Armor Stand
					Vec3d standPos = armorStand.getPos();

					// Вычисляем направление движения к плечу игрока
					Vec3d direction = shoulderPos.subtract(standPos).normalize();

					// Вычисляем расстояние между Armor Stand и плечом игрока
					double distance = shoulderPos.distanceTo(standPos);

					// Чем больше расстояние, тем больше скорость (добавим минимальную скорость)
					double speed = Math.max(0, distance * 0.5); // Минимальная скорость - 0.3, скорость увеличивается с расстоянием

					// Новая позиция Armor Stand (на шаг ближе к плечу игрока с учетом скорости)
					Vec3d newPos = standPos.add(direction.multiply(speed));

					// Телепортируем Armor Stand на новую позицию
					armorStand.teleport(newPos.x, newPos.y, newPos.z);

				} else {
					// Если алмаза нет, удаляем Armor Stand
					if (playerArmorStands.containsKey(playerId)) {
						ArmorStandEntity armorStand = playerArmorStands.remove(playerId);
						armorStand.discard(); // Удаляем сущность
					}
				}
			}
		});

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
	private Vec3d getPlayerShoulderPosition(ServerPlayerEntity player) {
		// Базовая позиция игрока
		Vec3d playerPos = player.getPos();

		// Уровень плеча: немного выше уровня глаз (примерно на 1.5 блока выше ног)
		double shoulderHeight = playerPos.y + 1.6; // Плечи выше уровня глаз на 1.6 блока

		// Получаем угол, куда смотрит игрок (yaw)
		float yaw = player.getYaw(1.0f);

		// Рассчитываем смещение для плеча в зависимости от направления взгляда игрока
		// Смещение вправо относительно направления игрока
		double offsetX = -Math.cos(Math.toRadians(yaw)) * 0.4; // Смещаем по горизонтали (вправо)
		double offsetZ = -Math.sin(Math.toRadians(yaw)) * 0.4; // Смещаем по оси Z (глубина)

		// Возвращаем позицию правого плеча игрока
		return new Vec3d(playerPos.x + offsetX, shoulderHeight, playerPos.z + offsetZ);
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


