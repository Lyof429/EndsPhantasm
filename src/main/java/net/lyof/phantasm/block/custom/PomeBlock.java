package net.lyof.phantasm.block.custom;

import net.fabricmc.fabric.mixin.biome.TheEndBiomeSourceMixin;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.LandingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PomeBlock extends FallingBlock {
    public PomeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {}

    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return state.getMapColor(world, pos).color;
    }

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        super.configureFallingBlockEntity(entity);
        entity.setDestroyedOnLanding();
    }

    @Override
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        for (Entity entity : world.getOtherEntities(null, new Box(pos).expand(5))) {
            if (entity instanceof LivingEntity living) {
                living.damage(living.getDamageSources().dragonBreath(), 5);
                living.addStatusEffect(new StatusEffectInstance(ModEffects.CORROSION, 200, 0));
                entity.setVelocity(entity.getPos().add(0, 0.5, 0).subtract(pos.toCenterPos()).normalize().multiply(3));
            }
        }

        world.addBlockBreakParticles(pos, this.getDefaultState());
        world.playSound(null, pos, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS);
        super.onDestroyedOnLanding(world, pos, fallingBlockEntity);
    }
}
