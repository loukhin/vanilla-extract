package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.server.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Override
    void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof BundleS2CPacket bundledPacket) {
            for (Packet<?> eachPacket : bundledPacket.getPackets()) {
                PacketHandler.handle(player, eachPacket);
            }
        } else {
            PacketHandler.handle(player, packet);
        }
    }
}
