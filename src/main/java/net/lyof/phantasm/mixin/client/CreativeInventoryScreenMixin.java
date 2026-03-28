package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @WrapWithCondition(method = "setSelectedTab", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z"))
    private <E> boolean hidePolyppieSlot(DefaultedList<E> instance, E o) {
        return o instanceof Slot slot && !(slot.inventory instanceof PolyppieInventory);
    }
}
