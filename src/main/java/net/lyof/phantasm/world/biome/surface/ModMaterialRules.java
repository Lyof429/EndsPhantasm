package net.lyof.phantasm.world.biome.surface;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.mixin.NoiseGeneratorSettingsAccess;
import net.lyof.phantasm.world.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelStorage;

public class ModMaterialRules {
    private static MaterialRules.MaterialRule block(Block b) {
        return MaterialRules.block(b.getDefaultState());
    }

    private static final MaterialRules.MaterialRule VIVID_NIHILIUM = block(ModBlocks.VIVID_NIHILIUM_BLOCK);
    private static final MaterialRules.MaterialRule END_STONE = block(Blocks.END_STONE);

    public static MaterialRules.MaterialRule createDreamingDenRule() {
        MaterialRules.MaterialCondition dreaming_den = MaterialRules.biome(ModBiomes.DREAMING_DEN);
        MaterialRules.MaterialCondition noise = MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE_SWAMP, -0.15);
        //MaterialRules.MaterialCondition surface = MaterialRules.stoneDepth();

        MaterialRules.MaterialRule result = MaterialRules.sequence(
                //END_STONE,
                MaterialRules.condition(MaterialRules.biome(ModBiomes.DREAMING_DEN),
                        MaterialRules.condition(noise,
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                                        VIVID_NIHILIUM)
                        ))
        );
/*
        MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.verticalGradient("bedrock_floor", YOffset.getBottom(), YOffset.aboveBottom(5)), BEDROCK),
                MaterialRules.condition(
                        MaterialRules.not(
                                MaterialRules.verticalGradient("bedrock_roof", YOffset.belowTop(5), YOffset.getTop())), BEDROCK),
                MaterialRules.condition(materialCondition5, NETHERRACK),
                MaterialRules.condition(MaterialRules.biome(BiomeKeys.BASALT_DELTAS),
                        MaterialRules.sequence(
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_CEILING_WITH_SURFACE_DEPTH, BASALT),
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        MaterialRules.sequence(materialRule,
                                                MaterialRules.condition(materialCondition12, BASALT), BLACKSTONE)))),
                MaterialRules.condition(
                        MaterialRules.biome(BiomeKeys.SOUL_SAND_VALLEY),
                        MaterialRules.sequence(
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_CEILING_WITH_SURFACE_DEPTH,
                                        MaterialRules.sequence(
                                                MaterialRules.condition(materialCondition12, SOUL_SAND), SOUL_SOIL)),
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        MaterialRules.sequence(materialRule,
                                                MaterialRules.condition(materialCondition12, SOUL_SAND), SOUL_SOIL)))),
                MaterialRules.condition(
                        MaterialRules.STONE_DEPTH_FLOOR,
                        MaterialRules.sequence(
                                MaterialRules.condition(
                                        MaterialRules.not(materialCondition2),
                                        MaterialRules.condition(materialCondition6, LAVA)),
                                MaterialRules.condition(
                                        MaterialRules.biome(BiomeKeys.WARPED_FOREST),
                                        MaterialRules.condition(
                                                MaterialRules.not(materialCondition10),
                                                MaterialRules.condition(materialCondition,
                                                        MaterialRules.sequence(
                                                                MaterialRules.condition(materialCondition11, WARPED_WART_BLOCK), WARPED_NYLIUM)))),
                                MaterialRules.condition(
                                        MaterialRules.biome(BiomeKeys.CRIMSON_FOREST),
                                        MaterialRules.condition(
                                                MaterialRules.not(materialCondition10),
                                                MaterialRules.condition(materialCondition,
                                                        MaterialRules.sequence(
                                                                MaterialRules.condition(materialCondition11, NETHER_WART_BLOCK), CRIMSON_NYLIUM)))))),
                MaterialRules.condition(
                        MaterialRules.biome(BiomeKeys.NETHER_WASTES),
                        MaterialRules.sequence(
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        MaterialRules.condition(materialCondition7,
                                                MaterialRules.sequence(
                                                        MaterialRules.condition(
                                                                MaterialRules.not(materialCondition6),
                                                                MaterialRules.condition(materialCondition3,
                                                                        MaterialRules.condition(materialCondition4, SOUL_SAND))), NETHERRACK))),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR,
                                        MaterialRules.condition(materialCondition,
                                                MaterialRules.condition(materialCondition4,
                                                        MaterialRules.condition(materialCondition8,
                                                                MaterialRules.sequence(
                                                                        MaterialRules.condition(materialCondition2, GRAVEL),
                                                                        MaterialRules.condition(
                                                                                MaterialRules.not(materialCondition6), GRAVEL)))))))), NETHERRACK);
*/
        return result;
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
