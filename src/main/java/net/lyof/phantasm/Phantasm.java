package net.lyof.phantasm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.block.ModBlockEntities;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ModConfig;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.effect.ModPotions;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.item.ModItemGroups;
import net.lyof.phantasm.item.ModItems;
import net.lyof.phantasm.particle.ModParticles;
import net.lyof.phantasm.setup.ModDataGenerator;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.compat.FarmersDelightCompat;
import net.lyof.phantasm.setup.datagen.config.ConfiguredData;
import net.lyof.phantasm.sound.ModSounds;
import net.lyof.phantasm.world.ModWorldGeneration;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.feature.ModFeatures;
import net.lyof.phantasm.world.feature.custom.tree.ModTreePlacerTypes;
import net.lyof.phantasm.world.noise.ModDensityFunctions;
import net.lyof.phantasm.world.structure.ModProcessorTypes;
import net.lyof.phantasm.world.structure.ModStructures;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Phantasm implements ModInitializer {
	public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

	@Override
	public void onInitialize() {
		ModConfig.register();
		ConfiguredData.register();

		if (Phantasm.isFarmersDelightLoaded())
			FarmersDelightCompat.setup();

		ModItems.register();
		ModBlocks.register();
		ModItemGroups.register();

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
		ModProcessorTypes.register();
		ModStructures.register();
		ModWorldGeneration.register();

		registerPackets();
		registerEvents();
		registerModules();

		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) ModRegistry.clear();
	}

	private static void registerPackets() {
		ServerPlayNetworking.registerGlobalReceiver(ModPackets.BEGIN_CUTSCENE_ENDS, ModPackets.Server::beginCutsceneEnds);

		ServerPlayNetworking.registerGlobalReceiver(ModPackets.POLYPPIE_CLIENT_UPDATE, ModPackets.Server::polyppieClientUpdate);
	}

	private static void registerModules() {
		registerPack("phantasm_connected_glass", "Phantasm Connected Glass", false);

		if (Phantasm.isVinURLLoaded()) registerPack("compat_vinurl", "VinURL Compat", false);
		if (Phantasm.isFarmersDelightLoaded()) registerPack("compat_farmersdelight", "Farmer's Delight Compat", true);
		if (FabricLoader.getInstance().isModLoaded("jeed")) registerPack("compat_jeed", "JEED Compat", true);
	}

	private static void registerPack(String id, String name, boolean force) {
		Phantasm.log("Enabling module : " + name, 0);
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container ->
				ResourceManagerHelper.registerBuiltinResourcePack(makeID(id), container, Text.literal(name),
						force ? ResourcePackActivationType.ALWAYS_ENABLED : ResourcePackActivationType.DEFAULT_ENABLED));
	}

	private static void registerEvents() {
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
			List<PacketByteBuf> packets = new ArrayList<>();

			PacketByteBuf packet = PacketByteBufs.create();
			packet.writeInt(0);
			packets.add(packet);

			PolyppieEntity.Variant.write(packets);

			packets.forEach(p -> ServerPlayNetworking.send(player, ModPackets.INITIALIZE, p));
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((old, self, alive) -> {
			if (self instanceof PolyppieCarrier carrier && carrier.phantasm_getPolyppie() != null)
				carrier.phantasm_getPolyppie().stopPlaying();
		});
	}


	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	@Deprecated
	public static <T> T log(T message) {
		return log(message, 0);
	}

	public static <T> T log(T message, int level) {
		if (level == 3 && FabricLoader.getInstance().isDevelopmentEnvironment())
			level = 1;

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


	public static boolean isFarmersDelightLoaded() {
		return FabricLoader.getInstance().isModLoaded("farmersdelight");
	}

	public static boolean isVinURLLoaded() {
		return FabricLoader.getInstance().isModLoaded("vinurl");
	}
}