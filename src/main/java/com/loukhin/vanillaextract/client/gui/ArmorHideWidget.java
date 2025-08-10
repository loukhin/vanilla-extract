package com.loukhin.vanillaextract.client.gui;

import com.loukhin.vanillaextract.client.VanillaExtractClient;
import com.loukhin.vanillaextract.common.config.ArmorHide;
import com.loukhin.vanillaextract.common.network.ArmorStatePayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.util.Identifier;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class ArmorHideWidget implements Drawable, Element, Selectable  {
    private final HashMap<String, ToggleButtonWidget> toggleButtons = new HashMap<>();
    private HandledScreen<?> parent;
    private final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.of("vanillaextract", "hide"),
            Identifier.of("vanillaextract", "show"),
            Identifier.of("vanillaextract", "hide_highlighted"),
            Identifier.of("vanillaextract", "show_highlighted"));

    public void init(HandledScreen<?> parent) {
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = 0;
        for (String slot : ArmorHide.slotList) {
            ToggleButtonWidget button = new ToggleButtonWidget(this.parent.x, this.parent.y + 7 + (i * 16) + i * 2 + 1,
                    7, 16, VanillaExtractClient.armorHide.state.get(slot));

            button.setTextures(TEXTURES);
            button.render(context, mouseX, mouseY, delta);
            toggleButtons.put(slot, button);
            i++;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonNum) {
        for (String slot : ArmorHide.slotList) {
            ToggleButtonWidget button = this.toggleButtons.get(slot);
            if (button.mouseClicked(mouseX, mouseY, buttonNum)) {
                boolean newState = !VanillaExtractClient.armorHide.state.get(slot);
                VanillaExtractClient.armorHide.state.put(slot, newState);
                ClientPlayNetworking.send(new ArmorStatePayload(VanillaExtractClient.armorHide.state));
                button.setToggled(newState);
                this.toggleButtons.put(slot, button);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
