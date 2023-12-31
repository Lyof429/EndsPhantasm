package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.utils.BlockHelper;
import net.lyof.phantasm.world.feature.config.PurpurCabinFeatureConfig;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

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
        BiPredicate<WorldView, BlockPos> condition = (a, b) -> a.getBlockState(b.down()).isOf(ModBlocks.RAW_PURPUR)
                || a.getBlockState(b.down(2)).isOf(ModBlocks.RAW_PURPUR);

        BlockHelper.placeColumn(world, origin.east(-width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                condition);
        BlockHelper.placeColumn(world, origin.east(width).north(-length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                condition);
        BlockHelper.placeColumn(world, origin.east(-width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                condition);
        BlockHelper.placeColumn(world, origin.east(width).north(length), 2, ModBlocks.RAW_PURPUR_PILLAR.getDefaultState(),
                condition);


        List<BlockPos> positions = new ArrayList<>();
        for (int i = 1-width; i < width; i++) {
            positions.add(origin.north(length).east(i));
            positions.add(origin.north(-length).east(i));
        }
        for (int i = 1-length; i < length; i++) {
            positions.add(origin.east(width).north(i));
            positions.add(origin.east(-width).north(i));
        }

        List<BlockState> states = new ArrayList<>(List.of(
                Blocks.CRAFTING_TABLE.getDefaultState(),
                Blocks.STONECUTTER.getDefaultState(),
                Blocks.FURNACE.getDefaultState(),
                Blocks.FURNACE.getDefaultState().rotate(BlockRotation.CLOCKWISE_90),
                Blocks.FURNACE.getDefaultState().rotate(BlockRotation.CLOCKWISE_180),
                Blocks.FURNACE.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90),
                Blocks.BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.UP),
                Blocks.BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.UP)
        ));

        Random rnd = Random.create();
        BlockPos pos;
        for (int j = 0; j < rnd.nextBetween(3, 6); j++) {
            pos = positions.remove(rnd.nextBetweenExclusive(0, positions.size()));
            if (world.getBlockState(pos.down()).isOf(ModBlocks.RAW_PURPUR)) {
                world.setBlockState(pos, states.remove(rnd.nextBetweenExclusive(0, states.size())), 3);
                if (world.getBlockEntity(pos) instanceof BarrelBlockEntity barrel)
                    setLootBarrel(barrel);
            }
        }


        for (Direction direction : List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            for (int i = 0; i < rnd.nextBetween(12, 20); i++)
                BlockHelper.placeColumn(world, origin.offset(direction, i), 2, Blocks.AIR.getDefaultState(),
                        (a, b) -> a.getBlockState(b).isOf(ModBlocks.RAW_PURPUR));
        }
    }

    static int closestY(int base) {
        if (base >= 37) return 38;
        if (base >= 29) return 30;
        return 22;
    }

    static void setLootBarrel(BarrelBlockEntity barrel) {
        barrel.setLootTable(Identifier.of("minecraft", "chests/end_city_treasure"), RandomSeed.getSeed());
        //barrel.setStack(0, new ItemStack(Items.IRON_INGOT));
    }
}
