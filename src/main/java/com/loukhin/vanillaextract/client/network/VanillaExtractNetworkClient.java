package com.loukhin.vanillaextract.client.network;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.common.config.ArmorHide;
import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class VanillaExtractNetworkClient {
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ArmorStatePayload.ID, (payload, context) -> {
            VanillaExtractClient.armorHide = new ArmorHide(payload.armorStates());
        });
    }
}
