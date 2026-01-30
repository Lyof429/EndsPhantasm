package net.lyof.phantasm.mixin.client;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin implements MixinAccess<Boolean> {
    @Shadow private List<OrderedText> credits;
    @Shadow private IntSet centeredLines;
    @Shadow private int creditsHeight;

    @Shadow protected abstract void load(String id, CreditsScreen.CreditsReader reader);
    @Shadow protected abstract void readPoem(Reader reader) throws IOException;

    @Unique protected final Identifier BACKGROUND_TEXTURE = Phantasm.makeID("textures/gui/credits_background.png");

    @Unique private boolean beginningCredits = false;

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void initBeginning(CallbackInfo ci) {
        if (this.beginningCredits && this.credits == null) {
            this.credits = Lists.newArrayList();
            this.centeredLines = new IntOpenHashSet();
            this.load("phantasm:texts/begin.txt", this::readPoem);

            this.creditsHeight = this.credits.size() * 12 - 200;
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/gui/DrawContext;IFI)V"))
    private boolean cancelLogoDraw(LogoDrawer instance, DrawContext context, int screenWidth, float alpha, int y) {
        return !this.beginningCredits;
    }

    @WrapOperation(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIFFIIII)V"))
    private void moveBackground(DrawContext instance, Identifier texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
        if (this.beginningCredits) original.call(instance, BACKGROUND_TEXTURE, x, y, z, u, v, width, height, textureWidth, textureHeight);
        original.call(instance, texture, x, y, z, u, v, width, height, textureWidth, textureHeight);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "CONSTANT", args = "intValue=100"))
    private int removeLogoSpace(int constant) {
        if (this.beginningCredits) return 0;
        return constant;
    }

    @Override
    public void setMixinValue(Boolean value) {
        this.beginningCredits = value;
    }

    @Override
    public Boolean getMixinValue() {
        return this.beginningCredits;
    }
}
