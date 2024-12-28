package net.lyof.phantasm.world.biome;

import com.mojang.datafixers.util.Pair;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EndDataCompat {
    public static void register() {
        add(ModBiomes.DREAMING_DEN, () -> ConfigEntries.doDreamingDenBiome);
        add(BiomeKeys.END_HIGHLANDS, () -> ConfigEntries.doDreamingDenBiome && ConfigEntries.doAcidburntAbyssesBiome
                && Phantasm.getCompatibilityMode().equals("endercon"));
        add(ModBiomes.ACIDBURNT_ABYSSES, () -> ConfigEntries.doAcidburntAbyssesBiome);
    }


    private static final List<Pair<RegistryKey<Biome>, Supplier<Boolean>>> BIOMES = new ArrayList<>();

    public static void add(RegistryKey<Biome> biome, Supplier<Boolean> condition) {
        BIOMES.add(new Pair<>(biome, condition));
    }

    public static boolean contains(RegistryKey<Biome> biome) {
        return BIOMES.stream().anyMatch(pair -> pair.getFirst() == biome);
    }

    public static List<RegistryKey<Biome>> getEnabledBiomes() {
        List<RegistryKey<Biome>> result = new ArrayList<>();
        for (Pair<RegistryKey<Biome>, Supplier<Boolean>> pair : BIOMES) {
            if (pair.getSecond().get())
                result.add(pair.getFirst());
        }
        return result;
    }
}
