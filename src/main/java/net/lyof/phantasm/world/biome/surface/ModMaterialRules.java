package net.lyof.phantasm.world.biome.surface;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.mixin.access.ChunkGeneratorSettingsAccessor;
import net.lyof.phantasm.world.biome.EndDataCompat;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {
    private static MaterialRules.MaterialRule block(Block b) {
        return MaterialRules.block(b.getDefaultState());
    }

    private static final MaterialRules.MaterialRule RAW_PURPUR = block(ModBlocks.RAW_PURPUR);

    public static MaterialRules.MaterialRule createPhantasmRules() {
        MaterialRules.MaterialCondition band_noise =
                MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0);

        int raw_purpur_offset = EndDataCompat.getCompatibilityMode().equals("endercon") ? 20 : 0;
        raw_purpur_offset += ConfigEntries.rawPurpurOffset;
        MaterialRules.MaterialCondition band_y_below = MaterialRules.verticalGradient("raw_purpur_stripes_below1",
                YOffset.fixed(raw_purpur_offset + 40), YOffset.fixed(raw_purpur_offset + 42));
        MaterialRules.MaterialCondition band_y_above = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above1",
                YOffset.fixed(raw_purpur_offset + 35), YOffset.fixed(raw_purpur_offset + 37)));

        MaterialRules.MaterialCondition band_y_below2 = MaterialRules.verticalGradient("raw_purpur_stripes_below2",
                YOffset.fixed(raw_purpur_offset + 32), YOffset.fixed(raw_purpur_offset + 34));
        MaterialRules.MaterialCondition band_y_above2 = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above2",
                YOffset.fixed(raw_purpur_offset + 27), YOffset.fixed(raw_purpur_offset + 29)));

        MaterialRules.MaterialCondition band_y_below3 = MaterialRules.verticalGradient("raw_purpur_stripes_below3",
                YOffset.fixed(raw_purpur_offset + 24), YOffset.fixed(raw_purpur_offset + 26));
        MaterialRules.MaterialCondition band_y_above3 = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above3",
                YOffset.fixed(raw_purpur_offset + 19), YOffset.fixed(raw_purpur_offset + 21)));


        // RAW PURPUR RULES
        MaterialRules.MaterialRule raw_purpur_stripes =
                MaterialRules.condition(MaterialRules.not(MaterialRules.biome(BiomeKeys.THE_END)),
                        MaterialRules.sequence(
                        MaterialRules.condition(
                                band_y_above,
                                MaterialRules.condition(band_y_below,
                                        MaterialRules.condition(MaterialRules.noiseThreshold(NoiseParametersKeys.VEGETATION,
                                                        0.1),
                                                RAW_PURPUR
                                        )
                                )
                        ), MaterialRules.condition(
                                band_y_above2,
                                MaterialRules.condition(band_y_below2,
                                        MaterialRules.condition(MaterialRules.noiseThreshold(NoiseParametersKeys.CALCITE,
                                                        0),
                                                RAW_PURPUR
                                        )
                                )
                        ), MaterialRules.condition(
                                band_y_above3,
                                MaterialRules.condition(band_y_below3,
                                        MaterialRules.condition(band_noise,
                                                RAW_PURPUR
                                        )
                                )
                        )
                )
        );


        return ConfigEntries.doRawPurpur ?
                raw_purpur_stripes : MaterialRules.sequence();
    }

    public static void addModMaterialRules(MinecraftServer server, RegistryKey<DimensionOptions> dimensionKey) {
        DimensionOptions levelStem = server.getCombinedDynamicRegistries().getCombinedRegistryManager()
                .get(RegistryKeys.DIMENSION).get(dimensionKey);
        if (levelStem == null) {
            Phantasm.LOGGER.error("Couldn't find the End noise generation provider");
            return;
        }

        ChunkGenerator chunkGenerator = levelStem.chunkGenerator();
        boolean hasEndBiomes = chunkGenerator.getBiomeSource().getBiomes().stream().anyMatch(
                biomeHolder -> biomeHolder.getKey().orElseThrow().getValue().getNamespace().equals(Phantasm.MOD_ID));
        if (hasEndBiomes) {
            if (chunkGenerator instanceof NoiseChunkGenerator noiseGenerator) {
                ChunkGeneratorSettings settings = noiseGenerator.getSettings().value();
                ((ChunkGeneratorSettingsAccessor) (Object) settings).addSurfaceRule(
                        MaterialRules.sequence(
                            ModMaterialRules.createPhantasmRules(), settings.surfaceRule()
                        )
                );
                Phantasm.log("Successfully added Surface Rules for the End");
            }
        }
    }
}
