package com.example.mixin;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public class VillagerMixin {
	@Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		test7((ServerPlayerEntity) player);
		cir.setReturnValue(ActionResult.FAIL);
		// This code is injected into the start of MinecraftServer.loadWorld()V
	}


	@Unique
	private static void test7(ServerPlayerEntity player) {
		try {

			MerchantGui gui = new MerchantGui(player, false) {

				@Override
				public void onSelectTrade(TradeOffer offer) {
					this.player.sendMessage(Text.literal("Selected Trade: " + this.getOfferIndex(offer)), false);
				}

				@Override
				public void onSuggestSell(TradeOffer offer) {
					if (offer != null && offer.getSellItem() != null) {

						offer.getSellItem().set(DataComponentTypes.CUSTOM_NAME, ((MutableText) player.getName()).append(Text.literal("'s ")).append(offer.getSellItem().getName()));
						this.sendUpdate();
					}
				}
			};

			gui.setTitle(Text.literal("Trades wow!"));
			gui.setIsLeveled(true);
			gui.addTrade(new TradeOffer(
					new TradedItem(Items.STONE),
					new GuiElementBuilder(Items.DIAMOND_AXE)
							.glow()
							.setCount(1)
							.setName(Text.literal("Glowing Axe"))
							.asStack(),
					1,
					0,
					1
			));
			gui.open();

			gui.addTrade(new TradeOffer(
					new TradedItem(Items.EMERALD),
					new GuiElementBuilder(Items.STONE)
							.setCount(16)
							.asStack(),
					100,
					0,
					1
			));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}