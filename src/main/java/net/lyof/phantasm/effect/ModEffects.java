package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.custom.CorrodedStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEffects {
    public static void register() {
        Phantasm.log("Registering Effects for modid : " + Phantasm.MOD_ID);
    }


    public static final StatusEffect CORRODED = Registry.register(Registries.STATUS_EFFECT, "corroded",
            new CorrodedStatusEffect());
}
