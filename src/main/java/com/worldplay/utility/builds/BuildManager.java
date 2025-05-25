package com.worldplay.utility.builds;

import com.worldplay.PeopleMineSeason5;
import com.worldplay.particle.CubeParticle;
import eu.pb4.polymer.virtualentity.api.VirtualEntityUtils;
import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuildManager {
    static int tick = 0;
    private static final CopyOnWriteArrayList<PlaceBuild> builds = new CopyOnWriteArrayList<>();



    public static void addPlaceBuild(PlaceBuild placeBuild) {
        builds.add(placeBuild);
        System.out.println(builds);
    }

    public static void tickParticle(MinecraftServer server) {
        tick++;
        SaveStructure saveStructure = SaveStructure.getStructureState(Objects.requireNonNull(server));
        List<StructureData> structureData = saveStructure.getStructureData(true);
        if(tick == 1) {
            for (StructureData data : structureData) {
                Identifier identifier = Identifier.of(PeopleMineSeason5.MOD_ID, data.nameStructure);
                StructureTemplate structureTemplate = server.getStructureTemplateManager().getTemplateOrBlank(identifier);
                Vec3i vec3i = structureTemplate.getSize();

                CubeParticle cubeParticle = new CubeParticle(server.getWorld(World.OVERWORLD), data.posStructure, vec3i);
                cubeParticle.draw();
            }
        }
        if(tick > 5) {
            tick = 0;
        }
    }

    public static void tick_test() {
        if(PeopleMineSeason5.players == null) {return;}

        for(ServerPlayerEntity serverPlayer : PeopleMineSeason5.players) {
            Vec3d vec3d = new Vec3d(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
            PlayerPosition playerPosition = new PlayerPosition(vec3d, new Vec3d(0,0,0),0, 0);


            //ca0e4d78-71c6-4ed1-b81d-e92348253bc3

            UUID uuid = UUID.fromString("ca0e4d78-71c6-4ed1-b81d-e92348253bc3");

//            serverPlayer.networkHandler.sendPacket(VirtualEntityUtils.createSetCameraEntityPacket(serverPlayer.getServer().getWorld(World.OVERWORLD).getEntity(uuid).getId()));
//            serverPlayer.networkHandler.sendPacket(VirtualEntityUtils.createRidePacket(serverPlayer.getServer().getWorld(World.OVERWORLD).getEntity(uuid).getId(), IntList.of(serverPlayer.getId())));
            serverPlayer.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, GameMode.SPECTATOR.getIndex()));
            serverPlayer.networkHandler.sendPacket(new EntityS2CPacket.Rotate(serverPlayer.getId(), (byte) 90, (byte) 0, serverPlayer.isOnGround()));
            serverPlayer.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(serverPlayer.getId(), List.of(DataTracker.SerializedEntry.of(EntityTrackedData.POSE, EntityPose.STANDING))));

        }
    }


    public static void tick() {
        try {

//            tick_test();


            // Ваш код здесь
            if (!builds.isEmpty()) {
                for(PlaceBuild build: builds) {
                    build.place();

                    if(build.isPlace()) {
                        builds.remove(build);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка в методе tick: " + e.getMessage());
        }
    }


    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public static Runnable safeRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Ошибка в задаче: " + e.getMessage());
            }
        };
    }

    public static void start() {
        executor.scheduleAtFixedRate(safeRunnable(BuildManager::tick), 0, 5, TimeUnit.MILLISECONDS);
    }

}
