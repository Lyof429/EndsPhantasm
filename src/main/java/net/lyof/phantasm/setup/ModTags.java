package net.lyof.phantasm.setup;

import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> XP_BOOSTED = create("gets_xp_speed_boost");
        public static final TagKey<Item> PREAM_LOGS = create("pream_logs");


        private static TagKey<Item> create(String id) {
            return TagKey.of(RegistryKeys.ITEM, Phantasm.makeID(id));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> PREAM_BLOCKS = create("pream_blocks");
        public static final TagKey<Block> HANGING_PREAM_LEAVES_GROWABLE_ON = create("hanging_pream_leaves_growable_on");


        private static TagKey<Block> create(String id) {
            return TagKey.of(RegistryKeys.BLOCK, Phantasm.makeID(id));
        }
    }
}
