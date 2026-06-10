package net.lyof.phantasm.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.emi.trinkets.mixin.accessor.ScreenHandlerAccessor;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.screen.access.PlayerScreenHandlerHelper;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = PlayerScreenHandler.class, priority = 1500)
public abstract class PlayerScreenHandlerMixinSquared extends ScreenHandler {
    @Shadow @Final private PlayerEntity owner;

    protected PlayerScreenHandlerMixinSquared(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @TargetHandler(
            mixin = "dev.emi.trinkets.mixin.PlayerScreenHandlerMixin",
            name = "trinkets$updateTrinketSlots"
    )
    @WrapMethod(method = "@MixinSquared:Handler")
    private void initTrinketsPolyppieSlot(boolean slotsChanged, Operation<Void> original) {
        if (this instanceof PolyppieInventory.Handler handler) {
            Phantasm.log("Fired MixinSquared");
            Slot slot = handler.phantasm_getSlot();
            if (slot != null)
                phantasm_removeSlot(this, slot.id);

            original.call(slotsChanged);

            handler.phantasm_setSlot(PlayerScreenHandlerHelper.make(this.owner, this::addSlot, handler::phantasm_isVisible));
        }
    }

    @Unique
    private static void phantasm_removeSlot(ScreenHandler handler, int id) {
        Phantasm.log("Removing slot " + id);
        handler.slots.remove(id);
        ((ScreenHandlerAccessor) handler).getTrackedStacks().remove(id);
        ((ScreenHandlerAccessor) handler).getPreviousTrackedStacks().remove(id);
    }
}
