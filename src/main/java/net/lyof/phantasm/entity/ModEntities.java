package net.lyof.phantasm.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.BehemothEntity;
import net.lyof.phantasm.entity.custom.CrystieEntity;
import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static void register() {
        Phantasm.log("Registering Entities for modid : " + Phantasm.MOD_ID);

        FabricDefaultAttributeRegistry.register(CRYSTIE, CrystieEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BEHEMOTH, BehemothEntity.createAttributes());
    }

    public static final EntityType<CrystieEntity> CRYSTIE = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("crystie"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CrystieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static final EntityType<BehemothEntity> BEHEMOTH = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("behemoth"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BehemothEntity::new)
                    .dimensions(EntityDimensions.changing(1f, 2f)).build());

    public static final EntityType<ChoralArrowEntity> CHORAL_ARROW = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("choral_arrow"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChoralArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build());
}
