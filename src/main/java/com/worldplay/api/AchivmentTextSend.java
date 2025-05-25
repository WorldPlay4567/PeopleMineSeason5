package com.worldplay.api;

import com.worldplay.PeopleMineSeason5;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AchivmentTextSend {

    public static void sendMessage( ServerPlayerEntity player, String text1, String text2) {
        var advancementId =
                Objects.requireNonNull(Identifier.of(PeopleMineSeason5.MOD_ID, "message"));

// Build the advancement itself
        var advancement =
                Advancement.Builder.create()
                        .display(Items.AXOLOTL_BUCKET, Text.literal(text1),
                                Text.literal(text2), null, AdvancementFrame.GOAL,
                                true, true, true)
                        .criterion("custom_criterion",
                                Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                        .build(advancementId);

// Set up the progress for the advancement we're going to send
        var progress = new AdvancementProgress();
        progress.init(AdvancementRequirements.allOf(Set.of("custom_criterion")));
        progress.obtain("custom_criterion");

// Send the advancement, along with the player's "progress" on the advancement
// to the client
        player.networkHandler.sendPacket(
                new AdvancementUpdateS2CPacket(false, Set.of(advancement),
                        Collections.emptySet(), Map.of(advancementId, progress),true));

// Immediately remove the advancement so it doesn't appear in the "Advancements"
// screen (optional)
        player.networkHandler.sendPacket(new AdvancementUpdateS2CPacket(false,
                Collections.emptySet(), Set.of(advancementId), Collections.emptyMap(),true));
    }
}
