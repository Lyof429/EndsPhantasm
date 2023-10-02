package net.lyof.phantasm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.item.ModItemGroups;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Phantasm implements ModInitializer {
	public static final String MOD_ID = "phantasm";
    public static final Logger LOGGER = LoggerFactory.getLogger("End's Phantasm");

	@Override
	public void onInitialize() {
		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();
	}

	public static Identifier makeID(String id) {
		return new Identifier(MOD_ID, id);
	}

	public static <T> T log(T message) {
		LOGGER.info(String.valueOf(message));
		return message;
	}
}