package me.zero.aprilfools.api.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class EntityRegistry {
    public static final List<EntityType<?>> ENTITY_TYPES = new ArrayList<>();

    public static EntityType<?> registerEntityType(Identifier identifier, EntityType<?> entityType) {
        EntityType<?> registeredEntityType = Registry.register(Registry.ENTITY_TYPE, identifier, entityType);
        ENTITY_TYPES.add(registeredEntityType);
        return registeredEntityType;
    }
}
