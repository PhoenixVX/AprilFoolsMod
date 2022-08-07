package me.zero.aprilfools.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static Block registerBlock(ResourceLocation resourceLocation, Block block) {
        Block registeredBlock = Registry.register(Registry.BLOCK, resourceLocation, block);
        BLOCKS.add(registeredBlock);
        return registeredBlock;
    }

    public static BlockItem registerBlockItem(ResourceLocation resourceLocation, Block block, Item.Properties properties) {
        BlockItem registeredBlockItem = Registry.register(Registry.ITEM, resourceLocation, new BlockItem(block, properties));
        ItemRegistry.ITEMS.add(registeredBlockItem);
        return registeredBlockItem;
    }
}
