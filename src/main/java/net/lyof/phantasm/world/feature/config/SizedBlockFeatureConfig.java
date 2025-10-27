package net.lyof.phantasm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record SizedBlockFeatureConfig(IntProvider size, BlockStateProvider block) implements FeatureConfig {
    public static Codec<SizedBlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.VALUE_CODEC.fieldOf("size").forGetter(SizedBlockFeatureConfig::size),
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(SizedBlockFeatureConfig::block)
            ).apply(instance, SizedBlockFeatureConfig::new));
}