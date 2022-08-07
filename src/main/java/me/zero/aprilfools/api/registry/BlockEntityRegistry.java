package me.zero.aprilfools.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityRegistry {
    public static final List<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new ArrayList<>();

    public static BlockEntityType<?> registerBlockEntityType(ResourceLocation resourceLocation, BlockEntityType<?> blockEntityType) {
        BlockEntityType<?> registeredBlockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, resourceLocation, blockEntityType);
        BLOCK_ENTITY_TYPES.add(registeredBlockEntityType);
        return registeredBlockEntityType;
    }
}
