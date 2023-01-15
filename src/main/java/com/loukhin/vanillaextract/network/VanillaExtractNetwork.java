package com.loukhin.vanillaextract.network;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.access.ServerPlayerEntityAccess;
import com.loukhin.vanillaextract.config.ArmorHide;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaExtractNetwork {
    public static final Identifier ARMOR_STATE_ID = new Identifier("vanillaextract", "armor_state");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ARMOR_STATE_ID, (server, player, handler, buf, responseSender) -> {
            Map<String, Boolean> hideSettings = buf.readMap(PacketByteBuf::readString, PacketByteBuf::readBoolean);
            VanillaExtract.config().armorHideSettings.users.put(player.getUuid(), (HashMap<String, Boolean>) hideSettings);
            VanillaExtract.config().writeChanges();
            for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(player.getWorld(), player.getBlockPos())) {
                if (player.equals(eachPlayer)) continue;

                List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();
                hideSettings.forEach((slot, hidden) -> {
                    EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot);
                    if (hidden) {
                        equipmentList.add(Pair.of(equipmentSlot, ItemStack.EMPTY));
                    } else {
                        equipmentList.add(Pair.of(equipmentSlot, player.getEquippedStack(equipmentSlot)));
                    }
                });
                eachPlayer.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(player.getId(), equipmentList));
            }
        });
    }
}
