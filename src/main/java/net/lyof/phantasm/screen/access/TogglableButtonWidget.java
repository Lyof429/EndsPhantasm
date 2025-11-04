package net.lyof.phantasm.screen.access;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class TogglableButtonWidget extends TexturedButtonWidget {
    public TogglableButtonWidget(int x, int y, int width, int height, int u, int v, Identifier texture, Supplier<Boolean> toggle, PressAction pressAction) {
        super(x, y, width, height, u, v, texture, pressAction);
        this.toggled = toggle;
    }

    protected Supplier<Boolean> toggled;

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.setFocused(false);
        this.drawTexture(context, this.texture, this.getX(), this.getY(), this.toggled.get() ? this.u + this.width : this.u, this.v,
                this.hoveredVOffset, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
