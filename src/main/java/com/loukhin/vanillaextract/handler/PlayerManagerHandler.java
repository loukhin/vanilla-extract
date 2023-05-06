package com.loukhin.vanillaextract.handler;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.network.VanillaExtractNetwork;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(player.getWorld(), player.getBlockPos())) {
            if (player.equals(eachPlayer)) continue;

            HashMap<String, Boolean> eachPlayerHideSettings = VanillaExtract.config().armorHideSettings.users.get(eachPlayer.getUuid());
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
