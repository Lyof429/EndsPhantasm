package net.lyof.phantasm.world.feature.custom;

import com.mojang.serialization.Codec;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
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

public class ShatteredTowerStructure extends Feature<CountConfig> {
    public static final int R = 7;

    private static final Identifier DRAGLING = Identifier.of("unusualend", "dragling");
    private static final Identifier LOOT_TABLE = Phantasm.makeID("chests/shattered_tower");
    private static final Identifier CHALLENGE = Phantasm.makeID("shattered_tower");

    public static final Feature<CountConfig> INSTANCE = new ShatteredTowerStructure(CountConfig.CODEC);

    public ShatteredTowerStructure(Codec<CountConfig> configCodec) {
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

        this.setBlockState(world, origin.withY(maxy + 1), ModBlocks.CHALLENGE_RUNE.getDefaultState());
        if (world.getBlockEntity(origin.withY(maxy + 1)) instanceof ChallengeRuneBlockEntity challengeRune) {
            challengeRune.renderBase = true;
            challengeRune.setChallenge(CHALLENGE);
        }

        for (int sy = maxy; sy >= miny; sy--) {
            for (int sx = -R; sx <= R; sx++) {
                for (int sz = -R; sz <= R; sz++) {
                    if (sx*sx + sz*sz < R*R) {
                        if (sx*sx + sz*sz >= (R-1)*(R-1)) {
                            Block block = Blocks.OBSIDIAN;
                            double crying = (sy - 70) / (maxy - 70.0);
                            if (Math.random() + 0.1 < crying * crying * crying)
                                block = Blocks.CRYING_OBSIDIAN;
                            else if (Math.random() < 0.35)
                                block = Math.random() < 0.4 ? ModBlocks.POLISHED_OBSIDIAN : ModBlocks.POLISHED_OBSIDIAN_BRICKS;

                            this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                    block.getDefaultState());
                        }
                        else if (sy == maxy) {
                            this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                    ModBlocks.POLISHED_OBSIDIAN_BRICKS.getDefaultState());
                        }
                        else if (sy == miny) {
                            this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                    Blocks.END_PORTAL.getDefaultState());
                        }
                    }
                    else if (sx*sx + sz*sz < (R-1)*(R-1)) {
                        this.setBlockState(world, origin.withY(sy).east(sx).north(sz),
                                Blocks.AIR.getDefaultState());
                    }
                }
            }
            if (world.getBlockState(origin.withY(sy)).isAir()) this.setBlockState(world, origin.withY(sy), Blocks.CHAIN.getDefaultState());
            if (sy < maxy && sy > miny) this.putStairs(world, origin.withY(sy));
            if (sy % 7 == 0 && sy != maxy && sy != miny) this.putPlatform(world, origin.withY(sy), random.nextInt(7));
        }

        return true;
    }

    public void putStairs(StructureWorldAccess world, BlockPos center) {
        int y = center.getY() - world.getBottomY();

        for (int sx = -R+1; sx <= R-1; sx++) {
            for (int sz = -R+1; sz <= R-1; sz++) {
                if (sx*sx + sz*sz >= (R-2)*(R-2) && sx*sx + sz*sz < (R-1)*(R-1)) {
                    if (sx == R-3 && sz == R-3 && y % 4 == 1
                            || sx == -R+3 && sz == -R+3 && y % 4 == 3
                            || sx == R-3 && sz == -R+3 && y % 4 == 0
                            || sx == -R+3 && sz == R-3 && y % 4 == 2) {

                        this.setBlockState(world, center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM));
                    }

                    if (sx > R-3 && y % 4 == 0
                            || sx < -R+3 && y % 4 == 2
                            || sz > R-3 && y % 4 == 1
                            || sz < -R+3 && y % 4 == 3)
                        this.setBlockState(world, center.east(sx).north(sz),
                                Blocks.PURPUR_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP));
                }
            }
        }

        if (Math.random() < 0.2 || center.getY() == 1) {
            BlockPos door = center.mutableCopy();
            if (y % 4 == 0) door = door.east(R-1);
            else if (y % 4 == 2) door = door.west(R-1);
            else if (y % 4 == 1) door = door.north(R-1);
            else door = door.south(R-1);

            door = door.up();
            this.setBlockState(world, door, Blocks.AIR.getDefaultState());
            this.setBlockState(world, door.up(), Blocks.AIR.getDefaultState());
        }
    }

    public void putPlatform(ServerWorldAccess world, BlockPos center, int roomtype) {
        if (roomtype == 0) {
            for (int sx = -R+3; sx <= R-3; sx++) {
                for (int sz = -R+3; sz <= R-3; sz++) {
                    if (sx * sx + sz * sz < (R-4)*(R-4)) {
                        this.setBlockState(world, center.east(sx).north(sz), Blocks.PURPUR_BLOCK.getDefaultState());
                    }
                }
            }
        }
        else if (roomtype == 1) {
            for (int sx = -R+3; sx <= R-3; sx++) {
                for (int sz = -R+3; sz <= R-3; sz++) {
                    if (sx * sx + sz * sz < (R-4)*(R-4)) {
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
            center = center.north(world.getRandom().nextBetween(-R+4, R-4)).east(world.getRandom().nextBetween(-R+4, R-4));
            BlockPos.iterateInSquare(center, 1, Direction.NORTH, Direction.EAST).forEach(pos ->
                    this.setBlockState(world, pos, Blocks.OBSIDIAN.getDefaultState())
            );
            this.setBlockState(world, center.up(), Blocks.CHEST.getDefaultState());
            if (world.getBlockEntity(center.up()) instanceof ChestBlockEntity chest)
                chest.setLootTable(LootTables.END_CITY_TREASURE_CHEST, world.getRandom().nextLong());
        }
    }

    /*
    public static void generateRoom(StructureWorldAccess world, BlockPos center) {
        if (Math.random() < 0.5 || center.getY() == 1) {
            BlockPos door = center.mutableCopy();
            if (Math.random() <= 0.25) door = door.east(R-1);
            else if (Math.random() <= 0.25) door = door.west(R-1);
            else if (Math.random() <= 0.25) door = door.north(R-1);
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
    }*/
}
