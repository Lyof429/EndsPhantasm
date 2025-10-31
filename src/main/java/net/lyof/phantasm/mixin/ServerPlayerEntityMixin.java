package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.util.MixinAccess;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class, priority = 1001)
public abstract class ServerPlayerEntityMixin extends Entity implements MixinAccess<Boolean> {
    public ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ServerWorld getServerWorld();
    @Shadow public abstract PlayerAdvancementTracker getAdvancementTracker();
    @Shadow private boolean seenCredits;

    @WrapOperation(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;createEndSpawnPlatform(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    public void noPlatform(ServerPlayerEntity instance, ServerWorld world, BlockPos centerPos, Operation<Void> original) {
        original.call(instance, world, ServerWorld.END_SPAWN_POS.mutableCopy());
    }

    @Unique private static final Identifier CREDITS_ADVANCEMENT = new Identifier("minecraft", "end/kill_dragon");

    @Unique private boolean seenBeginning = false;

    @WrapMethod(method = "moveToWorld")
    public Entity cancelCredits(ServerWorld destination, Operation<Entity> original) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        self.notInAnyWorld = false;

        if (destination.getRegistryKey() == World.END && !this.seenBeginning) {
            this.seenBeginning = true;

            if (ConfigEntries.beginCutscene) {
                self.detach();
                this.getServerWorld().removePlayer(self, RemovalReason.CHANGED_DIMENSION);
                self.notInAnyWorld = true;
                ServerPlayNetworking.send(self, ModPackets.BEGIN_CUTSCENE_STARTS, PacketByteBufs.empty());

                return this;
            }
        }

        Advancement advancement = this.getServerWorld().getServer().getAdvancementLoader().get(CREDITS_ADVANCEMENT);
        if (this.getServerWorld().getRegistryKey() == World.END && destination.getRegistryKey() == World.OVERWORLD
                && advancement != null && !this.getAdvancementTracker().getProgress(advancement).isDone()) {

            this.seenCredits = true;
            Entity result = original.call(destination);
            this.seenCredits = false;
            return result;
        }

        return original.call(destination);
    }

    @Unique private static final String SEEN_BEGINNING_KEY = Phantasm.MOD_ID + "_SeenBeginning";

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(SEEN_BEGINNING_KEY)) this.seenBeginning = nbt.getBoolean(SEEN_BEGINNING_KEY);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean(SEEN_BEGINNING_KEY, this.seenBeginning);
    }

    @Inject(method = "copyFrom", at = @At("HEAD"))
    private void copySeenBeginning(ServerPlayerEntity old, boolean alive, CallbackInfo ci) {
        this.seenBeginning = ((MixinAccess<Boolean>) old).getMixinValue();

        if (this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY) && old instanceof PolyppieCarrier oldCarrier
                && this instanceof PolyppieCarrier carrier && oldCarrier.getCarriedPolyppie() != null) {
            //oldCarrier.getCarriedPolyppie().stopPlaying();
            carrier.setCarriedPolyppie(oldCarrier.getCarriedPolyppie());
        }
    }

    @Override
    public void setMixinValue(Boolean value) {
        this.seenBeginning = value;
    }

    @Override
    public Boolean getMixinValue() {
        return this.seenBeginning;
    }
}
