package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.client.handler.BlockItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

@Mixin(BlockItem.class)
public class BlockItemMixin {
  @Inject(at = @At("HEAD"), method = "place*", cancellable = true)
  public void onPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
    if (BlockItemHandler.shouldCancelPlace(context)) {
      cir.setReturnValue(ActionResult.FAIL);
    }
  }
}
