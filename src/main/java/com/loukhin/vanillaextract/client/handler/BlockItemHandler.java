package com.loukhin.vanillaextract.client.handler;

import com.loukhin.vanillaextract.utils.CustomPumpkinUtils;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;

public class BlockItemHandler {
  public static boolean shouldCancelPlace(ItemPlacementContext context) {
    ItemStack stack = context.getStack();
    return CustomPumpkinUtils.isCustomPumpkin(stack);
  }
}
