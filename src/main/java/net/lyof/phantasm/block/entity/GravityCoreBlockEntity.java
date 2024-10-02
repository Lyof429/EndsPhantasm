package net.lyof.phantasm.block.entity;

import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class GravityCoreBlockEntity extends BlockEntity {
    public int ticks = 0;
    public int range = -1;

    public GravityCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVITY_CORE, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Range")) this.range = nbt.getInt("Range");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Range", this.range);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, GravityCoreBlockEntity entity) {
        entity.ticks++;
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, GravityCoreBlockEntity entity) {
        entity.ticks++;
        if (entity.ticks % 20 != 0) return;

        Box box = new Box(pos).expand(entity.range < 0 ? ConfigEntries.gravityCoreRange : entity.range);
        for (Entity e : world.getOtherEntities(null, box)) {
            if (e instanceof PlayerEntity p)
                p.addStatusEffect(new StatusEffectInstance(ModEffects.FLOATATION, 100, 0, true, true));
        }
    }
}
