package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class HangingFruitBlock extends HangingPlantBlock {
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

        if (random.nextDouble() < 0.05)
            world.setBlockState(pos, state.with(HAS_FRUIT, true));
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.get(HAS_FRUIT))
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.drop)));
        super.onStacksDropped(state, world, pos, tool, dropExperience);
    }
}
