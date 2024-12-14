package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.client.access.PlayerEntityRenderStateAccess;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateAccess {
    @Unique
    UUID uuid;

    @Override
    public UUID vanilla_extract$getUuid() {
        return this.uuid;
    }

    @Override
    public void vanilla_extract$setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
