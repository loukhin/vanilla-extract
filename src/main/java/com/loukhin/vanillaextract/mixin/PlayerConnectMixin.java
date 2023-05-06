package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.VanillaExtract;
import com.loukhin.vanillaextract.handler.PlayerManagerHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerConnectMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;onPlayerConnected(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER), method = "onPlayerConnect")
    void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PlayerManagerHandler.onPlayerConnect(player);
    }
}
