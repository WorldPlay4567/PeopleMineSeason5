package com.worldplay.utility.villager;

import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

public class VillagerGui extends MerchantGui {

    static boolean update;
    String name;
    VillagerShop villagerShop;
    int activeTrade;

    public VillagerGui(ServerPlayerEntity player, String name) {
        super(player, false);
        this.name = name;
        villagerShop = VillagerShopList.getVillagerShop(name);
        this.setTitle(Text.literal("")
                .append(Text.translatable("space.-470").styled(style -> style.withFont(Identifier.of("space","default"))))
                .append(Text.literal("\u0010").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));
        openGui();

    }

    public void openGui() {
        if(isOpen()) {
            this.merchant.getOffers().clear();
        }

        for(int i = 0; i < villagerShop.getTradeSize(); i++) {
            this.addTrade(villagerShop.getTrade(i));
        }

        if(!isOpen()) {
            super.open();
        }
    }

    @Override
    public void onSelectTrade(TradeOffer offer) {
        this.activeTrade = this.getOfferIndex(offer);
        this.player.sendMessage(Text.literal("Selected Trade: " + this.getOfferIndex(offer)), false);
    }

    @Override
    public void onTick() {
        super.onTick();

        if(getExperience() > 0) {
            int xp = getExperience();
//            int trade = this.getOfferIndex(getSelectedTrade());

            villagerShop.setBuys(xp,activeTrade);
            setExperience(0);
            this.openGui();
        }
    }
}
