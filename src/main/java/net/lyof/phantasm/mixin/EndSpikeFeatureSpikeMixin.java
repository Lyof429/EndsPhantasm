package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndSpikeFeature.Spike.class)
public class EndSpikeFeatureSpikeMixin {
    @Shadow @Final private int height;

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    public void increaseHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.height + ConfigEntries.extraSpiresHeight);
    }

    @Inject(method = "isGuarded", at = @At("HEAD"), cancellable = true)
    public void removeGuard(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigEntries.noCrystalCages)
            cir.setReturnValue(false);
    }
}
