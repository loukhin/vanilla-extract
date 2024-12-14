package com.loukhin.vanillaextract.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class CustomPumpkinUtils {
    static final Identifier ID = Identifier.of("minecraft", "carved_pumpkin");

    public static boolean isCustomPumpkin(ItemStack stack) {
        boolean isCarvedPumpkin = Registries.ITEM.getId(stack.getItem()).equals(ID);
        Identifier itemId = stack.get(DataComponentTypes.ITEM_MODEL);
        if (itemId == null) return false;

        boolean hasCustomModel = !itemId.equals(ID);
        return isCarvedPumpkin && hasCustomModel;
    }
}
