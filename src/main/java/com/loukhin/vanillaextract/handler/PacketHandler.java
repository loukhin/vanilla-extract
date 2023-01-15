package com.loukhin.vanillaextract.handler;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.access.ServerPlayerEntityAccess;
import com.loukhin.vanillaextract.config.ArmorHide;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class PacketHandler {
    public static void onEntityEquipmentUpdate(ServerWorld world, EntityEquipmentUpdateS2CPacket equipmentUpdatePacket) {
        Entity entity = world.getEntityById(equipmentUpdatePacket.getId());
        if (entity instanceof ServerPlayerEntity player) {
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = equipmentUpdatePacket.getEquipmentList();
            for (int i = 0; i < equipmentList.size(); i++) {
                Pair<EquipmentSlot, ItemStack> current = equipmentList.get(i);
                EquipmentSlot equipmentSlot = current.getFirst();
                ItemStack item = current.getSecond();

                if (equipmentSlot.getType().equals(EquipmentSlot.Type.ARMOR) && !(item.getItem() instanceof ElytraItem)) {
                    boolean isHidden = VanillaExtract.config().armorHideSettings.users.get(player.getUuid()).get(equipmentSlot.getName());
                    if (isHidden) {
                        equipmentList.set(i, Pair.of(equipmentSlot, ItemStack.EMPTY));
                    }
                }
            }
        }
    }
}
