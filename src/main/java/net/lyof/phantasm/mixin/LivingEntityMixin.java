package net.lyof.phantasm.mixin;

import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void oblifruitKeep(ItemStack instance, int amount) {
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.05 && instance.getCount() < instance.getMaxCount()) {
            instance.increment(1);
            return;
        }
        if (instance.isOf(ModItems.OBLIFRUIT) && Math.random() < 0.4) return;
        instance.decrement(1);
    }
}
