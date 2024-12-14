package com.loukhin.vanillaextract.server.access;

import com.loukhin.vanillaextract.common.config.ArmorHide;

public interface ServerPlayerEntityAccess {

    ArmorHide getArmorHide();

    void setArmorHide(ArmorHide armorHide);
}
