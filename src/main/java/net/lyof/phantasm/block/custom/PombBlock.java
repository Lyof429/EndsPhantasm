package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PombBlock extends FallingBlock {
    public PombBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {}

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        super.configureFallingBlockEntity(entity);
        entity.setDestroyedOnLanding();
    }

    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return state.getMapColor(world, pos).color;
    }

    @Override
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        for (Entity entity : world.getOtherEntities(null, new Box(pos).expand(5))) {
            if (entity instanceof LivingEntity living) {
                living.damage(living.getDamageSources().dragonBreath(), (float) ConfigEntries.fallingPombDamage);
                living.addStatusEffect(new StatusEffectInstance(ModEffects.CORROSION, ConfigEntries.fallingPombDuration, ConfigEntries.fallingPombAmplifier));
                entity.setVelocity(entity.getPos().add(0, 0.5, 0).subtract(pos.toCenterPos()).normalize().multiply(3));
            }
        }

        if (world.isClient()) world.createExplosion(null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                2, World.ExplosionSourceType.NONE);
        Block.dropStacks(this.getDefaultState(), world, pos, null, null, ItemStack.EMPTY);
        world.playSound(null, pos, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS);
        super.onDestroyedOnLanding(world, pos, fallingBlockEntity);
    }
}
