package net.lyof.phantasm.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.block.entity.GravityCoreBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static void register() {
        Phantasm.log("Registering BlockEntities for modid : " + Phantasm.MOD_ID);
    }

    public static final BlockEntityType<GravityCoreBlockEntity> GRAVITY_CORE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Phantasm.makeID("gravity_core"),
            FabricBlockEntityTypeBuilder.create(GravityCoreBlockEntity::new, ModBlocks.GRAVITY_CORE).build());
}
