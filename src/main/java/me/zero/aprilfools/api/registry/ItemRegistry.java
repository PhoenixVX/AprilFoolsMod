package me.zero.aprilfools.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static Item registerItem(ResourceLocation resourceLocation, Item item) {
        Item registeredItem = Registry.register(Registry.ITEM, resourceLocation, item);
        ItemRegistry.ITEMS.add(registeredItem);
        return registeredItem;
    }
}
