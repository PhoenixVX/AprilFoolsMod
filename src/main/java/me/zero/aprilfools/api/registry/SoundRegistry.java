package me.zero.aprilfools.api.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class SoundRegistry {
    public static final List<SoundEvent> SOUND_EVENTS = new ArrayList<>();

    public static SoundEvent registerSoundEvent(Identifier identifier, SoundEvent soundEvent) {
        SoundEvent registeredSoundEvent = Registry.register(Registry.SOUND_EVENT, identifier, soundEvent);
        SOUND_EVENTS.add(registeredSoundEvent);
        return registeredSoundEvent;
    }
}
