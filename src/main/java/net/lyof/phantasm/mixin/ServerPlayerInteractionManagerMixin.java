package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void putPolyppieDown(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult,
                                 CallbackInfoReturnable<ActionResult> cir) {

        if (player instanceof PolyppieCarrier carrier && carrier.phantasm_getPolyppie() != null && stack.isEmpty()
                && hitResult.getSide() == Direction.UP && !hitResult.isInsideBlock() && player.isSneaking()) {

            BlockPos pos = hitResult.getBlockPos().up();

            Box boundingBox = ModEntities.POLYPPIE.createSimpleBoundingBox(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
            List<Box> occupiedSpace = new ArrayList<>();
            for (BlockPos p : BlockPos.iterate(pos.add(-1, 0, -1), pos.add(1, 1, 1))) {
                if (!world.getBlockState(p).getCollisionShape(world, p).isEmpty())
                    occupiedSpace.add(new Box(p));
            }

            for (Box box : occupiedSpace) {
                if (boundingBox.intersects(box))
                    return;
            }

            carrier.phantasm_getPolyppie().setCarriedBy(player, hitResult.getPos());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
