package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Unique
    private static final Identifier INVENTORY_TEXTURE = Phantasm.makeID("textures/gui/polyppie_inventory.png");

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawPolyppieInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.handler instanceof PolyppieCarrier.ScreenHandler self && self.isPolyppieInventoryEnabled()) {
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;

            boolean open = self.isPolyppieInventoryOpen();
            context.drawTexture(INVENTORY_TEXTURE, i - 32, j + 76,
                    this.isPointWithinBounds(open ? -32 : -7, 76, 7, 23, mouseX, mouseY) ? 32 : 0,
                    open ? 0 : 32,
                    32, 32);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void openPolyppieInventory(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.handler instanceof PolyppieCarrier.ScreenHandler self
                && this.isPointWithinBounds(self.isPolyppieInventoryOpen() ? -32 : -7, 76, 7, 23, mouseX, mouseY)) {
            self.openPolyppieInventory();
            cir.setReturnValue(true);
        }
    }
}
