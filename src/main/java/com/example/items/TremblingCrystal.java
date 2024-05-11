
package com.example.items;

import com.example.PeopleMineSeason5;
import com.example.gui.PMGui;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.client.item.TooltipType;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;


public class TremblingCrystal extends Item implements PolymerItem {


    private final PolymerModelData modelData = PolymerResourcePackUtils.requestModel(Items.AMETHYST_SHARD, new Identifier(PeopleMineSeason5.MOD_ID, "item/trembling_crystal"));
    public TremblingCrystal(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

         BlockPos blockPos = context.getBlockPos();
         ServerWorld serverWorld = context.getWorld().getServer().getOverworld();
         serverWorld.spawnParticles(ParticleTypes.END_ROD, blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, 5, 0.1, 0.1,0.1,0.1);
         serverWorld.playSound(null,blockPos.getX() + 0.5, blockPos.getY()+ 0.5, blockPos.getZ()+ 0.5, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.1f,1);
         Iterable<ItemStack> itemStack = context.getPlayer().getHandItems();

         context.getPlayer().getItemCooldownManager().set(itemStack.iterator().next().getItem(), 120);
         ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) context.getPlayer();

         PMGui.open(serverPlayerEntity);


            return TypedActionResult.success(context).getResult();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(1,Text.literal("Этот кристал что-то излучает...").formatted(Formatting.WHITE));
        tooltip.add(2,Text.literal("*дрожит*").formatted(Formatting.BLUE));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return 2;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.AMETHYST_SHARD;
    }

}



