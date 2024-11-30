package net.lyof.phantasm.mixin;

import net.lyof.phantasm.world.ModWorldGeneration;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiNoiseBiomeSourceParameterList.class)
public class MultiNoiseBiomeSourceParameterListMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void catchLookup(MultiNoiseBiomeSourceParameterList.Preset preset, RegistryEntryLookup<Biome> biomeLookup, CallbackInfo ci) {
        ModWorldGeneration.register(biomeLookup);
    }
}
