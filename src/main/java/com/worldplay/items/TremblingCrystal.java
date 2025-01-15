
package com.worldplay.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;


public class TremblingCrystal extends Item implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {

    private final Identifier polymerModel;
//    private final PolymerModelData modelData = PolymerResourcePackUtils.requestModel(Items.AMETHYST_SHARD, Identifier.of(PeopleMineSeason5.MOD_ID, "item/trembling_crystal"));
    public TremblingCrystal(Settings settings, String modelId) {
        super(settings);
        this.polymerModel = PolymerResourcePackUtils.getBridgedModelId(Identifier.of("blocktest", modelId));
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

         BlockPos blockPos = context.getBlockPos();
         ServerWorld serverWorld = context.getWorld().getServer().getOverworld();
         serverWorld.spawnParticles(ParticleTypes.END_ROD, blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, 5, 0.1, 0.1,0.1,0.1);
         serverWorld.playSound(null,blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.1f,1);
         ItemStack itemStack = context.getPlayer().getHandItems().iterator().next();

         context.getPlayer().getItemCooldownManager().set(itemStack, 120);
         ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) context.getPlayer();

            return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, net.minecraft.item.tooltip.TooltipType type) {

        tooltip.add(1,Text.literal("Этот кристал что-то излучает...").formatted(Formatting.WHITE));
        tooltip.add(2,Text.literal("*дрожит*").formatted(Formatting.BLUE));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return Identifier.of("peoplemineseason5","trembling_crystal");
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return Items.AMETHYST_SHARD;
    }

//    @Override
//    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
//        return PolymerItem.super.getPolymerItemModel(stack, context);
//    }
}



