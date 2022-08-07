package me.zero.aprilfools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.zero.aprilfools.AprilFoolsMod;

@Config(name = AprilFoolsMod.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public ThreeDSharewareConfig threeDSharewareConfig = new ThreeDSharewareConfig();

    public static class ThreeDSharewareConfig {
        public boolean cheatCodesEnabled = false;
        public boolean sharewareHudEnabled = false;
    }
}
