package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.client.render.DimensionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {
    @Inject(method = "shouldBrightenLighting", at = @At("HEAD"), cancellable = true)
    public void unbrightenEnd(CallbackInfoReturnable<Boolean> cir) {
        if ((DimensionEffects) (Object) this instanceof DimensionEffects.End)
            cir.setReturnValue(!ConfigEntries.darkEnd);
    }
}
