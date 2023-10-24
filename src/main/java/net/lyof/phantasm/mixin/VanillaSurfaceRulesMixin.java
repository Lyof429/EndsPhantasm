package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.biome.surface.ModMaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaSurfaceRules.class)
public class VanillaSurfaceRulesMixin {
    @Inject(method = "getEndStoneRule", at = @At("RETURN"), cancellable = true)
    private static void addEPMaterialRules(CallbackInfoReturnable<MaterialRules.MaterialRule> cir) {
        MaterialRules.MaterialRule base = cir.getReturnValue();
        MaterialRules.MaterialRule override = MaterialRules.sequence(ModMaterialRules.createDreamingDenRule(), base);
        Phantasm.log("Applied SurfaceRules : " + override);
        cir.setReturnValue(override);
    }
}
