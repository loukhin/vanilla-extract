package com.loukhin.vanillaextract.client.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.UUID;

public class FeatureRenderHandler {
    public static boolean shouldCheckForCancellation(UUID uuid) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        return clientPlayerEntity != null && uuid.equals(clientPlayerEntity.getUuid());
    }
}
