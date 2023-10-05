package net.lyof.phantasm.setup;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lyof.phantasm.Phantasm;
import net.minecraft.block.Block;
import net.minecraft.data.client.Model;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

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

        public BlockBuilder tool(String tool_material) {
            String[] needed = tool_material.split("_");

            if (needed[0].equals("stone")) this.tag(BlockTags.NEEDS_STONE_TOOL);
            if (needed[0].equals("iron")) this.tag(BlockTags.NEEDS_IRON_TOOL);
            if (needed[0].equals("diamond")) this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

            if (needed[1].equals("pickaxe")) this.tag(BlockTags.PICKAXE_MINEABLE);
            if (needed[1].equals("axe")) this.tag(BlockTags.AXE_MINEABLE);
            if (needed[1].equals("shovel")) this.tag(BlockTags.SHOVEL_MINEABLE);
            if (needed[1].equals("hoe")) this.tag(BlockTags.HOE_MINEABLE);
            if (needed[1].equals("sword")) this.tag(BlockTags.SWORD_EFFICIENT);

            return this;
        }

        public BlockBuilder model() {
            return this.model(Models.CUBE);
        }

        public BlockBuilder model(Models model) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(this.block);
            return this;
        }

        public BlockBuilder model(Model model) {
            ITEM_MODELS.put(this.block.asItem(), model);
            return this;
        }

        public BlockBuilder cutout() {
            BLOCK_CUTOUT.add(this.block);
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

        public ItemBuilder model(Model model) {
            ITEM_MODELS.put(this.item, model);
            return this;
        }
    }


    public static class RecipeBuilder {
        protected RecipeBuilder() {}
    }


    public enum Models {
        CUBE,
        PILLAR,
        STAIRS,
        SLAB
    }

    public static class Foods {
        public static final FoodComponent PREAM_BERRY = new FoodComponent.Builder().alwaysEdible().hunger(4).statusEffect(
                        new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0, true, false),
                        1).build();
    }


    public static BlockBuilder ofBlock(String id, Block block) {
        return new BlockBuilder(Phantasm.makeID(id), block);
    }

    public static ItemBuilder ofItem(String id, Item item) {
        return new ItemBuilder(Phantasm.makeID(id), item);
    }


    public static void registerStairsAndSlab(Block parent, Block stairs, Block slab) {
        BLOCK_STAIRS_SLABS.putIfAbsent(parent, new ArrayList<>());
        BLOCK_STAIRS_SLABS.get(parent).add(new Pair<>(stairs, Models.STAIRS));
        BLOCK_STAIRS_SLABS.get(parent).add(new Pair<>(slab, Models.SLAB));

        BLOCK_MODELS.putIfAbsent(Models.STAIRS, new ArrayList<>());
        BLOCK_MODELS.putIfAbsent(Models.SLAB, new ArrayList<>());
        BLOCK_MODELS.get(Models.STAIRS).add(stairs);
        BLOCK_MODELS.get(Models.SLAB).add(slab);
    }


    public static List<Block> BLOCKS = new ArrayList<>();
    public static Map<TagKey<Block>, List<Block>> BLOCK_TAGS = new HashMap<>();
    public static List<Block> BLOCK_AUTODROPS = new ArrayList<>();
    public static Map<Models, List<Block>> BLOCK_MODELS = new HashMap<>();
    public static Map<Block, List<Pair<Block, Models>>> BLOCK_STAIRS_SLABS = new HashMap<>();
    public static List<Block> BLOCK_CUTOUT = new ArrayList<>();

    public static List<Item> ITEMS = new ArrayList<>();
    public static Map<TagKey<Item>, List<Item>> ITEM_TAGS = new HashMap<>();
    public static Map<Item, Model> ITEM_MODELS = new HashMap<>();
}
