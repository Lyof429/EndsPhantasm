package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.world.feature.custom.config.BoulderFeatureConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class BoulderFeature extends Feature<BoulderFeatureConfig> {
    public static final Feature<BoulderFeatureConfig> INSTANCE = new BoulderFeature(BoulderFeatureConfig.CODEC);

    public BoulderFeature(Codec<BoulderFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BoulderFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BoulderFeatureConfig config = context.getConfig();

        List<BlockPos> toPlace = new ArrayList<>();

        BlockPos pos = origin.mutableCopy();
        int size = config.size().get(random);
        Direction primary = Direction.fromHorizontal(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.rotateYClockwise() : primary.rotateYCounterclockwise();

        this.spike(toPlace, pos, 1);
        pos = this.move(pos, primary, secondary, random, world);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2);
            pos = this.move(pos, primary, secondary, random, world);
        }
        this.spike(toPlace, pos, 1);

        for (BlockPos place : toPlace)
            this.setBlockStateIf(world, place, config.block().get(random, place), block -> block.isTransparent(world, place));

        return true;
    }

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, Random random, StructureWorldAccess world) {
        pos = pos.offset(primary);

        if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYClockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYCounterclockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(secondary);

        if (!world.getBlockState(pos).isTransparent(world, pos))
            pos = pos.up();
        if (world.getBlockState(pos.down()).isTransparent(world, pos))
            pos = pos.down();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            if (!world.contains(pos.up())) world.add(pos.up());
            if (!world.contains(pos.up(2))) world.add(pos.up(2));
            if (!world.contains(pos.down())) world.add(pos.down());
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.offset(dir), layer - 1);
    }
}