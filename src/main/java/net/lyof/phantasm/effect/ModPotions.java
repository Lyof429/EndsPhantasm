package net.lyof.phantasm.effect;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.item.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModPotions {
    public static void register() {
        Phantasm.log("Registering Potions for modid : " + Phantasm.MOD_ID);

        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.POME_SLICE, CORROSION);
        BrewingRecipeRegistry.registerPotionRecipe(CORROSION, Items.REDSTONE, LONG_CORROSION);
        BrewingRecipeRegistry.registerPotionRecipe(CORROSION, Items.GLOWSTONE_DUST, STRONG_CORROSION);
    }


    public static final Potion CORROSION = Registry.register(Registries.POTION, Phantasm.makeID("corrosion"),
            new Potion(new StatusEffectInstance(ModEffects.CORROSION, 3600)));

    public static final Potion LONG_CORROSION = Registry.register(Registries.POTION, Phantasm.makeID("long_corrosion"),
            new Potion("corrosion", new StatusEffectInstance(ModEffects.CORROSION, 9600)));

    public static final Potion STRONG_CORROSION = Registry.register(Registries.POTION, Phantasm.makeID("strong_corrosion"),
            new Potion("corrosion", new StatusEffectInstance(ModEffects.CORROSION, 1800, 1)));
}
