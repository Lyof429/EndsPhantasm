package net.lyof.phantasm.setup.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class BiomeConfig {
    public BiomeConfig(double w) {
        this.weight = w;
    }

    @Comment(value = "Should the biome generate")
    public boolean generate = true;
    @Comment(value = "Weight for the biomes's generation")
    public double weight;



    public static class DreamingDenConfig extends BiomeConfig {
        public DreamingDenConfig() {
            super(2.5);
        }

        @Comment(value = "Noise threshold for Nihilium generation")
        public double nihilium_noise = -0.2;
    }
}
