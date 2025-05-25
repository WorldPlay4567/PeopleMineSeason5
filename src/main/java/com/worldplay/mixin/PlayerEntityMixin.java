package com.worldplay.mixin;


import com.worldplay.utility.villager.anvil.ICustomScreenState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ICustomScreenState {
    @Unique
    private boolean customScreenOpen = false;

    @Override
    public boolean isCustomScreenOpen() {
        return customScreenOpen;
    }

    @Override
    public void setCustomScreenOpen(boolean state) {
        this.customScreenOpen = state;
    }
}