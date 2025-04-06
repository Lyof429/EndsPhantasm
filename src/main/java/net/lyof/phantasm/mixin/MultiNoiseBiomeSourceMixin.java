package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin {
    @Shadow protected abstract MultiNoiseUtil.Entries<RegistryEntry<Biome>> getBiomeEntries();

    @Inject(method = "getBiome", at = @At("HEAD"), cancellable = true)
    public void forceMainIsland(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise,
                                CallbackInfoReturnable<RegistryEntry<Biome>> cir) {

        if (!ConfigEntries.forceMainIsland || ModWorldGeneration.LOOKUP == null ||
                this.getBiomeEntries().getEntries().stream().noneMatch(p -> p.getSecond().matchesKey(BiomeKeys.THE_END)))
            return;

        int i = BiomeCoords.toBlock(x);
        int k = BiomeCoords.toBlock(z);
        long l = ChunkSectionPos.getSectionCoord(i);
        long m = ChunkSectionPos.getSectionCoord(k);
        if (l*l + m*m <= 4096L) {
            cir.setReturnValue(ModWorldGeneration.LOOKUP.getOrThrow(BiomeKeys.THE_END));
        }
    }
}
