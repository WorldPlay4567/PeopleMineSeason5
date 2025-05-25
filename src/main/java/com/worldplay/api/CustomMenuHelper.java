package com.worldplay.api;

import com.mojang.datafixers.kinds.IdF;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CustomMenuHelper {

    public static MutableText TEST = registerTwoLayer("\u0006","\u0007");
    public static MutableText BLUE_PRINT = registerTwoLayer("\u0011","\u0012");


    public static void init() {}

    public static MutableText registerOneLayer(String idMenu) {
        return Text.literal("")
                .append(Text.translatable("space.-470").styled(style -> style.withFont(Identifier.of("space","default"))))
                .append(Text.literal(idMenu).styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE)));
    }

    public static MutableText registerTwoLayer(String idMenu, String idMenu2) {
        return Text.literal("")
                .append(Text.translatable("space.-86").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal(idMenu).styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))))

                .append(Text.translatable("space.-2").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal(idMenu2).styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));

    }
}
