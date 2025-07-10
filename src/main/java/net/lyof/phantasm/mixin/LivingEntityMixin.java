package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.block.challenge.Challenger;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Challenger {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @WrapOperation(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void keepOblifruit(ItemStack instance, int amount, Operation<Void> original) {
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.05 && instance.getCount() < instance.getMaxCount()) {
            instance.increment(1);
            return;
        }
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.4) return;
        original.call(instance, amount);
    }

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    public float applyVulnerability(float original) {
        if (!this.hasStatusEffect(ModEffects.CORROSION)) return original;

        int i = this.getStatusEffect(ModEffects.CORROSION).getAmplifier() + 1;
        return original * (1 + 0.2f * i);
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

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void countRuneKill(DamageSource damageSource, CallbackInfo ci) {
        if (this.getChallengeRune() != null && this.getCommandTags().contains(Phantasm.MOD_ID + ".challenge"))
            this.getChallengeRune().progress();
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeRune(NbtCompound nbt, CallbackInfo ci) {
        if (this.getChallengeRune() != null) {
            nbt.putInt(Phantasm.MOD_ID + "_RuneX", this.getChallengeRune().getPos().getX());
            nbt.putInt(Phantasm.MOD_ID + "_RuneY", this.getChallengeRune().getPos().getY());
            nbt.putInt(Phantasm.MOD_ID + "_RuneZ", this.getChallengeRune().getPos().getZ());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readRune(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(Phantasm.MOD_ID + "_RuneX"))
            this.challengePos = new BlockPos(nbt.getInt(Phantasm.MOD_ID + "_RuneX"),
                    nbt.getInt(Phantasm.MOD_ID + "_RuneY"),
                    nbt.getInt(Phantasm.MOD_ID + "_RuneZ"));
    }


    @Unique private ChallengeRuneBlockEntity challengeRune = null;
    @Unique private BlockPos challengePos = null;

    @Override
    public PlayerEntity asPlayer() { return null; }

    @Override
    public @Nullable ChallengeRuneBlockEntity getChallengeRune() {
        if (this.challengeRune == null && this.challengePos != null
                && this.getWorld().getBlockEntity(this.challengePos) instanceof ChallengeRuneBlockEntity rune)
            this.challengeRune = rune;
        return this.challengeRune;
    }

    @Override
    public void setChallengeRune(ChallengeRuneBlockEntity rune) {
        this.challengeRune = rune;
        this.challengePos = rune == null ? null : rune.getPos();
    }
}
