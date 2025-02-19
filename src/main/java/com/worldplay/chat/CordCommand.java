package com.worldplay.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import eu.pb4.sgui.api.GuiHelpers;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class CordCommand {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Регистрируем команду /compass с обязательным аргументом "cords"
            dispatcher.register(literal("compass")
                    .then(argument("x", IntegerArgumentType.integer())
                            .then(argument("y", IntegerArgumentType.integer())
                                    .then(argument("z", IntegerArgumentType.integer())
                                            .executes(commandContext -> {
                                                int x = IntegerArgumentType.getInteger(commandContext, "x");
                                                int y = IntegerArgumentType.getInteger(commandContext, "y");
                                                int z = IntegerArgumentType.getInteger(commandContext, "z");

                                                ItemStack itemStack = new ItemStack(Items.COMPASS, 1);
                                                BlockPos b = new BlockPos(x,y,z);

                                                LodestoneTrackerComponent l = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(World.OVERWORLD, b)), true);
                                                itemStack.set(DataComponentTypes.LODESTONE_TRACKER,  l);

                                                List<Text> list = new ArrayList<>();
                                                list.add(Text.literal(""));
                                                list.add(Text.literal("Показывает координаты: " + x + " " + y + " " + z).setStyle(Style.EMPTY.withColor(0xeb66ff).withItalic(false)));
                                                LoreComponent loreComponent = new LoreComponent(list);
                                                itemStack.set(DataComponentTypes.LORE, loreComponent);

                                                itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Намагничений компас дружбы").setStyle(Style.EMPTY.withItalic(false)));


                                                ServerPlayerEntity serverPlayer = commandContext.getSource().getPlayer();
                                                assert serverPlayer != null;

                                                serverPlayer.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.PLAYERS, 1,1);
                                                serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(serverPlayer.currentScreenHandler.syncId,0, 44, itemStack));

                                                return 1;
                                            })

                    )
            )));
        });
    }




}
