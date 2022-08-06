package me.zero.threedshareware.registry;

import me.zero.aprilfools.api.registry.BlockRegistry;
import me.zero.threedshareware.ThreeDSharewareMod;
import me.zero.threedshareware.registry.block.AprilBarrelBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block BARREL_BLOCK = new AprilBarrelBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));

    public static void initBlocks() {
        BlockRegistry.registerBlock(new Identifier(ThreeDSharewareMod.MOD_ID, "barrel"), BARREL_BLOCK);
    }
}
