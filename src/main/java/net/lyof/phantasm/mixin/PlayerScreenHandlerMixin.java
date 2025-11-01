package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
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

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements PolyppieCarrier.ScreenHandler {
    @Shadow @Final private PlayerEntity owner;

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique private Inventory polyppieInventory = null;
    @Unique private Slot polyppieSlot = null;
    @Unique private boolean isPolyppieInventoryOpen = true;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initPolyppieScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (this.owner instanceof PolyppieCarrier carrier) {
            this.polyppieInventory = new PolyppieCarrier.Inventory(carrier);

            this.polyppieSlot = this.addSlot(new Slot(this.polyppieInventory, this.slots.size(), -19, 84) {
                @Override
                public void onQuickTransfer(ItemStack newItem, ItemStack original) {
                    super.onQuickTransfer(newItem, original);
                    this.inventory.markDirty();
                }

                @Override
                public boolean isEnabled() {
                    return PlayerScreenHandlerMixin.this.isPolyppieInventoryEnabled()
                            && PlayerScreenHandlerMixin.this.isPolyppieInventoryOpen;
                }
            });
        }
    }

    @Override
    public void openPolyppieInventory() {
        this.isPolyppieInventoryOpen = !this.isPolyppieInventoryOpen;
    }

    @Override
    public boolean isPolyppieInventoryOpen() {
        return this.isPolyppieInventoryOpen;
    }

    @Override
    public boolean isPolyppieInventoryEnabled() {
        return PlayerScreenHandlerMixin.this.owner instanceof PolyppieCarrier carrier
                && carrier.getCarriedPolyppie() != null;
    }
}
