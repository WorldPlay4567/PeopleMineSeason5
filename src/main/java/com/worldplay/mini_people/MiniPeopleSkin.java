package com.worldplay.mini_people;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class MiniPeopleSkin {

    public static final String RUSLAN_SINGER = getSkinUrl("Hi_Im_Leo_");
    public static final String WEWE707 = getSkinUrl("wewe707");




    public static String getSkinUrl(String username) {

        try {
            URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection uuidConnection = (HttpURLConnection) uuidUrl.openConnection();
            uuidConnection.setRequestMethod("GET");
            JsonObject uuidJson = JsonParser.parseReader(new InputStreamReader(uuidConnection.getInputStream())).getAsJsonObject();
            String uuid = uuidJson.get("id").getAsString();

            URL skinUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection skinConnection = (HttpURLConnection) skinUrl.openConnection();
            skinConnection.setRequestMethod("GET");
            JsonObject skinJson = JsonParser.parseReader(new InputStreamReader(skinConnection.getInputStream())).getAsJsonObject();

            return skinJson.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
