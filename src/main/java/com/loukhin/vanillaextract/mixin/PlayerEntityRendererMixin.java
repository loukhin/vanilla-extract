package com.loukhin.vanillaextract.mixin;


import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.client.access.PlayerEntityRenderStateAccess;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class PlayerEntityRendererMixin<T extends Entity, S extends EntityRenderState, M extends EntityModel<? super S>> {
    @Inject(at=@At("HEAD"), method = "updateRenderState")
    public void updateRenderState(T entity, S state, float tickDelta, CallbackInfo ci) {
        if (state instanceof PlayerEntityRenderState) {
            ((PlayerEntityRenderStateAccess)state).vanilla_extract$setUuid(entity.getUuid());
        }
    }
}