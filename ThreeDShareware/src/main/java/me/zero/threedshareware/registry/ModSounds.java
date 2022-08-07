package me.zero.threedshareware.registry;

import me.zero.aprilfools.api.registry.SoundRegistry;
import me.zero.threedshareware.ThreeDSharewareMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent AWESOME_INTRO = new SoundEvent(new ResourceLocation(ThreeDSharewareMod.MOD_ID, "awesome_intro"));

    public static void initSoundEvents() {
        SoundRegistry.registerSoundEvent(AWESOME_INTRO.getLocation(), AWESOME_INTRO);
    }
}
