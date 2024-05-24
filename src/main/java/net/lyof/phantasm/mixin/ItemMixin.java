package net.lyof.phantasm.mixin;

import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void showCrystalBonus(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (stack.isIn(ModTags.Items.XP_BOOSTED))
            tooltip.add(Text.translatable("tooltip.xp_boosted").formatted(Formatting.GREEN));
    }

    @Inject(method = "canRepair", at = @At("RETURN"), cancellable = true)
    public void universalRepair(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || ingredient.isOf(ModBlocks.OBLIVION.asItem()));
    }
}
