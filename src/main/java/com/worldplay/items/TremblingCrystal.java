
package com.worldplay.items;

import com.worldplay.api.RPFast;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
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
import java.util.Objects;
import java.util.function.Consumer;


public class TremblingCrystal extends SimplePolymerItem implements PolymerItem, PolymerKeepModel, PolymerClientDecoded {

    private final Identifier polymerModel;
//    private final PolymerModelData modelData = PolymerResourcePackUtils.requestModel(Items.AMETHYST_SHARD, Identifier.of(PeopleMineSeason5.MOD_ID, "item/trembling_crystal"));
    public TremblingCrystal(Settings settings, String modelId) {
        super(settings);

        this.polymerModel = RPFast.getItemModel(modelId);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

         BlockPos blockPos = context.getBlockPos();
         ServerWorld serverWorld = context.getWorld().getServer().getOverworld();
         serverWorld.spawnParticles(ParticleTypes.END_ROD, blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, 5, 0.1, 0.1,0.1,0.1);
         serverWorld.playSound(null,blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.1f,1);
         ItemStack itemStack = Objects.requireNonNull(context.getPlayer()).getStackInHand(Hand.MAIN_HAND);

         context.getPlayer().getItemCooldownManager().set(itemStack, 120);
         ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) context.getPlayer();

            return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {

        textConsumer.accept(Text.literal("Этот кристал что-то излучает...").formatted(Formatting.WHITE));
        textConsumer.accept(Text.literal("*дрожит*").formatted(Formatting.BLUE));

    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return this.polymerModel;
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



