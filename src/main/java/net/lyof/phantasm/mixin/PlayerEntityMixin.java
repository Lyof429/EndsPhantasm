package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	@Inject(at = @At("RETURN"), method = "getBlockBreakingSpeed", cancellable = true)
	private void init(BlockState block, CallbackInfoReturnable<Float> cir) {
		ItemStack stack = ((PlayerEntity) (Object) this).getMainHandStack();
		Phantasm.log(stack + " " + stack.isIn(ModTags.Items.XP_BOOSTED));

		if (stack.isIn(ModTags.Items.XP_BOOSTED))
			cir.setReturnValue(cir.getReturnValue() * 2);
	}
}