package com.worldplay.utility.villager;

import com.google.gson.JsonObject;
import com.worldplay.PeopleMineSeason5;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Objects;
import java.util.Optional;

public class VillagerTrades {
    short count;
    short count2;
    short count_out;

    String buy;
    String buy2;
    String buy_out;
    
    int buys;
    float multiplier; //  # Коэффициент роста
    float rate_multiplier; // # Коэффициент ускорения на поздних этапах

    public VillagerTrades(JsonObject trade) {
        this.buy = trade.get("buy").getAsString();
        this.count = trade.get("count").getAsShort();

        this.buy2 = trade.get("buy2").getAsString();
        this.count2 = trade.get("count2").getAsShort();

        this.buy_out = trade.get("out").getAsString();
        this.count_out = trade.get("out_count").getAsShort();

        this.multiplier = trade.get("multiplier").getAsFloat();
        this.rate_multiplier = trade.get("rate_multiplier").getAsFloat();

        this.buys = trade.get("buys").getAsInt();
    }

    private TradedItem itemTrade(short count, String name) {
        int price = calculate_price(count);
        if(Objects.equals(name, "minecraft:emerald")) {
            if(price > 64) {
                int count1 = price / 64;
                return new TradedItem(Registries.ITEM.get(Identifier.of("minecraft:emerald_block")), count1);
            }
        }
        return new TradedItem(Registries.ITEM.get(Identifier.of(name)), price);
    }

    public void setBuys(int count) {
        this.buys += count * this.count;
    }

    public TradeOffer getTrade() {

        PeopleMineSeason5.LOGGER.info("Trade :{} {} {}", buy, count, buys);
        PeopleMineSeason5.LOGGER.info("Trade :{} {} {}", buy2, count2, buys);
        PeopleMineSeason5.LOGGER.info("Trade :{} {} {} {} {}", buy_out, count_out, buys, multiplier, rate_multiplier);
        PeopleMineSeason5.LOGGER.info("Trade ===========================");

        if(!Objects.equals(buy2, "minecraft:air")) {
            return new TradeOffer(
                    itemTrade(count,buy),
                    Optional.of(itemTrade(count2,buy2)),

                    new GuiElementBuilder( Registries.ITEM.get(Identifier.of(buy_out)))
                            .setCount(count_out)
                            .asStack(),
                    9999,
                    1,
                    1
            );
        }

        return new TradeOffer(
                itemTrade(count,buy),
                new GuiElementBuilder( Registries.ITEM.get(Identifier.of(buy_out)))
                        .setCount(count_out)
                        .asStack(),
                9999,
                1,
                1
        );

    }

    public int calculate_price(int low_price) {
        return (int) (low_price * Math.pow( (1 + multiplier * Math.log(buys + 1)), rate_multiplier));
    }

    public JsonObject getConfig() {
        JsonObject trade = new JsonObject();
        trade.addProperty("buy", buy);
        trade.addProperty("count", count);

        trade.addProperty("buy2", buy2);
        trade.addProperty("count2", count2);

        trade.addProperty("out", buy_out);
        trade.addProperty("out_count", count_out);

        trade.addProperty("multiplier", multiplier);
        trade.addProperty("rate_multiplier", rate_multiplier);

        trade.addProperty("buys",buys);

        return trade;
    }
}
