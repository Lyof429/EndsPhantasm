package net.lyof.phantasm.mixin;

import com.mojang.datafixers.util.Either;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin {/*
    @Inject(method = "getBiome", at = @At("RETURN"), cancellable = true)
    public void addEndBiomes(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise, CallbackInfoReturnable<RegistryEntry<Biome>> cir) {
        if (cir.getReturnValue().matchesKey(BiomeKeys.END_HIGHLANDS) && ModWorldGeneration.LOOKUP != null) {
            cir.setReturnValue(TheEndBiomeSource.createVanilla(ModWorldGeneration.LOOKUP).getBiome(x, y, z, noise));
        }
    }*/

    @Inject(method = "<init>", at = @At("TAIL"))
    public void hookEndBiomes(Either<MultiNoiseUtil.Entries<RegistryEntry<Biome>>, RegistryEntry<MultiNoiseBiomeSourceParameterList>> entries, CallbackInfo ci) {
        Thread.dumpStack();
        Phantasm.log("i have been called");
    }
}
