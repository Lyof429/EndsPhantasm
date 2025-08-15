package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
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

        int size = world.getRandom().nextBetween(4, 12);

        BlockPos pos = new BlockPos(origin).withY(world.getBottomY());
        while (pos.getY() < world.getTopY() && !(world.getBlockState(pos.up()).isOf(ModBlocks.OBLIVION)
                && world.getBlockState(pos).isOf(Blocks.AIR))) {

            pos = pos.up();
        }

        for (int i = 0; i < size; i++) {
            if (pos.getY() < world.getBottomY() || pos.getY() >= world.getTopY())
                return false;

            BlockState state = config.layers().get(0).state().get(random, pos);
            this.setBlockStateIf(world, pos, state, AbstractBlock.AbstractBlockState::isAir);
            pos = pos.down();
        }
        if (random.nextInt(5) == 0)
            this.setBlockState(world, pos, ModBlocks.CRYSTALILY.getDefaultState());

        if (Math.random() < 0.9) {
            FeatureContext<BlockColumnFeatureConfig> contextnext =
                    new FeatureContext<>(context.getFeature(),
                            context.getWorld(),
                            context.getGenerator(),
                            context.getRandom(),
                            context.getOrigin().east(random.nextBetween(-5, 5)).north(random.nextBetween(-5, 5)),
                            config);
            if (world.isChunkLoaded(contextnext.getOrigin()))
                this.generate(contextnext);
        }

        return true;
    }
}
