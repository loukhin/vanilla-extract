package com.loukhin.vanillaextract;

import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;

public class VanillaExtract implements ModInitializer {
    public static final String MOD_ID = "vanillaextract";
    public static final Logger LOGGER = LoggerFactory.getLogger("vanillaextract");

    @Override
    public void onInitialize() {
        LOGGER.info("VanillaExtract Loaded!");

        PayloadTypeRegistry.playC2S().register(ArmorStatePayload.ID, ArmorStatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ArmorStatePayload.ID, ArmorStatePayload.CODEC);
    }
}