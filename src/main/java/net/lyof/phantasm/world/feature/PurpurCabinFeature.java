package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.utils.BlockHelper;
import net.lyof.phantasm.world.feature.config.PurpurCabinFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PurpurCabinFeature extends Feature<PurpurCabinFeatureConfig> {
    public static final Feature<PurpurCabinFeatureConfig> INSTANCE = new PurpurCabinFeature(PurpurCabinFeatureConfig.CODEC);

    public PurpurCabinFeature(Codec<PurpurCabinFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<PurpurCabinFeatureConfig> context) {
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin().withY(world.getTopY());

        int width = context.getConfig().width().get(random) / 2;
        int length = context.getConfig().length().get(random) / 2;

        int i = world.getTopY();
        while (i > world.getBottomY() && !world.getBlockState(origin.down(world.getTopY() - i)).isOf(ModBlocks.RAW_PURPUR)) {
            i--;
        }

        origin = origin.withY(i);
        if (!world.getBlockState(origin).isOf(ModBlocks.RAW_PURPUR))
            return false;

        origin = origin.withY(closestY(origin.getY()));

        for (int sx = -width; sx <= width; sx ++) {
            for (int sz = -length; sz <= length; sz ++) {
                BlockHelper.placeColumn(world, origin.east(sx).north(sz), 2, Blocks.AIR.getDefaultState());
            }
        }

        decorate(world, origin, width, length);
        return true;
    }

    static void decorate(WorldAccess world, BlockPos origin, int width, int length) {
        BlockHelper.placeColumn(world, origin.east(-width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                (a, b) -> !world.getBlockState(origin.down()).isOf(Blocks.AIR));
        BlockHelper.placeColumn(world, origin.east(width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                (a, b) -> !world.getBlockState(origin.down()).isOf(Blocks.AIR));
        BlockHelper.placeColumn(world, origin.east(-width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                (a, b) -> !world.getBlockState(origin.down()).isOf(Blocks.AIR));
        BlockHelper.placeColumn(world, origin.east(width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                (a, b) -> !world.getBlockState(origin.down()).isOf(Blocks.AIR));

        // TODO: make these take a few positions from a predetermined list, remove them once taken,
        //  and with blocks taken in another list, so it's fully random
        if (!world.getBlockState(origin.east(width-1).north(length-1).down()).isOf(Blocks.AIR))
            world.setBlockState(origin.east(width-1).north(length), Blocks.CRAFTING_TABLE.getDefaultState(), 0);
    }

    static int closestY(int base) {
        if (base >= 37) return 38;
        if (base >= 29) return 30;
        return 22;
    }
}
