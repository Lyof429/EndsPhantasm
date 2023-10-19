package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record CrystalSpikeFeatureConfig(int number, Identifier blockId) implements FeatureConfig {
    public static Codec<CrystalSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance.group(
                            // you can add as many of these as you want, one for each parameter
                            Codecs.POSITIVE_INT.fieldOf("number").forGetter(CrystalSpikeFeatureConfig::number),
                            Identifier.CODEC.fieldOf("blockID").forGetter(CrystalSpikeFeatureConfig::blockId)
                    ).apply(instance, CrystalSpikeFeatureConfig::new));
}