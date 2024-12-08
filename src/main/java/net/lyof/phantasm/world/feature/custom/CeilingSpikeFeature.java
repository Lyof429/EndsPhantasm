package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.custom.config.BoulderFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CeilingSpikeFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new CeilingSpikeFeature(BoulderFeatureConfig.CODEC);

    public CeilingSpikeFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BoulderFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BoulderFeatureConfig config = context.getConfig();

        int size = config.size().get(random);

        List<BlockPos> toPlace = new ArrayList<>();
        Map<Direction, Integer> sizes = new HashMap<>();
        for (Direction dir : Direction.values())
            sizes.put(dir, size - random.nextBetween(2, 4));


        BlockPos pos = new BlockPos(origin).withY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.up()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).isOf(Blocks.AIR))) {

            pos = pos.up();
        }
        pos = pos.up();
        if (pos.getY() >= world.getHeight() - 2) return false;

        for (int i = 0; i < size; i++) {
            BlockPos finalPos = pos;
            this.setBlockStateIf(world, pos, config.block().get(random, pos), block -> block.isTransparent(world, finalPos));
            for (int k = 0; k < 4; k++) {
                if (i < sizes.get(Direction.fromHorizontal(k)))
                    this.setBlockStateIf(world, pos.offset(Direction.fromHorizontal(k)), config.block().get(random, pos),
                            block -> block.isTransparent(world, finalPos));
            }

            pos = pos.down();
        }

        return true;
    }
}