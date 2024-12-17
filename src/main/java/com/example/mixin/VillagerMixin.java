package com.example.mixin;

import com.example.PeopleMineSeason5;
import com.example.utility.ConfigVillager;
import com.example.utility.ConfigVillagerRegister;
import com.example.utility.build.BuildCrafting;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ObjectInputFilter;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin {


	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

		VillagerEntity villager = (VillagerEntity) (Object) this;

		if (villager.hasCustomName() && "Каменщик".equals(villager.getCustomName().getString())) {
			test7((ServerPlayerEntity) player, ConfigVillagerRegister.STONE);
			cir.setReturnValue(ActionResult.FAIL);
		}
		if (villager.hasCustomName() && "Фермер".equals(villager.getCustomName().getString())) {
			test7((ServerPlayerEntity) player, ConfigVillagerRegister.PRODUCT);
			cir.setReturnValue(ActionResult.FAIL);
		}
		if (villager.hasCustomName() && "Строитель".equals(villager.getCustomName().getString()) && !BuildCrafting.playerInteract) {
		BuildCrafting.selectionGUI((ServerPlayerEntity) player, null);
		}
		// This code is injected into the start of MinecraftServer.loadWorld()V
	}


	@Unique
	private static void test7(ServerPlayerEntity player, ConfigVillager configVillager) {
		try {
			MerchantGui gui = new MerchantGui(player, false) {

				@Override
				public void onSelectTrade(TradeOffer offer) {
					this.player.sendMessage(Text.literal("Selected Trade: " + this.getOfferIndex(offer)), false);
				}

				@Override
				public void onSuggestSell(TradeOffer offer) {
					if (offer != null && offer.getSellItem() != null) {
						this.sendUpdate();
					}

				}
				@Override
				public void setExperience(int experience) {

				}


				@Override
				public boolean onTrade(TradeOffer offer) {

					int i = this.getOfferIndex(offer);
//					ConfigVillager.setBuyMoney(i, "stone");
					configVillager.setBuyMoney(i);
					this.sendUpdate();
					return true;
				}

			};

			gui.setTitle(Text.literal("Продавец камня"));
			gui.setIsLeveled(true);


//			gui.addTrade(new TradeOffer(
//					new TradedItem(Items.EMERALD, 1),
//					new GuiElementBuilder(Items.ANDESITE)
//							.glow()
//							.setCount(16)
//							.asStack(),
//					1,
//					1,
//					1
//			));

//			ConfigVillager.getTrader("stone", gui);
			configVillager.getTrader(gui);

			gui.open();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}