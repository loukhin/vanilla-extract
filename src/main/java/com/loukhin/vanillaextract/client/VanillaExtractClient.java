package com.loukhin.vanillaextract.client;

import com.loukhin.vanillaextract.common.network.VanillaExtractNetwork;
import com.loukhin.vanillaextract.common.config.ArmorHide;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class VanillaExtractClient implements ClientModInitializer {
    public static ArmorHide armorHide = ArmorHide.empty();

    @Override
    public void onInitializeClient() {
        VanillaExtractNetwork.registerS2CPackets();
    }
}
