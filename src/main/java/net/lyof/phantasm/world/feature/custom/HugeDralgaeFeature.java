package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.Sized2BlocksFeatureConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class HugeDralgaeFeature extends Feature<Sized2BlocksFeatureConfig> {
    public static final Feature<Sized2BlocksFeatureConfig> INSTANCE = new HugeDralgaeFeature(Sized2BlocksFeatureConfig.CODEC);

    public HugeDralgaeFeature(Codec<Sized2BlocksFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<Sized2BlocksFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int originy = origin.getY();
        Random random = context.getRandom();
        Sized2BlocksFeatureConfig config = context.getConfig();

        if (!world.getBlockState(origin.down()).isIn(ModTags.Blocks.DRALGAE_GROWABLE_ON)) return false;

        int size = config.size().get(random);
        Direction dir = Direction.random(random);
        if (dir.getAxis() == Direction.Axis.Y) dir = dir.rotateClockwise(Direction.Axis.X);

        for (int i = 0; i < size; i++) {
            if (originy + i > world.getTopY() || !world.getBlockState(origin.up(i)).isAir()) return true;

            this.setBlockState(world, origin.up(i), config.primary().get(random, origin.up(i)));
            if (i >= 3 && i % 2 == 0 && i < size - 3) {
                this.setBlockState(world, origin.up(i).offset(dir), config.secondary().get(random, origin.up(i)));
                dir = dir.getOpposite();
            }
        }

        return true;
    }
}