package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/EnderPearlEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void removeRandom(EnderPearlEntity instance, Entity entity, float pitch, float yaw, float roll, float speed, float spread,
                              Operation<Void> original) {

        original.call(instance, entity, pitch, yaw, roll, speed, 0f);
    }
}
