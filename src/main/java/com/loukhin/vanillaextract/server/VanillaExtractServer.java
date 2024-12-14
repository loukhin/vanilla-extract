package com.loukhin.vanillaextract.server;

import com.loukhin.vanillaextract.server.commands.ArmorCommands;
import com.loukhin.vanillaextract.server.config.VanillaExtractConfig;
import com.loukhin.vanillaextract.server.network.VanillaExtractNetworkServer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.SERVER)
public class VanillaExtractServer implements DedicatedServerModInitializer {
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
    public void onInitializeServer() {
        VanillaExtractNetworkServer.registerC2SPackets();

        ServerMessageDecoratorEvent.EVENT.register(ServerMessageDecoratorEvent.CONTENT_PHASE, VanillaExtractServer::messageHandler);
        CommandRegistrationCallback.EVENT.register(ArmorCommands::register);
    }


    public static void playerConnectHandler(ServerPlayerEntity player) {
//        player.networkHandler.sendPacket(new SetEq);
//        if (config().multiplierSettings.expMultiplierEnabled && config().multiplierSettings.roAnnounce) {
//            player.sendMessage(getRoAnnounce());
//        }
    }

    public static Text messageHandler(ServerPlayerEntity sender, Text message) {
        if (message.getString().contains("{i}")) {
            ItemStack stack = sender.getMainHandStack();
            ArrayList<String> textStr = new ArrayList<>(List.of(message.getString().split("((?<=\\{i})|(?=\\{i}))")));
            MutableText text = MutableText.of(PlainTextContent.EMPTY);
            textStr.forEach((s) -> {
                if (s.equals("{i}")) {
                    text.append(stack.toHoverableText());
                } else {
                    text.append(s);
                }
            });
            return text;
        }
        return message;
    }
}