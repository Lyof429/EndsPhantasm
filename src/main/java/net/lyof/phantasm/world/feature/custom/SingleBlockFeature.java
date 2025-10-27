package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.world.feature.config.SizedBlockFeatureConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SingleBlockFeature extends Feature<SizedBlockFeatureConfig> {
    public static final Feature<SizedBlockFeatureConfig> INSTANCE = new SingleBlockFeature(SizedBlockFeatureConfig.CODEC);

    public SingleBlockFeature(Codec<SizedBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SizedBlockFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        SizedBlockFeatureConfig config = context.getConfig();

        this.setBlockStateIf(world, origin.withY(config.size().get(random)),
                config.block().get(random, origin), AbstractBlock.AbstractBlockState::isReplaceable);

        return true;
    }
}