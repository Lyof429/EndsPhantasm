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
import net.minecraft.item.ItemConvertible;
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
import java.util.function.Supplier;

public class ModRegistry {
    public static class BlockBuilder {
        protected BlockBuilder(Identifier id, Block block, Supplier<Item> item) {
            this.id = id;
            this.block = Registry.register(Registries.BLOCK, id, block);
            if (item != null)
                ModRegistry.ofItem(id.getPath(), item.get()).build();
        }

        public Block build() {
            BLOCKS.add(this.block);
            return this.block;
        }

        protected Identifier id;
        protected Block block;

        public BlockBuilder drop() {
            return this.drop(this.block);
        }

        public BlockBuilder drop(ItemConvertible loot) {
            BLOCK_DROPS.putIfAbsent(this.block, loot);
            return this;
        }
        /*
        public BlockBuilder drop_silk(ItemConvertible loot) {
            BLOCK_SILK_DROPS.putIfAbsent(this.block, loot);
            return this;
        }

        public BlockBuilder drop_shears(ItemConvertible loot) {
            BLOCK_SHEARS_DROPS.putIfAbsent(this.block, loot);
            return this;
        }*/

        public BlockBuilder tag(TagKey<Block> tagname) {
            BLOCK_TAGS.putIfAbsent(tagname, new ArrayList<>());
            BLOCK_TAGS.get(tagname).add(this.block);
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tag(TagKey<Block>... tags) {
            for (TagKey<Block> tagname : tags) {
                this.tag(tagname);
            }
            return this;
        }

        public BlockBuilder tagitem(TagKey<Item> tagname) {
            ITEM_TAGS.putIfAbsent(tagname, new ArrayList<>());
            ITEM_TAGS.get(tagname).add(this.block.asItem());
            return this;
        }

        @SafeVarargs
        public final BlockBuilder tagitem(TagKey<Item>... tags) {
            for (TagKey<Item> tagname : tags) {
                this.tagitem(tagname);
            }
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

        public BlockBuilder end_plant() {
            return this.tag(ModTags.Blocks.END_PLANTS);
        }

        public BlockBuilder end_soil() {
            return this.tag(ModTags.Blocks.END_PLANTS_GROWABLE_ON);
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

        public BlockBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.block, duration);
            return this;
        }

        public BlockBuilder flammable(int duration, int spread) {
            BLOCK_FLAMMABLE.put(this.block, new Pair<>(duration, spread));
            return this;
        }

        public BlockBuilder strip(Block stripped) {
            BLOCK_STRIPPED.putIfAbsent(this.block, stripped);
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

        @SafeVarargs
        public final ItemBuilder tag(TagKey<Item>... tags) {
            for (TagKey<Item> tagname : tags) {
                this.tag(tagname);
            }
            return this;
        }

        public ItemBuilder model() {
            return this.model(net.minecraft.data.client.Models.GENERATED);
        }

        public ItemBuilder model(Model model) {
            ITEM_MODELS.put(this.item, model);
            return this;
        }

        public ItemBuilder fuel(int duration) {
            ITEM_BURNABLE.put(this.item, duration);
            return this;
        }
    }


    public enum Models {
        CUBE,
        CROSS,
        PILLAR,
        WOOD,
        STAIRS,
        SLAB,
        BUTTON,
        PRESSURE_PLATE,
        FENCE,
        FENCE_GATE,
        DOOR,
        TRAPDOOR,
        SIGN,
        WALL_SIGN,
        HANGING_SIGN,
        WALL_HANGING_SIGN,
        PANE,
        ROTATABLE,
        WALL
    }

    public static class Foods {
        public static final FoodComponent PREAM_BERRY = new FoodComponent.Builder().alwaysEdible()
                .saturationModifier(1).hunger(4)
                .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1, true, false), 1)
                .build();

        public static final FoodComponent OBLIFRUIT = new FoodComponent.Builder()
                .hunger(6).saturationModifier(1)
                .build();

        public static final FoodComponent CHORUS_SALAD = new FoodComponent.Builder().alwaysEdible()
                .hunger(6).saturationModifier(1.5f)
                .build();

