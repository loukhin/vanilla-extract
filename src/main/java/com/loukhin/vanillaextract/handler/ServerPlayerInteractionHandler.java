package com.loukhin.vanillaextract.handler;

import com.loukhin.vanillaextract.utils.CustomPumpkinUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ServerPlayerInteractionHandler {
    public static void equipIfSlotAvailable(PlayerEntity player, ItemStack stack) {
        ItemStack headSlot = player.getInventory().getArmorStack(3);
        if (headSlot.isEmpty()) {
            ItemStack hat = stack.copy();
            hat.setCount(1);
            player.getInventory().armor.set(3, hat);
            player.getInventory().removeOne(stack);
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
        }
        player.playerScreenHandler.syncState();
    }

    public static ActionResult useOnBlockHandler(ItemStack stack, ItemUsageContext context) {
        if (CustomPumpkinUtils.isCustomPumpkin(stack)) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                equipIfSlotAvailable(player, stack);
            }
            return ActionResult.FAIL;
        }
        return stack.useOnBlock(context);
    }

    public static void interactItemHandler(PlayerEntity player, ItemStack stack, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (CustomPumpkinUtils.isCustomPumpkin(stack)) {
            equipIfSlotAvailable(player, stack);
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
