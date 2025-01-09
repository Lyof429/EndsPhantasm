package net.lyof.phantasm.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.screenhandler.Networking;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.custom.SubwooferBlock;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Redirect(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static boolean shootSonicWave(World instance, Entity entity, World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (entity instanceof ChoralArrowEntity arrow) {
            Vec3d direction = arrow.getVelocity().normalize();
            Vec3d position = entity.getPos();
            int range = ConfigEntries.subwooferRange * 2;

            if (!shooter.isSneaking()) {
                shooter.setVelocity(direction.add(0, 0.1, 0));
                shooter.velocityModified = true;
                shooter.fallDistance = 0;
            }

            List<UUID> affected = new ArrayList<>();

            BlockPos pos;
            for (int i = 0; i < range; i++) {
                pos = new BlockPos((int) Math.round(position.x-0.5), (int) Math.round(position.y-0.5), (int) Math.round(position.z-0.5));
                List<Entity> entities = world.getOtherEntities(shooter, new Box(pos).expand(1), SubwooferBlock::canPush);

                // TODO: packet mess
                world.addImportantParticle(ParticleTypes.SONIC_BOOM,
                        position.x, position.y, position.z,
                        0, 0, 0);
                world.playSound(null, pos, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS);

                for (Entity e : entities) {
                    if (affected.contains(e.getUuid())) continue;

                    affected.add(e.getUuid());
                    e.damage(shooter.getDamageSources().arrow((PersistentProjectileEntity) entity, shooter), 6);
                    e.setVelocity(direction.multiply(2.5).add(0, 0.2, 0));
                    e.velocityModified = true;
                }

                if (world.getBlockState(pos).isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) return true;

                position = position.add(direction);
            }

            return true;
        }
        else
            return instance.spawnEntity(entity);
    }
}
