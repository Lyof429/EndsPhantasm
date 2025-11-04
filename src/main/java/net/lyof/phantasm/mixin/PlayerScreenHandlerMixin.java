package net.lyof.phantasm.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.lyof.phantasm.setup.ModPackets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
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
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements PolyppieInventory.Handler {
    @Shadow @Final private PlayerEntity owner;

    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique private Inventory polyppieInventory = null;
    @Unique private Slot phantasm_slot = null;
    @Unique private boolean phantasm_visible = true;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initPolyppieScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        if (this.owner instanceof PolyppieCarrier carrier) {
            this.polyppieInventory = new PolyppieInventory(carrier);

            int x = 8, y = 166 - 10 + 8;

            this.phantasm_slot = this.addSlot(new Slot(this.polyppieInventory, this.slots.size(), x, y) {
                @Override
                public void onQuickTransfer(ItemStack newItem, ItemStack original) {
                    super.onQuickTransfer(newItem, original);
                    this.inventory.markDirty();
                }

                @Override
                public boolean isEnabled() {
                    return PlayerScreenHandlerMixin.this.phantasm_isEnabled()
                            && PlayerScreenHandlerMixin.this.phantasm_visible;
                }
            });
        }
    }

    @Override
    public void phantasm_toggleVisibility() {
        this.phantasm_visible = !this.phantasm_visible;
    }

    @Override
    public boolean phantasm_isOpen() {
        return this.phantasm_visible;
    }

    @Override
    public boolean phantasm_isEnabled() {
        return PlayerScreenHandlerMixin.this.owner instanceof PolyppieCarrier carrier
                && carrier.phantasm_getPolyppie() != null;
    }

    @Override
    public int phantasm_getSlotX() {
        return this.phantasm_slot.x;
    }

    @Override
    public int phantasm_getSlotY() {
        return this.phantasm_slot.y;
    }
}
