package com.loukhin.vanillaextract.server.handler;

import com.loukhin.vanillaextract.server.VanillaExtractServer;
import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerManagerHandler {
    public static void onPlayerConnect(ServerPlayerEntity player) {
        HashMap<String, Boolean> armorHideSettings = VanillaExtractServer.config().armorHideSettings.users.get(player.getUuid());
        if (armorHideSettings == null) {
            armorHideSettings = new HashMap<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    armorHideSettings.put(slot.getName(), false);
                }
            }
            VanillaExtractServer.config().armorHideSettings.users.put(player.getUuid(), armorHideSettings);
            VanillaExtractServer.config().writeChanges();
        }
        ServerPlayNetworking.send(player, new ArmorStatePayload(armorHideSettings));

        for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(player.getServerWorld(), player.getBlockPos())) {
            if (player.equals(eachPlayer)) continue;

            HashMap<String, Boolean> eachPlayerHideSettings = VanillaExtractServer.config().armorHideSettings.users.get(eachPlayer.getUuid());
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();
            eachPlayerHideSettings.forEach((slot, hidden) -> {
                EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot);
                if (hidden) {
                    equipmentList.add(Pair.of(equipmentSlot, ItemStack.EMPTY));
                } else {
                    equipmentList.add(Pair.of(equipmentSlot, eachPlayer.getEquippedStack(equipmentSlot)));
                }
            });
            player.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(eachPlayer.getId(), equipmentList));
        }
    }
}
