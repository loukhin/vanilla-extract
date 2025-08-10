package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.client.access.CapeFeatureRendererAccess;
import com.loukhin.vanillaextract.client.access.PlayerEntityRenderStateAccess;
import com.loukhin.vanillaextract.client.handler.FeatureRenderHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin implements CapeFeatureRendererAccess {
    @Unique
    boolean shouldConsiderCancelling = false;

    @Override
    public boolean vanilla_extract$shouldConsiderCancelling() {
        return this.shouldConsiderCancelling;
    }

    @Override
    public void vanilla_extract$setShouldConsiderCancelling(boolean shouldConsiderCancelling) {
        this.shouldConsiderCancelling = shouldConsiderCancelling;
    }

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V")
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, PlayerEntityRenderState playerEntityRenderState, float f, float g, CallbackInfo ci) {
        this.vanilla_extract$setShouldConsiderCancelling(FeatureRenderHandler.shouldCheckForCancellation(((PlayerEntityRenderStateAccess) playerEntityRenderState).vanilla_extract$getUuid()));
    }

    @Inject(at = @At("HEAD"), method = "hasCustomModelForLayer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;)Z", cancellable = true)
    public void hasCustomModelForLayerProxy(ItemStack stack, EquipmentModel.LayerType layerType, CallbackInfoReturnable<Boolean> cir) {
        if (layerType.equals(EquipmentModel.LayerType.HUMANOID) && this.vanilla_extract$shouldConsiderCancelling() && VanillaExtractClient.armorHide.state.get(EquipmentSlot.CHEST.getName())) {
            cir.setReturnValue(false);
        }
    }
}
