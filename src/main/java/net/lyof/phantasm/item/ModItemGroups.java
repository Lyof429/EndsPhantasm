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

public class ModItemGroups {
    public static final ItemGroup PHANTASM = register(Phantasm.MOD_ID,
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.phantasm"))
                    .icon(() -> new ItemStack(ModBlocks.FALLEN_STAR))
                    .entries((displayContext, entries) -> {
                        if (false) return;


                        entries.add(ModBlocks.FALLEN_STAR);

                        entries.add(ModBlocks.VIVID_NIHILIA);
                        entries.add(ModBlocks.VIVID_NIHILIUM);

                        entries.add(ModBlocks.PREAM_SAPLING);
                        entries.add(ModBlocks.PREAM_LEAVES);

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

                        entries.add(ModItems.CHORUS_FRUIT_SALAD);

                        entries.add(ModBlocks.PURPUR_LAMP);

                        entries.add(ModBlocks.RAW_PURPUR);
                        entries.add(ModBlocks.RAW_PURPUR_BRICKS);
                        entries.add(ModBlocks.RAW_PURPUR_PILLAR);
                        entries.add(ModBlocks.RAW_PURPUR_TILES);
                        entries.add(ModBlocks.RAW_PURPUR_BRICKS_STAIRS);
                        entries.add(ModBlocks.RAW_PURPUR_BRICKS_SLAB);

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
                        entries.add(ModBlocks.CRYSTAL_TILES_STAIRS);
                        entries.add(ModBlocks.CRYSTAL_TILES_SLAB);

                        entries.add(ModBlocks.VOID_CRYSTAL_TILES);
                        entries.add(ModBlocks.VOID_CRYSTAL_PILLAR);
                        entries.add(ModBlocks.VOID_CRYSTAL_TILES_STAIRS);
                        entries.add(ModBlocks.VOID_CRYSTAL_TILES_SLAB);

                        entries.add(ModBlocks.POLISHED_OBSIDIAN);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS_STAIRS);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS_SLAB);


                        //for (Item item : ModRegistry.ITEMS)
                        //    entries.add(item);
                    })
                    .build());


    public static ItemGroup register(String id, ItemGroup tab) {
        return Registry.register(Registries.ITEM_GROUP, Phantasm.makeID(id), tab);
    }

    @SuppressWarnings("all")
    public static void register() {
        Phantasm.log("Registering ItemGroups for modid : " + Phantasm.MOD_ID);


        if (false) return;
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(ModItems.PREAM_BERRY);
            entries.add(ModItems.CHORUS_FRUIT_SALAD);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Items.FLOWERING_AZALEA_LEAVES, ModBlocks.PREAM_LEAVES);
            entries.addAfter(Items.FLOWERING_AZALEA, ModBlocks.PREAM_SAPLING);

            entries.addAfter(Items.END_STONE, ModBlocks.VIVID_NIHILIA);

            entries.addAfter(ModBlocks.VIVID_NIHILIA, ModBlocks.RAW_PURPUR);

            entries.add(ModBlocks.VIVID_NIHILIUM);

            entries.add(ModBlocks.FALLEN_STAR);

            entries.add(ModBlocks.CRYSTAL_SHARD);
            entries.add(ModBlocks.VOID_CRYSTAL_SHARD);
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

            entries.addAfter(Items.PURPUR_PILLAR, ModBlocks.PURPUR_LAMP);

            entries.addAfter(Items.PURPUR_SLAB, ModBlocks.RAW_PURPUR);
            entries.addAfter(ModBlocks.RAW_PURPUR, ModBlocks.RAW_PURPUR_BRICKS);
            entries.addAfter(ModBlocks.RAW_PURPUR_BRICKS, ModBlocks.RAW_PURPUR_PILLAR);
            entries.addAfter(ModBlocks.RAW_PURPUR_PILLAR, ModBlocks.RAW_PURPUR_TILES);
            entries.addAfter(ModBlocks.RAW_PURPUR_TILES, ModBlocks.RAW_PURPUR_BRICKS_STAIRS);
            entries.addAfter(ModBlocks.RAW_PURPUR_BRICKS_STAIRS, ModBlocks.RAW_PURPUR_BRICKS_SLAB);

            entries.addAfter(ModBlocks.RAW_PURPUR_BRICKS_SLAB, Blocks.OBSIDIAN);
            entries.addAfter(Blocks.OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN, ModBlocks.POLISHED_OBSIDIAN_BRICKS);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_BRICKS, ModBlocks.POLISHED_OBSIDIAN_BRICKS_STAIRS);
            entries.addAfter(ModBlocks.POLISHED_OBSIDIAN_BRICKS_STAIRS, ModBlocks.POLISHED_OBSIDIAN_BRICKS_SLAB);

            entries.add(ModBlocks.CRYSTAL_BLOCK);
            entries.add(ModBlocks.CRYSTAL_TILES);
            entries.add(ModBlocks.CRYSTAL_PILLAR);
            entries.add(ModBlocks.CRYSTAL_TILES_STAIRS);
            entries.add(ModBlocks.CRYSTAL_TILES_SLAB);

            entries.add(ModBlocks.VOID_CRYSTAL_BLOCK);
            entries.add(ModBlocks.VOID_CRYSTAL_TILES);
            entries.add(ModBlocks.VOID_CRYSTAL_PILLAR);
            entries.add(ModBlocks.VOID_CRYSTAL_TILES_STAIRS);
            entries.add(ModBlocks.VOID_CRYSTAL_TILES_SLAB);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Items.WARPED_HANGING_SIGN, ModItems.PREAM_SIGN);
            entries.addAfter(ModItems.PREAM_SIGN, ModItems.PREAM_HANGING_SIGN);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.IRON_HOE, ModItems.CRYSTALLINE_SHOVEL);
            entries.addAfter(ModItems.CRYSTALLINE_SHOVEL, ModItems.CRYSTALLINE_PICKAXE);
            entries.addAfter(ModItems.CRYSTALLINE_PICKAXE, ModItems.CRYSTALLINE_AXE);
            entries.addAfter(ModItems.CRYSTALLINE_AXE, ModItems.CRYSTALLINE_HOE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.IRON_SWORD, ModItems.CRYSTALLINE_SWORD);
            entries.addAfter(Items.IRON_AXE, ModItems.CRYSTALLINE_AXE);
        });
    }
}
