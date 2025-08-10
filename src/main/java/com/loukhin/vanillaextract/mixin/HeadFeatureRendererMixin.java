package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.client.access.PlayerEntityRenderStateAccess;
import com.loukhin.vanillaextract.client.handler.FeatureRenderHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererMixin<S extends LivingEntityRenderState> {
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V", cancellable = true)
    public void onRenderArmor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci) {
        if (livingEntityRenderState instanceof PlayerEntityRenderState && FeatureRenderHandler.shouldCheckForCancellation(((PlayerEntityRenderStateAccess) livingEntityRenderState).vanilla_extract$getUuid())) {
            if (VanillaExtractClient.armorHide.state.get(EquipmentSlot.HEAD.getName())) {
                ci.cancel();
            }
        }
    }
}
