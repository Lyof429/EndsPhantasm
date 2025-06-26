package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean isObsidian(BlockState instance, Block block, Operation<Boolean> original, ItemUsageContext context) {
        if (block == Blocks.OBSIDIAN) {
            if (original.call(instance, block) || instance.isIn(ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON))
                return true;
            if (instance.isOf(ModBlocks.CHALLENGE_RUNE))
                    return context.getPlayer() instanceof ServerPlayerEntity serverPlayer
                            && context.getWorld().getBlockEntity(context.getBlockPos()) instanceof ChallengeRuneBlockEntity rune
                            && rune.canStart(serverPlayer);
        }
        return original.call(instance, block);
    }

    @WrapMethod(method = "useOnBlock")
    public ActionResult startChallenge(ItemUsageContext context, Operation<ActionResult> original) {
        ActionResult result = original.call(context);

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity user = context.getPlayer();

        if (!(user instanceof ServerPlayerEntity serverPlayer)) return result;

        if (world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity rune) {
            if (result.isAccepted())
                rune.startChallenge(user);
            else
                rune.displayHint(serverPlayer);
            user.swingHand(context.getHand(), true);
        }
        return result;
    }
}
