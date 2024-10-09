package com.example.utility;

import com.example.PeopleMineSeason5;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.MerchantGui;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

public class ConfigVillager {


    private static final String CONFIG_PATH = "config/villager_trade.json";
    private static JsonObject config;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loadOrCreateConfig() {
        File configFile = new File(CONFIG_PATH);

        if (configFile.exists()) {
            loadConfig();
        } else {
            PeopleMineSeason5.LOGGER.warn("Файл конфигураций не был найден!");
        }
    }

    // Метод для загрузки конфигурации
    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_PATH)) {
            // Парсим JSON и сохраняем его в переменную config
            config = JsonParser.parseReader(reader).getAsJsonObject();
            System.out.println("Конфигурация загружена: " + config.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке конфигурации.");
        }
    }

    public static void getTrader(String villager, MerchantGui gui) {

        JsonArray jsonArray = config.getAsJsonArray(villager);
        for(JsonElement jsonElement : jsonArray) {
            JsonObject trade = jsonElement.getAsJsonObject();

            String buy = trade.get("buy").getAsString();
            int count = trade.get("count").getAsInt();

            String buy2 = trade.get("buy2").getAsString();
            int count2 = trade.get("count2").getAsInt();

            String out = trade.get("out").getAsString();
            int outCount = trade.get("out_count").getAsInt();

            if(!Objects.equals(buy2, "air")) {
                gui.addTrade(
                        new TradeOffer(
                             new TradedItem( Registries.ITEM.get(new Identifier(buy)), count),

                                Optional.of(new TradedItem( Registries.ITEM.get(new Identifier(buy2)), count2)),

                        new GuiElementBuilder( Registries.ITEM.get(new Identifier(out)))
                            .setCount(outCount)
                            .asStack(),
                    9999,
                    1,
                    1
            ));}
            else {
                gui.addTrade(
                        new TradeOffer(

                                new TradedItem( Registries.ITEM.get(new Identifier(buy)), count),
                                new GuiElementBuilder(Registries.ITEM.get(new Identifier(out)))
                                        .setCount(outCount)
                                        .asStack(),
                                9999,
                                1,
                                1
                        ));
            }
        }
    }


    public static void setBuyMoney(int i, String villager) {
        JsonArray jsonArray = config.getAsJsonArray(villager);
        JsonElement jsonElement = jsonArray.get(i);
        JsonObject trade = jsonElement.getAsJsonObject();
        int buyMultiplier = 1;

        if(trade.get("count").getAsInt() >= 64) {
            trade.addProperty("buy", "minecraft:emerald_block");
            trade.addProperty("count", 1);
        } else {
            trade.addProperty("count", trade.get("count").getAsInt() + buyMultiplier);
        }
        saveConfig();
        loadConfig();
    }
    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH)) {
            gson.toJson(config, writer);
            System.out.println("Конфигурация обновлена.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при сохранении конфигурации.");
        }
    }


}
