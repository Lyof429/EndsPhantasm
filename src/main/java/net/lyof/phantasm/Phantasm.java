package net.lyof.phantasm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.effect.ModPotions;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.item.ModItemGroups;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.setup.ModDataGenerator;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.feature.ModFeatures;
import net.lyof.phantasm.world.feature.custom.tree.ModTreePlacerTypes;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Phantasm implements ModInitializer {
	public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

	@Override
	public void onInitialize() {
		ModConfig.register();

		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();

		ModEntities.register();

		ModParticles.register();
		ModSounds.register();

		ModEffects.register();
		ModPotions.register();

		ModTreePlacerTypes.register();

		ModDataGenerator.registerBurnable();
		ModDataGenerator.registerFuels();
		ModDataGenerator.registerStripped();

		ModFeatures.register();
		ModWorldGeneration.register();
		EndDataCompat.register();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return makeID("reload_listener");
			}

			@Override
			public void reload(ResourceManager manager) {
				ModConfig.register();
			}
		});
	}

	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static <T> T log(T message) {
		LOGGER.info(String.valueOf(message));
		return message;
	}
}