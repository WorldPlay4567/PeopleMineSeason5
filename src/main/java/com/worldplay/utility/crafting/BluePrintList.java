package com.worldplay.utility.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BluePrintList {

    static List<BluePrintRequirement> BLUE_PRINT = new ArrayList<>();
    static boolean is_load = false;
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void load(MinecraftServer server) {
        if(is_load) {
            return;
        }

        try {

            Path worldDir = server.getSavePath(WorldSavePath.ROOT);
            worldDir = worldDir.resolve("builds").normalize();

            if (!Files.exists(worldDir)) {
                Files.createDirectories(worldDir);
            }

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(worldDir, "*.json")) {
                for(Path entry: stream) {

                    try(FileReader fileReader = new FileReader(entry.toFile())) {
                        BluePrintRequirement bluePrintRequirement = new BluePrintRequirement(JsonParser.parseReader(fileReader));
                        BLUE_PRINT.add(bluePrintRequirement);
                    }
                }
            }

            System.out.println(BLUE_PRINT);
            is_load = true;
            System.out.println("Файл успешно создан по пути");
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }



    public static void save(MinecraftServer server){
        int size = 0;

        try {
            Path worldDir = server.getSavePath(WorldSavePath.ROOT);
            worldDir = worldDir.resolve("builds").normalize();

            if (!Files.exists(worldDir)) {
                Files.createDirectories(worldDir);
            }

            try(DirectoryStream<Path> stream = Files.newDirectoryStream(worldDir, "*.json")) {
                for(Path entry: stream) {

                    try(FileWriter writer = new FileWriter(String.valueOf(entry))) {
                        gson.toJson(BLUE_PRINT.get(size).getJson(), writer);
                        size++;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при сохранений файла: " + e.getMessage());
        }
    }
}
