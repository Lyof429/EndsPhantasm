package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.custom.ModStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEffects {
    public static void register() {
    }


    public static final StatusEffect CORROSION = Registry.register(Registries.STATUS_EFFECT, Phantasm.makeID("corrosion"),
            new ModStatusEffect(StatusEffectCategory.HARMFUL, 0xca2656));

    public static final StatusEffect CHARM = Registry.register(Registries.STATUS_EFFECT, Phantasm.makeID("charm"),
            new ModStatusEffect(StatusEffectCategory.HARMFUL, 0xffffaa));
}
