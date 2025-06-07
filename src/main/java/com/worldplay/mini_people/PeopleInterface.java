package com.worldplay.mini_people;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;

public interface PeopleInterface {
    public void setModel(ArmorStandEntity entity);
    public void tick();
    public NbtCompound getNbt();
}
