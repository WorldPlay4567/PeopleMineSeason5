package com.worldplay;


import com.worldplay.api.CustomMenuHelper;
import com.worldplay.blocks.BlockInit;
import com.worldplay.chat.ChatCord;
import com.worldplay.chat.CordCommand;
import com.worldplay.items.BluePrint;
import com.worldplay.items.ItemsInit;
import com.worldplay.mini_people.MiniPeople;
import com.worldplay.mini_people.MiniPeopleTools;
import com.worldplay.test.Tesst;
import com.worldplay.utility.builds.*;
import com.mojang.brigadier.context.CommandContext;

import com.worldplay.utility.crafting.BluePrintList;
import com.worldplay.utility.villager.VillagerShopList;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import eu.pb4.sgui.api.GuiHelpers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PeopleMineSeason5 implements ModInitializer {

	public static final  String MOD_ID = "peoplemineseason5";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private final Map<UUID, ArmorStandEntity> playerArmorStands = new HashMap<>();
	public static Iterable<ServerPlayerEntity> players;
//	public static final Model MODEL_FROM_ID = AjModelLoader.load(Identifier.of("peoplemineseason5","test"));
//	public static final Model MODEL_FROM_FILEPATH = BbModelLoader.load("model/peoplemineseason5/test.ajmodel");



	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(PeopleMineSeason5.MOD_ID);
		PolymerResourcePackUtils.addModAssets("minecraft");
		PolymerResourcePackUtils.addModAssets("space");
		ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.of(MOD_ID, "item"));
		ResourcePackExtras.forDefault().addBridgedModelsFolder(Identifier.of(MOD_ID, "block"));




		CordCommand.init();
		ChatCord.init();
		VillagerShopList.init();
		BuildManager.start();
		CustomMenuHelper.init();
		MiniPeopleTools.register();
		MiniPeople.init();
		LOGGER.info("=====================");
		LOGGER.info("PeopleMineSeason5");
		LOGGER.info("=====================");
		LOGGER.info(FabricLoader.getInstance().getGameDir().toString());
//		ConfigVillager.loadOrCreateConfig();
//		ServerLifecycleEvents.SERVER_STARTED.register(BuildCraftingList::onServerStart);
//		ServerLifecycleEvents.SERVER_STOPPING.register(BuildCraftingList::onServerClose);


		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("peopleminereload")
					.executes(PeopleMineSeason5::reload));
		});

		ServerWorldEvents.UNLOAD.register((server, world) -> {
			VillagerShopList.save();
			BluePrintList.save(server);
		});
		ServerWorldEvents.LOAD.register(((minecraftServer, serverWorld) -> {
			BuildStructure.load(minecraftServer,serverWorld);
			BluePrintList.load(minecraftServer);
        }));
//		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
//			dispatcher.register(CommandManager.literal("gui")
//					.executes(PeopleMineSeason5::gui));
//		});

		UseBlockCallback.EVENT.register((player, world, hand,hitResult) -> {

			ItemStack itemStack = player.getStackInHand(hand);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

			BlockHitResult blockHitResult = (BlockHitResult) hitResult;

			BlockPos blockPos = blockHitResult.getBlockPos(); // Получаем позицию блока, по которому кликнули

			if (itemStack.getItem() == Items.CARROT && (CarrotBlockCheck(world, blockPos) || world.getBlockState(blockPos).getBlock() == Blocks.FARMLAND)) {

				System.out.print("DEBUG : NO PLACE");

				if (!world.isClient) {
					serverPlayer.playerScreenHandler.sendContentUpdates();
				}
				for (int i = 0; i < 9; i++) {
					GuiHelpers.sendSlotUpdate(serverPlayer,-2, i, player.getInventory().getStack(i));
				}
				return ActionResult.FAIL;

            }
			return ActionResult.PASS;
		});


