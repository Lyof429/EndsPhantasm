package net.lyof.phantasm.world.biome.surface;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.mixin.NoiseGeneratorSettingsAccess;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
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

    public static MaterialRules.MaterialRule createDreamingDenRule() {
        MaterialRules.MaterialCondition dreaming_den = MaterialRules.biome(ModBiomes.DREAMING_DEN);
        MaterialRules.MaterialCondition dreaming_den_noise =
                MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, -0.2);

        MaterialRules.MaterialCondition band_noise =
                MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0);

        MaterialRules.MaterialCondition band_y_below = MaterialRules.verticalGradient("obsidian_stripes_below1",
                YOffset.fixed(40), YOffset.fixed(42));
        MaterialRules.MaterialCondition band_y_above = MaterialRules.not(MaterialRules.verticalGradient("obsidian_stripes_above1",
                YOffset.fixed(35), YOffset.fixed(37)));

        MaterialRules.MaterialCondition band_y_below2 = MaterialRules.verticalGradient("obsidian_stripes_below2",
                YOffset.fixed(32), YOffset.fixed(34));
        MaterialRules.MaterialCondition band_y_above2 = MaterialRules.not(MaterialRules.verticalGradient("obsidian_stripes_above2",
                YOffset.fixed(27), YOffset.fixed(29)));

        MaterialRules.MaterialCondition band_y_below3 = MaterialRules.verticalGradient("obsidian_stripes_below3",
                YOffset.fixed(24), YOffset.fixed(26));
        MaterialRules.MaterialCondition band_y_above3 = MaterialRules.not(MaterialRules.verticalGradient("obsidian_stripes_above3",
                YOffset.fixed(19), YOffset.fixed(21)));



        return MaterialRules.sequence(
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
                ),

                MaterialRules.condition(
                    dreaming_den,
                    MaterialRules.condition(
                        dreaming_den_noise,
                        MaterialRules.condition(
                            MaterialRules.STONE_DEPTH_FLOOR,
                            MaterialRules.condition(
                                MaterialRules.aboveY(YOffset.aboveBottom(50), 0),
                                    VIVID_NIHILIUM
                            )
                        )
                    )
                )
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
