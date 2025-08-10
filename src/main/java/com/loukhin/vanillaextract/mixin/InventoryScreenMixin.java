package com.loukhin.vanillaextract.mixin;

import com.loukhin.vanillaextract.client.gui.ArmorHideWidget;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin extends RecipeBookScreenMixin {
    @Unique
    ArmorHideWidget armorHideWidget;

    protected InventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {
        this.armorHideWidget = new ArmorHideWidget();
        this.armorHideWidget.init(((InventoryScreen)(Object)this));
        this.addDrawableChild(this.armorHideWidget);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.armorHideWidget.mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
    }
}
