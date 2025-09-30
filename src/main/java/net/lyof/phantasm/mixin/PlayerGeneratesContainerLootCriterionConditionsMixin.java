package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lyof.phantasm.setup.datagen.fabric.ModAdvancementProvider;
import net.minecraft.advancement.criterion.PlayerGeneratesContainerLootCriterion;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerGeneratesContainerLootCriterion.Conditions.class)
public class PlayerGeneratesContainerLootCriterionConditionsMixin {
    @Shadow @Final private Identifier lootTable;

    @WrapMethod(method = "test")
    private boolean testSynthetic(Identifier lootTable, Operation<Boolean> original) {
        if (this.lootTable.equals(ModAdvancementProvider.SYNTHETIC_CHALLENGE))
            return lootTable.getPath().startsWith("chests/challenges/");
        return original.call(lootTable);
    }
}
