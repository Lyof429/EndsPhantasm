package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.screen.DiscVisuals;
import net.lyof.phantasm.screen.access.PolyppieInventory;
import net.lyof.phantasm.screen.custom.TogglableButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    @Shadow protected abstract void init();
    @Shadow public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Unique private static final Identifier INVENTORY_TEXTURE = Phantasm.makeID("textures/gui/polyppie_inventory.png");

    @Unique private ButtonWidget phantasm_stop;
    @Unique private ButtonWidget phantasm_play;
    @Unique private ButtonWidget phantasm_hide;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractInventoryScreen;init()V"))
    private void initHeight(CallbackInfo ci) {
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isVisible())
            this.backgroundHeight = 166 - 5 + 27;
        else this.backgroundHeight = 166;
    }

    @WrapOperation(method = "init", at = @At(value = "NEW", target = "(IIIIIIILnet/minecraft/util/Identifier;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/TexturedButtonWidget;"))
    private TexturedButtonWidget moveButtons(int x, int y, int width, int height, int u, int v, int hoveredVOffset,
                                             Identifier texture, ButtonWidget.PressAction pressAction, Operation<TexturedButtonWidget> original) {
        return original.call(x, y, width, height, u, v, hoveredVOffset, texture, (ButtonWidget.PressAction) button -> {
            pressAction.onPress(button);
            this.phantasm_play.setX(this.x + 145);
            this.phantasm_stop.setX(this.x + 157);
            this.phantasm_hide.setX(this.x + 161);
        });
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()
                && player instanceof PolyppieCarrier carrier) {

            this.addDrawableChild(this.phantasm_stop = new TogglableButtonWidget(this.x + 145, this.y + 180 + (self.phantasm_isVisible() ? 0 : -11), 12, 12,
                    0, 32, INVENTORY_TEXTURE, () -> carrier.phantasm_getPolyppie().isPaused(),
                    button -> PolyppieInventory.Handler.onButtonClick(player, 0)));
            this.addDrawableChild(this.phantasm_play = new TexturedButtonWidget(this.x + 157, this.y + 180 + (self.phantasm_isVisible() ? 0 : -11), 12, 12,
                    24, 32, INVENTORY_TEXTURE,
                    button -> PolyppieInventory.Handler.onButtonClick(player, 1)));

            this.addDrawableChild(this.phantasm_hide = new TogglableButtonWidget(this.x + 161, this.y + 172 + (self.phantasm_isVisible() ? 0 : -11), 8, 8,
                    0, 56, INVENTORY_TEXTURE, () -> !self.phantasm_isVisible(), button -> {
                        self.phantasm_toggleVisibility();

                        this.backgroundHeight = self.phantasm_isVisible() ? 166 - 5 + 27 : 166;
                        this.y = (this.height - this.backgroundHeight) / 2;
                        this.phantasm_play.active = self.phantasm_isVisible();
                        this.phantasm_stop.active = self.phantasm_isVisible();
            }));

            this.phantasm_play.active = self.phantasm_isVisible();
            this.phantasm_stop.active = self.phantasm_isVisible();
        }
    }

    @Definition(id = "backgroundHeight", field = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;backgroundHeight:I")
    @Expression("this.backgroundHeight")
    @ModifyExpressionValue(method = "drawBackground", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int fixRenderHeight(int original) {
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isVisible())
            return original + 5 - 27;
        return original;
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawPolyppieInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.handler instanceof PolyppieInventory.Handler self && self.phantasm_isEnabled()) {
            int x = 0, y = 166 - 5;

            if (self.phantasm_isVisible() && MinecraftClient.getInstance().player instanceof PolyppieCarrier carrier) {
                context.drawTexture(INVENTORY_TEXTURE, this.x + x, this.y + y,
                        0, 0, 176, 27);

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
