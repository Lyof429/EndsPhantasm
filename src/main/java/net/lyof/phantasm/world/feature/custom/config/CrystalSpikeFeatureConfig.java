package net.lyof.phantasm.world.feature.custom.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record CrystalSpikeFeatureConfig(IntProvider size, float voidChance) implements FeatureConfig {
    public static Codec<CrystalSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(CrystalSpikeFeatureConfig::size),
                    Codecs.POSITIVE_FLOAT.fieldOf("void_chance").forGetter(CrystalSpikeFeatureConfig::voidChance)
            ).apply(instance, CrystalSpikeFeatureConfig::new));
}