package net.lyof.phantasm.world.feature;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ObsidianTowerStructure extends Feature<CountConfig> {
    public static final Feature<CountConfig> INSTANCE = new ObsidianTowerStructure(CountConfig.CODEC);

    public ObsidianTowerStructure(Codec<CountConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<CountConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        CountConfig config = context.getConfig();
        origin = world.getChunk(origin).getPos().getCenterAtY(origin.getY());

        Phantasm.log("Hi " + origin);

        int maxy = config.getCount().get(random) + origin.getY();
        maxy = maxy + 5 - maxy % 5;

        for (int sy = 0; sy <= maxy; sy++) {
            for (int sx = -8; sx < 9; sx++) {
                for (int sz = -8; sz < 9; sz++) {
                    if (sx*sx + sz*sz < 64 && (sx*sx + sz*sz >= 49 || sy == maxy)) {
                        Block block = Blocks.OBSIDIAN;
                        double crying = (sy - 60) / (maxy - 60.0);
                        if (Math.random() + 0.1 < crying * crying && crying > 0)
                            block = Blocks.CRYING_OBSIDIAN;
                        else if (Math.random() < 0.2)
                            block = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN : ModBlocks.POLISHED_OBSIDIAN_BRICKS;

                        world.setBlockState(origin.withY(sy).east(sx).north(sz),
                                block.getDefaultState(), Block.NOTIFY_NEIGHBORS);
                    }
                    else if (sx*sx + sz*sz < 49) {
                        world.setBlockState(origin.withY(sy).east(sx).north(sz),
                                Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS);
                    }
                }
            }
            if (sy != maxy) generateStairs(world, origin.withY(sy));
            //if (sy % 5 == 0 && sy != 0) generateRoom(world, origin.withY(sy - 4));
        }

        return true;
    }

    public static void generateStairs(StructureWorldAccess world, BlockPos center) {
        int y = center.getY();

        for (int sx = -7; sx < 8; sx++) {
            for (int sz = -7; sz < 8; sz++) {
                if (sx*sx + sz*sz >= 36 && sx*sx + sz*sz < 49) {
                    if (sx >= 5 && y % 4 == 0
                            || sx <= -5 && y % 4 == 2
                            || sz >= 5 && y % 4 == 1
                            || sz <= -5 && y % 4 == 3)
                        world.setBlockState(center.east(sx).north(sz),
                                ModBlocks.RAW_PURPUR_BRICKS_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP),
                                Block.NOTIFY_NEIGHBORS);
                }
            }
        }

        if (y > 3) {
            center = center.withY(y - 3);

            if (Math.random() < 0.2 || center.getY() == 1) {
                BlockPos door = center.mutableCopy();
                if (y % 4 == 0) door = door.east(7);
                else if (y % 4 == 2) door = door.west(7);
                else if (y % 4 == 1) door = door.north(7);
                else door = door.south(7);

                door = door.up();
                world.setBlockState(door, Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS);
                world.setBlockState(door.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS);
            }
        }
    }

    public static void generateRoom(StructureWorldAccess world, BlockPos center) {
        if (Math.random() < 0.5 || center.getY() == 1) {
            BlockPos door = center.mutableCopy();
            if (Math.random() <= 0.25) door = door.east(7);
            else if (Math.random() <= 0.25) door = door.west(7);
            else if (Math.random() <= 0.25) door = door.north(7);
            else door = door.south(7);

            world.setBlockState(door, Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS);
            world.setBlockState(door.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_NEIGHBORS);
        }

        if (Math.random() < 0.3) {
            world.setBlockState(center, Blocks.SPAWNER.getDefaultState(), Block.NOTIFY_NEIGHBORS);
            if (world.getBlockEntity(center) instanceof MobSpawnerBlockEntity spawner)
                spawner.setEntityType(EntityType.ZOMBIE, world.getRandom());
            world.setBlockState(center.up(), Blocks.CHEST.getDefaultState(), Block.NOTIFY_NEIGHBORS);
            if (world.getBlockEntity(center.up()) instanceof ChestBlockEntity chest)
                chest.setLootTable(LootTables.STRONGHOLD_CORRIDOR_CHEST, world.getRandom().nextLong());
        }
    }
}
