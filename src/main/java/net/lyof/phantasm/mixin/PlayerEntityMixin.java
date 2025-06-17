package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.block.entity.Challenger;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Challenger {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
	private float modifyBreakSpeed(float original, BlockState state) {
		PlayerEntity self = ((PlayerEntity) (Object) this);

		ItemStack stack = self.getMainHandStack();
		if (!stack.isIn(ModTags.Items.XP_BOOSTED) || !stack.getItem().isSuitableFor(state)) return original;

		float bonus = 1 + (float) ConfigEntries.crystalXPBoost * self.experienceLevel / 100f;

		return original * bonus;
	}

	@Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
	public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
		if (this.hasStatusEffect(ModEffects.CHARM)) cir.setReturnValue(0f);
	}


	@Unique private ChallengeRuneBlockEntity challengeRune = null;

	@Override
	public PlayerEntity phantasm$asPlayer() {
		return (PlayerEntity) (Object) this;
	}

	@Override
	public ChallengeRuneBlockEntity phantasm$getRune() {
		return this.challengeRune;
	}

	@Override
	public void phantasm$setRune(ChallengeRuneBlockEntity challengeRune) {
		this.challengeRune = challengeRune;
	}
}