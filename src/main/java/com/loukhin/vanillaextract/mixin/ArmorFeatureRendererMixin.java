package com.loukhin.vanillaextract.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.client.access.ArmorFeatureRendererAccess;
import com.loukhin.vanillaextract.client.access.PlayerEntityRenderStateAccess;
import com.loukhin.vanillaextract.client.handler.FeatureRenderHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> implements ArmorFeatureRendererAccess {
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

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V")
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        if (bipedEntityRenderState instanceof PlayerEntityRenderState) {
            boolean shouldDoThingy = FeatureRenderHandler.shouldCheckForCancellation(((PlayerEntityRenderStateAccess) bipedEntityRenderState).vanilla_extract$getUuid());
            VanillaExtract.LOGGER.info("thingy -> {}", shouldDoThingy);
            this.vanilla_extract$setShouldConsiderCancelling(shouldDoThingy);
        }
    }

    @Inject(at = @At("HEAD"), method = "renderArmor", cancellable = true)
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, EquipmentSlot slot, int light, A armorModel, CallbackInfo ci) {
        VanillaExtract.LOGGER.info("render armor feature -> {}", this.vanilla_extract$shouldConsiderCancelling());

        if (this.vanilla_extract$shouldConsiderCancelling() && VanillaExtractClient.armorHide.state.get(slot.getName())) {
            ci.cancel();
        }
    }
}
