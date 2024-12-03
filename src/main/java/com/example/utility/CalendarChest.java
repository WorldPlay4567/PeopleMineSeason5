package com.example.utility;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarChest {

    // Метод для создания календаря в сундуке
    public static void createCalendarChest(World world, BlockPos chestPos) {


        // Проверяем, что на указанной позиции сундук
        if (world.getBlockState(chestPos).getBlock() == Blocks.CHEST) {
            BlockEntity blockEntity = world.getBlockEntity(chestPos);

            if (blockEntity instanceof ChestBlockEntity) {
                ChestBlockEntity chest = (ChestBlockEntity) blockEntity;

                // Создаем календарь на основе текущей даты
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy (EEEE)", Locale.ENGLISH);

                for (int slot = 0; slot < chest.size(); slot++) {
                    if (slot >= 31) break; // В сундуке максимум 27-54 слотов (в зависимости от размера), берем 31 день

                    // Устанавливаем текущую дату и день недели
                    String formattedDate = dateFormat.format(calendar.getTime());

                    // Создаем предмет как символ дня
                    ItemStack dayItem = new ItemStack(Items.PAPER); // Используем бумагу для представления дня
                    dayItem.set(DataComponentTypes.CUSTOM_NAME, Text.literal(formattedDate));
                    // Добавляем метку (NBT) с дополнительной информацией (если необходимо)


                    // Помещаем предмет в сундук
                    chest.setStack(slot, dayItem);

                    // Переходим к следующему дню
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
    }
}
