package com.worldplay.utility.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.util.Resource;
import com.worldplay.PeopleMineSeason5;
import com.worldplay.utility.builds.BuildCraftingConfig;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.injection.Inject;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BluePrintRequirement {

    boolean is_complete;
    String nameBluePrint;
    List<Items> listRequirement = new ArrayList<>();

    public BluePrintRequirement(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("items").getAsJsonArray();

        is_complete = jsonObject.get("is_complete").getAsBoolean();
        nameBluePrint = jsonObject.get("name").getAsString();

        for(JsonElement jsonElement1 : jsonArray) {
            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

            Identifier identifier = Identifier.of(jsonObject1.get("name").getAsString());
            int count = jsonObject1.get("count").getAsInt();
            boolean is_compile = jsonObject1.get("is_complete").getAsBoolean();

            listRequirement.add(new Items(identifier,count,is_compile));
        }
    }

    public JsonElement getJson() {

        JsonArray jsonArray = new JsonArray();

        for(Items items : listRequirement) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("is_complete", items.is_compile);
            jsonObject.addProperty("count", items.count);
            jsonObject.addProperty("name", String.valueOf(items.item));

            jsonArray.add(jsonObject);
        }

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("is_complete", is_complete);
        jsonObject.addProperty("name", nameBluePrint);
        jsonObject.add("items", jsonArray);

        return jsonObject;
    }

    public boolean is_allComplete() {
        int no_complete = 0;

        for(Items items : listRequirement) {
            if(!items.is_compile) {
                no_complete ++;
            }
        }

        return no_complete <= 0;
    }

    public List<Items> getListRequirement() {
        return listRequirement;
    }

    public static class Items {
        Identifier item;
        int count;
        boolean is_compile;

        private Items(Identifier item, int count, boolean is_compile) {
            this.item = item;
            this.count = count;
            this.is_compile = is_compile;
        }
    }
}


