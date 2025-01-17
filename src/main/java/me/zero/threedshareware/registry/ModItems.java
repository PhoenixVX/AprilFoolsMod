package me.zero.threedshareware.registry;

import me.zero.aprilfools.api.registry.ItemRegistry;
import me.zero.threedshareware.ThreeDSharewareMod;
import me.zero.threedshareware.registry.item.ThreeDItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item RED_KEY = new Item(new FabricItemSettings().group(CreativeModeTab.TAB_MISC));
    public static final Item BLUE_KEY = new Item(new FabricItemSettings().group(CreativeModeTab.TAB_MISC));
    public static final Item YELLOW_KEY = new Item(new FabricItemSettings().group(CreativeModeTab.TAB_MISC));
    public static final Item THREE_D_ITEM = new ThreeDItem(new FabricItemSettings().group(CreativeModeTab.TAB_MISC));

    public static void initItems() {
        ItemRegistry.registerItem(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "red_key"), RED_KEY);
        ItemRegistry.registerItem(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "blue_key"), BLUE_KEY);
        ItemRegistry.registerItem(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "yellow_key"), YELLOW_KEY);
        ItemRegistry.registerItem(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "3d"), THREE_D_ITEM);
    }
}
