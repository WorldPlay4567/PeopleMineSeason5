package com.worldplay.utility.builds;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BuildCraftingConfig {
    static String build_configDefault = """
{
    "target": "",
    "build": [
        {
            "build.shop_farmer": [
                { "name": "minecraft:diamond", "count": 999 },
                { "name": "minecraft:oak_log", "count": 64 }
            ]
        },
        {
            "build.shop_stone": [
                { "name": "minecraft:iron_block", "count": 64 },
                { "name": "minecraft:oak_log", "count": 64 }
            ]
        }
    ]
}
""";

    public static void defaultConfig(Path path) {
        try(FileWriter writer = new FileWriter(path.toFile(), false)) {
            writer.write(String.valueOf(build_configDefault));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
