package com.worldplay.mini_people.people;

import com.worldplay.mini_people.MiniPeople;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;

public class PeopleDefault {

    public String id;
    public String head;
    public String lore;

    ArrayList<ItemStack> items = new ArrayList<>();

    int tick = 0;

    public PeopleDefault(String id, String head, String lore, ItemStack head_item, ItemStack chest, ItemStack legins, ItemStack boots) {
        this.id = id;
        this.head = head;
        this.lore = lore;
        this.items.add(head_item);
        this.items.add(chest);
        this.items.add(legins);
        this.items.add(boots);
    }

    public void setModel(ArmorStandEntity entity) {
        entity.getDataTracker().set(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte) (ArmorStandEntity.SMALL_FLAG | ArmorStandEntity.SHOW_ARMS_FLAG));

        entity.equipStack(EquipmentSlot.HEAD, GuiElementBuilder.from(Items.PLAYER_HEAD.getDefaultStack())
                .setSkullOwner(head)
                .asStack());
        entity.equipStack(EquipmentSlot.CHEST, items.get(1));
        entity.equipStack(EquipmentSlot.LEGS, items.get(2));
        entity.equipStack(EquipmentSlot.FEET, items.get(3));
    }

    public void tick() {
        tick ++;
        if(tick > 20) {
            tick = 0;
        }
    }

    public NbtCompound getNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("id", id);
        return nbtCompound;
    }



}
