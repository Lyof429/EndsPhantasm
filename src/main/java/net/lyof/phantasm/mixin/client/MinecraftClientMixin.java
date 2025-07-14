package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    public void charmAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.hasStatusEffect(ModEffects.CHARM))
            cir.setReturnValue(false);
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    public void charmItemUse(CallbackInfo ci) {
        if (this.player != null && this.player.hasStatusEffect(ModEffects.CHARM))
            ci.cancel();
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void charmBlockBreaking(CallbackInfo ci) {
        if (this.player != null && this.player.hasStatusEffect(ModEffects.CHARM))
            ci.cancel();
    }
}
