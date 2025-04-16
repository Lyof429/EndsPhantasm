package net.lyof.phantasm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Unique private static final Identifier VANILLA_ICONS = new Identifier("textures/gui/icons.png");
    @Unique private static final Identifier CORROSION_ARMOR = Phantasm.makeID("textures/gui/corrosion_armor.png");

    @WrapOperation(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    public void renderCorrodedArmor(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (v == 9 && (u == 16 || u == 25 || u == 34) && texture.equals(VANILLA_ICONS)
                && this.getCameraPlayer().hasStatusEffect(ModEffects.CORROSION)) {
            int offset = (int) Math.round(Math.pow(Math.sin((x + this.getCameraPlayer().getWorld().getTimeOfDay()) / 2f), 2));
            instance.drawTexture(CORROSION_ARMOR, x, y + offset, 0, 0, 9, 9, 9, 9);
        }
        else
            original.call(instance, texture, x, y, u, v, width, height);
    }
}
