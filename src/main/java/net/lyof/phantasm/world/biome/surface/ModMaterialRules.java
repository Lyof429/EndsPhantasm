package net.lyof.phantasm.world.biome.surface;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.mixin.access.NoiseGeneratorSettingsAccess;
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

    private static final MaterialRules.MaterialRule VIVID_NIHILIUM = block(ModBlocks.VIVID_NIHILIUM);
    private static final MaterialRules.MaterialRule RAW_PURPUR = block(ModBlocks.RAW_PURPUR);
    private static final MaterialRules.MaterialRule OBLIVION = block(ModBlocks.OBLIVION);

    private static final MaterialRules.MaterialRule ACIDIC_NIHILIUM = block(ModBlocks.ACIDIC_NIHILIUM);
    private static final MaterialRules.MaterialRule ACIDIC_MASS = block(ModBlocks.ACIDIC_MASS);

    public static MaterialRules.MaterialRule createDreamingDenRule() {
        double min_noise = -0.4;

        MaterialRules.MaterialCondition is_dreaming_den = MaterialRules.biome(ModBiomes.DREAMING_DEN);
        MaterialRules.MaterialCondition is_acidburnt_abysses = MaterialRules.biome(ModBiomes.ACIDBURNT_ABYSSES);

        MaterialRules.MaterialCondition nihilium_noise_main =
                MaterialRules.noiseThreshold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD, min_noise, 0);
        MaterialRules.MaterialCondition nihilium_noise_sub =
                MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, -0.1);

        MaterialRules.MaterialCondition band_noise =
                MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0);

        MaterialRules.MaterialCondition band_y_below = MaterialRules.verticalGradient("raw_purpur_stripes_below1",
                YOffset.fixed(40), YOffset.fixed(42));
        MaterialRules.MaterialCondition band_y_above = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above1",
                YOffset.fixed(35), YOffset.fixed(37)));

        MaterialRules.MaterialCondition band_y_below2 = MaterialRules.verticalGradient("raw_purpur_stripes_below2",
                YOffset.fixed(32), YOffset.fixed(34));
        MaterialRules.MaterialCondition band_y_above2 = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above2",
                YOffset.fixed(27), YOffset.fixed(29)));

        MaterialRules.MaterialCondition band_y_below3 = MaterialRules.verticalGradient("raw_purpur_stripes_below3",
                YOffset.fixed(24), YOffset.fixed(26));
        MaterialRules.MaterialCondition band_y_above3 = MaterialRules.not(MaterialRules.verticalGradient("raw_purpur_stripes_above3",
                YOffset.fixed(19), YOffset.fixed(21)));


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

        // DREAMING DEN RULES
        MaterialRules.MaterialRule dreaming_den_nihilium = MaterialRules.condition(
                is_dreaming_den,
                MaterialRules.condition(
                    MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.condition(
                                MaterialRules.aboveY(YOffset.aboveBottom(50), 0),
                                VIVID_NIHILIUM
                        )
                )
        );

        MaterialRules.MaterialRule oblivion = MaterialRules.condition(
                MaterialRules.aboveY(YOffset.belowTop(220), 0),
                MaterialRules.condition(
                        MaterialRules.stoneDepth(2, false, VerticalSurfaceType.CEILING),
                        OBLIVION
                )
        );

        MaterialRules.MaterialRule dreaming_den = MaterialRules.sequence(
                MaterialRules.condition(
                        nihilium_noise_main,
                        dreaming_den_nihilium
                ),
                MaterialRules.condition(
                        MaterialRules.noiseThreshold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD, min_noise),
                        MaterialRules.condition(
                                nihilium_noise_sub,
                                dreaming_den_nihilium
                        )
                ),
                MaterialRules.condition(
                        is_dreaming_den,
                        MaterialRules.condition(
                                nihilium_noise_main,
                                oblivion
                        )
                ),
                MaterialRules.condition(
                        is_dreaming_den,
                        MaterialRules.condition(
                                nihilium_noise_sub,
                                oblivion
                        )
                )
        );

        // ACIDBURNT ABYSSES RULES
        MaterialRules.MaterialRule acidburnt_abysses_nihilium = MaterialRules.condition(
                is_acidburnt_abysses,
                MaterialRules.condition(
                        MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.condition(
                                MaterialRules.aboveY(YOffset.aboveBottom(50), 0),
                                ACIDIC_NIHILIUM
                        )
                )
        );

        MaterialRules.MaterialRule acidburnt_abysses_mass = MaterialRules.condition(
                is_acidburnt_abysses,
                MaterialRules.condition(
                        MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.condition(
                                MaterialRules.aboveY(YOffset.aboveBottom(50), 0),
                                ACIDIC_MASS
                        )
                )
        );


        MaterialRules.MaterialRule acidburnt_abysses = MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.noiseThreshold(NoiseParametersKeys.BADLANDS_PILLAR, 0.25),
                        acidburnt_abysses_mass
                ),
                MaterialRules.condition(
                        nihilium_noise_main,
                        acidburnt_abysses_nihilium
                ),
                MaterialRules.condition(
                        MaterialRules.noiseThreshold(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD, min_noise),
                        MaterialRules.condition(
                                nihilium_noise_sub,
                                acidburnt_abysses_nihilium
                        )
                )
        );


        return ConfigEntries.doRawPurpur ?
                MaterialRules.sequence(
                        dreaming_den,
                        acidburnt_abysses,
                        raw_purpur_stripes
                ) : MaterialRules.sequence(
                        dreaming_den,
                        acidburnt_abysses
        );
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
                ((NoiseGeneratorSettingsAccess) (Object) settings).addSurfaceRule(
                        MaterialRules.sequence(
                            ModMaterialRules.createDreamingDenRule(), settings.surfaceRule()
                        )
                );
                Phantasm.log("Successfully added Surface Rules for the End");
            }
        }
    }
}
