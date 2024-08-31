package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModPotions {
    public static void register() {
        Phantasm.log("Registering Potions for modid : " + Phantasm.MOD_ID);
    }


    public static final Potion CORROSION = Registry.register(Registries.POTION, "corrosion",
            new Potion(new StatusEffectInstance(ModEffects.CORROSION, 3600)));

    public static final Potion LONG_CORROSION = Registry.register(Registries.POTION, "long_corrosion",
            new Potion("corrosion", new StatusEffectInstance(ModEffects.CORROSION, 9600)));

    public static final Potion STRONG_CORROSION = Registry.register(Registries.POTION, "strong_corrosion",
            new Potion("corrosion", new StatusEffectInstance(ModEffects.CORROSION, 1800, 1)));
}
