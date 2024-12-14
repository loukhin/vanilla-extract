package com.loukhin.vanillaextract.common.config;

import net.minecraft.entity.EquipmentSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArmorHide {
    public static final ArrayList<String> slotList = getArmorSlotList();
    public final HashMap<String, Boolean> state = new HashMap<>();

    private static ArrayList<String> getArmorSlotList() {
        ArrayList<String> slotList = new ArrayList<>();
        Arrays.stream(EquipmentSlot.values())
                .filter((equipmentSlot) -> equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)
                .sorted((slot1, slot2) -> slot2.getEntitySlotId() - slot1.getEntitySlotId())
                .forEach((equipmentSlot) -> {
                    slotList.add(equipmentSlot.getName());
                });
        return slotList;
    }

    public ArmorHide(Map<String, Boolean> hideSettings) {
        this.state.putAll(hideSettings);
    }

    public static ArmorHide empty() {
        HashMap<String, Boolean> state = new HashMap<>();
        getArmorSlotList().forEach((slot) -> {
            state.put(slot, false);
        });
        return new ArmorHide(state);
    }
}
