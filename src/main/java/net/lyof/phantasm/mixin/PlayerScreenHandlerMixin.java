package net.lyof.phantasm.mixin;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.access.PlayerScreenHandlerHelper;
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

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements PolyppieInventory.Handler {
    @Shadow @Final private PlayerEntity owner;

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique private Slot phantasm_slot = null;
    @Unique private boolean phantasm_visible = true;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 5, shift = At.Shift.AFTER))
    private void initPolyppieScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (!Phantasm.isTrinketsLoaded())
            this.phantasm_slot = PlayerScreenHandlerHelper.make(owner, this::addSlot, this::phantasm_isVisible);
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
        return this.owner instanceof PolyppieCarrier carrier && carrier.phantasm_getPolyppie() != null;
    }

    @Override
    public Slot phantasm_getSlot() {
        return this.phantasm_slot;
    }

    @Override
    public void phantasm_setSlot(Slot slot) {
        this.phantasm_slot = slot;
    }
}
