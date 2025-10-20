package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void putPolyppieDown(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult,
                                 CallbackInfoReturnable<ActionResult> cir) {

        PolyppieEntity polyppie;
        if (player instanceof PolyppieCarrier carrier && (polyppie = carrier.getCarriedPolyppie()) != null && stack.isEmpty()
                && hitResult.getSide() == Direction.UP && !hitResult.isInsideBlock() && player.isSneaking()) {

             Phantasm.log("Unpacking Polyppie");
             polyppie.setCarriedBy(player, hitResult.getPos());
             cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
