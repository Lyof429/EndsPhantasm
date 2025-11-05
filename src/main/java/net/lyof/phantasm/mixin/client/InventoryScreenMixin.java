package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.DiscVisuals;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.lyof.phantasm.screen.access.TogglableButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNGuiHints;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Unique private static final Identifier INVENTORY_TEXTURE = Phantasm.makeID("textures/gui/polyppie_inventory.png");

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractInventoryScreen;init()V"))
    private void initHeight(CallbackInfo ci) {
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled())
            this.backgroundHeight = 166 - 5 + 27;
        else
            this.backgroundHeight = 166;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()
                && player instanceof PolyppieCarrier carrier) {
            int x = 0;
            int y = 166 - 5;

            this.addDrawableChild(new TogglableButtonWidget(this.x + x + 145, this.y + y + 8 + 11, 12, 12,
                    0, 32, INVENTORY_TEXTURE, () -> carrier.phantasm_getPolyppie().isPaused(),
                    (button) -> PolyppieInventory.Handler.onButtonClick(player, 0)));
            this.addDrawableChild(new TexturedButtonWidget(this.x + x + 145 + 12, this.y + y + 8 + 11, 12, 12,
                    24, 32, INVENTORY_TEXTURE,
                    (button) -> PolyppieInventory.Handler.onButtonClick(player, 1)));
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawPolyppieInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()) {
            int x = 0, y = 166 - 5;
            context.drawTexture(INVENTORY_TEXTURE, this.x + x, this.y + y,
                    0, 0, 176, 27);

            if (self.phantasm_isOpen() && MinecraftClient.getInstance().player instanceof PolyppieCarrier carrier) {
                DiscVisuals visuals = DiscVisuals.get(carrier.phantasm_getPolyppie().getStack());
                x = self.phantasm_getSlotX() - 8;
                y = self.phantasm_getSlotY() - 8;

                context.drawText(this.textRenderer, carrier.phantasm_getPolyppie().getName(),
                        this.x + x + 32, this.y + y + 8, 0x373737, false);

                context.drawTexture(visuals.notes, this.x + x, this.y + y,
                        0, 0, 32, 32, 32, 32);

                context.drawTexture(visuals.progressBar, this.x + x + 32, this.y + y + 17, 0, 0,
                        96, 7,
                        128, 16);
                context.drawTexture(visuals.progressBar, this.x + x + 32, this.y + y + 17, 0, 8,
                        (int) (carrier.phantasm_getPolyppie().getSongProgress() * 96), 7,
                        128, 16);
            }
        }
    }
}
