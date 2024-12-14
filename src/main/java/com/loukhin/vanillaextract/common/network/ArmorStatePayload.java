package com.loukhin.vanillaextract.common.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.Map;

public record ArmorStatePayload(Map<String, Boolean> armorStates) implements CustomPayload {
    public static final CustomPayload.Id<ArmorStatePayload> ID = new CustomPayload.Id<>(VanillaExtractNetworkConstants.ARMOR_STATE_ID);
    public static final PacketCodec<RegistryByteBuf, ArmorStatePayload> CODEC = new PacketCodec<RegistryByteBuf, ArmorStatePayload>() {
        @Override
        public ArmorStatePayload decode(RegistryByteBuf buf) {
            return new ArmorStatePayload(buf.readMap(PacketByteBuf::readString, PacketByteBuf::readBoolean));
        }

        @Override
        public void encode(RegistryByteBuf buf, ArmorStatePayload value) {
            buf.writeMap(value.armorStates, PacketByteBuf::writeString, PacketByteBuf::writeBoolean);
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
