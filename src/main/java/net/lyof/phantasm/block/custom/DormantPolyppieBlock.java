package net.lyof.phantasm.block.custom;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.DormantPolyppieBlockEntity;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DormantPolyppieBlock extends DirectionalBlock implements BlockEntityProvider {
    public DormantPolyppieBlock(Settings properties) {
        super(properties);
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        if (type == 0) {
            world.breakBlock(pos, false);
            PolyppieEntity polyppie = ModEntities.POLYPPIE.create(world);
            polyppie.setPosition(pos.toCenterPos());
            world.spawnEntity(polyppie);
            return true;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DormantPolyppieBlockEntity(pos, state);
    }
}
