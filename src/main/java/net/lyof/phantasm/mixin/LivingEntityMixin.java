package net.lyof.phantasm.mixin;

import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void keepOblifruit(ItemStack instance, int amount) {
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.05 && instance.getCount() < instance.getMaxCount()) {
            instance.increment(1);
            return;
        }
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.4) return;
        instance.decrement(1);
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    public void applyVulnerability(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (!this.hasStatusEffect(ModEffects.CORROSION)) return;

        int i = this.getStatusEffect(ModEffects.CORROSION).getAmplifier() + 1;
        cir.setReturnValue(amount * (1 + 0.2f * i));
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof LivingEntity attacker && attacker.hasStatusEffect(ModEffects.CHARM))
            cir.setReturnValue(false);
    }

    @Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
    public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (this.hasStatusEffect(ModEffects.CHARM)) cir.setReturnValue(0f);
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void charmMovement(CallbackInfo ci) {
        if (this.hasStatusEffect(ModEffects.CHARM)) ci.cancel();
    }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    public void charmAi(CallbackInfo ci) {
        if (this.hasStatusEffect(ModEffects.CHARM)) ci.cancel();
    }
}
