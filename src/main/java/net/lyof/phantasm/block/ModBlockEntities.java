package net.lyof.phantasm.block;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.entity.ChallengeRuneBlockEntity;
import net.lyof.phantasm.block.entity.DormantPolyppieBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static void register() {}

    public static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Phantasm.makeID(name), blockEntityType);
    }

    public static final BlockEntityType<ChallengeRuneBlockEntity> CHALLENGE_RUNE = register("challenge_rune",
            BlockEntityType.Builder.create(ChallengeRuneBlockEntity::new, ModBlocks.CHALLENGE_RUNE).build(null));

    public static final BlockEntityType<DormantPolyppieBlockEntity> DORMANT_POLYPPIE = register("dormant_polyppie",
            BlockEntityType.Builder.create(DormantPolyppieBlockEntity::new, ModBlocks.DORMANT_POLYPPIE).build(null));
}
