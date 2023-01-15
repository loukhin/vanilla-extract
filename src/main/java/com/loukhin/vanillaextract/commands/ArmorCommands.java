package com.loukhin.vanillaextract.commands;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.access.ServerPlayerEntityAccess;
import com.loukhin.vanillaextract.config.ArmorHide;
import com.loukhin.vanillaextract.network.VanillaExtractNetwork;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.minecraft.server.command.CommandManager.*;

public class ArmorCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment environment) {
        dispatcher.register(
            literal("hidearmor").then(
                argument("slot", StringArgumentType.string()).suggests(new SlotSuggestionProvider()).then(argument("hidden", BoolArgumentType.bool()).executes(context -> {
                    String slot = StringArgumentType.getString(context, "slot");
                    if (!ArmorHide.slotList.contains(slot)) {
                        throw new SimpleCommandExceptionType(Text.literal("Invalid slot!")).create();
                    }
                    ServerCommandSource source = context.getSource();
                    if (!source.isExecutedByPlayer() || source.getPlayer() == null) {
                        throw new SimpleCommandExceptionType(Text.literal("This command can only be used by player!")).create();
                    }
                    ServerPlayerEntity player = source.getPlayer();
                    Boolean hidden = BoolArgumentType.getBool(context, "hidden");
                    String hidingString = (hidden) ? "hiding" : "showing";
                    context.getSource().sendMessage(Text.literal("%s is now %s".formatted(StringUtil.capitalize(slot), hidingString)));
                    HashMap<String, Boolean> hideSettings = VanillaExtract.config().armorHideSettings.users.get(player.getUuid());
                    hideSettings.put(slot, hidden);
                    VanillaExtract.config().armorHideSettings.users.put(player.getUuid(), hideSettings);
                    VanillaExtract.config().writeChanges();
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeMap(hideSettings, PacketByteBuf::writeString, PacketByteBuf::writeBoolean);
                    ServerPlayNetworking.send(player, VanillaExtractNetwork.ARMOR_STATE_ID, buf);
                    for (ServerPlayerEntity eachPlayer : PlayerLookup.tracking(player.getWorld(), player.getBlockPos())) {
                        if (player.equals(eachPlayer)) continue;

                        EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot);
                        List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();
                        if (hidden) {
                            equipmentList.add(Pair.of(equipmentSlot, ItemStack.EMPTY));
                        } else {
                            equipmentList.add(Pair.of(equipmentSlot, player.getEquippedStack(equipmentSlot)));
                        }
                        eachPlayer.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(player.getId(), equipmentList));
                    }
                    return 0;
                })
            ))
        );
    }
}
