package me.zero.twentywfourteeninfinite.registry;

import me.zero.aprilfools.api.registry.ItemRegistry;
import me.zero.twentywfourteeninfinite.TwentyWFourteenInfiniteMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item NETHERITE_STAIRS = new BlockItem(ModBlocks.NETHERITE_STAIRS, new FabricItemSettings().tab(CreativeModeTab.TAB_BUILDING_BLOCKS).fireResistant());
    public static final Item BOOK_BOX = new BlockItem(ModBlocks.BOOK_BLOCK, new FabricItemSettings().tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    public static final Item CURSOR = new BlockItem(ModBlocks.CURSOR, new FabricItemSettings().tab(CreativeModeTab.TAB_DECORATIONS));

    public static void initItems() {
        ItemRegistry.registerItem(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "netherite_stairs"), NETHERITE_STAIRS);
        ItemRegistry.registerItem(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "book_box"), BOOK_BOX);
        ItemRegistry.registerItem(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "cursor"), CURSOR);
    }
}
