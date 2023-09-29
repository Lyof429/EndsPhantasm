package net.lyof.phantasm;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModRegistry {
    public static class BlockBuilder {
        protected BlockBuilder(Identifier id, Block block) {
            this.id = id;
            this.block = Registry.register(Registries.BLOCK, id, block);
            ModRegistry.ofItem(id.getPath(), new BlockItem(block, new FabricItemSettings())).build();
        }

        public Block build() {
            BLOCKS.add(this.block);
            return this.block;
        }

        protected Identifier id;
        protected Block block;

        public BlockBuilder drop() {
            BLOCK_AUTODROPS.add(this.block);
            return this;
        }

        public BlockBuilder tag(TagKey<Block> tagname) {
            BLOCK_TAGS.putIfAbsent(tagname, new ArrayList<>());
            BLOCK_TAGS.get(tagname).add(this.block);
            return this;
        }
    }


    public static class ItemBuilder {
        protected ItemBuilder(Identifier id, Item item) {
            this.id = id;
            this.item = Registry.register(Registries.ITEM, id, item);
        }

        public Item build() {
            ITEMS.add(this.item);
            return this.item;
        }

        protected Identifier id;
        protected Item item;

        public ItemBuilder tag(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.item);
            return this;
        }
    }


    public static class RecipeBuilder {
        protected RecipeBuilder() {}
    }


    public static BlockBuilder ofBlock(String id, Block block) {
        return new BlockBuilder(Phantasm.makeID(id), block);
    }

    public static ItemBuilder ofItem(String id, Item item) {
        return new ItemBuilder(Phantasm.makeID(id), item);
    }


    public static List<Block> BLOCKS = new ArrayList<>();
    public static Map<TagKey<Block>, List<Block>> BLOCK_TAGS = new HashMap<>();
    public static List<Block> BLOCK_AUTODROPS = new ArrayList<>();

    public static List<Item> ITEMS = new ArrayList<>();
    public static Map<TagKey<Item>, List<Item>> ITEM_TAGS = new HashMap<>();
}
