package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.world.gen.structure.StructureType;

public class ModItemGroups {
    public static final ItemGroup PHANTASM = register(Phantasm.MOD_ID,
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.phantasm"))
                    .icon(() -> new ItemStack(ModBlocks.FALLEN_STAR))
                    .entries((context, entries) -> {
                        if (false) return;

                        entries.add(ModBlocks.FALLEN_STAR);

                        entries.add(ModBlocks.VIVID_NIHILIUM);
                        entries.add(ModBlocks.VIVID_NIHILIS);
                        entries.add(ModBlocks.TALL_VIVID_NIHILIS);

                        entries.add(ModBlocks.STARFLOWER);

                        entries.add(ModBlocks.PREAM_SAPLING);
                        entries.add(ModBlocks.PREAM_LEAVES);
                        entries.add(ModBlocks.HANGING_PREAM_LEAVES);

                        entries.add(ModBlocks.PREAM_LOG);
                        entries.add(ModBlocks.PREAM_WOOD);
                        entries.add(ModBlocks.STRIPPED_PREAM_LOG);
                        entries.add(ModBlocks.STRIPPED_PREAM_WOOD);

                        entries.add(ModBlocks.PREAM_PLANKS);
                        entries.add(ModBlocks.PREAM_STAIRS);
                        entries.add(ModBlocks.PREAM_SLAB);
                        entries.add(ModBlocks.PREAM_DOOR);
                        entries.add(ModBlocks.PREAM_TRAPDOOR);
                        entries.add(ModBlocks.PREAM_FENCE);
                        entries.add(ModBlocks.PREAM_FENCE_GATE);

                        entries.add(ModBlocks.PREAM_PRESSURE_PLATE);
                        entries.add(ModBlocks.PREAM_BUTTON);
                        entries.add(ModItems.PREAM_SIGN);
                        entries.add(ModItems.PREAM_HANGING_SIGN);

                        entries.add(ModItems.PREAM_BERRY);

                        entries.add(ModBlocks.OBLIVION);
                        entries.add(ModBlocks.OBLIVINE);
                        entries.add(ModBlocks.CRYSTALILY);

                        entries.add(ModItems.OBLIFRUIT);

                        entries.add(ModItems.CHORUS_FRUIT_SALAD);

                        entries.add(ModBlocks.PURPUR_WALL);
                        entries.add(ModBlocks.PURPUR_LAMP);

                        entries.add(ModBlocks.RAW_PURPUR);
                        entries.add(ModBlocks.RAW_PURPUR_COAL_ORE);
                        entries.add(ModBlocks.RAW_PURPUR_BRICKS);
                        entries.add(ModBlocks.RAW_PURPUR_PILLAR);
                        entries.add(ModBlocks.RAW_PURPUR_TILES);
                        entries.add(ModBlocks.RAW_PURPUR_BRICK_STAIRS);
                        entries.add(ModBlocks.RAW_PURPUR_BRICK_SLAB);
                        entries.add(ModBlocks.RAW_PURPUR_BRICK_WALL);

                        entries.add(ModBlocks.CRYSTAL_SHARD);
                        entries.add(ModBlocks.VOID_CRYSTAL_SHARD);

                        entries.add(ModBlocks.CRYSTAL_BLOCK);
                        entries.add(ModBlocks.VOID_CRYSTAL_BLOCK);

                        entries.add(ModItems.CRYSTALLINE_SWORD);
                        entries.add(ModItems.CRYSTALLINE_SHOVEL);
                        entries.add(ModItems.CRYSTALLINE_PICKAXE);
                        entries.add(ModItems.CRYSTALLINE_AXE);
                        entries.add(ModItems.CRYSTALLINE_HOE);

                        entries.add(ModBlocks.CRYSTAL_TILES);
                        entries.add(ModBlocks.CRYSTAL_PILLAR);
                        entries.add(ModBlocks.CRYSTAL_TILE_STAIRS);
                        entries.add(ModBlocks.CRYSTAL_TILE_SLAB);

                        entries.add(ModBlocks.VOID_CRYSTAL_TILES);
                        entries.add(ModBlocks.VOID_CRYSTAL_PILLAR);
                        entries.add(ModBlocks.VOID_CRYSTAL_TILE_STAIRS);
                        entries.add(ModBlocks.VOID_CRYSTAL_TILE_SLAB);

                        entries.add(ModBlocks.CRYSTAL_GLASS);
                        entries.add(ModBlocks.CRYSTAL_GLASS_PANE);
                        entries.add(ModBlocks.VOID_CRYSTAL_GLASS);
                        entries.add(ModBlocks.VOID_CRYSTAL_GLASS_PANE);

                        entries.add(ModBlocks.DELAYER);
                        entries.add(ModBlocks.SPLITTER);

                        entries.add(ModItems.BEHEMOTH_MEAT);
                        entries.add(ModItems.BEHEMOTH_STEAK);

                        entries.add(ModItems.CRYSTIE_SPAWN_EGG);
                        entries.add(ModItems.BEHEMOTH_SPAWN_EGG);


                        entries.add(ModBlocks.ACIDIC_NIHILIUM);
                        entries.add(ModBlocks.ACIDIC_NIHILIS);
                        entries.add(ModBlocks.TALL_ACIDIC_NIHILIS);
                        entries.add(ModBlocks.DRAGON_MINT);

                        entries.add(ModBlocks.DRALGAE);
                        entries.add(ModBlocks.POME);
                        entries.add(ModItems.POME_SLICE);

                        entries.add(ModBlocks.ACIDIC_MASS);

                        entries.add(ModBlocks.CIRITE);
                        entries.add(ModBlocks.CIRITE_IRON_ORE);
                        entries.add(ModBlocks.CIRITE_BRICKS);
                        entries.add(ModBlocks.CIRITE_BRICK_STAIRS);
                        entries.add(ModBlocks.CIRITE_BRICK_SLAB);
                        entries.add(ModBlocks.CIRITE_BRICK_WALL);
                        entries.add(ModBlocks.CIRITE_PILLAR);
                        entries.add(ModBlocks.CHISELED_CIRITE);

                        entries.add(ModBlocks.CHORAL_BLOCK);
                        entries.add(ModBlocks.CHORAL_FAN);
                        entries.add(ModBlocks.SUBWOOFER_BLOCK);

                        entries.add(ModItems.CHORAL_ARROW);

                        entries.add(ModItems.MUSIC_DISC_ABRUPTION);


                        entries.add(ModBlocks.POLISHED_OBSIDIAN);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICK_STAIRS);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_PILLAR);
                        entries.add(ModBlocks.CHISELED_OBSIDIAN);

                        entries.add(ModBlocks.CHALLENGE_RUNE);
                        entries.add(ModItems.SHATTERED_PENDANT);

                        //entries.add(ModItems.REALITY_BREAKER);

                        //for (Item item : ModRegistry.ITEMS)
                        //    entries.add(item);
                    })
                    .build());


    public static ItemGroup register(String id, ItemGroup tab) {
        return Registry.register(Registries.ITEM_GROUP, Phantasm.makeID(id), tab);
    }

    @SuppressWarnings("all")
    public static void register() {
        if (false) return;
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(ModItems.PREAM_BERRY);
            entries.add(ModItems.OBLIFRUIT);
            entries.add(ModItems.CHORUS_FRUIT_SALAD);

            entries.add(ModItems.BEHEMOTH_MEAT);
            entries.add(ModItems.BEHEMOTH_STEAK);

            entries.add(ModItems.POME_SLICE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Items.FLOWERING_AZALEA_LEAVES, ModBlocks.PREAM_LEAVES);
            entries.addAfter(Items.FLOWERING_AZALEA, ModBlocks.PREAM_SAPLING);
            entries.addAfter(ModBlocks.PREAM_LEAVES, ModBlocks.HANGING_PREAM_LEAVES);

            entries.addAfter(Items.END_STONE, ModBlocks.VIVID_NIHILIUM);
            entries.addAfter(Items.CHORUS_FLOWER, ModBlocks.VIVID_NIHILIS);
            entries.addAfter(ModBlocks.VIVID_NIHILIS, ModBlocks.TALL_VIVID_NIHILIS);
            entries.addAfter(ModBlocks.TALL_VIVID_NIHILIS, ModBlocks.STARFLOWER);

            entries.addAfter(ModBlocks.STARFLOWER, ModBlocks.RAW_PURPUR);
            entries.addAfter(Items.DEEPSLATE_COAL_ORE, ModBlocks.RAW_PURPUR_COAL_ORE);

            entries.addAfter(ModBlocks.RAW_PURPUR, ModBlocks.OBLIVION);
            entries.addAfter(ModBlocks.OBLIVION, ModBlocks.OBLIVINE);
            entries.addAfter(ModBlocks.OBLIVINE, ModBlocks.CRYSTALILY);

            entries.add(ModBlocks.FALLEN_STAR);

            entries.add(ModBlocks.CRYSTAL_SHARD);
            entries.add(ModBlocks.VOID_CRYSTAL_SHARD);

            entries.addAfter(ModBlocks.VIVID_NIHILIUM, ModBlocks.ACIDIC_NIHILIUM);
            entries.addAfter(ModBlocks.ACIDIC_NIHILIUM, ModBlocks.ACIDIC_MASS);
            entries.addAfter(ModBlocks.CRYSTALILY, ModBlocks.ACIDIC_NIHILIS);
            entries.addAfter(ModBlocks.ACIDIC_NIHILIS, ModBlocks.TALL_ACIDIC_NIHILIS);
            entries.addAfter(ModBlocks.TALL_ACIDIC_NIHILIS, ModBlocks.DRAGON_MINT);

            entries.addAfter(ModBlocks.DRAGON_MINT, ModBlocks.DRALGAE);
            entries.addAfter(ModBlocks.DRALGAE, ModBlocks.POME);

            entries.addAfter(ModBlocks.ACIDIC_MASS, ModBlocks.CIRITE);
            entries.addAfter(ModBlocks.CIRITE, ModBlocks.CIRITE_IRON_ORE);

            entries.addAfter(Blocks.HORN_CORAL_BLOCK, ModBlocks.CHORAL_BLOCK);
            entries.addAfter(Blocks.HORN_CORAL_WALL_FAN, ModBlocks.CHORAL_FAN);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.WARPED_BUTTON, ModBlocks.PREAM_LOG);
            entries.addAfter(ModBlocks.PREAM_LOG, ModBlocks.PREAM_WOOD);
            entries.addAfter(ModBlocks.PREAM_WOOD, ModBlocks.STRIPPED_PREAM_LOG);
            entries.addAfter(ModBlocks.STRIPPED_PREAM_LOG, ModBlocks.STRIPPED_PREAM_WOOD);

            entries.addAfter(ModBlocks.STRIPPED_PREAM_WOOD, ModBlocks.PREAM_PLANKS);
            entries.addAfter(ModBlocks.PREAM_PLANKS, ModBlocks.PREAM_STAIRS);
            entries.addAfter(ModBlocks.PREAM_STAIRS, ModBlocks.PREAM_SLAB);
            entries.addAfter(ModBlocks.PREAM_SLAB, ModBlocks.PREAM_FENCE);
            entries.addAfter(ModBlocks.PREAM_FENCE, ModBlocks.PREAM_FENCE_GATE);
            entries.addAfter(ModBlocks.PREAM_FENCE_GATE, ModBlocks.PREAM_DOOR);
            entries.addAfter(ModBlocks.PREAM_DOOR, ModBlocks.PREAM_TRAPDOOR);
            entries.addAfter(ModBlocks.PREAM_TRAPDOOR, ModBlocks.PREAM_PRESSURE_PLATE);
            entries.addAfter(ModBlocks.PREAM_PRESSURE_PLATE, ModBlocks.PREAM_BUTTON);

            entries.addAfter(Items.PURPUR_PILLAR, ModBlocks.PURPUR_WALL);
            entries.addAfter(ModBlocks.PURPUR_WALL, ModBlocks.PURPUR_LAMP);

            entries.addAfter(Items.PURPUR_SLAB, ModBlocks.RAW_PURPUR);
            entries.addAfter(ModBlocks.RAW_PURPUR, ModBlocks.RAW_PURPUR_BRICKS);
            entries.addAfter(ModBlocks.RAW_PURPUR_BRICKS, ModBlocks.RAW_PURPUR_PILLAR);
            entries.addAfter(ModBlocks.RAW_PURPUR_PILLAR, ModBlocks.RAW_PURPUR_TILES);
            entries.addAfter(ModBlocks.RAW_PURPUR_TILES, ModBlocks.RAW_PURPUR_BRICK_STAIRS);
            entries.addAfter(ModBlocks.RAW_PURPUR_BRICK_STAIRS, ModBlocks.RAW_PURPUR_BRICK_SLAB);
            entries.addAfter(ModBlocks.RAW_PURPUR_BRICK_SLAB, ModBlocks.RAW_PURPUR_BRICK_WALL);

            entries.addAfter(ModBlocks.RAW_PURPUR_BRICK_WALL, Blocks.OBSIDIAN);
            entries.addAfter(Blocks.OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN_BRICKS);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_BRICKS, ModBlocks.POLISHED_OBSIDIAN_BRICK_STAIRS);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_BRICK_STAIRS, ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_BRICK_SLAB, ModBlocks.POLISHED_OBSIDIAN_PILLAR);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_PILLAR, ModBlocks.CHISELED_OBSIDIAN);

            entries.add(ModBlocks.CRYSTAL_BLOCK);
            entries.add(ModBlocks.CRYSTAL_TILES);
            entries.add(ModBlocks.CRYSTAL_PILLAR);
            entries.add(ModBlocks.CRYSTAL_TILE_STAIRS);
            entries.add(ModBlocks.CRYSTAL_TILE_SLAB);

            entries.add(ModBlocks.VOID_CRYSTAL_BLOCK);
            entries.add(ModBlocks.VOID_CRYSTAL_TILES);
            entries.add(ModBlocks.VOID_CRYSTAL_PILLAR);
            entries.add(ModBlocks.VOID_CRYSTAL_TILE_STAIRS);
            entries.add(ModBlocks.VOID_CRYSTAL_TILE_SLAB);

            entries.add(ModBlocks.CIRITE_BRICKS);
            entries.add(ModBlocks.CIRITE_BRICK_STAIRS);
            entries.add(ModBlocks.CIRITE_BRICK_SLAB);
            entries.add(ModBlocks.CIRITE_BRICK_WALL);
            entries.add(ModBlocks.CIRITE_PILLAR);
            entries.add(ModBlocks.CHISELED_CIRITE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Items.WARPED_HANGING_SIGN, ModItems.PREAM_SIGN);
            entries.addAfter(ModItems.PREAM_SIGN, ModItems.PREAM_HANGING_SIGN);

            entries.addAfter(Items.JUKEBOX, ModBlocks.SUBWOOFER_BLOCK);

            entries.addAfter(Items.ENDER_EYE, ModBlocks.CHALLENGE_RUNE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.addAfter(Items.NOTE_BLOCK, ModBlocks.SUBWOOFER_BLOCK);

            entries.addAfter(Items.COMPARATOR, ModBlocks.DELAYER);
            entries.addAfter(ModBlocks.DELAYER, ModBlocks.SPLITTER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_STAINED_GLASS, ModBlocks.CRYSTAL_GLASS);
            entries.addAfter(ModBlocks.CRYSTAL_GLASS, ModBlocks.VOID_CRYSTAL_GLASS);
            entries.addAfter(Items.PINK_STAINED_GLASS_PANE, ModBlocks.CRYSTAL_GLASS_PANE);
            entries.addAfter(ModBlocks.CRYSTAL_GLASS_PANE, ModBlocks.VOID_CRYSTAL_GLASS_PANE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.IRON_HOE, ModItems.CRYSTALLINE_SHOVEL);
            entries.addAfter(ModItems.CRYSTALLINE_SHOVEL, ModItems.CRYSTALLINE_PICKAXE);
            entries.addAfter(ModItems.CRYSTALLINE_PICKAXE, ModItems.CRYSTALLINE_AXE);
            entries.addAfter(ModItems.CRYSTALLINE_AXE, ModItems.CRYSTALLINE_HOE);

            //entries.addBefore(Items.BUCKET, ModItems.REALITY_BREAKER);

            entries.add(ModItems.SHATTERED_PENDANT);

            entries.addAfter(Items.MUSIC_DISC_RELIC, ModItems.MUSIC_DISC_ABRUPTION);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.IRON_SWORD, ModItems.CRYSTALLINE_SWORD);
            entries.addAfter(Items.IRON_AXE, ModItems.CRYSTALLINE_AXE);

            entries.addAfter(Items.SPECTRAL_ARROW, ModItems.CHORAL_ARROW);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(ModItems.CRYSTIE_SPAWN_EGG);
            entries.add(ModItems.BEHEMOTH_SPAWN_EGG);
        });
    }
}
