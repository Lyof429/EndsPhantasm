package net.lyof.phantasm.effect.custom;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ModStatusEffect extends StatusEffect {
    public ModStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}
