package com.worldplay.utility;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class KillEntityBox {


    private static Vec3d vec3d;

    public static List<Entity> getEntitiesAt(ServerWorld world, Vec3d position) {
        // Создаем bounding box вокруг позиции
        Box boundingBox = new Box(
                position.x, position.y, position.z,
                position.x, position.y, position.z
        );

        // Получаем сущности внутри bounding box
        return world.getEntitiesByClass(Entity.class, boundingBox, entity -> true);
    }

    public static void killAll(ServerWorld world,Vec3d vec3d) {
        List<Entity> entities = getEntitiesAt(world,vec3d);

        for(Entity entity : entities) {
            entity.kill(world);
        }
    }
}
