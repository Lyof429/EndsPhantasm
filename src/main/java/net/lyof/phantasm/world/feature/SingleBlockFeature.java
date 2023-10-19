package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.world.feature.config.SingleBlockFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SingleBlockFeature extends Feature<SingleBlockFeatureConfig> {
    public static final Feature<SingleBlockFeatureConfig> INSTANCE = new SingleBlockFeature(SingleBlockFeatureConfig.CODEC);

    public SingleBlockFeature(Codec<SingleBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SingleBlockFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        SingleBlockFeatureConfig config = context.getConfig();

        int y = config.y().get(random);
        BlockState state = config.state().get(random, origin);

        world.setBlockState(origin.withY(y), state, 0x10);

        return true;
    }
}