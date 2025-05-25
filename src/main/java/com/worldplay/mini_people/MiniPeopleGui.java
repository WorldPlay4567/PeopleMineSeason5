package com.worldplay.mini_people;

import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import static com.worldplay.mini_people.MiniPeopleSkin.RUSLAN_SINGER;
import static com.worldplay.mini_people.MiniPeopleSkin.WEWE707;

public class MiniPeopleGui extends SimpleGui {

    public MiniPeopleGui(ServerPlayerEntity player) {
        super(ScreenHandlerType.SHULKER_BOX, player, true);

        try {
            NbtList nbtList = MiniPeopleTools.getListPeople(player);

            for(int i = 0; i < nbtList.size(); i ++) {
                NbtElement nbtElement = nbtList.get(i);
                NbtCompound nbtCompound = nbtElement.asCompound().get();

                MiniPeople.People people = MiniPeople.getPeople(nbtCompound.getString("id").get());

                setSlot(i, GuiElementBuilder.from(Items.PLAYER_HEAD.getDefaultStack())
                        .setSkullOwner(people.head)
                        .setName(Text.literal(people.id))
                        .setCallback(((index, clickType, actionType) -> {
                            if (clickType == ClickType.MOUSE_LEFT) {
                                MiniPeople.spawnPet(player, people);
                            }
                        }))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean open() {
        return super.open();
    }
}
