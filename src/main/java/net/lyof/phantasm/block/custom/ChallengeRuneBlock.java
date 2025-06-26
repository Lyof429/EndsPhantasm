package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class ChallengeRuneBlock extends BlockWithEntity {
    public static final BooleanProperty COMPLETED = BooleanProperty.of("completed");

    public ChallengeRuneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(COMPLETED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(COMPLETED);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChallengeRuneBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getStackInHand(hand).isOf(Items.END_CRYSTAL)) {
            if (!world.isClient()) {
                if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune && challengeRune.hasCompleted(player))
                    player.sendMessage(Text.translatable("block.phantasm.challenge_rune.hint")
                            .formatted(Formatting.LIGHT_PURPLE), true);
                else
                    player.sendMessage(Text.translatable("block.phantasm.challenge_rune.hint.crystal" +
                            player.getRandom().nextInt(5)).formatted(Formatting.LIGHT_PURPLE), true);
            }
            return ActionResult.success(world.isClient());
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune && challengeRune.isChallengeRunning())
            challengeRune.stopChallenge(false);
        super.onBroken(world, pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune && challengeRune.isChallengeRunning())
            challengeRune.stopChallenge(false);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.CHALLENGE_RUNE, ChallengeRuneBlockEntity::tick);
    }
}
