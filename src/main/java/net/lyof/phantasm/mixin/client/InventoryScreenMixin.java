package net.lyof.phantasm.mixin.client;

import com.vinurl.VinURL;
import com.vinurl.util.Constants;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.setup.compat.VinURLCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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

    @Unique private static final Identifier INVENTORY_TEXTURE = Phantasm.makeID("textures/gui/polyppie_inventory.png");
    @Unique private static final Identifier PROGRESS_BAR_TEXTURE = Phantasm.makeID("textures/gui/polyppie_progress_bar.png");


    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawPolyppieInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.handler instanceof PolyppieCarrier.ScreenHandler self && self.isPolyppieInventoryEnabled()) {
            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;

            boolean open = self.isPolyppieInventoryOpen();

            int anchor = MathHelper.clamp(ConfigEntries.polyppieSlotAnchor, 0, 3);
            int x, y;
            switch (anchor) {
                case 0 -> {
                    x = -32;
                    y = ConfigEntries.polyppieSlotOffset;
                }
                case 1 -> {
                    x = ConfigEntries.polyppieSlotOffset;
                    y = -32;
                }
                case 2 -> {
                    x = this.backgroundWidth;
                    y = ConfigEntries.polyppieSlotOffset;
                }
                default -> {
                    x = ConfigEntries.polyppieSlotOffset;
                    y = this.backgroundHeight;
                }
            }

            context.drawTexture(INVENTORY_TEXTURE, i + x, j + y,
                    this.isHoveringButton(open, mouseX, mouseY) ? 128 : 0,
                    64 * anchor + (open ? 0 : 32),
                    96, 32);

            if (self.isPolyppieInventoryOpen() && anchor % 2 == 1 && MinecraftClient.getInstance().player instanceof PolyppieCarrier carrier) {
                context.drawText(this.textRenderer, carrier.getCarriedPolyppie().getName(),
                        i + x + 30, j + y + 13, 0x373737, false);

                context.drawTexture(PROGRESS_BAR_TEXTURE, i + x + 32, j + y + 25, 0, 0,
                        32, 4,
                        32, 16);
                context.drawTexture(PROGRESS_BAR_TEXTURE, i + x + 32, j + y + 25, 0, 8,
                        (int) (carrier.getCarriedPolyppie().getSongProgress() * 32), 4,
                        32, 16);
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void openPolyppieInventory(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.handler instanceof PolyppieCarrier.ScreenHandler self) {
            if (this.isHoveringButton(self.isPolyppieInventoryOpen(), mouseX, mouseY)) {
                self.openPolyppieInventory();
                cir.setReturnValue(true);
            }
        }
    }

    @Unique private boolean isHoveringButton(boolean open, double mouseX, double mouseY) {
        int anchor = MathHelper.clamp(ConfigEntries.polyppieSlotAnchor, 0, 3);
        int x, y, width, height;
        switch (anchor) {
            case 0 -> {
                x = open ? -32 : -32 + 25;
                y = ConfigEntries.polyppieSlotOffset;
                width = 7;
                height = 24;
            } case 1 -> {
                x = ConfigEntries.polyppieSlotOffset;
                y = open ? -32 : -32 + 25;
                width = 24;
                height = 7;
            } case 2 -> {
                x = open ? this.backgroundWidth + 25 : this.backgroundWidth;
                y = ConfigEntries.polyppieSlotOffset;
                width = 7;
                height = 24;
            } default -> {
                x = ConfigEntries.polyppieSlotOffset;
                y = open ? this.backgroundHeight + 25 : this.backgroundHeight;
                width = 24;
                height = 7;
            }
        }
        return this.isPointWithinBounds(x, y, width, height, mouseX, mouseY);
    }
}
