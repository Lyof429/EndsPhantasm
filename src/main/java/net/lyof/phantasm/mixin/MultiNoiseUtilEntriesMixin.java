package net.lyof.phantasm.mixin;

import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Mixin(value = MultiNoiseUtil.Entries.class, priority = 1004)
public abstract class MultiNoiseUtilEntriesMixin<T> {
    @Shadow public abstract List<Pair<MultiNoiseUtil.NoiseHypercube, T>> getEntries();

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
            int customCount = EndDataCompat.getEnabledBiomes().size();

            // hook the custom biomes in
            int j = 0;
            for (RegistryKey<Biome> biome : EndDataCompat.getEnabledBiomes()) {
                Phantasm.log("Adding " + biome.getValue() + " to the End biome source at slice " + (j/2 + 1) + " out of " + customCount);
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, customCount, j),
                        (T) ModWorldGeneration.LOOKUP.getOrThrow(biome)
                ));
                j += 2;
            }

            // add all the default biomes
            this.endEntries.addAll(entries.stream()
                    .filter(p -> p.getSecond() instanceof RegistryEntry r && !r.matchesKey(BiomeKeys.END_HIGHLANDS)
                            && !(r.getKey().get() instanceof RegistryKey k && EndDataCompat.contains(k))).toList());

            // add back the end highlands
            for (int i = 1; i <= customCount; i += 2)
                this.endEntries.add(new Pair<>(
                        splitHypercube(highlands, customCount, i),
                        (T) ModWorldGeneration.LOOKUP.getOrThrow(BiomeKeys.END_HIGHLANDS)
                ));
        }
    }

    private static MultiNoiseUtil.NoiseHypercube splitHypercube(MultiNoiseUtil.NoiseHypercube base, int biomes, int i) {
        biomes = biomes * 2 - 1;
        //i = i * 2;

        if (EndDataCompat.getCompatibilityMode().equals("endercon"))
            return MultiNoiseUtil.createNoiseHypercube(
                    splitRange(base.temperature(), biomes, i),
                    base.humidity(),
                    base.continentalness(),
                    base.erosion(),
                    base.depth(),
                    base.weirdness(),
                    base.offset() / 10000f);

        else if (EndDataCompat.getCompatibilityMode().equals("nullscape"))
            return MultiNoiseUtil.createNoiseHypercube(
                    base.temperature(),
                    base.humidity(),
                    base.continentalness(),//splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
                    base.depth(),
                    splitRange(base.weirdness(), biomes, i),
                    base.offset() / 10000f);

        else
            return MultiNoiseUtil.createNoiseHypercube(
                    base.temperature(), //splitRange(base.temperature(), biomes, i),
                    base.humidity(), //splitRange(base.humidity(), biomes, i),
                    splitRange(base.continentalness(), biomes, i),
                    base.erosion(),
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
        return point.max() - point.min();
    }

    @ModifyArg(method = "<init>", index = 0, at = @At(value = "INVOKE",
               target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;create(Ljava/util/List;)Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$SearchTree;"))
    public List<Pair<MultiNoiseUtil.NoiseHypercube, T>> modifyTree(List<Pair<MultiNoiseUtil.NoiseHypercube, T>> entries) {
        return this.getEntries();
    }

    @Inject(method = "getEntries", at = @At("HEAD"), cancellable = true)
    public void redirectEntries(CallbackInfoReturnable<List<Pair<MultiNoiseUtil.NoiseHypercube, T>>> cir) {
        if (!this.endEntries.isEmpty()) cir.setReturnValue(this.endEntries);
    }
}
