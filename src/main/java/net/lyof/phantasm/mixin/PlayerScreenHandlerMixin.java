package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerScreenHandler.class, priority = 0)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements PolyppieInventory.Handler {
    @Shadow @Final private PlayerEntity owner;

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique private Slot phantasm_slot = null;
    @Unique private boolean phantasm_visible = true;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 5, shift = At.Shift.AFTER))
    private void initPolyppieScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (owner instanceof PolyppieCarrier carrier) {
            int x = 8, y = 166 - 10 + 8;

            this.phantasm_slot = this.addSlot(new Slot(new PolyppieInventory(carrier), 0, x, y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return this.inventory.isValid(this.id, stack);
                }

                @Override
                public boolean isEnabled() {
                    return PlayerScreenHandlerMixin.this.phantasm_isEnabled()
                            && PlayerScreenHandlerMixin.this.phantasm_visible;
                }
            });
        }
    }

    @Inject(method = "quickMove", at = @At("HEAD"), cancellable = true)
    public void quickPolyppieMove(PlayerEntity player, int slotid, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.getSlot(slotid);
        ItemStack stack = slot.getStack();

        if (slot == this.phantasm_slot && this.insertItem(stack, 6, 42, true)) {
            cir.setReturnValue(ItemStack.EMPTY);
            this.phantasm_slot.markDirty();
        } else if (this.phantasm_slot.insertStack(stack).isEmpty()) {
            cir.setReturnValue(ItemStack.EMPTY);
            this.phantasm_slot.markDirty();
        }
    }

    @Override
    public void phantasm_toggleVisibility() {
        this.phantasm_visible = !this.phantasm_visible;
    }

    @Override
    public boolean phantasm_isVisible() {
        return this.phantasm_visible && this.phantasm_isEnabled();
    }

    @Override
    public boolean phantasm_isEnabled() {
        return PlayerScreenHandlerMixin.this.owner instanceof PolyppieCarrier carrier
                && carrier.phantasm_getPolyppie() != null;
    }

    @Override
    public Slot phantasm_getSlot() {
        return this.phantasm_slot;
    }
}
