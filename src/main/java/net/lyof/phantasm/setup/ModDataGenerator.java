package net.lyof.phantasm.setup;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.lyof.phantasm.setup.datagen.fabric.*;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.lyof.phantasm.world.feature.ModConfiguredFeatures;
import net.lyof.phantasm.world.feature.ModPlacedFeatures;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;

import java.util.Map;

public class ModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModGameEventTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModWorldGenProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder builder) {
		builder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		builder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
		builder.addRegistry(RegistryKeys.BIOME, ModBiomes::bootstrap);
	}


	public static void registerFuels() {
		for (Map.Entry<ItemConvertible, Integer> entry : ModRegistry.ITEM_BURNABLE.entrySet())
			FuelRegistry.INSTANCE.add(entry.getKey(), entry.getValue());
	}

	public static void registerBurnable() {
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
		for (Map.Entry<Block, Pair<Integer, Integer>> entry : ModRegistry.BLOCK_FLAMMABLE.entrySet()) {
			registry.add(entry.getKey(), entry.getValue().getLeft(), entry.getValue().getRight());
		}
	}

	public static void registerStripped() {
		for (Map.Entry<Block, Block> entry : ModRegistry.BLOCK_STRIPPED.entrySet())
			StrippableBlockRegistry.register(entry.getKey(), entry.getValue());
	}
}
