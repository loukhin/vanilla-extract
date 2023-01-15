package com.loukhin.vanillaextract.utils;

import net.minecraft.item.ItemStack;

public class CustomPumpkinUtils {
    public static String getCustomModelData(ItemStack stack) {
        if (stack.getNbt() != null) {
            return String.valueOf(stack.getNbt().get("CustomModelData"));
        }
        return "";
    }

    public static boolean isCustomPumpkin(ItemStack stack) {
        boolean isCarvedPumpkin = String.valueOf(stack.getItem()).equals("carved_pumpkin");
        boolean hasCustomModelData = !getCustomModelData(stack).equals("");
        return isCarvedPumpkin && hasCustomModelData;
    }
}
