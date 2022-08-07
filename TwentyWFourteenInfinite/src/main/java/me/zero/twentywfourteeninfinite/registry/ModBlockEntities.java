package me.zero.twentywfourteeninfinite.registry;

import me.zero.aprilfools.api.registry.BlockEntityRegistry;
import me.zero.twentywfourteeninfinite.TwentyWFourteenInfiniteMod;
import me.zero.twentywfourteeninfinite.registry.block.entity.NeitherPortalEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType NEITHER = FabricBlockEntityTypeBuilder.create(NeitherPortalEntity::new, ModBlocks.NEITHER_PORTAL).build(null);

    public static void initBlockEntities() {
        BlockEntityRegistry.registerBlockEntityType(new ResourceLocation(TwentyWFourteenInfiniteMod.MOD_ID, "neither"), NEITHER);
    }
}
