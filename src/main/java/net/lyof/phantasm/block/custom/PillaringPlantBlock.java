package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class PillaringPlantBlock extends PlantBlock implements Fertilizable {
    public TagKey<Block> growableOn;
    public VoxelShape shape;

    public PillaringPlantBlock(Settings settings, TagKey<Block> growableOn, VoxelShape shape) {
        super(settings);
        this.growableOn = growableOn;
        this.shape = shape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shape;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(this.growableOn);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextInt(3) == 0 && this.isFertilizable(world, pos, state, world.isClient());
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.isOf(ModBlocks.DRALGAE) && random.nextInt(10) == 0)
            world.setBlockState(pos.up(), ModBlocks.POME.getDefaultState());
        else
            world.setBlockState(pos.up(), this.getDefaultState());
    }
}
