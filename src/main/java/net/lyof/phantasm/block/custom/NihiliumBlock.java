package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.world.feature.ModPlacedFeatures;
import net.minecraft.block.*;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

import java.util.List;
import java.util.Optional;

public class NihiliumBlock extends Block implements Fertilizable {
    public NihiliumBlock(Settings settings) {
        super(settings);
    }


    public static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos up = pos.up();
        BlockState state_up = world.getBlockState(up);

        if (state_up.getFluidState().getLevel() == 8)
            return false;

        int i = ChunkLightProvider
                .getRealisticOpacity(world, state, pos, state_up, up, Direction.UP, state_up.getOpacity(world, up));
        return i < world.getMaxLightLevel();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos))
            world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
    }


    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos target;
        BlockState block;

        for (int i = 0; i < 6; i++) {
            target = pos.mutableCopy().add(random.nextBetween(-1, 1), random.nextBetween(-1, 1), random.nextBetween(-1, 1));

            if (canSurvive(world.getBlockState(target), world, target) && world.getBlockState(target).getBlock() == Blocks.END_STONE) {
                world.setBlockState(target, this.getDefaultState());

                block = world.getBlockState(target);
                if (random.nextInt(3) == 0)
                    ((Fertilizable) block.getBlock()).grow(world, random, target, block);
            }
        }

        // TODO: Add plants

        Optional<RegistryEntry.Reference<PlacedFeature>> optional = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE)
                .getEntry(ModPlacedFeatures.VIVID_NIHILIUM_PATCH);
        RegistryEntry<PlacedFeature> registryEntry;
        BlockPos test = pos.up();

        if (!world.getBlockState(test).isAir())
            return;
        if (world.isClient())
            return;

        if (random.nextInt(2) == 0) {
            List<ConfiguredFeature<?, ?>> list = world.getBiome(test).value().getGenerationSettings().getFlowerFeatures();
            if (list.isEmpty()) return;
            registryEntry = ((RandomPatchFeatureConfig) list.get(random.nextInt(list.size())).config()).feature();
        } else {
            if (optional.isEmpty()) return;
            registryEntry = optional.get();
        }

        registryEntry.value().generate(world, world.getChunkManager().getChunkGenerator(), random, test);
    }
}
