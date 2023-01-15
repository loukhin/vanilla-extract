package com.loukhin.vanillaextract;

import com.loukhin.vanillaextract.commands.ArmorCommands;
import com.loukhin.vanillaextract.config.VanillaExtractConfig;
import com.loukhin.vanillaextract.network.VanillaExtractNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;

public class VanillaExtract implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("vanillaextract");
    private static VanillaExtractConfig CONFIG;

    public static VanillaExtractConfig config() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    private static VanillaExtractConfig loadConfig() {
        return VanillaExtractConfig.load(FabricLoader.getInstance().getConfigDir().resolve("vanilla-extract-config.json").toFile());
    }

    @Override
    public void onInitialize() {
        LOGGER.info("VanillaExtract Loaded!");
        VanillaExtractNetwork.registerC2SPackets();

        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, VanillaExtract::messageHandler);

        CommandRegistrationCallback.EVENT.register(ArmorCommands::register);
    }


    public static void playerConnectHandler(ServerPlayerEntity player) {
//        player.networkHandler.sendPacket(new SetEq);
//        if (config().multiplierSettings.expMultiplierEnabled && config().multiplierSettings.roAnnounce) {
//            player.sendMessage(getRoAnnounce());
//        }
    }

    public static CompletableFuture<Text> messageHandler(ServerPlayerEntity sender, Text message) {
        if (message.getString().contains("{i}")) {
            ItemStack stack = sender.getMainHandStack();
            ArrayList<String> textStr = new ArrayList<>(List.of(message.getString().split("((?<=\\{i})|(?=\\{i}))")));
            MutableText text = MutableText.of(TextContent.EMPTY);
            textStr.forEach((s) -> {
                if (s.equals("{i}")) {
                    text.append(stack.toHoverableText());
                } else {
                    text.append(s);
                }
            });
            return CompletableFuture.completedFuture(text);
        }
        return CompletableFuture.completedFuture(message);
    }

}