package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.CrystalShardBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.config.CrystalSpikeFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.BlockColumnFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class OblivineFeature extends Feature<BlockColumnFeatureConfig> {
    public static final Feature<BlockColumnFeatureConfig> INSTANCE = new OblivineFeature(BlockColumnFeatureConfig.CODEC);

    public OblivineFeature(Codec<BlockColumnFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BlockColumnFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BlockColumnFeatureConfig config = context.getConfig();

        int size = world.getRandom().nextBetween(3, 12);
        Phantasm.log(size);

        BlockPos pos = new BlockPos(origin).withY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.up()).isIn(ModTags.Blocks.OBLIVINE_GROWABLE_ON)
                && world.getBlockState(pos).isOf(Blocks.AIR))) {

            pos = pos.up();
        }

        for (int i = 0; i < size; i++) {
            if (pos.getY() <= world.getBottomY() + 1)
                return true;

            BlockState state = config.layers().get(0).state().get(random, pos);
            world.setBlockState(pos, state, Block.NOTIFY_NEIGHBORS);
            pos = pos.down();
        }

        return true;
    }
}
