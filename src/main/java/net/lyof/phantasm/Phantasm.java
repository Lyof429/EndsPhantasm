package net.lyof.phantasm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItemGroups;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.setup.ModDataGenerator;
import net.lyof.phantasm.world.ModFeatures;
import net.lyof.phantasm.world.ModPlacedFeatures;
import net.lyof.phantasm.world.feature.CrystalSpikeFeature;
import net.lyof.phantasm.world.feature.config.CrystalSpikeFeatureConfig;
import net.lyof.phantasm.world.gen.ModWorldGeneration;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// 1709 62 74
public class Phantasm implements ModInitializer {
	public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

	@Override
	public void onInitialize() {
		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();

		ModDataGenerator.registerBurnable();
		ModDataGenerator.registerFuels();
		ModDataGenerator.registerStripped();

		ModFeatures.register();
		ModWorldGeneration.register();
	}

	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static <T> T log(T message) {
		LOGGER.info(String.valueOf(message));
		return message;
	}
}