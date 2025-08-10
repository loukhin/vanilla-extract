package com.loukhin.vanillaextract.server.handler;

import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import com.loukhin.vanillaextract.common.network.VanillaExtractNetwork;
import com.loukhin.vanillaextract.server.VanillaExtractServer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerManagerHandler {
    public static void onPlayerConnect(ServerPlayerEntity player) {
        HashMap<String, Boolean> hideSettings = VanillaExtractServer.config().armorHideSettings.users.get(player.getUuid());
        if (hideSettings == null) {
            hideSettings = new HashMap<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    hideSettings.put(slot.getName(), false);
                }
            }
            VanillaExtractServer.config().armorHideSettings.users.put(player.getUuid(), hideSettings);
            VanillaExtractServer.config().writeChanges();
        }
        ServerPlayNetworking.send(player, new ArmorStatePayload(hideSettings));

        VanillaExtractNetwork.updateNearbyEquipment(player, false);
    }
}
