package net.lyof.phantasm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.config.ConfigEntries;
import net.lyof.phantasm.effect.ModEffects;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.access.Challenger;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Challenger, PolyppieCarrier {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
	private float modifyBreakSpeed(float original, BlockState state) {
		PlayerEntity self = ((PlayerEntity) (Object) this);

		ItemStack stack = self.getMainHandStack();
		if (!stack.isIn(ModTags.Items.XP_BOOSTED) || !stack.getItem().isSuitableFor(state)) return original;

		float bonus = 1 + (float) ConfigEntries.crystallineXpBoost * self.experienceLevel / 100f;

		return original * bonus;
	}

	@Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
	public void charmMovementSpeed(CallbackInfoReturnable<Float> cir) {
		if (this.hasStatusEffect(ModEffects.CHARM)) cir.setReturnValue(0f);
	}


	@Override
	public PlayerEntity asPlayer() {
		return (PlayerEntity) (Object) this;
	}


	@Unique private static final String POLYPPIE_KEY = Phantasm.MOD_ID + "_CarriedPolyppie";
	@Unique private static final TrackedData<NbtCompound> POLYPPIE = DataTracker.registerData(PlayerEntityMixin.class,
			TrackedDataHandlerRegistry.NBT_COMPOUND);
	@Unique private PolyppieEntity polyppie = null;

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	private void initPolyppieDataTracker(CallbackInfo ci) {
		this.getDataTracker().startTracking(POLYPPIE, new NbtCompound());
	}

	@Override
	public void phantasm_setPolyppie(PolyppieEntity polyppie) {
		this.polyppie = polyppie;

		if (polyppie == null)
			this.getDataTracker().set(POLYPPIE, new NbtCompound());
		else {
			this.polyppie.setYaw(180);
			this.polyppie.remove(RemovalReason.UNLOADED_WITH_PLAYER);
			this.getDataTracker().set(POLYPPIE, this.polyppie.writeNbt(new NbtCompound()));
		}
	}

	@Override
	public PolyppieEntity phantasm_getPolyppie() {
		if (!this.getDataTracker().get(POLYPPIE).isEmpty() && this.getWorld() != null && this.polyppie == null) {
			this.polyppie = ModEntities.POLYPPIE.create(this.getWorld());
			this.polyppie.readNbt(this.getDataTracker().get(POLYPPIE));
			this.polyppie.setOwner((PlayerEntity) (Object) this);
			this.polyppie.remove(RemovalReason.UNLOADED_WITH_PLAYER);
		}
		return this.polyppie;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeCustom(NbtCompound nbt, CallbackInfo ci) {
		if (this.phantasm_getPolyppie() != null)
			nbt.put(POLYPPIE_KEY, this.phantasm_getPolyppie().writeNbt(new NbtCompound()));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readCustom(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains(POLYPPIE_KEY, 10)) {
			this.getDataTracker().set(POLYPPIE, nbt.getCompound(POLYPPIE_KEY));
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickMixins(CallbackInfo ci) {
		if (this.phantasm_getPolyppie() != null) {
			if (this.polyppie.getWorld() != this.getWorld())
				this.polyppie.setWorld(this.getWorld());

			if (this.polyppie.tickCount % 10 == 0)
				this.polyppie.setPosition(this.getPos().add(0, 1, 0));

			this.polyppie.tick();
		}
/*
		if (this.age % 20 == 0 && this.getChallengeRune() != null && this.getWorld() instanceof ServerWorld server) {
			ChunkPos chunk = server.getChunk(this.getChallengeRune().getPos()).getPos();
			server.setChunkForced(chunk.x, chunk.z, this.getChallengeRune().isChallengeRunning());
		}*/
	}

	@Inject(method = "dropInventory", at = @At("HEAD"))
	private void dropCarriedPolyppie(CallbackInfo ci) {
		if (!this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY) && this.phantasm_getPolyppie() != null)
			this.phantasm_getPolyppie().setCarriedBy((PlayerEntity) (Object) this, this.getPos());
	}
}