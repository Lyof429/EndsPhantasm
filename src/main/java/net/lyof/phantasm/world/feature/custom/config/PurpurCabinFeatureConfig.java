package net.lyof.phantasm.world.feature.custom.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record PurpurCabinFeatureConfig(IntProvider width, IntProvider length) implements FeatureConfig {
    public static Codec<PurpurCabinFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("width").forGetter(PurpurCabinFeatureConfig::width),
                    IntProvider.POSITIVE_CODEC.fieldOf("length").forGetter(PurpurCabinFeatureConfig::length)
            ).apply(instance, PurpurCabinFeatureConfig::new));
}