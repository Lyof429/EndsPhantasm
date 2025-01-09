package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseRouter.class)
public abstract class NoiseRouterMixin {
    private static final DensityFunction END =
            DensityFunctionTypes.add(
                    DensityFunctionTypes.mul(
                            DensityFunctionTypes.add(
                                    DensityFunctionTypes.endIslands(0),
                            DensityFunctionTypes.constant(0.84375)),
                    DensityFunctionTypes.constant(2 / (0.5625 + 0.84375))),
            DensityFunctionTypes.constant(-1));

    @Shadow @Final private DensityFunction temperature;

    @Inject(method = "temperature", at = @At("HEAD"), cancellable = true)
    public void overrideEndTemperature(CallbackInfoReturnable<DensityFunction> cir) {
        if (EndDataCompat.getCompatibilityMode().equals("endercon")
                && this.temperature.minValue() == this.temperature.maxValue() && this.temperature.minValue() == 0) {
            cir.setReturnValue(END);
        }
    }
}
