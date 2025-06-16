package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    public boolean isObsidian(BlockState instance, Block block, Operation<Boolean> original) {
        if (block == Blocks.OBSIDIAN)
            return original.call(instance, block) || instance.isIn(ModTags.Blocks.END_CRYSTAL_PLACEABLE_ON);
        return original.call(instance, block);
    }

    @Inject(method = "useOnBlock", at = @At("TAIL"))
    public void startChallenge(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!cir.getReturnValue().isAccepted()) return;

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity user = context.getPlayer();

        if (user != null && world.getBlockEntity(pos) instanceof ChallengeRuneBlockEntity challengeRune) {
            if (challengeRune.canStart(user))
                challengeRune.startChallenge(user);
        }
    }
}
