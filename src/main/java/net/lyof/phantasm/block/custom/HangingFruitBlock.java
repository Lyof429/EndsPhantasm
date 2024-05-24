package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class HangingFruitBlock extends HangingPlantBlock implements Fertilizable {
    public ItemConvertible drop;

    public static final BooleanProperty HAS_FRUIT = BooleanProperty.of("has_fruit");

    public HangingFruitBlock(Settings settings, ItemConvertible drop, TagKey<Block> growable_on, VoxelShape shape) {
        super(settings.ticksRandomly(), growable_on, shape);
        this.drop = drop;
        this.setDefaultState(this.getDefaultState()
                .with(HAS_FRUIT, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_FRUIT);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(HAS_FRUIT)) return;

        if (random.nextDouble() < 0.05 && this.canGrow(world, random, pos, state))
            this.grow(world, random, pos, state);
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.get(HAS_FRUIT))
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.drop)));
        super.onStacksDropped(state, world, pos, tool, dropExperience);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(HAS_FRUIT)) {
            world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(this.drop)));
            world.setBlockState(pos, state.with(HAS_FRUIT, false));

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(HAS_FRUIT);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(HAS_FRUIT);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0)
            world.setBlockState(pos, state.with(HAS_FRUIT, true));

        if (state.isOf(ModBlocks.OBLIVINE) && random.nextInt(4) == 0 && world.getBlockState(pos.down()).isAir())
            world.setBlockState(pos.down(), this.getDefaultState());
        else if (state.isOf(ModBlocks.OBLIVINE) && random.nextInt(5) == 0 && world.getBlockState(pos.down()).isAir())
            world.setBlockState(pos.down(), ModBlocks.CRYSTALILY.getDefaultState());
    }
}
