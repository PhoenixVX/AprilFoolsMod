package me.zero.threedshareware.registry;

import me.zero.aprilfools.api.registry.SoundRegistry;
import me.zero.threedshareware.ThreeDSharewareMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent AWESOME_INTRO = new SoundEvent(new Identifier(ThreeDSharewareMod.MOD_ID, "awesome_intro"));

    public static void initSoundEvents() {
        SoundRegistry.registerSoundEvent(AWESOME_INTRO.getId(), AWESOME_INTRO);
    }
}
