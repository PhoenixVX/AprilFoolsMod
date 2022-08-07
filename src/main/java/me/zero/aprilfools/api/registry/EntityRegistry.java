package me.zero.aprilfools.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityRegistry {
    public static final List<EntityType<?>> ENTITY_TYPES = new ArrayList<>();

    public static EntityType<?> registerEntityType(ResourceLocation resourceLocation, EntityType<?> entityType) {
        EntityType<?> registeredEntityType = Registry.register(Registry.ENTITY_TYPE, resourceLocation, entityType);
        ENTITY_TYPES.add(registeredEntityType);
        return registeredEntityType;
    }
}
