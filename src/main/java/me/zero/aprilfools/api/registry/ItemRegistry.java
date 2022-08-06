package me.zero.aprilfools.api.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static Item registerItem(Identifier identifier, Item item) {
        Item registeredItem = Registry.register(Registry.ITEM, identifier, item);
        ItemRegistry.ITEMS.add(registeredItem);
        return registeredItem;
    }
}
