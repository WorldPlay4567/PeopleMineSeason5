
package com.example.items;

import com.example.PeopleMineSeason5;
import com.example.particle.CubeParticle;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;

import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;
import java.util.Objects;


public class BluePrint extends Item implements PolymerItem {
    private static int tick;
    private final PolymerModelData modelData = PolymerResourcePackUtils.requestModel(Items.PAPER, new Identifier(PeopleMineSeason5.MOD_ID, "item/blue_print"));
    public BluePrint(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        ItemStack itemStack = playerEntity.getStackInHand(hand);

            // Если предмет в руке - палка, выводим сообщение в чат
            if (!world.isClient) {

                if (playerEntity.getWorld() instanceof ServerWorld serverWorld) {


                    NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
                    assert nbtComponent != null;
                    NbtCompound nbtCompound = nbtComponent.copyNbt();

                    if (nbtCompound.get("structure") == null) {
                        String message = "Чертеж пустой";
                        int hexColor = 0xff7575;
                        Style style = Style.EMPTY.withColor(hexColor);

                        playerEntity.sendMessage(Text.literal(message).setStyle(style),true);

                        return new TypedActionResult<>(ActionResult.PASS, itemStack);
                    } else {

                    Identifier identifier = new Identifier(nbtCompound.get("structure").asString());

                    float x = playerEntity.getBlockPos().getX();
                    float y = playerEntity.getBlockPos().getY() - 1;
                    float z = playerEntity.getBlockPos().getZ();

                    BlockPos pos = BlockPos.ofFloored(new Vec3d(x,y,z));

                    StructurePlacerAPI structurePlacerAPI = new StructurePlacerAPI((ServerWorld) playerEntity.getWorld(), identifier , pos, BlockMirror.NONE,BlockRotation.NONE,false,1,new BlockPos(0, 0, 0));

                    structurePlacerAPI.loadStructure();

                    serverWorld.playSound(null,pos.getX(),pos.getY(),pos.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 1,1);
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, pos.getX(),pos.getY(),pos.getZ(), 300, 5, 5,5,0);
                }
                }

                itemStack.decrement(1);
            }
            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {

            NbtCompound nbtCompound = nbtComponent.copyNbt();
            if (nbtCompound.get("structure") != null) {
                tooltip.add(Text.literal(nbtCompound.get("structure").asString()).formatted(Formatting.AQUA));
            } else {
                tooltip.add(Text.literal("Пусто чертеж").formatted(Formatting.AQUA));
            }
            super.appendTooltip(stack, context, tooltip, type);
        } else {
            tooltip.add(Text.literal("Пусто чертеж").formatted(Formatting.AQUA));
        }
    }
    public static void tick(PlayerEntity player) {
        if (player.getInventory().getMainHandStack().getItem() == ItemsInit.BLUE_PRINT){

            ItemStack itemStack = player.getInventory().getMainHandStack();
            NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
            if (nbtComponent != null) {
                NbtCompound nbtCompound = nbtComponent.copyNbt();




            if (nbtCompound.get("structure") == null) {
                String message = "Чертеж пустой";
                int hexColor = 0xff7575;
                Style style = Style.EMPTY.withColor(hexColor);

                player.sendMessage(Text.literal(message).setStyle(style),true);
            } else {
                Identifier identifier = new Identifier(nbtCompound.get("structure").asString());
                StructureTemplate structureTemplate = Objects.requireNonNull(player.getServer()).getStructureTemplateManager().getTemplateOrBlank(identifier);
                Vec3i vec3i = structureTemplate.getSize();
//                System.out.println(vec3i);

                tick++;
                if(tick >= 2) {
                    CubeParticle cubeParticle = new CubeParticle((ServerWorld) player.getWorld(), player.getPos(), vec3i);
                    cubeParticle.draw();
                    tick = 0;
                }
            }
            }

        }
    }



    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return 2;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.PAPER;
    }

}



