package net.lyof.phantasm.mixin;

import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiNoiseUtil.Entries.class)
public class MultiNoiseUtilEntriesMixin<T> {
    @Unique private final List<Pair<MultiNoiseUtil.NoiseHypercube, T>> endEntries = new ArrayList<>();

    @Inject(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;create(Ljava/util/List;)Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;"))
    public void addEndBiomes(List<Pair<MultiNoiseUtil.NoiseHypercube, T>> entries, CallbackInfo ci) {
        MultiNoiseUtil.NoiseHypercube highlands = null;
        for (Pair<MultiNoiseUtil.NoiseHypercube, T> e : entries) {
            if (e.getSecond() instanceof RegistryEntry r && r.matchesKey(BiomeKeys.END_HIGHLANDS))
                highlands = e.getFirst();
        }

        if (highlands != null && ModWorldGeneration.LOOKUP != null) {
            int custom_count = EndDataCompat.getEnabledBiomes().size();

            // hook the custom biomes in
            int j = 0;
            for (RegistryKey<Biome> biome : EndDataCompat.getEnabledBiomes()) {
                Phantasm.log("Adding " + biome.getValue() + " to the End biome source at slice " + (j/2 + 1) + " out of " + custom_count);
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, custom_count, j),
                        (T) ModWorldGeneration.LOOKUP.getOrThrow(biome)
                ));
                j += 2;
            }

            // add all the default biomes
            this.endEntries.addAll(entries.stream()
                    .filter(p -> p.getSecond() instanceof RegistryEntry r && !r.matchesKey(BiomeKeys.END_HIGHLANDS)
                            && !(r.getKey().get() instanceof RegistryKey k && EndDataCompat.contains(k))).toList());

            // add back the end highlands
            for (int i = 1; i <= custom_count; i += 2)
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, custom_count, i),
                        (T) ModWorldGeneration.LOOKUP.getOrThrow(BiomeKeys.END_HIGHLANDS)
                ));
        }
    }

    private static MultiNoiseUtil.NoiseHypercube splitHypercube(MultiNoiseUtil.NoiseHypercube base, int biomes, int i) {
        biomes = biomes * 2 - 1;
        //i = i * 2;

        if (ConfigEntries.overrideTemperature)
            return MultiNoiseUtil.createNoiseHypercube(
                    splitRange(base.temperature(), biomes, i),
                    base.humidity(),
                    base.continentalness(),
                    base.erosion(),
                    base.depth(),
                    base.weirdness(),
                    base.offset() / 10000f);

        else if (Phantasm.getCompatibilityMode().equals("endercon"))
            return MultiNoiseUtil.createNoiseHypercube(
                    base.temperature(),
                    base.humidity(),
                    splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
                    base.depth(),
                    base.weirdness(),
                    base.offset() / 10000f);

        else if (Phantasm.getCompatibilityMode().equals("nullscape"))
            return MultiNoiseUtil.createNoiseHypercube(
                    base.temperature(),
                    base.humidity(),
                    splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
                    base.depth(),
                    base.weirdness(),
                    base.offset() / 10000f);

        else
            return MultiNoiseUtil.createNoiseHypercube(
                    base.temperature(), //splitRange(base.temperature(), biomes, i),
                    base.humidity(), //splitRange(base.humidity(), biomes, i),
                    base.continentalness(),
                    splitRange(base.erosion(), biomes, i),
                    base.depth(), //splitRange(base.depth(), biomes, i),
                    base.weirdness(), //splitRange(base.weirdness(), biomes, i),
                    base.offset() / 10000f);
    }

    private static MultiNoiseUtil.ParameterRange splitRange(MultiNoiseUtil.ParameterRange point, int biomes, int i) {
        long min = (getRange(point) / biomes * i) + point.min();
        long max = (getRange(point) / biomes * (i+1)) + point.min();
        return MultiNoiseUtil.ParameterRange.of(min / 10000f, max / 10000f);
    }

    private static long getRange(MultiNoiseUtil.ParameterRange point) {
        if (ConfigEntries.overrideTemperature)
            return MultiNoiseUtil.toLong(0.5625f + 0.84375f);
        return point.max() - point.min();
    }

    @ModifyArg(method = "<init>", index = 0, at = @At(value = "INVOKE",
               target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;create(Ljava/util/List;)Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;"))
    public List<Pair<MultiNoiseUtil.NoiseHypercube, T>> modifyTree(List<Pair<MultiNoiseUtil.NoiseHypercube, T>> entries) {
        return this.endEntries.isEmpty() ? entries : this.endEntries;
    }

    @Inject(method = "getEntries", at = @At("HEAD"), cancellable = true)
    public void redirectEntries(CallbackInfoReturnable<List<Pair<MultiNoiseUtil.NoiseHypercube, T>>> cir) {
        if (!this.endEntries.isEmpty()) cir.setReturnValue(this.endEntries);
    }
}