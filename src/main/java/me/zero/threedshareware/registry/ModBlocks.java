package me.zero.threedshareware.registry;

import me.zero.aprilfools.api.registry.BlockRegistry;
import me.zero.threedshareware.ThreeDSharewareMod;
import me.zero.threedshareware.registry.block.AprilBarrelBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class ModBlocks {
    public static final Block BARREL_BLOCK = new AprilBarrelBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD));

    public static void initBlocks() {
        BlockRegistry.registerBlock(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "barrel"), BARREL_BLOCK);
    }
}
