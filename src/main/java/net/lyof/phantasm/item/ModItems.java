package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.lyof.phantasm.item.custom.*;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.compat.FarmersDelightCompat;
import net.lyof.phantasm.sound.ModSounds;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.data.client.Models;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ModItems {
    public static void register() {
        DispenserBlock.registerBehavior(CHORAL_ARROW, new ProjectileDispenserBehavior() {
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ArrowEntity arrowEntity = ChoralArrowEntity.create(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });

        if (Phantasm.isFarmersDelightLoaded())
            FarmersDelightCompat.register();
    }


    public static final Item PREAM_BERRY = ModRegistry.ofItem("pream_berry",
            new DescribedItem(new FabricItemSettings().food(ModRegistry.Foods.PREAM_BERRY)))
            .tag(ConventionalItemTags.FOODS).model().build();
    public static final Item OBLIFRUIT = ModRegistry.ofItem("oblifruit",
            new DescribedItem(new FabricItemSettings().food(ModRegistry.Foods.OBLIFRUIT)))
            .tag(ConventionalItemTags.FOODS).model().build();

    public static final Item CRYSTALLINE_SHOVEL = ModRegistry.ofItem("crystalline_shovel",
            new ShovelItem(ModTiers.CRYSTALLINE, 2, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.SHOVELS).build();
    public static final Item CRYSTALLINE_PICKAXE = ModRegistry.ofItem("crystalline_pickaxe",
            new PickaxeItem(ModTiers.CRYSTALLINE, 2, -2.8f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.PICKAXES).build();
    public static final Item CRYSTALLINE_AXE = ModRegistry.ofItem("crystalline_axe",
            new AxeItem(ModTiers.CRYSTALLINE, 7, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.AXES).build();
    public static final Item CRYSTALLINE_HOE = ModRegistry.ofItem("crystalline_hoe",
            new HoeItem(ModTiers.CRYSTALLINE, 0, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.HOES).build();
    public static final Item CRYSTALLINE_SWORD = ModRegistry.ofItem("crystalline_sword",
            new SwordItem(ModTiers.CRYSTALLINE, 4, -2.4f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.SWORDS).build();

    public static Item CRYSTALLINE_KNIFE = null;

    /*public static final Item REALITY_BREAKER = ModRegistry.ofItem("reality_breaker",
            new RealityBreakerItem(ModTiers.RELIC, 1, -3.2f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ItemTags.TOOLS, ItemTags.PICKAXES).build();*/

    public static final Item PREAM_SIGN = ModRegistry.ofItem("pream_sign",
            new SignItem(new FabricItemSettings(), ModBlocks.PREAM_SIGN, ModBlocks.PREAM_WALL_SIGN))
            .fuel(200)
            .build();
    public static final Item PREAM_HANGING_SIGN = ModRegistry.ofItem("pream_hanging_sign",
            new HangingSignItem(ModBlocks.PREAM_HANGING_SIGN, ModBlocks.PREAM_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200)
            .build();

    public static final Item CHORUS_FRUIT_SALAD = ModRegistry.ofItem("chorus_fruit_salad",
            new ChorusFruitSaladItem(new FabricItemSettings().food(ModRegistry.Foods.CHORUS_SALAD).recipeRemainder(Items.BOWL)
                    .maxCount(ConfigEntries.chorusSaladStack)))
            .tag(ConventionalItemTags.FOODS)
            .model().build();

    public static final Item BEHEMOTH_MEAT = ModRegistry.ofItem("behemoth_meat",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.BEHEMOTH_MEAT)))
            .tag(ConventionalItemTags.FOODS)
            .model().build();
    public static final Item BEHEMOTH_STEAK = ModRegistry.ofItem("behemoth_steak",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.BEHEMOTH_STEAK)))
            .tag(ConventionalItemTags.FOODS)
            .model().build();

    public static final Item SHATTERED_PENDANT = ModRegistry.ofItem("shattered_pendant",
            new ShatteredPendantItem(new FabricItemSettings()))
            .model().build();

    public static final Item POME_SLICE = ModRegistry.ofItem("pome_slice",
            new PomeSliceItem(new FabricItemSettings().food(ModRegistry.Foods.POME_SLICE)))
            .tag(ConventionalItemTags.FOODS).model().build();

    public static final Item MUSIC_DISC_ABRUPTION = ModRegistry.ofItem("music_disc_abruption",
            new MusicDiscItem(4, ModSounds.MUSIC_DISC_ABRUPTION, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE), 239))
            .model().tag(ItemTags.MUSIC_DISCS).build();

    public static final Item CHORAL_ARROW = ModRegistry.ofItem("choral_arrow",
            new ChoralArrowItem(new FabricItemSettings()))
            .model().tag(ItemTags.ARROWS).build();

    public static final Item EGGS_NIHILO = ModRegistry.ofItem("eggs_nihilo",
            new EggsNihiloBlockItem(ModBlocks.EGGS_NIHILO, new FabricItemSettings().food(ModRegistry.Foods.EGGS_NIHILO).rarity(Rarity.EPIC)))
            .model().build();


    public static final Item CRYSTIE_SPAWN_EGG = ModRegistry.ofItem("crystie_spawn_egg",
            new SpawnEggItem(ModEntities.CRYSTIE, 0xB7C6E2, 0x5E58A0, new FabricItemSettings()))
            .build();
    public static final Item BEHEMOTH_SPAWN_EGG = ModRegistry.ofItem("behemoth_spawn_egg",
            new SpawnEggItem(ModEntities.BEHEMOTH, 0x60548D, 0x1C1947, new FabricItemSettings()))
            .build();
    public static final Item POLYPPIE_SPAWN_EGG = ModRegistry.ofItem("polyppie_spawn_egg",
                    new SpawnEggItem(ModEntities.POLYPPIE, 0xCD98BC, 0x9A4E99, new FabricItemSettings()))
            .build();
}
