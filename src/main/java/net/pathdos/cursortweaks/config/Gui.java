package net.pathdos.cursortweaks.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static me.shedaniel.autoconfig.AutoConfig.getConfigHolder;

@Config(name = "cursortweaks")
public class Gui implements ConfigData {

    public enum DropdownOption {
        cursormemory,
        customposition
    }

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public DropdownOption dropdownChoice = DropdownOption.cursormemory;

    @ConfigEntry.Gui.Tooltip
    public boolean recordCursorStaticPosition = false;

    @ConfigEntry.Gui.Excluded
    public int xStaticPosition = -1;

    @ConfigEntry.Gui.Excluded
    public int yStaticPosition = -1;

    public static Gui get() {
        return getConfigHolder(net.pathdos.cursortweaks.config.Gui.class).getConfig();
    }

    public static void save() {
        getConfigHolder(Gui.class).save();
    }
}