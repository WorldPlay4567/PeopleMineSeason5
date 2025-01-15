package com.worldplay.utility.builds;

import com.worldplay.items.ItemsInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuildGui extends SimpleGui {
    SimpleInventory simpleInventory = new SimpleInventory(1);
    List<String> needItem = new CopyOnWriteArrayList<>();
    JsonElement element;
    BuildGui buildGui;

    int noComplete = 0;

    public BuildGui(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots, JsonElement element) {
        super(type, player, manipulatePlayerSlots);
        this.setSlotRedirect(13,new Slot(simpleInventory,0,0,0));
        this.element = element;
    }

    public void setNoComplete() {
        noComplete ++;
    }

    @Override
    public void onOpen() {
        super.onOpen();

        player.sendMessage(Text.literal(String.valueOf(noComplete)));
    }

    @Override
    public void onClose() {
        super.onClose();
        BuildCrafting.playerInteract = false;

        ItemEntity item = new ItemEntity(player.getWorld(),player.getX(),player.getY() + 1,player.getZ(),simpleInventory.getStack(0));
        player.getWorld().spawnEntity(item);
    }

    @Override
    public void onTick() {
        BuildCrafting.playerInteract = true;

        JsonObject jsonObject =  BuildCraftingList.BUILD_LIST.getAsJsonObject();
        JsonArray buildArray = jsonObject.getAsJsonArray("build");

        for(JsonElement jsonElement1: buildArray) {
            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

            if(Objects.equals(jsonObject1.keySet().iterator().next(), element.getAsJsonObject().keySet().iterator().next())) {

                System.out.println("Тест: " + jsonObject1.keySet().iterator().next());
                System.out.println("Тест: " + BuildCraftingList.isComplete(jsonObject1.keySet().iterator().next()));

                if(noComplete == 0 && !BuildCraftingList.isComplete(jsonObject1.keySet().iterator().next())) {
                    BuildCraftingList.setComplete(jsonObject1.keySet().iterator().next());

                    ItemStack itemStack = new ItemStack(ItemsInit.BLUE_PRINT, 1);
                    NbtCompound nbtCompound = new NbtCompound();
                    nbtCompound.putString("structure", jsonObject1.keySet().iterator().next());
                    itemStack.set(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(nbtCompound));
                    simpleInventory.setStack(0, itemStack);
                }
            }


        }

        if(needItem != null) {

            for(String s: needItem) {
//                System.out.println("NEEDITEM" + needItem);
//                System.out.println(s);

                //Получаем предмет который уже лежит в слоте
                Identifier identifier = Registries.ITEM.getId(simpleInventory.getStack(0).getItem());

                //Если он лежит идем дальше
                if(Objects.equals(s, identifier.toString())) {

                    //Получаем список строений


                    for(JsonElement jsonElement1: buildArray) {
                        JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                        if(jsonObject1.keySet().iterator().next() == element.getAsJsonObject().keySet().iterator().next()) {
                            //ПОлучаем строение
                            JsonArray jsonArray = jsonObject1.getAsJsonArray(jsonObject1.keySet().iterator().next());

                            for (JsonElement itemElement : jsonArray) {
                                JsonObject itemObject = itemElement.getAsJsonObject();
                                // Проверка имени предмета
                                if (itemObject.get("name").getAsString().equals(identifier.toString())) {

                                    if(itemObject.get("count").getAsInt() - simpleInventory.getStack(0).getCount() >= 1) {
                                        itemObject.addProperty("count", itemObject.get("count").getAsInt() - simpleInventory.getStack(0).getCount());
                                    }else {
                                        itemObject.addProperty("name", "minecraft:green_stained_glass_pane");
                                        itemObject.addProperty("count", 1);
                                    }



                                    player.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_GUITAR.value(), SoundCategory.PLAYERS,1,1);
                                    BuildCraftingList.BUILD_LIST = jsonObject;
                                    simpleInventory.setStack(0, Items.AIR.getDefaultStack());
                                    BuildCrafting.updateGUI(player,element,buildGui);

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setBuildGui(BuildGui buildGui) {
        this.buildGui = buildGui;
    }

    public void setNeedItem(String name) {
        needItem.add(name);
    }




}
