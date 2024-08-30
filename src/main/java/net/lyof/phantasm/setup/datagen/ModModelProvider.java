package net.lyof.phantasm.setup.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        BlockStateModelGenerator.BlockTexturePool pool;
        BlockStateModelGenerator.LogTexturePool poollog;

        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CUBE))
            generator.registerSimpleCubeAll(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.PILLAR))
            generator.registerLog(block).log(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.CROSS))
            generator.registerTintableCross(block, BlockStateModelGenerator.TintType.NOT_TINTED);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.DOOR))
            generator.registerDoor(block);
        for (Block block : ModRegistry.getModelList(ModRegistry.Models.TRAPDOOR))
            generator.registerTrapdoor(block);

        for (Block block : ModRegistry.BLOCK_SETS.keySet()) {
            if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.WOOD)) {
                poollog = generator.registerLog(block);
                poollog.log(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.WOOD)
                        poollog.wood(entry.getValue());
                }
            }

            else if (ModRegistry.BLOCK_SETS.get(block).containsKey(ModRegistry.Models.PANE)) {
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.PANE)
                        generator.registerGlassPane(block, entry.getValue());
                }
            }

            else {
                pool = generator.registerCubeAllModelTexturePool(block);
                for (Map.Entry<ModRegistry.Models, Block> entry : ModRegistry.BLOCK_SETS.get(block).entrySet()) {
                    if (entry.getKey() == ModRegistry.Models.STAIRS)
                        pool.stairs(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SLAB)
                        pool.slab(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.BUTTON)
                        pool.button(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.PRESSURE_PLATE)
                        pool.pressurePlate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE)
                        pool.fence(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.FENCE_GATE)
                        pool.fenceGate(entry.getValue());
                    if (entry.getKey() == ModRegistry.Models.SIGN)
                        pool.family(new BlockFamily.Builder(block)
                                .sign(entry.getValue(), ModRegistry.BLOCK_SETS.get(block).get(ModRegistry.Models.WALL_SIGN))
                                .build());
                }
            }
        }

        generator.registerHangingSign(ModBlocks.STRIPPED_PREAM_LOG,
                ModBlocks.PREAM_HANGING_SIGN, ModBlocks.PREAM_WALL_HANGING_SIGN);

        generator.registerParentedItemModel(ModBlocks.PURPUR_LAMP, Phantasm.makeID("block/purpur_lamp"));

        generator.registerParentedItemModel(ModBlocks.CRYSTALILY, Phantasm.makeID("block/crystalily"));

        generator.registerParentedItemModel(ModBlocks.VIVID_NIHILIUM, Phantasm.makeID("block/vivid_nihilium"));
        generator.registerDoubleBlock(ModBlocks.TALL_VIVID_NIHILIS, BlockStateModelGenerator.TintType.NOT_TINTED);
        generator.registerParentedItemModel(ModBlocks.STARFLOWER, Phantasm.makeID("block/starflower"));

        generator.registerParentedItemModel(ModBlocks.ACIDIC_NIHILIUM, Phantasm.makeID("block/acidic_nihilium"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Item, Model> entry : ModRegistry.ITEM_MODELS.entrySet())
            generator.register(entry.getKey(), entry.getValue());

        generator.register(ModItems.CRYSTIE_SPAWN_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")),
                Optional.empty()));
        generator.register(ModItems.BEHEMOTH_SPAWN_EGG, new Model(Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")),
                Optional.empty()));
    }
}
