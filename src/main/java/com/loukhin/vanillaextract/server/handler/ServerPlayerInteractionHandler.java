package com.loukhin.vanillaextract.server.handler;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.utils.CustomPumpkinUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ServerPlayerInteractionHandler {
    public static void equipOnHead(PlayerEntity player, ItemStack stack) {
        ItemStack headStack = player.getInventory().getStack(39);
        PlayerInventory inventory = player.getInventory();
        int handSlot = inventory.getSlotWithStack(stack);
        ItemStack hat = stack.copyWithCount(1);
        stack.decrement(1);
        if (headStack.isEmpty()) {
            inventory.setStack(39, hat);
            inventory.setStack(handSlot, stack);
        } else {
            inventory.setStack(handSlot, headStack);
            inventory.setStack(39, hat);
            inventory.offerOrDrop(stack);
        }
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
        player.playerScreenHandler.syncState();
    }

    public static ActionResult useOnBlockHandler(ItemStack stack, ItemUsageContext context) {
        if (CustomPumpkinUtils.isCustomPumpkin(stack)) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                equipOnHead(player, stack);
            }
            return ActionResult.FAIL;
        }
        return stack.useOnBlock(context);
    }

    public static void interactItemHandler(PlayerEntity player, ItemStack stack, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (CustomPumpkinUtils.isCustomPumpkin(stack)) {
            equipOnHead(player, stack);
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
