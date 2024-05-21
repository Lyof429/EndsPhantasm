package net.lyof.phantasm.world.feature.custom.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record SingleBlockFeatureConfig(IntProvider y, BlockStateProvider state) implements FeatureConfig {
    public static Codec<SingleBlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.VALUE_CODEC.fieldOf("y_level").forGetter(SingleBlockFeatureConfig::y),
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(SingleBlockFeatureConfig::state)
            ).apply(instance, SingleBlockFeatureConfig::new));
}