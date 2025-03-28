package net.lyof.phantasm.setup.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.data.server.tag.vanilla.VanillaGameEventTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.event.GameEvent;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagProvider extends VanillaGameEventTagProvider {

    public ModGameEventTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.GameEvents.BEHEMOTH_CAN_LISTEN).add(
                GameEvent.STEP,
                GameEvent.JUKEBOX_PLAY,
                GameEvent.JUKEBOX_STOP_PLAY,
                GameEvent.NOTE_BLOCK_PLAY
        );
    }
}