//		UseBlockCallback.EVENT.register((player, world, hand,hitResult) -> {
//
//			BlockPos blockPos = hitResult.getBlockPos();
//			if(world.getBlockState(blockPos).getBlock() == Blocks.CHEST) {
//				CalendarChest.createCalendarChest(world,blockPos);
//			}
//
//
//			return ActionResult.PASS;
//		});
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
//		ServerTickEvents.START_SERVER_TICK.register(server -> {
//			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//
//
//				World world = player.getWorld();
//				UUID playerId = player.getUuid();
//
//				// Проверяем наличие алмаза в инвентаре
//				boolean hasDiamond = player.getInventory().contains(Items.DIAMOND.getDefaultStack());
//
//				if (hasDiamond) {
//					// Если у игрока есть алмаз, создаем или двигаем Armor Stand
//					if (!playerArmorStands.containsKey(playerId)) {
//						// Спавним Armor Stand
//						ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
//						NbtCompound nbt = new NbtCompound();
//						nbt.putBoolean("Small", true);  // Устанавливаем флаг маленького размера
//						armorStand.readNbt(nbt);
//						armorStand.setNoGravity(true);  // Отключаем гравитацию
//						armorStand.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), 0, 0);
//						world.spawnEntity(armorStand);
//						playerArmorStands.put(playerId, armorStand);
//					}
//
//					// Плавно двигаем Armor Stand к плечу игрока с помощью телепортации
//					ArmorStandEntity armorStand = playerArmorStands.get(playerId);
//
//					// Вычисляем позицию плеча
//					Vec3d shoulderPos = getPlayerShoulderPosition(player);
//
//					// Вычисляем текущую позицию Armor Stand
//					Vec3d standPos = armorStand.getPos();
//
//					// Вычисляем направление движения к плечу игрока
//					Vec3d direction = shoulderPos.subtract(standPos).normalize();
//
//					// Вычисляем расстояние между Armor Stand и плечом игрока
//					double distance = shoulderPos.distanceTo(standPos);
//
//					// Чем больше расстояние, тем больше скорость (добавим минимальную скорость)
//					double speed = Math.max(0, distance * 0.5); // Минимальная скорость - 0.3, скорость увеличивается с расстоянием
//
//					// Новая позиция Armor Stand (на шаг ближе к плечу игрока с учетом скорости)
//					Vec3d newPos = standPos.add(direction.multiply(speed));
//
//					// Телепортируем Armor Stand на новую позицию
//					armorStand.teleport(newPos.x, newPos.y, newPos.z);
//
//				} else {
//					// Если алмаза нет, удаляем Armor Stand
//					if (playerArmorStands.containsKey(playerId)) {
//						ArmorStandEntity armorStand = playerArmorStands.remove(playerId);
//						armorStand.discard(); // Удаляем сущность
//					}
//				}
//			}
//		});

		ServerTickEvents.START_SERVER_TICK.register((server)-> {
//			BuildManager.tick();
			Iterable<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
			PeopleMineSeason5.players = server.getPlayerManager().getPlayerList();
			Tesst.init(server);

			for (ServerPlayerEntity player : players) {
				BluePrint.tick(player);

				player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, player));
			}
			BuildManager.tickParticle(server);

		});

//		CustomBlockList.init();
		ItemsInit.init();
		BlockInit.init();

	}

	private boolean CarrotBlockCheck(World world, BlockPos blockPos) {

		for (Direction direction : new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {

			BlockPos Pos = blockPos.offset(direction).down();
			BlockState adjacentBlockState = world.getBlockState(Pos);

			if(adjacentBlockState.getBlock() == Blocks.FARMLAND) {
				return true;
            };
		}
		return false;
	}

	private static int reload(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
		serverCommandSourceCommandContext.getSource().sendMessage(Text.literal("Все конфиги перезагружены"));
//		ConfigVillagerRegister.init();
		ServerPlayerEntity serverPlayer = serverCommandSourceCommandContext.getSource().getPlayer();
//		serverCommandSourceCommandContext.getSource().sendMessage(Text.literal(" Test : " +  SaveStructure.getStructureState(serverPlayer.getServer()).getAllString()));

		System.out.println();
//		ArrayList<BuildBlock> buildBlocks = BuildStructure.getBuild("shop_stone");
//			ServerWorld serverWorld = serverPlayer.getServer().getOverworld();
//
//			for(BuildBlock block : buildBlocks) {
//
//				BlockPos blockPos = serverPlayer.getBlockPos();
//				BlockPos original = new BlockPos(
//						blockPos.getX() + block.getBlockPos().getX(),
//						blockPos.getY() + block.getBlockPos().getY(),
//						blockPos.getZ() + block.getBlockPos().getZ()
//                        );
//
//				serverWorld.setBlockState(original,block.getBlockState());
//			}
//			return 1;
//
		return 0;
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


