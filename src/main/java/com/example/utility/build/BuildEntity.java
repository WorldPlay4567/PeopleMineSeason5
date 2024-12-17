package com.example.utility.build;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class BuildEntity {
    private Vec3d entityPos;
    private NbtCompound nbtEntity;
    private Optional<EntityType<?>> entityType;

    public BuildEntity(Vec3d entityPos, NbtCompound nbtEntity, Optional<EntityType<?>> entityType) {
        this.entityPos = entityPos;
        this.nbtEntity = nbtEntity;
        this.entityType = entityType;

    }

    public Vec3d getEntityPos() {
        return entityPos;
    }
    public NbtCompound getNbtEntity() {
        return nbtEntity;
    }
    public Optional<EntityType<?>> getEntityType() {
        return entityType;
    }

}
