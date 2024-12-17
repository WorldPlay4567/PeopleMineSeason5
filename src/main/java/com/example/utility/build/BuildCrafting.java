package com.example.utility.build;


import com.example.items.ItemsInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;



public class BuildCrafting {
    public static boolean playerInteract = false;

    public static void openGUI(boolean update, ServerPlayerEntity player, JsonElement element) {
        BuildGui simpleGui = new BuildGui(ScreenHandlerType.GENERIC_9X3,player,false, element);

        Text message = Text.literal("")

                .append(Text.translatable("space.-49").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal("\u0009").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));

        simpleGui.setTitle(message);

        simpleGui.setBuildGui(simpleGui);
        simpleGui.setAutoUpdate(true);
        progressGUI(player,element,simpleGui);
        simpleGui.open();
    }

    public static void updateGUI(ServerPlayerEntity player, JsonElement element ,BuildGui buildGui){
        buildGui.noComplete = 0;
        progressGUI(player,element,buildGui);
    }

    public static void progressGUI(ServerPlayerEntity player, JsonElement element, BuildGui simpleGui) {
        final int[] SLOTS = new int[]{0};

        JsonObject jsonObject = element.getAsJsonObject();

        JsonArray jsonArray = jsonObject.getAsJsonArray(jsonObject.keySet().iterator().next());

        for(int i = 0; i < jsonArray.size(); i++) {
            if(i >= 8) {
                break;
            }

            JsonElement jsonElement1 = jsonArray.get(i);
            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
            simpleGui.setNeedItem(jsonObject1.get("name").getAsString());


            if(!Objects.equals(jsonObject1.get("name").getAsString(), "minecraft:green_stained_glass_pane")) {
                simpleGui.setSlot(18+i, new GuiElementBuilder(Registries.ITEM.get(Identifier.of(jsonObject1.get("name").getAsString())))
                        .setCount(jsonObject1.get("count").getAsInt())
                        .setName(Text.literal("Нужнее предметы: ").styled(style -> {
                            return style.withColor(Formatting.RED);
                        }).append(Registries.ITEM.get(Identifier.of(jsonObject1.get("name").getAsString())).getName())
                                .append(Text.literal(" " + jsonObject1.get("count").getAsString()))));
                simpleGui.setNoComplete();
            } else {
                simpleGui.setSlot(18+i, new GuiElementBuilder(Items.GREEN_STAINED_GLASS_PANE)
                        .glow(true)
                        .setCount(jsonObject1.get("count").getAsInt())
                        .setName(Text.literal("Выполнено ").styled(style -> {
                                    return style.withColor(Formatting.GREEN);
                                })));

            }

        }

    }


    public static void selectionGUI(ServerPlayerEntity player, SimpleGui simpleGui2) {

        SimpleGui simpleGui;

        JsonElement jsonElement = BuildCraftingList.BUILD_LIST;
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if(simpleGui2 == null) {
            simpleGui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false);
        } else {
            simpleGui = simpleGui2;
        }

        Text message = Text.literal("")

                .append(Text.translatable("space.-49").styled(style -> style.withFont(Identifier.of("space","default")))
                        .append(Text.literal("\u0008").styled(style -> style.withFont(Identifier.of("peoplemineseason5","custom")).withColor(Formatting.WHITE))));

        simpleGui.setTitle(message);

        JsonArray buildArray = jsonObject.getAsJsonArray("build");

        for(int i = 0; i < buildArray.size(); i++) {
            JsonElement jsonElement1 = buildArray.get(i);
            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

            simpleGui.setSlot(18 + i, new GuiElementBuilder(ItemsInit.BLUE_PRINT)
                    .setName(Text.literal("Выбрать цель: ")
                            .styled(style -> {
                        return style.withColor(Formatting.GOLD);
                    })
                            .append(Text.translatable(jsonObject1.keySet().toString()))
                            )

                    .addLoreLine(Text.literal("Нажмите чтобы выбрать цель"))
                    .setCallback(((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT) {
                            player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.PLAYERS,1,1);
                            BuildCraftingList.BUILD_LIST.getAsJsonObject().addProperty("target", jsonObject1.keySet().toString());
                            selectionGUI(player, simpleGui);
                        }
                    })));

        }


        if(!Objects.equals(jsonObject.get("target").getAsString(), "")) {
            String target_name = jsonObject.get("target").getAsString();
            simpleGui.setSlot(13, new GuiElementBuilder(ItemsInit.BLUE_PRINT)

                    .setName(Text.literal("Текущая цель: ").styled(style -> {
                        return style.withColor(Formatting.GOLD);
                    }).append(Text.translatable(target_name)))
                    .addLoreLine(Text.literal("Нажмите чтобы проверить прогресс"))
                    .setCallback((index, clickType, actionType) -> {
                        if (clickType == ClickType.MOUSE_LEFT) {
                            for (JsonElement element : buildArray) {

                                JsonObject jsonObject1 = element.getAsJsonObject();

                                if(Objects.equals(jsonObject1.keySet().toString(), target_name)) {
                                    player.playSoundToPlayer(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.PLAYERS,1,1);
                                    openGUI(false,player,jsonObject1);
                                    break;
                                }
                            }

                        }
                    }));
        } else {
            simpleGui.setSlot(13, new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
                    .setName(Text.literal("Текущая цель: Пусто").styled(style -> {
                        return style.withColor(Formatting.RED);
                    })));
        }

        if(simpleGui2 == null) {
            simpleGui.open();
        }

    }


}
