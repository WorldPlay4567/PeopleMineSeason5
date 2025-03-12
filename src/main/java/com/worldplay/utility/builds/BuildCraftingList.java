package com.worldplay.utility.builds;

import com.google.gson.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.WorldSavePath;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BuildCraftingList {
    static Path logFilePath;
    static JsonElement BUILD_LIST;

    public static void onServerStart(MinecraftServer server) {

        String command = "polymer generate-pack reload";
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, server.getCommandSource());
        try {
            dispatcher.execute(parseResults);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            // Получаем путь к корневой папке мира
            Path worldDir = server.getSavePath(WorldSavePath.ROOT);

            // Формируем путь к файлу внутри этой папки
            logFilePath = worldDir.resolve("build_progress.json");

            // Убедимся, что директория существует
            if (!Files.exists(worldDir)) {
                Files.createDirectories(worldDir);
            }

            // Убедимся, что файл существует, иначе создаем его
            if (!Files.exists(logFilePath)) {
                Files.createFile(logFilePath);
                BuildCraftingConfig.defaultConfig(logFilePath);
            }

            //Чтение файла
            try(FileReader fileReader = new FileReader(String.valueOf(logFilePath))) {
                BUILD_LIST = JsonParser.parseReader(fileReader);
            }

            System.out.println("Файл успешно создан по пути: " + logFilePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    public static void onServerClose(MinecraftServer server) {
        try {
            try (FileWriter writer = new FileWriter(logFilePath.toFile(), false)) {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting() // Включает красивый вывод
                        .create();

                writer.write(gson.toJson(BUILD_LIST));
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл чата: " + e.getMessage());
        }
    }

    public static void setComplete(String name) {
        JsonObject jsonObject =  BuildCraftingList.BUILD_LIST.getAsJsonObject();
        JsonArray buildArray = jsonObject.getAsJsonArray("complete");

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty(name,true);

        buildArray.add(jsonObject1);
    }

    public static boolean isComplete(String name) {
        JsonObject jsonObject =  BuildCraftingList.BUILD_LIST.getAsJsonObject();
        JsonArray buildArray = jsonObject.getAsJsonArray("complete");

        for(JsonElement jsonElement: buildArray) {
            JsonObject jsonObject1 = jsonElement.getAsJsonObject();
            System.out.println(jsonObject1.keySet().iterator().next());
            System.out.println(name);
            if(Objects.equals(jsonObject1.keySet().iterator().next(), name)) {
                return true;
            }
        }

        return false;
    }

}
