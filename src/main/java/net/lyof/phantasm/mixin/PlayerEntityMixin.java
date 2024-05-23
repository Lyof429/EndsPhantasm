package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(at = @At("RETURN"), method = "getBlockBreakingSpeed", cancellable = true)
	private void modifyBreakSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
		PlayerEntity self = ((PlayerEntity) (Object) this);

		ItemStack stack = self.getMainHandStack();
		if (!stack.isIn(ModTags.Items.XP_BOOSTED) || !stack.getItem().isSuitableFor(block)) return;

		float bonus = 1;
		bonus += ConfigEntries.crystalXPBoost * self.experienceLevel / 10 / 10f;

		cir.setReturnValue(cir.getReturnValue() * bonus);
	}
}