package net.lyof.phantasm.sound;

import net.lyof.phantasm.Phantasm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static void register() {
        Phantasm.log("Registering Sounds for mod id : " + Phantasm.MOD_ID);
    }

    public static SoundEvent register(String name) {
        Identifier id = Phantasm.makeID(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static final SoundEvent MUSIC_DISC_ABRUPTION = register("music_disc_abruption");
}
