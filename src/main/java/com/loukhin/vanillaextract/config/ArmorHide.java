package com.loukhin.vanillaextract.config;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ArmorHide {
    public static final ArrayList<String> slotList = getArmorSlotList();
    public final HashMap<String, Boolean> state = new HashMap<>();

    private static ArrayList<String> getArmorSlotList() {
        ArrayList<String> slotList = new ArrayList<>();
        Arrays.stream(EquipmentSlot.values())
                .filter((equipmentSlot) -> equipmentSlot.getType() == EquipmentSlot.Type.ARMOR)
                .sorted((slot1, slot2) -> slot2.getEntitySlotId() - slot1.getEntitySlotId())
                .forEach((equipmentSlot) -> {
                    slotList.add(equipmentSlot.getName());
                });
        return slotList;
    }

    public ArmorHide(NbtCompound nbt) {
        for(String slot: slotList.toArray(new String[0])) {
            this.state.put(slot, nbt.getBoolean(slot));
        }
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        for(String slot: slotList.toArray(new String[0])) {
            nbt.putBoolean(slot, this.state.get(slot));
        }
        return nbt;
    }
}
