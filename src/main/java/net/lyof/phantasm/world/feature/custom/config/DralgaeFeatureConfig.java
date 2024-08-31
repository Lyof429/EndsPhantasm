package net.lyof.phantasm.world.feature.custom.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record DralgaeFeatureConfig(IntProvider size, BlockStateProvider stem, BlockStateProvider fruit) implements FeatureConfig {
    public static Codec<DralgaeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(DralgaeFeatureConfig::size),
                    BlockStateProvider.TYPE_CODEC.fieldOf("stem").forGetter(DralgaeFeatureConfig::stem),
                    BlockStateProvider.TYPE_CODEC.fieldOf("fruit").forGetter(DralgaeFeatureConfig::fruit)
            ).apply(instance, DralgaeFeatureConfig::new));
}