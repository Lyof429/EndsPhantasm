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
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
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

		float bonus = 1 + (float) ConfigEntries.crystalXPBoost * self.experienceLevel / 100f;

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


	@Unique private NbtCompound polyppieNbt = null;
	@Unique private PolyppieEntity polyppie = null;

	@Override
	public void setCarriedPolyppie(PolyppieEntity polyppie) {
		this.polyppie = polyppie;
	}

	@Override
	public PolyppieEntity getCarriedPolyppie() {
		return this.polyppie;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	private void writeCustom(NbtCompound nbt, CallbackInfo ci) {
		if (this.getCarriedPolyppie() != null) {
			nbt.put(Phantasm.MOD_ID + "_CarriedPolyppie", this.getCarriedPolyppie().writeNbt(new NbtCompound()));
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	private void readCustom(NbtCompound nbt, CallbackInfo ci) {/*
		if (nbt.contains(Phantasm.MOD_ID + "_CarriedPolyppie", 10)) {
			this.polyppieNbt = nbt.getCompound(Phantasm.MOD_ID + "_CarriedPolyppie");
		}*/
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickCarriedPolyppie(CallbackInfo ci) {
		if (this.polyppieNbt != null && this.polyppie == null) {
			this.polyppie = ModEntities.POLYPPIE.create(this.getWorld());
			this.polyppie.readNbt(this.polyppieNbt);
			this.polyppie.remove(RemovalReason.UNLOADED_WITH_PLAYER);
			this.polyppieNbt = null;
		}

		if (this.polyppie != null) {
			if (this.age % 20 == 0) {
				this.polyppie.setPosition(this.getPos().add(0, 1, 0));
			}
			this.polyppie.tick();
		}
	}
}