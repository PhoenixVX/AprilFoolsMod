package me.zero.twentywfourteeninfinite;

import me.zero.twentywfourteeninfinite.registry.ModBlocks;
import me.zero.twentywfourteeninfinite.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class TwentyWFourteenInfiniteMod implements ModInitializer {
    public static final String MOD_ID = "twentywfourteeninfinite";

    @Override
    public void onInitialize() {
        ModBlocks.initBlocks();
        ModItems.initItems();
    }
}
