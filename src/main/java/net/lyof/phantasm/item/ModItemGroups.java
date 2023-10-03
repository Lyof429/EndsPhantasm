package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final ItemGroup PHANTASM = register(Phantasm.MOD_ID,
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.phantasm"))
                    .icon(() -> new ItemStack(ModBlocks.FALLEN_STAR))
                    .entries((displayContext, entries) -> {
                        if (false) return;

                        //for (Item item : ModRegistry.ITEMS)
                        //    entries.add(item);

                        entries.add(ModBlocks.FALLEN_STAR);

                        entries.add(ModItems.PREAM_BERRY);

                        entries.add(ModBlocks.CRYSTAL_SHARD);
                        entries.add(ModBlocks.VOID_CRYSTAL_SHARD);

                        entries.add(ModBlocks.POLISHED_OBSIDIAN);
                        entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS);

                    })
                    .build());


    public static ItemGroup register(String id, ItemGroup tab) {
        return Registry.register(Registries.ITEM_GROUP, Phantasm.makeID(id), tab);
    }

    public static void register() {
        Phantasm.log("Registering ItemGroups for modid : " + Phantasm.MOD_ID);


        if (false) return;
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(ModItems.PREAM_BERRY);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ModBlocks.FALLEN_STAR);

            entries.add(ModBlocks.CRYSTAL_SHARD);
            entries.add(ModBlocks.VOID_CRYSTAL_SHARD);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.POLISHED_OBSIDIAN);
            entries.add(ModBlocks.POLISHED_OBSIDIAN_BRICKS);
        });
    }
}
