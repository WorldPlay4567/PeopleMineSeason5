package com.worldplay.mini_people.people;

import eu.pb4.polymer.virtualentity.api.tracker.EntityTrackedData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;

public class PeopleCustomModel extends PeopleDefault {

    public PeopleCustomModel(String id, String lore, ItemStack head_item) {
        super(id, null, lore, head_item, null, null, null);
    }

    @Override
    public void setModel(ArmorStandEntity entity) {
        entity.equipStack(EquipmentSlot.HEAD, items.getFirst());

        entity.getDataTracker().set(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte) (ArmorStandEntity.SMALL_FLAG));
        entity.getDataTracker().set(EntityTrackedData.SILENT, true);
        entity.setInvisible(true);
    }
}
