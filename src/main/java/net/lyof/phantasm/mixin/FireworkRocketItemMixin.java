package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"))
    public boolean cancelBoost(PlayerEntity instance, Operation<Boolean> original) {
        if (instance instanceof ServerPlayerEntity player) {
            Advancement freetheend = player.getServer().getAdvancementLoader().get(new Identifier(ConfigEntries.elytraBoostAdvancement));
            if ((freetheend == null || player.getAdvancementTracker().getProgress(freetheend).isDone()) && original.call(instance)) {
                instance.swingHand(instance.getActiveHand(), true);
                return true;
            }
            else if (freetheend != null && player.isFallFlying()) {
                player.sendMessage(Text.translatable("item.minecraft.firework_rocket.cannot_use", freetheend.toHoverableText()),
                        true);
            }
        }
        return false;
    }
}
