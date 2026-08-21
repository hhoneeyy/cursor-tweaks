package net.pathdos.cursortweaks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.pathdos.cursortweaks.config.Gui;

public class CursorTweaks implements ModInitializer {

    @Override
    public void onInitialize() {
        AutoConfig.register(Gui.class, GsonConfigSerializer::new);
    }
}
