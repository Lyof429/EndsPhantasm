package net.lyof.phantasm.screen.access;

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
    public boolean isSelected() {
        return super.isSelected() || this.toggled.get();
    }
}
