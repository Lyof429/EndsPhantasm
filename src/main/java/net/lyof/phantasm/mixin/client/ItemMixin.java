package net.lyof.phantasm.mixin.client;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void showCrystalBonus(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (stack.isIn(ModTags.Items.XP_BOOSTED) && MinecraftClient.getInstance().player != null) {
            tooltip.add(Text.translatable("tooltip.xp_boosted").formatted(Formatting.GREEN));
            String bonus = Math.round((float) ConfigEntries.crystalXPBoost * MinecraftClient.getInstance().player.experienceLevel) + "%";
            tooltip.add(Text.translatable("tooltip.xp_boosted.value", bonus).formatted(Formatting.GREEN));
        }
    }
}