        public static final FoodComponent BEHEMOTH_MEAT = new FoodComponent.Builder().alwaysEdible()
                .hunger(6).saturationModifier(0.5f)
                .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 0, true, true), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1, true, true), 1)
                .meat()
                .build();

        public static final FoodComponent BEHEMOTH_STEAK = new FoodComponent.Builder().alwaysEdible().hunger(10).saturationModifier(0.8f)
                .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 0, true, true), 1)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1, true, true), 1)
                .meat()
                .build();

        public static final FoodComponent POMB_SLICE = new FoodComponent.Builder().alwaysEdible()
                .hunger(4).saturationModifier(1.3f)
                .snack()
                .build();

        public static FoodComponent EGGS_NIHILO = new FoodComponent.Builder().hunger(5).saturationModifier(0.8f)
                .build();
    }


    public static BlockBuilder ofBlock(String id, Block block) {
        return ModRegistry.ofBlock(id, block, () -> new BlockItem(block, new FabricItemSettings()));
    }

    public static BlockBuilder ofBlock(String id, Block block, Supplier<Item> item) {
        return new BlockBuilder(Phantasm.makeID(id), block, item);
    }

    public static ItemBuilder ofItem(String id, Item item) {
        return new ItemBuilder(Phantasm.makeID(id), item);
    }


    public static List<Block> getModelList(Models key) {
        return BLOCK_MODELS.getOrDefault(key, new ArrayList<>());
    }


    public static void registerStairsAndSlab(Block parent, Block stairs, Block slab) {
        registerSet(parent, Map.of(
                Models.STAIRS, stairs,
                Models.SLAB, slab
        ));
    }

    public static void registerGlass(Block parent, Block pane) {
        registerSet(parent, Map.of(
                Models.PANE, pane
        ));
    }

    public static void registerSet(Block parent, Map<Models, Block> set) {
        BLOCK_SETS.putIfAbsent(parent, set);
        for (Models model : set.keySet()) {
            BLOCK_MODELS.putIfAbsent(model, new ArrayList<>());
            BLOCK_MODELS.get(model).add(set.get(model));
        }
    }

    public static void addDrop(Block block, ItemConvertible loot) {
        BLOCK_DROPS.putIfAbsent(block, loot);
    }


    public static List<Block> BLOCKS = new ArrayList<>();
    public static Map<TagKey<Block>, List<Block>> BLOCK_TAGS = new HashMap<>();

    public static Map<Block, ItemConvertible> BLOCK_DROPS = new HashMap<>();
    //public static Map<Block, ItemConvertible> BLOCK_SILK_DROPS = new HashMap<>();
    //public static Map<Block, ItemConvertible> BLOCK_SHEARS_DROPS = new HashMap<>();

    public static Map<Block, Block> BLOCK_STRIPPED = new HashMap<>();
    public static Map<Block, Map<Models, Block>> BLOCK_SETS = new HashMap<>();

    public static Map<Models, List<Block>> BLOCK_MODELS = new HashMap<>();
    public static List<Block> BLOCK_CUTOUT = new ArrayList<>();

    public static Map<Block, Pair<Integer, Integer>> BLOCK_FLAMMABLE = new HashMap<>();


    public static List<Item> ITEMS = new ArrayList<>();
    public static Map<TagKey<Item>, List<Item>> ITEM_TAGS = new HashMap<>();
    public static Map<Item, Model> ITEM_MODELS = new HashMap<>();
    public static Map<ItemConvertible, Integer> ITEM_BURNABLE = new HashMap<>();

    public static void clear() {
        BLOCKS.clear();
        BLOCK_TAGS.clear();
        BLOCK_DROPS.clear();
        BLOCK_STRIPPED.clear();
        BLOCK_SETS.clear();
        BLOCK_MODELS.clear();
        //BLOCK_CUTOUT.clear();
        BLOCK_FLAMMABLE.clear();

        ITEMS.clear();
        ITEM_TAGS.clear();
        ITEM_MODELS.clear();
        ITEM_BURNABLE.clear();
    }
}
