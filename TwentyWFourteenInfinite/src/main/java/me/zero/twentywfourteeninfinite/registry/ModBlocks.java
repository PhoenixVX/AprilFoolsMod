package me.zero.twentywfourteeninfinite.registry;

import me.zero.aprilfools.api.registry.BlockRegistry;
import me.zero.twentywfourteeninfinite.TwentyWFourteenInfiniteMod;
import me.zero.twentywfourteeninfinite.registry.block.AntBlock;
import me.zero.twentywfourteeninfinite.registry.block.BoxOfBooksBlock;
import me.zero.twentywfourteeninfinite.registry.block.NeitherPortalBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import static net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK;

public class ModBlocks {
    public static final Block NEITHER_PORTAL = new NeitherPortalBlock(BlockBehaviour.Properties.of(Material.PORTAL).noCollission().strength(-1.0F).sound(SoundType.GLASS).lightLevel((cdt) -> 11).noLootTable());
    public static final Block NETHERITE_STAIRS = new StairBlock(NETHERITE_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(NETHERITE_BLOCK));
    public static final Block ANT_BLOCK = new AntBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).sound(SoundType.WET_GRASS).strength(-1.0F, 3600000.0F).noLootTable());
    public static final Block BOOK_BLOCK = new BoxOfBooksBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD));

    public static void initBlocks() {
        BlockRegistry.registerBlock(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "neither_portal"), NEITHER_PORTAL);
        BlockRegistry.registerBlock(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "netherite_stairs"), NETHERITE_STAIRS);
        BlockRegistry.registerBlock(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "ant"), ANT_BLOCK);
        BlockRegistry.registerBlock(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "book_box"), BOOK_BLOCK);
    }
}
