package com.worldplay.api;


import com.worldplay.PeopleMineSeason5;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import net.minecraft.util.Identifier;

public class RPFast {
    public static Identifier getItemModel(String name) {
        return ResourcePackExtras.bridgeModel(Identifier.of(PeopleMineSeason5.MOD_ID, "item/" + name));
    }
}
