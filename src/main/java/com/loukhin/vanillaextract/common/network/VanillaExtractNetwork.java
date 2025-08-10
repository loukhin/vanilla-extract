package com.loukhin.vanillaextract.common.network;

import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.common.config.ArmorHide;
import com.loukhin.vanillaextract.server.VanillaExtractServer;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaExtractNetwork {
    public static void updateNearbyEquipment(ServerPlayerEntity player, boolean isSource) {
        for (ServerPlayerEntity nearbyPlayer : PlayerLookup.tracking(player.getWorld(), player.getBlockPos())) {
            if (player.equals(nearbyPlayer))
                continue;

            ServerPlayerEntity sourcePlayer;
            ServerPlayerEntity destinationPlayer;

            if (isSource) {
                sourcePlayer = player;
                destinationPlayer = nearbyPlayer;
            } else {
                sourcePlayer = nearbyPlayer;
                destinationPlayer = player;
            }

            List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();
            ArmorHide.slotList.forEach((slot) -> {
                EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot);
                ItemStack equippedItem = sourcePlayer.getEquippedStack(equipmentSlot);
                equipmentList.add(Pair.of(equipmentSlot, equippedItem));
            });

            destinationPlayer.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(sourcePlayer.getId(), equipmentList));
        }
    }

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ArmorStatePayload.ID, (payload, context) -> {
            Map<String, Boolean> hideSettings = payload.armorStates();
            VanillaExtractServer.config().armorHideSettings.users.put(context.player().getUuid(), (HashMap<String, Boolean>) hideSettings);
            VanillaExtractServer.config().writeChanges();
            for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(context.player().getWorld(), context.player().getBlockPos())) {
                if (context.player().equals(eachPlayer)) continue;

                List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();
                hideSettings.forEach((slot, hidden) -> {
                    EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot);
                    if (hidden) {
                        equipmentList.add(Pair.of(equipmentSlot, ItemStack.EMPTY));
                    } else {
                        equipmentList.add(Pair.of(equipmentSlot, context.player().getEquippedStack(equipmentSlot)));
                    }
                });
                eachPlayer.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(context.player().getId(), equipmentList));
            }
        });
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ArmorStatePayload.ID, (payload, context) -> VanillaExtractClient.armorHide = new ArmorHide(payload.armorStates()));
    }
}
