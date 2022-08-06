package me.zero.aprilfools;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.zero.aprilfools.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AprilFoolsMod implements ModInitializer {
    public static final String MOD_ID = "aprilfools";
    public static final Logger LOGGER = LogManager.getLogger("AprilFools");
    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
