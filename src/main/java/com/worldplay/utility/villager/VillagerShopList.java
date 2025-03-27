package com.worldplay.utility.villager;

import com.google.gson.*;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.worldplay.PeopleMineSeason5;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VillagerShopList {
    public static String CONFIG_PATH = "config/";
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static List<VillagerShop> register = new ArrayList<>();

    public static void init() {
        register("stone");
    }

    public static VillagerShop getVillagerShop(String name) {
        for(VillagerShop villagerShop: register) {
            if(Objects.equals(villagerShop.name, name)) {
                return villagerShop;
            }
        }
        return null;
    }

    public static VillagerShop register(String nameTrade) {
        try (FileReader reader = new FileReader(CONFIG_PATH + nameTrade + ".json")) {

            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();

            VillagerShop villagerShop = new VillagerShop(config, nameTrade);

            register.add(villagerShop);

            PeopleMineSeason5.LOGGER.info("Конфигурация загружена: " + nameTrade);

            return villagerShop;
        } catch (IOException e) {
            PeopleMineSeason5.LOGGER.error("Ошибка при загрузке конфигурации.");
        }
        return null;
    }

    public static void save() {
        for(VillagerShop villagerShop: register) {
            try (FileWriter writer = new FileWriter(CONFIG_PATH + villagerShop.name + ".json")) {
                villagerShop.save();
                gson.toJson(villagerShop.config, writer);

                System.out.println("Конфигурация обновлена.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Ошибка при сохранении конфигурации.");
            }
        }
    }
}
