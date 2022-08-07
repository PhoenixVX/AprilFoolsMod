package me.zero.aprilfools.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundRegistry {
    public static final List<SoundEvent> SOUND_EVENTS = new ArrayList<>();

    public static SoundEvent registerSoundEvent(ResourceLocation resourceLocation, SoundEvent soundEvent) {
        SoundEvent registeredSoundEvent = Registry.register(Registry.SOUND_EVENT, resourceLocation, soundEvent);
        SOUND_EVENTS.add(registeredSoundEvent);
        return registeredSoundEvent;
    }
}
