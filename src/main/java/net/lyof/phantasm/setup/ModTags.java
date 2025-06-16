package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> XP_BOOSTED = create("has_xp_boost");
        public static final TagKey<Item> PREAM_LOGS = create("pream_logs");
        public static final TagKey<Item> CRYSTAL_FLOWERS = create("crystal_flowers");


        private static TagKey<Item> create(String name) {
            return TagKey.of(RegistryKeys.ITEM, Phantasm.makeID(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> PREAM_BLOCKS = create("pream_blocks");
        public static final TagKey<Block> END_CRYSTAL_PLACEABLE_ON = create("end_crystal_placeable_on");
        public static final TagKey<Block> HANGING_PREAM_LEAVES_GROWABLE_ON = create("hanging_pream_leaves_growable_on");

        public static final TagKey<Block> END_PLANTS = create("end_plants");
        public static final TagKey<Block> END_PLANTS_GROWABLE_ON = create("end_plants_growable_on");

        public static final TagKey<Block> OBLIVINE_GROWABLE_ON = create("oblivine_growable_on");
        public static final TagKey<Block> DRALGAE_GROWABLE_ON = create("dralgae_growable_on");


        private static TagKey<Block> create(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Phantasm.makeID(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> DREAMING_DEN = create("is_dreaming_den");
        public static final TagKey<Biome> ACIDBURNT_ABYSSES = create("is_acidburnt_abysses");


        private static TagKey<Biome> create(String name) {
            return TagKey.of(RegistryKeys.BIOME, Phantasm.makeID(name));
        }
    }

    public static class GameEvents {
        public static final TagKey<GameEvent> BEHEMOTH_CAN_LISTEN = create("behemoth_can_listen");

        private static TagKey<GameEvent> create(String name) {
            return TagKey.of(RegistryKeys.GAME_EVENT, Phantasm.makeID(name));
        }
    }
}
