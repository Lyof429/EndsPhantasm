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
import org.spongepowered.asm.mixin.Unique;
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

    @Unique private static final int oblivionRepair = 200;
    @Unique private static final int acidicRepair = 30;

    @Inject(method = "updateResult", at = @At(value = "HEAD"), cancellable = true)
    public void customAnvilRecipes(CallbackInfo ci) {
        ItemStack input = this.input.getStack(0);
        ItemStack add = this.input.getStack(1);

        if (!input.isEmpty() && input.getDamage() > 0 && add.isOf(ModBlocks.OBLIVION.asItem())) {
            int dura = Math.min((input.getDamage() + oblivionRepair - 1) / oblivionRepair, add.getCount());
            ItemStack output = input.copy();
            output.setDamage(Math.min(0, output.getDamage() - oblivionRepair*dura));

            this.repairItemUsage = dura;

            if (this.newItemName != null && !Util.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(input.getName().getString())) {
                    dura += 1;
                    output.setCustomName(Text.literal(this.newItemName));
                }
            } else if (input.hasCustomName()) {
                dura += 1;
                output.removeCustomName();
            }

            this.levelCost.set(dura);

            this.output.setStack(0, output);
            ci.cancel();
        }
    }
}
