package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void oblifruitKeep(ItemStack instance, int amount) {
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.05 && instance.getCount() < instance.getMaxCount()) {
            instance.increment(1);
            return;
        }
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.4) return;
        instance.decrement(1);
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    public void applyVulnerability(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!this.hasStatusEffect(ModEffects.CORRODED)) return;

        int i = this.getStatusEffect(ModEffects.CORRODED).getAmplifier() + 1;
    }
}
