package com.worldplay.mini_people;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.worldplay.mini_people.MiniPeopleSkin.RUSLAN_SINGER;
import static com.worldplay.mini_people.MiniPeopleSkin.WEWE707;

public class MiniPeople {

    public static List<People> peopleList = new ArrayList<>();

    private static final Map<UUID, ArmorStandEntity> PETS = new ConcurrentHashMap<>();

    public static void init(){
        register("wewe707", WEWE707, "MAGIC", Items.DIAMOND_CHESTPLATE.getDefaultStack(),  Items.DIAMOND_LEGGINGS.getDefaultStack(), Items.DIAMOND_BOOTS.getDefaultStack());
        register("ruslan_singer", RUSLAN_SINGER, "MAGIC", GuiElementBuilder.from(Items.LEATHER_CHESTPLATE.getDefaultStack()).setComponent(DataComponentTypes.DYED_COLOR, new DyedColorComponent(Colors.BLACK)).asStack(),  GuiElementBuilder.from(Items.LEATHER_LEGGINGS.getDefaultStack()).setComponent(DataComponentTypes.DYED_COLOR, new DyedColorComponent(Colors.BLACK)).asStack(), GuiElementBuilder.from(Items.LEATHER_BOOTS.getDefaultStack()).setComponent(DataComponentTypes.DYED_COLOR, new DyedColorComponent(Colors.BLACK)).asStack());
        ServerTickEvents.START_SERVER_TICK.register(MiniPeople::tick);
    }

    public static void tick(MinecraftServer server){

        for (Map.Entry<UUID, ArmorStandEntity> entry : PETS.entrySet()) {
            UUID playerId = entry.getKey();
            ArmorStandEntity stand = entry.getValue();

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerId);
            if (player == null || stand.isRemoved()) {
                if (stand != null && !stand.isRemoved()) stand.remove(Entity.RemovalReason.KILLED);
                PETS.remove(playerId);
                continue;
            }

            float headYaw = player.getYaw(1.0F);
            double rad = Math.toRadians(headYaw + 180.0F);
            Vec3d rightHoriz = new Vec3d(
                    Math.cos(rad),
                    0,
                    Math.sin(rad)
            ).normalize();

            Vec3d targetPos2 = player.getEyePos()
                    .add(rightHoriz.multiply(0.5))
                    .add(0, 0.0, 0);


            Vec3d dir = new Vec3d(
                    targetPos2.getX() - stand.getX(),
                    targetPos2.getY() - stand.getY(),
                    targetPos2.getZ() - stand.getZ()
            );

            stand.setVelocity(dir);
            stand.velocityDirty = true;
        }
    }

    static void spawnPet(ServerPlayerEntity player, People skin) {

        removePet(player);

        ArmorStandEntity stand = new ArmorStandEntity(EntityType.ARMOR_STAND, player.getWorld());
        stand.setPos(player.getX(),player.getY(), player.getZ());

        byte flags = stand.getDataTracker().get(ArmorStandEntity.ARMOR_STAND_FLAGS);

        flags |= 0x01;
        flags |= 0x02;
        flags |= 0x04;

        stand.getDataTracker().set(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte)(flags));

        stand.equipStack(EquipmentSlot.HEAD, GuiElementBuilder.from(Items.PLAYER_HEAD.getDefaultStack())
                .setSkullOwner(skin.head)
                .asStack());
        stand.equipStack(EquipmentSlot.CHEST, skin.items.get(0));
        stand.equipStack(EquipmentSlot.LEGS, skin.items.get(1));
        stand.equipStack(EquipmentSlot.FEET, skin.items.get(2));

        stand.setHideBasePlate(true);
        stand.setNoGravity(false);

        player.getWorld().spawnEntity(stand);
        PETS.put(player.getUuid(), stand);

        player.sendMessage(Text.literal("Spawned pet for " + player.getName().getString()), false);

    }

    private static void removePet(ServerPlayerEntity player) {

        ArmorStandEntity stand = PETS.remove(player.getUuid());
        if (stand != null && !stand.isRemoved()) {
            stand.remove(Entity.RemovalReason.KILLED);
            player.sendMessage(Text.literal("Removed your pet."), false);
        } else {
            player.sendMessage(Text.literal("You have no pet."), false);
        }
    }

    public static void register(String id, String head, String lore, ItemStack chest, ItemStack legins, ItemStack boots) {
        People people = new People(id, head, lore, chest, legins, boots);
        peopleList.add(people);

    }

    public static NbtCompound getPeopleId(String id) {
        for(People people : peopleList) {
            System.out.println(people.id + "   " + id);

            if(Objects.equals(people.id, id)) {
                return people.getNbt();
            }
        }
        return null;
    }

    public static People getPeople(String id) {

        for(People people : peopleList) {
            if(Objects.equals(people.id, id)) {
                return people;
            }
        }

        return null;
    }

    public static class People {

        String id;
        String head;
        String lore;

        ArrayList<ItemStack> items = new ArrayList<>();

        public People(String id, String head, String lore, ItemStack chest, ItemStack legins, ItemStack boots) {
            this.id = id;
            this.head = head;
            this.lore = lore;
            this.items.add(chest);
            this.items.add(legins);
            this.items.add(boots);
        }

        public NbtCompound getNbt() {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("id", id);
            return nbtCompound;
        }
    }

}
