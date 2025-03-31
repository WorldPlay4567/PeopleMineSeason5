package com.worldplay.chat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCord {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(?:\\d+\\s+){2}\\d+");

    public static void init() {
        // Регистрируем обработчик, который решает, отправлять ли сообщение в чат.
        // Если возвращаем false, стандартное сообщение не будет отправлено.
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, parameters) -> {

            String original = message.getSignedContent();

            Matcher matcher = NUMBER_PATTERN.matcher(original);

            String start = "";
            String number = "";
            String end = "";

            System.out.println(matcher);

            if (matcher.find()) {
                start = original.substring(0,matcher.start());
                number = original.substring(matcher.start(),matcher.end());
                end = original.substring(matcher.end());
            } else {
                return true;
            }

            ClickEvent clickEvent = new ClickEvent.RunCommand("/compass " + number);
            HoverEvent hoverEvent = new HoverEvent.ShowText(Text.literal("Нажми чтобы получить компас"));

            // Формируем итоговое сообщение с ником игрока (вы можете изменить форматирование по своему усмотрению)
            MutableText fullMessage = Text.literal("<" + sender.getName().getString() + "> ")
                    .append(Text.literal(start).setStyle(Style.EMPTY))
                    .append(Text.literal(number).setStyle(Style.EMPTY.withClickEvent(clickEvent).withHoverEvent(hoverEvent).withColor(Formatting.RED)))
                    .append(Text.literal(end).setStyle(Style.EMPTY));

            sender.getServer().getPlayerManager().broadcast(fullMessage, false);


            return false;
        });
    }
}
