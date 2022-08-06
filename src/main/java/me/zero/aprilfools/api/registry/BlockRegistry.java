package me.zero.aprilfools.api.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static Block registerBlock(Identifier identifier, Block block) {
        Block registeredBlock = Registry.register(Registry.BLOCK, identifier, block);
        BLOCKS.add(registeredBlock);
        return registeredBlock;
    }

    public static BlockItem registerBlockItem(Identifier identifier, Block block, Item.Settings settings) {
        BlockItem registeredBlockItem = Registry.register(Registry.ITEM, identifier, new BlockItem(block, settings));
        ItemRegistry.ITEMS.add(registeredBlockItem);
        return registeredBlockItem;
    }
}
