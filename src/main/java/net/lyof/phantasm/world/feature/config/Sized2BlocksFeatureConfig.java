package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record Sized2BlocksFeatureConfig(IntProvider size, BlockStateProvider primary, BlockStateProvider secondary) implements FeatureConfig {
    public static Codec<Sized2BlocksFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.VALUE_CODEC.fieldOf("size").forGetter(Sized2BlocksFeatureConfig::size),
                    BlockStateProvider.TYPE_CODEC.fieldOf("primary").forGetter(Sized2BlocksFeatureConfig::primary),
                    BlockStateProvider.TYPE_CODEC.fieldOf("secondary").forGetter(Sized2BlocksFeatureConfig::secondary)
            ).apply(instance, Sized2BlocksFeatureConfig::new));
}