package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.custom.CorrodedStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModPotions {
    public static void register() {
        Phantasm.log("Registering Potions for modid : " + Phantasm.MOD_ID);
    }


    public static final Potion CORRODED = Registry.register(Registries.POTION, "corroded",
            new Potion(new StatusEffectInstance(ModEffects.CORRODED, 3600)));

    public static final Potion LONG_CORRODED = Registry.register(Registries.POTION, "long_corroded",
            new Potion("corroded", new StatusEffectInstance(ModEffects.CORRODED, 9600)));

    public static final Potion STRONG_CORRODED = Registry.register(Registries.POTION, "strong_corroded",
            new Potion("corroded", new StatusEffectInstance(ModEffects.CORRODED, 1800, 1)));
}
