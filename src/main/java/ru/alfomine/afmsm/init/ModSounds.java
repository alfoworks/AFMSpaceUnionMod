package ru.alfomine.afmsm.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSounds {
    public static SoundEvent SOLARATLAS_ZOOM;

    public static void registerSounds() {
        SOLARATLAS_ZOOM = registerSound(new ResourceLocation("afmsm", "gui_solaratlas_zoom"), "gui_solaratlas_zoom");
    }

    private static SoundEvent registerSound(ResourceLocation loc, String name) {
        SoundEvent soundEvent = new SoundEvent(loc);
        soundEvent.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);

        return soundEvent;
    }
}
