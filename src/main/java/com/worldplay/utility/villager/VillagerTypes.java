package com.worldplay.utility.villager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.worldplay.PeopleMineSeason5;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.ai.brain.task.FindInteractionTargetTask;

public class VillagerTypes {

    Map<Identifier, String> loadedFiles = new HashMap<>();
    String FOLDER = "villager_trade";

    public void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of(PeopleMineSeason5.MOD_ID, FOLDER);
            }

            @Override
            public void reload(ResourceManager manager) {

                Map<Identifier, Resource> resources = manager.findResources(FOLDER, id ->
                        id.getNamespace().equals(PeopleMineSeason5.MOD_ID) && id.getPath().endsWith(".json")
                );

                for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
                    Identifier id = entry.getKey();
                    Resource resource = entry.getValue();
                    try (InputStream stream = resource.getInputStream()) {
                        String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
                        Gson gson = new Gson();
                        gson.toJson(content);



                        // Сохраняем содержимое файла в Map
                        loadedFiles.put(id, content);
                        
                        // Здесь можно выполнять дополнительные проверки содержимого файла.
                        if (content.contains("какое-то_значение")) {
                            System.out.println("Файл " + id + " содержит нужное значение.");
                        } else {
                            System.out.println("Файл " + id + " не содержит нужное значение.");
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка при чтении файла " + id);
                        e.printStackTrace();
                    }
                }

                System.out.println("Всего загружено файлов: " + loadedFiles.size());
            }
        });

        }
}
