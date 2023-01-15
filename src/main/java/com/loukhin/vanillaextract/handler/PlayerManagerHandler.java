package com.loukhin.vanillaextract.handler;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.network.VanillaExtractNetwork;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerManagerHandler {
    public static void onPlayerConnect(ServerPlayerEntity player) {
        HashMap<String, Boolean> hideSettings = VanillaExtract.config().armorHideSettings.users.get(player.getUuid());
        if (hideSettings == null) {
            hideSettings = new HashMap<>();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    hideSettings.put(slot.getName(), false);
                }
            }
            VanillaExtract.config().armorHideSettings.users.put(player.getUuid(), hideSettings);
            VanillaExtract.config().writeChanges();
        }
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeMap(hideSettings, PacketByteBuf::writeString, PacketByteBuf::writeBoolean);
        ServerPlayNetworking.send(player, VanillaExtractNetwork.ARMOR_STATE_ID, buf);
    }
}
