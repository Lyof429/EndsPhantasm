package net.lyof.phantasm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.effect.ModPotions;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItemGroups;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.setup.ModDataGenerator;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.datagen.config.ConfiguredData;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.feature.ModFeatures;
import net.lyof.phantasm.world.feature.custom.tree.ModTreePlacerTypes;
import net.lyof.phantasm.world.noise.ModDensityFunctions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Phantasm implements ModInitializer {
	public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

	@Override
	public void onInitialize() {
		ModConfig.register();
		ConfiguredData.register();

		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();

		ModEntities.register();
		ModBlockEntities.register();

		ModParticles.register();
		ModSounds.register();

		ModEffects.register();
		ModPotions.register();

		ModTreePlacerTypes.register();

		ModDataGenerator.registerBurnable();
		ModDataGenerator.registerFuels();
		ModDataGenerator.registerStripped();

		EndDataCompat.register();
		ModFeatures.register();
		ModDensityFunctions.register();
		ModWorldGeneration.register();

		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) ModRegistry.clear();

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(makeID("phantasm_connected_glass"),
				container, Text.literal("Phantasm Connected Glass"), ResourcePackActivationType.DEFAULT_ENABLED));
	}

	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	@Deprecated
	public static <T> T log(T message) {
		return log(message, 0);
	}

	public static <T> T log(T message, int level) {
		if (level == 0)
			LOGGER.info("[Phantasm] {}", message);
		else if (level == 1)
			LOGGER.warn("[Phantasm] {}", message);
		else if (level == 2)
			LOGGER.error("[Phantasm] {}", message);
		else if (level == 3)
			LOGGER.debug("[Phantasm] {}", message);
		return message;
	}
}