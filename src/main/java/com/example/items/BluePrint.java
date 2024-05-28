
package com.example.items;

import com.example.PeopleMineSeason5;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;


public class BluePrint extends Item implements PolymerItem {

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
                    Identifier bastionId = new Identifier("peoplemineseason5", "shop");


                    float x = playerEntity.getBlockPos().getX();
                    float y = playerEntity.getBlockPos().getY() - 1;
                    float z = playerEntity.getBlockPos().getZ();

                    BlockPos pos = BlockPos.ofFloored(new Vec3d(x,y,z));

                    StructureTemplate structureTemplate = serverWorld.getStructureTemplateManager().getTemplateOrBlank(bastionId);
                    structureTemplate.place(serverWorld, pos, pos, new StructurePlacementData(), Random.create(), 2);
                    playerEntity.sendMessage(Text.literal(playerEntity.getName() + " использовал палку!"), false);
                    playerEntity.sendMessage(Text.literal(String.valueOf(playerEntity.getWorld())), false);
                    serverWorld.playSound(null,pos.getX(),pos.getY(),pos.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 1,1);
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, pos.getX(),pos.getY(),pos.getZ(), 300, 5, 5,5,0);
                }

                itemStack.decrement(1);
            }
            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        super.appendTooltip(stack, context, tooltip, type);
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



