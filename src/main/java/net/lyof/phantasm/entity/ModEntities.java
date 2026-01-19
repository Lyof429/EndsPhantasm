package net.lyof.phantasm.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static void register() {
        TrackedDataHandlerRegistry.register(TRACKED_IDENTIFIER);

        FabricDefaultAttributeRegistry.register(CRYSTIE, CrystieEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BEHEMOTH, BehemothEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(POLYPPIE, PolyppieEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BRONSTED_BLOB, BronstedBlobEntity.createAttributes());
    }


    public static final EntityType<CrystieEntity> CRYSTIE = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("crystie"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CrystieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static final EntityType<BehemothEntity> BEHEMOTH = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("behemoth"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BehemothEntity::new)
                    .dimensions(EntityDimensions.changing(1f, 2f)).build());

    public static final EntityType<PolyppieEntity> POLYPPIE = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("polyppie"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, PolyppieEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 0.775f)).build());

    public static final EntityType<BronstedBlobEntity> BRONSTED_BLOB = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("bronsted_blob"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BronstedBlobEntity::new)
                    .dimensions(EntityDimensions.changing(2.04f, 2.04f)).build());

    public static final EntityType<ChoralArrowEntity> CHORAL_ARROW = Registry.register(Registries.ENTITY_TYPE,
            Phantasm.makeID("choral_arrow"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChoralArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(4).trackedUpdateRate(20).build());


    public static final TrackedDataHandler<Identifier> TRACKED_IDENTIFIER = TrackedDataHandler.of(PacketByteBuf::writeIdentifier, PacketByteBuf::readIdentifier);
}
