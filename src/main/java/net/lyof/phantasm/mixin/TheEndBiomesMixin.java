package net.lyof.phantasm.mixin;

import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TheEndBiomes.class)
public class TheEndBiomesMixin {
    @Inject(method = "addHighlandsBiome", at = @At("HEAD"))
    private static void catchFabricEndBiomes(RegistryKey<Biome> biome, double weight, CallbackInfo ci) {
        EndDataCompat.add(biome, () -> weight);
    }
}
