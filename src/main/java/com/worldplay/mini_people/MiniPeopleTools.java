package com.worldplay.mini_people;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.worldplay.mini_people.people.PeopleDefault;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class MiniPeopleTools {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("mini-people-edit")
                            .requires(source -> source.hasPermissionLevel(4))
                            .then(literal("add").then(
                                    argument("id", StringArgumentType.word())
                                            .executes(ctx -> execute(ctx, StringArgumentType.getString(ctx, "id")))
                            ))
                            .then(literal("remove").then(
                                    argument("id", StringArgumentType.word())
                                            .executes(ctx -> {
                                                String id = StringArgumentType.getString(ctx, "id");
                                                removeListPeople(ctx.getSource().getPlayer(), id) ;
                                                return 1;
                                            })
                            ))
            );
    });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("mini-people")
                                .executes(commandContext -> {

                                    MiniPeopleGui miniPeopleGui = new MiniPeopleGui(commandContext.getSource().getPlayer());
                                    miniPeopleGui.open();
                                    return 1;
                                }));
        });

    }

    private static int execute(CommandContext<ServerCommandSource> context, String id) {
        ServerCommandSource source = context.getSource();

        return addListPeople(source.getPlayer(), id);
    }

    public static NbtCompound getActivePeople(ServerPlayerEntity serverPlayer) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();

        return nbtCompound.getCompound("active").get();
    }

    public static NbtList getListPeople(ServerPlayerEntity serverPlayer) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();

        return nbtCompound.getList("list").get();
    }

    public static void setActivePeople(ServerPlayerEntity serverPlayer, NbtCompound active) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();

        nbtCompound.put("active", active);
    }

    public static void removeActivePeople(ServerPlayerEntity serverPlayer) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();

        nbtCompound.put("active", null);
    }

    public static int addListPeople(ServerPlayerEntity serverPlayer, String id) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();
        NbtList nbtList = nbtCompound.getList("list").get();

        for(PeopleDefault people : MiniPeople.peopleList) {
            System.out.println(people.id + "  " + id);
            if(Objects.equals(people.id, id)) {

                for(NbtElement nbtElement : nbtList) {
                    NbtCompound nbtCompound1 = nbtElement.asCompound().get();

                    if(nbtCompound1.getString("id").get().equals(id)) {
                        serverPlayer.sendMessage(Text.literal("Не возможно дать мини-чела который уже есть"));
                        return 1;
                    }

                }

                serverPlayer.sendMessage(Text.literal("Успешно добавлен мини-чел"));
                nbtList.add(MiniPeople.getPeopleId(id));

            } else {
                serverPlayer.sendMessage(Text.literal("[!] Очень странно... Его нет в списке..."));
            }
        }

        return 1;
    }

    public static void removeListPeople(ServerPlayerEntity serverPlayer, String id) {
        NbtCompound nbtCompound = ((EntityDataSaver)serverPlayer).peopleMineSeason5$getPersistentData();
        NbtList nbtList = nbtCompound.getList("list").get();

        for(NbtElement nbtElement : nbtList){
            NbtCompound nbtCompound1 = nbtElement.asCompound().get();

            if(nbtCompound1.getString("id").get().equals(id)) {
                serverPlayer.sendMessage(Text.literal("Успешно удален мини-чел"));
                nbtList.remove(nbtCompound1);

                return;
            }
        }

        serverPlayer.sendMessage(Text.literal("Мини чел не удален. Возможно его и так нет"));
    }
}
