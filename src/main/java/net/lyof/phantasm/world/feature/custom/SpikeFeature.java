package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.SizedBlockFeatureConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.HashMap;
import java.util.Map;

public class SpikeFeature extends Feature<SizedBlockFeatureConfig> {
    public static final Feature<SizedBlockFeatureConfig> INSTANCE = new SpikeFeature(SizedBlockFeatureConfig.CODEC);

    public SpikeFeature(Codec<SizedBlockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SizedBlockFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();
        SizedBlockFeatureConfig config = context.getConfig();


        this.generate(world, pos, config, random, 0);

        return true;
    }

    public void generate(StructureWorldAccess world, BlockPos pos, SizedBlockFeatureConfig config, Random random, int spread) {

        if (!world.getBlockState(pos.down()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON))
            return;

        int size = config.size().get(random) - spread;
        if (size < 0) return;

        Map<Direction, Integer> sizes = new HashMap<>();
        if (size < random.nextBetween(6, 8)) for (Direction dir : Direction.values())
            sizes.put(dir, -2);
        else for (Direction dir : Direction.values())
            sizes.put(dir, size - random.nextBetween(2, 4));

        pos = pos.down(2);
        for (int i = -2; i < size; i++) {
            BlockPos finalPos = pos;
            this.setBlockStateIf(world, pos, config.block().get(random, pos), block -> block.isTransparent(world, finalPos));
            for (int k = -0; k < 4; k++) {
                if (i < sizes.get(Direction.fromHorizontal(k)))
                    this.setBlockStateIf(world, pos.offset(Direction.fromHorizontal(k)), config.block().get(random, pos),
                            block -> block.isTransparent(world, finalPos));
            }

            pos = pos.up();
        }

        if (random.nextDouble() < 0.8) {
            BlockPos newPos = this.findY(world, pos.east(random.nextBetween(-5, 5)).north(random.nextBetween(-5, 5)));
            if (newPos != null)
                this.generate(world, newPos, config, random, spread + 1);
        }
        if (spread < 2) {
            BlockPos newPos = this.findY(world, pos.east(random.nextBetween(-5, 5)).north(random.nextBetween(-5, 5)));
            if (newPos != null)
                this.generate(world, newPos, config, random, spread + 1);
        }
    }

    public BlockPos findY(StructureWorldAccess world, BlockPos start) {
        BlockPos pos;
        for (int i = 0; i < 20; i++) {
            pos = start.withY((int) (start.getY() + Math.pow(-1, i)*i/2));
            if (world.getBlockState(pos).isReplaceable() && world.getBlockState(pos.down()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                    && !world.getBlockState(pos.down()).isOf(ModBlocks.CIRITE))
                return pos;
        }
        return null;
    }
}