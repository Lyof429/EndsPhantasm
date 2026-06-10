package net.lyof.phantasm.screen.access;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlayerScreenHandlerHelper {
    public static Slot make(PlayerEntity owner, UnaryOperator<Slot> addSlot, Supplier<Boolean> isEnabled) {
        if (owner instanceof PolyppieCarrier carrier) {
            int x = 8, y = 166 - 10 + 8;

            return addSlot.apply(new Slot(new PolyppieInventory(carrier), 0, x, y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return this.inventory.isValid(this.id, stack);
                }

                @Override
                public boolean isEnabled() {
                    return isEnabled.get();
                }
            });
        }
        throw new IllegalStateException("""
            Tried to assign a PolyppieInventory to a Player that is not a subclass of PolyppieCarrier
            This should not be possible and likely means that somebody tempered with Phantasm's mixins"""
        );
    }
}
