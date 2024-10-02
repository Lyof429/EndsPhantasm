package net.lyof.phantasm.block.entity;

import net.lyof.phantasm.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ConduitBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GravityCoreBlockEntity extends BlockEntity {
    public GravityCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVITY_CORE, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ConduitBlockEntity blockEntity) {

    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ConduitBlockEntity blockEntity) {

    }
}
