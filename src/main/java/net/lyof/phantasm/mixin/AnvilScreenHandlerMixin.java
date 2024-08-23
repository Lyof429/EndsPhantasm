package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;canRepair(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    public boolean universalRepair(Item instance, ItemStack stack, ItemStack ingredient) {
        if (ingredient.isOf(ModBlocks.OBLIVION.asItem())) return true;
        return instance.canRepair(stack, ingredient);
    }
}
