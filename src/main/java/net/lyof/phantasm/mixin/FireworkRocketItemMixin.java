package net.lyof.phantasm.mixin;

import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.data.server.advancement.AdvancementProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
public class FireworkRocketItemMixin {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"))
    public boolean cancelBoost(PlayerEntity instance) {
        if (instance instanceof ServerPlayerEntity player) {
            Advancement freetheend = player.getServer().getAdvancementLoader().get(new Identifier(ConfigEntries.elytraBoostAdvancement));
            if (player.getAdvancementTracker().getProgress(freetheend).isDone() && player.isFallFlying()) {
                instance.swingHand(instance.getActiveHand(), true);
                return true;
            }
        }
        return false;
    }
}
