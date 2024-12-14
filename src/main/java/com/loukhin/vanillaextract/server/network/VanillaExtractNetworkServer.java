package com.loukhin.vanillaextract.server.network;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import com.loukhin.vanillaextract.server.VanillaExtractServer;
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
import java.util.Map;

public class VanillaExtractNetworkServer {
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ArmorStatePayload.ID, (payload, context) -> {
            Map<String, Boolean> hideSettings = payload.armorStates();
            VanillaExtractServer.config().armorHideSettings.users.put(context.player().getUuid(), (HashMap<String, Boolean>) hideSettings);
            VanillaExtractServer.config().writeChanges();
            for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(context.player().getServerWorld(), context.player().getBlockPos())) {
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

}
