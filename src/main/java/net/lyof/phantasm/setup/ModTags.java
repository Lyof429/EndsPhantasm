package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> XP_BOOSTED = create("gets_xp_speed_boost");
        public static final TagKey<Item> PREAM_LOGS = create("pream_logs");


        private static TagKey<Item> create(String name) {
            return TagKey.of(RegistryKeys.ITEM, Phantasm.makeID(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> PREAM_BLOCKS = create("pream_blocks");
        public static final TagKey<Block> HANGING_PREAM_LEAVES_GROWABLE_ON = create("hanging_pream_leaves_growable_on");

        public static final TagKey<Block> END_PLANTS = create("end_plants");
        public static final TagKey<Block> END_PLANTS_GROWABLE_ON = create("end_plants_growable_on");

        public static final TagKey<Block> OBLIVINE_GROWABLE_ON = create("oblivine_growable_on");


        private static TagKey<Block> create(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Phantasm.makeID(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> DREAMING_DEN = create("is_dreaming_den");


        private static TagKey<Biome> create(String name) {
            return TagKey.of(RegistryKeys.BIOME, Phantasm.makeID(name));
        }
    }
}
