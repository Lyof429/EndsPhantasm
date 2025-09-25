package net.lyof.phantasm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.lyof.phantasm.item.custom.*;
import net.lyof.phantasm.setup.ModRegistry;
import net.lyof.phantasm.setup.ModTags;
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
    }


    public static final Item PREAM_BERRY = ModRegistry.ofItem("pream_berry",
            new DescribedItem(new FabricItemSettings().food(ModRegistry.Foods.PREAM_BERRY))).model().build();
    public static final Item OBLIFRUIT = ModRegistry.ofItem("oblifruit",
            new DescribedItem(new FabricItemSettings().food(ModRegistry.Foods.OBLIFRUIT))).model().build();

    public static final Item CRYSTALLINE_SHOVEL = ModRegistry.ofItem("crystalline_shovel",
            new ShovelItem(ModTiers.CRYSTALLINE, 2, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED, ItemTags.SHOVELS).build();
    public static final Item CRYSTALLINE_PICKAXE = ModRegistry.ofItem("crystalline_pickaxe",
            new PickaxeItem(ModTiers.CRYSTALLINE, 2, -2.8f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED, ItemTags.PICKAXES).build();
    public static final Item CRYSTALLINE_AXE = ModRegistry.ofItem("crystalline_axe",
            new AxeItem(ModTiers.CRYSTALLINE, 7, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED, ItemTags.AXES).build();
    public static final Item CRYSTALLINE_HOE = ModRegistry.ofItem("crystalline_hoe",
            new HoeItem(ModTiers.CRYSTALLINE, 0, -3f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED, ItemTags.HOES).build();
    public static final Item CRYSTALLINE_SWORD = ModRegistry.ofItem("crystalline_sword",
            new SwordItem(ModTiers.CRYSTALLINE, 4, -2.4f, new FabricItemSettings()))
            .model(Models.HANDHELD).tag(ModTags.Items.XP_BOOSTED, ItemTags.SWORDS).build();

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
            .model().build();

    public static final Item BEHEMOTH_MEAT = ModRegistry.ofItem("behemoth_meat",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.BEHEMOTH_MEAT)))
            .model().build();
    public static final Item BEHEMOTH_STEAK = ModRegistry.ofItem("behemoth_steak",
            new Item(new FabricItemSettings().food(ModRegistry.Foods.BEHEMOTH_STEAK)))
            .model().build();

    public static final Item SHATTERED_PENDANT = ModRegistry.ofItem("shattered_pendant",
            new ShatteredPendantItem(new FabricItemSettings()))
            .model().build();

    public static final Item POME_SLICE = ModRegistry.ofItem("pome_slice",
            new PomeSliceItem(new FabricItemSettings().food(ModRegistry.Foods.POME_SLICE))).model().build();

    public static final Item MUSIC_DISC_ABRUPTION = ModRegistry.ofItem("music_disc_abruption",
            new MusicDiscItem(4, ModSounds.MUSIC_DISC_ABRUPTION, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE), 239))
            .model().tag(ItemTags.MUSIC_DISCS).build();

    public static final Item CHORAL_ARROW = ModRegistry.ofItem("choral_arrow",
            new ChoralArrowItem(new FabricItemSettings()))
            .model().tag(ItemTags.ARROWS).build();


    public static final Item CRYSTIE_SPAWN_EGG = ModRegistry.ofItem("crystie_spawn_egg",
            new SpawnEggItem(ModEntities.CRYSTIE, 0xfaf0ff, 0xa0a0ff, new FabricItemSettings()))
            .build();
    public static final Item BEHEMOTH_SPAWN_EGG = ModRegistry.ofItem("behemoth_spawn_egg",
            new SpawnEggItem(ModEntities.BEHEMOTH, 0xafa0ff, 0x0f000f, new FabricItemSettings()))
            .build();
}
