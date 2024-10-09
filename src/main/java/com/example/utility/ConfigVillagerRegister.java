package com.example.utility;

public class ConfigVillagerRegister {

    public static ConfigVillager STONE = new ConfigVillager("stone");
    public static ConfigVillager PRODUCT = new ConfigVillager("product");


    public static void init() {
        STONE.loadConfig();
        PRODUCT.loadConfig();
    }

    public static void save() {
        STONE.saveConfig();
        PRODUCT.saveConfig();
    }
}
