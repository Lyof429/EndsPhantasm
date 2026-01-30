package net.lyof.phantasm.item;

import net.fabricmc.yarn.constants.MiningLevels;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum ModTiers implements ToolMaterial {

    CRYSTALLINE(MiningLevels.IRON, ConfigEntries.crystallineDurability, 8f, 1f, 17,
            Ingredient.ofItems(ModBlocks.CRYSTAL_SHARD, ModBlocks.VOID_CRYSTAL_SHARD))/*,
    RELIC(MiningLevels.NETHERITE, 3145, 10f, 4f, 0,
            Ingredient.empty())*/
    ;

    private final int durability;
    private final float speed;
    private final float damage;
    private final int miningLevel;
    private final int enchantability;
    private final Ingredient repair;

    ModTiers(int miningLevel, int durability, float speed, float damage, int enchantability, Ingredient repair) {
        this.durability = durability;
        this.speed = speed;
        this.damage = damage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repair = repair;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.speed;
    }

    @Override
    public float getAttackDamage() {
        return this.damage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repair;
    }
}
