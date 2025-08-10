package com.loukhin.vanillaextract.server.handler;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.server.VanillaExtractServer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PacketHandler {
    public static void onEntityEquipmentUpdate(ServerWorld world, EntityEquipmentUpdateS2CPacket equipmentUpdatePacket) {
        Entity entity = world.getEntityById(equipmentUpdatePacket.getEntityId());
        if (entity instanceof ServerPlayerEntity player) {
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = equipmentUpdatePacket.getEquipmentList();
            for (int i = 0; i < equipmentList.size(); i++) {
                Pair<EquipmentSlot, ItemStack> current = equipmentList.get(i);
                EquipmentSlot equipmentSlot = current.getFirst();
                ItemStack item = current.getSecond();

//                AttributeModifiersComponent a = item.getItem().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
//                if (a == null) return;
//                boolean isArmor = a.modifiers().stream().anyMatch((modifier) -> modifier.attribute().matchesId(Identifier.of("minecraft:armor")));
                if (equipmentSlot.getType().equals(EquipmentSlot.Type.HUMANOID_ARMOR)) {
                    HashMap<String, Boolean> userDatas = VanillaExtractServer.config().armorHideSettings.users.get(player.getUuid());
                    if (userDatas != null) {
                        boolean isHidden = userDatas.get(equipmentSlot.getName());
                        if (isHidden) {
                            equipmentList.set(i, Pair.of(equipmentSlot, ItemStack.EMPTY));
                        }
                    }
                }
            }
        }
    }

    public static void handle(ServerPlayerEntity serverPlayer, Packet<?> packet) {
        if (packet instanceof EntityEquipmentUpdateS2CPacket equipmentUpdatePacket) {
            PacketHandler.onEntityEquipmentUpdate(serverPlayer.getWorld(), equipmentUpdatePacket);
        }
    }
}
