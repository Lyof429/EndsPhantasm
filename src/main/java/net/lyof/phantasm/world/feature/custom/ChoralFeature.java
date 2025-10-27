package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.custom.DirectionalBlock;
import net.lyof.phantasm.setup.ModTags;
import net.lyof.phantasm.world.feature.config.Sized2BlocksFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class ChoralFeature extends Feature<Sized2BlocksFeatureConfig> {
    public static final Feature<Sized2BlocksFeatureConfig> INSTANCE = new ChoralFeature(Sized2BlocksFeatureConfig.CODEC);

    public ChoralFeature(Codec<Sized2BlocksFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<Sized2BlocksFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        Sized2BlocksFeatureConfig config = context.getConfig();

        List<BlockPos> toPlace = new ArrayList<>();
        List<BlockPos> fans = new ArrayList<>();

        int size = config.size().get(random);
        Direction primary = Direction.fromHorizontal(random.nextInt(4));
        Direction secondary = random.nextBoolean() ? primary.rotateYClockwise() : primary.rotateYCounterclockwise();

        BlockPos pos = new BlockPos(origin).withY(0);
        while (pos.getY() < world.getHeight() && !(world.getBlockState(pos.up()).isIn(ModTags.Blocks.END_PLANTS_GROWABLE_ON)
                && world.getBlockState(pos).isOf(Blocks.AIR))) {

            pos = pos.up();
        }
        if (pos.getY() >= world.getHeight() - 2) return false;

        this.spike(toPlace, pos, 1);
        this.spike(fans, pos, 2);
        pos = this.move(pos, primary, secondary, random, world);
        for (int i = 0; i < size; i++) {
            this.spike(toPlace, pos, 2);
            this.spike(fans, pos, 3);
            pos = this.move(pos, primary, secondary, random, world);
        }
        this.spike(toPlace, pos, 1);
        this.spike(fans, pos, 2);

        for (BlockPos p : toPlace)
            this.setBlockStateIf(world, p, config.primary().get(random, p),
                    block -> size > 0 && block.isTransparent(world, p));
        for (BlockPos p : fans) {
            BlockState state = config.secondary().get(random, p);
            if (state.getBlock() instanceof DirectionalBlock directional)
                state = directional.getPlacementState(world, p);
            this.setBlockStateIf(world, p, state,
                    block -> random.nextInt(5) == 0 && block.isTransparent(world, p));
        }


        if (size == 0 && Math.random() < 0.7) {
            FeatureContext<Sized2BlocksFeatureConfig> contextnext =
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

    public BlockPos move(BlockPos pos, Direction primary, Direction secondary, Random random, StructureWorldAccess world) {
        pos = pos.offset(primary);

        if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYClockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(primary.rotateYCounterclockwise());
        else if (random.nextInt(4) == 0) pos = pos.offset(secondary);

        if (!world.getBlockState(pos).isTransparent(world, pos))
            pos = pos.down();
        if (world.getBlockState(pos.up()).isTransparent(world, pos))
            pos = pos.up();

        return pos;
    }

    public void spike(List<BlockPos> world, BlockPos pos, int layer) {
        if (layer <= 0) {
            if (!world.contains(pos)) world.add(pos);
            return;
        }

        for (Direction dir : Direction.values())
            this.spike(world, pos.offset(dir), layer - 1);
    }
}