package com.worldplay.mixin;

import com.worldplay.mini_people.EntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public class EntityMixin implements EntityDataSaver {
    @Unique
    private NbtCompound persistentData;

    @Override
    public NbtCompound peopleMineSeason5$getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
            NbtList nbtList = new NbtList();
            NbtCompound nbtCompound = new NbtCompound();

            this.persistentData.put("active", nbtCompound);
            this.persistentData.put("list", nbtList);
        }
        System.out.println(persistentData);
        return persistentData;

    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if(persistentData != null) {
            nbt.put("data_rewards", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("data_rewards")) {
            persistentData = nbt.getCompound("data_rewards").get();
        }
    }
}
