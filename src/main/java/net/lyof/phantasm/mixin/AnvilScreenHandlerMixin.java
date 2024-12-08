package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Shadow @Nullable private String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "HEAD"), cancellable = true)
    public void oblivionRepair(CallbackInfo ci) {
        ItemStack stack = this.input.getStack(0);
        if (!stack.isEmpty() && stack.getDamage() > 0 && this.input.getStack(1).isOf(ModBlocks.OBLIVION.asItem())) {
            int dura = Math.min((stack.getDamage() + 199) / 200, this.input.getStack(1).getCount());
            ItemStack output = stack.copy();
            output.setDamage(output.getDamage() - 200*dura);

            this.repairItemUsage = dura;

            if (this.newItemName != null && !Util.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(stack.getName().getString())) {
                    dura += 1;
                    output.setCustomName(Text.literal(this.newItemName));
                }
            } else if (stack.hasCustomName()) {
                dura += 1;
                output.removeCustomName();
            }

            this.levelCost.set(dura);

            this.output.setStack(0, output);
            ci.cancel();
        }
    }
}
