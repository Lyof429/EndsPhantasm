package net.lyof.phantasm.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class DragonMintBlock extends TallPlantBlock implements Fertilizable {
    public DragonMintBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(HangingFruitBlock.HAS_FRUIT, false)
        );
    }

    public static BlockState getOtherHalf(WorldView world, BlockPos pos, BlockState state) {
        return state.get(TallPlantBlock.HALF) == DoubleBlockHalf.LOWER ?
                world.getBlockState(pos.up()) : world.getBlockState(pos.down());
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(HangingFruitBlock.HAS_FRUIT) && !getOtherHalf(world, pos, state).get(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(HangingFruitBlock.HAS_FRUIT) && !getOtherHalf(world, pos, state).get(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (random.nextInt(3) == 0)
            world.setBlockState(pos, state.with(HangingFruitBlock.HAS_FRUIT, true));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HangingFruitBlock.HAS_FRUIT);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(HangingFruitBlock.HAS_FRUIT)) return;

        if (random.nextDouble() < 0.02 && this.canGrow(world, random, pos, state))
            this.grow(world, random, pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.GLASS_BOTTLE) && state.get(HangingFruitBlock.HAS_FRUIT)) {
            world.setBlockState(pos, state.with(HangingFruitBlock.HAS_FRUIT, false));

            if (!player.isCreative()) stack.decrement(1);
            player.getInventory().offerOrDrop(Items.DRAGON_BREATH.getDefaultStack());

            player.incrementStat(Stats.USED.getOrCreateStat(Items.GLASS_BOTTLE));
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
