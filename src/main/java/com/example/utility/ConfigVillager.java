package com.example.utility;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

public class ConfigVillager {


    private static final String CONFIG_PATH = "config/villager_trade.json";
    private static JsonObject config;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void loadOrCreateConfig() {
        File configFile = new File(CONFIG_PATH);

        if (configFile.exists()) {
            // Если файл существует, загружаем его содержимое
            loadConfig();
        } else {
            // Если файла нет, создаем его
            createDefaultConfig();
        }
    }

    // Метод для загрузки конфигурации
    private static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_PATH)) {
            // Парсим JSON и сохраняем его в переменную config
            config = JsonParser.parseReader(reader).getAsJsonObject();
            System.out.println("Конфигурация загружена: " + config.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке конфигурации.");
        }
    }

    // Метод для создания базовой конфигурации
    private static void createDefaultConfig() {
        // Создаем базовую конфигурацию в виде JSON объекта
        config = new JsonObject();

        JsonObject stone = new JsonObject();
            stone.addProperty("item", "Items.DIAMOND");

        config.add("stone", stone);


        // Записываем эту конфигурацию в файл
        try (FileWriter writer = new FileWriter(CONFIG_PATH)) {
            gson.toJson(config, writer);
            System.out.println("Конфигурация создана по умолчанию.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при создании конфигурации.");
        }
    }

    public static Item getItem(String villager) {

        for (Item item :  Registries.ITEM) {

           String id = String.valueOf(config.get(villager).getAsJsonObject().get("item").getAsString());

            System.out.println(item);
            System.out.println(id);
            if(Objects.equals(id, item.asItem().toString())) {

                return item;
            }

        }
        return null;
    }




//    // Метод для получения значений конфигурации
//    public static String getSetting1() {
//        return config.get("setting1").getAsString();
//    }
//
//    public static int getSetting2() {
//        return config.get("setting2").getAsInt();
//    }
//
//    public static boolean getSetting3() {
//        return config.get("setting3").getAsBoolean();
//    }

    // Метод для обновления значения в конфигурации
    public static void updateConfig(String key, String value) {
        config.addProperty(key, value);
        saveConfig();
    }

    // Метод для сохранения обновленной конфигурации
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
