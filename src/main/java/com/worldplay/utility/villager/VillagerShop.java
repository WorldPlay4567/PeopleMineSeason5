package com.worldplay.utility.villager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.village.TradeOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillagerShop {

    JsonObject config;
    String name;

    List<VillagerTrades> tradesList = new ArrayList<>();

    public VillagerShop(JsonObject jsonObject, String name) {
        this.config = jsonObject;
        this.name = name;

        JsonArray jsonArray = config.getAsJsonArray(name);

        for(JsonElement jsonElement : jsonArray) {
            JsonObject trade = jsonElement.getAsJsonObject();
            VillagerTrades villagerTrades = new VillagerTrades(trade);
            tradesList.add(villagerTrades);
        }
    }

    public int getTradeSize() {
        return tradesList.size();
    }

    public TradeOffer getTrade(int i) {
        return tradesList.get(i).getTrade();
    }

    public void save() {
        JsonArray jsonArray = config.getAsJsonArray(name);

        for(int i = 0; i < tradesList.size(); i++) {
            VillagerTrades villagerTrades = tradesList.get(i);
            JsonElement jsonElement = jsonArray.get(i);
            JsonObject trade = jsonElement.getAsJsonObject();
            JsonObject config1 = villagerTrades.getConfig();

            for (Map.Entry<String, JsonElement> entry : config1.entrySet()) {
                trade.add(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setBuys(int xp, int trade) {
        tradesList.get(trade).setBuys(xp);
    }
}