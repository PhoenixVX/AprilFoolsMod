package me.zero.threedshareware;

import me.zero.threedshareware.registry.ModBlocks;
import me.zero.threedshareware.registry.ModItems;
import me.zero.threedshareware.registry.ModSounds;
import net.fabricmc.api.ModInitializer;

public class ThreeDSharewareMod implements ModInitializer {
    public static final String MOD_ID = "threedshareware";

    @Override
    public void onInitialize() {
        ModBlocks.initBlocks();
        ModItems.initItems();
        ModSounds.initSoundEvents();
    }
}
