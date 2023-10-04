package net.lyof.phantasm.item;

import net.fabricmc.yarn.constants.MiningLevels;
import net.lyof.phantasm.block.ModBlocks;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum ModTiers implements ToolMaterial {

    CRYSTALLINE(MiningLevels.IRON, 312, 8f, 1f, 17,
            Ingredient.ofItems(ModBlocks.CRYSTAL_SHARD, ModBlocks.VOID_CRYSTAL_SHARD)),
    STELLIUM(MiningLevels.NETHERITE, 1014, 8f, 5f, 13,
            Ingredient.empty())
    ;

    private final int durability;
    private final float speed;
    private final float damage;
    private final int mining_level;
    private final int enchantability;
    private final Ingredient repair;

    ModTiers(int mining_level, int durability, float speed, float damage, int enchantability, Ingredient repair) {
        this.durability = durability;
        this.speed = speed;
        this.damage = damage;
        this.mining_level = mining_level;
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
        return this.mining_level;
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
