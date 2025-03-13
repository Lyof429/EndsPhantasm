package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ObsidianTowerStructure extends Feature<CountConfig> {
    private static final Identifier DRAGLING = Identifier.of("unusualend", "dragling");
    private static final Identifier LOOT_TABLE = Phantasm.makeID("chests/obsidian_tower");

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


        int miny = world.getBottomY();
        int maxy = config.getCount().get(random) + origin.getY();
        maxy = maxy + 7 - maxy % 7;

        for (int sy = maxy; sy >= miny; sy--) {
            for (int sx = -8; sx < 9; sx++) {
                for (int sz = -8; sz < 9; sz++) {
                    if (sx*sx + sz*sz < 64 && (sx*sx + sz*sz >= 49 || sy == maxy)) {
                        Block block = Blocks.OBSIDIAN;
                        double crying = (sy - 60) / (maxy - 60.0);
                        if (Math.random() + 0.1 < crying * crying && crying > 0)
                            block = Blocks.CRYING_OBSIDIAN;
                        else if (Math.random() < 0.2)
                            block = Math.random() < 0.5 ? ModBlocks.POLISHED_OBSIDIAN : ModBlocks.POLISHED_OBSIDIAN_BRICKS;

                        this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                block.getDefaultState());
                    }
                    else if (sx*sx + sz*sz < 64 && sy == miny) {
                        this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                Blocks.END_PORTAL.getDefaultState());
                    }
                    else if (sx*sx + sz*sz < 49) {
                        this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                Blocks.AIR.getDefaultState());
                    }
                }
            }
            if (world.getBlockState(origin.withY(sy)).isAir()) this.setBlockState(world, origin.withY(sy), Blocks.CHAIN.getDefaultState());
            if (sy < maxy && sy > miny) this.putStairs(world, origin.withY(sy));
            if (sy % 7 == 0 && sy != maxy && sy != miny) this.putPlatform(world, origin.withY(sy), random.nextInt(7));
            //if (sy % 5 == 0 && sy != 0) generateRoom(world, origin.withY(sy - 4));
        }

        return true;
    }

    public void putStairs(StructureWorldAccess world, BlockPos center) {
        int y = center.getY() - world.getBottomY();

        for (int sx = -7; sx < 8; sx++) {
            for (int sz = -7; sz < 8; sz++) {
                if (sx*sx + sz*sz >= 36 && sx*sx + sz*sz < 49) {
                    if (sx >= 5 && y % 4 == 0
                            || sx <= -5 && y % 4 == 2
                            || sz >= 5 && y % 4 == 1
                            || sz <= -5 && y % 4 == 3)
                        this.setBlockState(world, center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP));
                }
            }
        }

        if (Math.random() < 0.2 || center.getY() == 1) {
            BlockPos door = center.mutableCopy();
            if (y % 4 == 0) door = door.east(7);
            else if (y % 4 == 2) door = door.west(7);
            else if (y % 4 == 1) door = door.north(7);
            else door = door.south(7);

            door = door.up();
            this.setBlockState(world, door, Blocks.AIR.getDefaultState());
            this.setBlockState(world, door.up(), Blocks.AIR.getDefaultState());
        }
    }

    public void putPlatform(ServerWorldAccess world, BlockPos center, int roomtype) {
        if (roomtype == 0) {
            for (int sx = -5; sx < 6; sx++) {
                for (int sz = -5; sz < 6; sz++) {
                    if (sx * sx + sz * sz < 16) {
                        this.setBlockState(world, center.east(sx).north(sz), Blocks.PURPUR_BLOCK.getDefaultState());
                    }
                }
            }
        }
        else if (roomtype == 1) {
            for (int sx = -5; sx < 6; sx++) {
                for (int sz = -5; sz < 6; sz++) {
                    if (sx * sx + sz * sz < 16) {
                        this.setBlockState(world, center.east(sx).north(sz), Blocks.PURPUR_BLOCK.getDefaultState());
                    }
                }
            }
            this.setBlockState(world, center.up(), Blocks.CHEST.getDefaultState());
            if (world.getBlockEntity(center.up()) instanceof ChestBlockEntity chest)
                chest.setLootTable(LOOT_TABLE, world.getRandom().nextLong());
        }
        else if (roomtype == 2) {
            this.setBlockState(world, center, Blocks.SPAWNER.getDefaultState());
            if (world.getBlockEntity(center) instanceof MobSpawnerBlockEntity spawner) {
                NbtCompound nbt = spawner.getLogic().writeNbt(new NbtCompound());
                nbt.putShort("MinSpawnDelay", (short) 200);
                nbt.putShort("SpawnCount", (short) 1);
                nbt.putShort("MaxNearbyEntities", (short) 2);
                nbt.remove("SpawnData");
                spawner.getLogic().readNbt(null, center, nbt);

                EntityType<?> dragling =  Registries.ENTITY_TYPE.get(DRAGLING);
                spawner.setEntityType(dragling == EntityType.PIG ? EntityType.VEX : dragling, world.getRandom());
            }
        }
        else if (roomtype == 3) {
            center = center.north(world.getRandom().nextBetween(-3, 3)).east(world.getRandom().nextBetween(-3, 3));
            BlockPos.iterateInSquare(center, 2, Direction.NORTH, Direction.EAST).forEach(pos ->
                    this.setBlockState(world, pos, Blocks.OBSIDIAN.getDefaultState())
            );
            this.setBlockState(world, center.up(), Blocks.CHEST.getDefaultState());
            if (world.getBlockEntity(center.up()) instanceof ChestBlockEntity chest)
                chest.setLootTable(LootTables.END_CITY_TREASURE_CHEST, world.getRandom().nextLong());
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
