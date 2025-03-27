package com.worldplay.mixin;

import com.worldplay.utility.builds.update.UpdateGui;
import com.worldplay.utility.crafting.VillagerBluePrint;
import com.worldplay.utility.villager.VillagerGui;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin {


	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

		VillagerEntity villager = (VillagerEntity) (Object) this;
		Set<String> tags = villager.getCommandTags();

		for (String s: tags) {
			System.out.println(s);
			if("structureVillager".equals(s)) {
				UpdateGui updateGui = new UpdateGui((ServerPlayerEntity) player, villager);
				updateGui.openUpdateGui();
			}
		}

		if (villager.hasCustomName() && "Каменщик".equals(villager.getCustomName().getString())) {
			VillagerGui villagerGui = new VillagerGui((ServerPlayerEntity) player, "stone");
//			test7((ServerPlayerEntity) player, ConfigVillagerRegister.STONE);
			cir.setReturnValue(ActionResult.FAIL);
		}
//		if (villager.hasCustomName() && "Фермер".equals(villager.getCustomName().getString())) {
//			test7((ServerPlayerEntity) player, ConfigVillagerRegister.PRODUCT);
//			cir.setReturnValue(ActionResult.FAIL);
//		}
		if (villager.hasCustomName() && "Строитель".equals(villager.getCustomName().getString())) {
			new VillagerBluePrint((ServerPlayerEntity) player);
		}
		// This code is injected into the start of MinecraftServer.loadWorld()V
	}
	
//	@Unique
//	private static void test7(ServerPlayerEntity player, ConfigVillager configVillager) {
//		try {
//			MerchantGui gui = new MerchantGui(player, false) {
//
//				@Override
//				public void onSelectTrade(TradeOffer offer) {
//					this.player.sendMessage(Text.literal("Selected Trade: " + this.getOfferIndex(offer)), false);
//				}
//
//				@Override
//				public void onSuggestSell(TradeOffer offer) {
//					if (offer != null && offer.getSellItem() != null) {
//						this.sendUpdate();
//					}
//
//				}
//
//				@Override
//				public void setExperience(int experience) {
//					System.out.println("exp : " + experience);
//				}
//
//				@Override
//				public boolean onTrade(TradeOffer offer) {
//					System.out.println("BUY : " + this.getExperience());
//					int i = this.getOfferIndex(offer);
////					ConfigVillager.setBuyMoney(i, "stone");
//					configVillager.setBuyMoney(i);
//					this.sendUpdate();
//					return true;
//				}
//
//
//				@Override
//				public void onClose() {
//					super.onClose();
//					System.out.println("TEST EXP GET : " + this.getExperience());
//				}
//			};
//
//			gui.setTitle(Text.literal("Продавец камня"));
//			gui.setIsLeveled(true);
//
//
////			gui.addTrade(new TradeOffer(
////					new TradedItem(Items.EMERALD, 1),
////					new GuiElementBuilder(Items.ANDESITE)
////							.glow()
////							.setCount(16)
////							.asStack(),
////					1,
////					1,
////					1
////			));
//
////			ConfigVillager.getTrader("stone", gui);
//			configVillager.getTrader(gui);
//
//			gui.open();
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}