package net.lyof.phantasm.entity.custom;

import com.vinurl.util.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.block.ModBlocks;
import net.lyof.phantasm.entity.ModEntities;
import net.lyof.phantasm.entity.client.SongHandler;
import net.lyof.phantasm.setup.ModPackets;
import net.lyof.phantasm.sound.custom.PolyppieSoundInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class PolyppieEntity extends PassiveEntity {
    private static final TrackedData<ItemStack> ITEM_STACK = DataTracker.registerData(PolyppieEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    //private static final TrackedData<Integer> SOUND_KEY = DataTracker.registerData(PolyppieEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static int OFFSET = 0;

    protected boolean isPlaying;
    protected long tickCount;
    protected long recordStartTick;
    protected int ticksThisSecond;

    protected int soundKey;

    public PolyppieEntity(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.POLYPPIE.create(world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ARMOR, 4);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ITEM_STACK, ItemStack.EMPTY);
        //this.getDataTracker().startTracking(SOUND_KEY, -1);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (!this.getStack().isEmpty()) {
            nbt.put("RecordItem", this.getStack().writeNbt(new NbtCompound()));
        }

        nbt.putBoolean("IsPlaying", this.isPlaying);
        //nbt.putLong("RecordStartTick", this.recordStartTick);
        nbt.putLong("TickCount", this.tickCount - this.recordStartTick);

        nbt.putInt("SoundKey", this.getSoundKey());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("RecordItem", 10))
            this.setStack(ItemStack.fromNbt(nbt.getCompound("RecordItem")));

        this.isPlaying = nbt.getBoolean("IsPlaying");
        this.recordStartTick = 0;//nbt.getLong("RecordStartTick");
        this.tickCount = nbt.getLong("TickCount");

        this.setSoundKey(nbt.getInt("SoundKey"));
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.getSoundKey());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setSoundKey(packet.getEntityData());

        if (this.getWorld().isClient()) {
            PolyppieSoundInstance soundInstance = SongHandler.instance.get(this.getSoundKey());
            if (soundInstance != null) soundInstance.update(this);
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return this.getHeight();
    }

    public ItemStack getStack() {
        return this.getDataTracker().get(ITEM_STACK);
    }

    public void setStack(ItemStack stack) {
        this.getDataTracker().set(ITEM_STACK, stack);
    }

    public void initSoundKey() {
        if (this.getSoundKey() <= 0)
            this.setSoundKey((int) (System.currentTimeMillis() % 10000) + OFFSET++);
    }

    public int getSoundKey() {
        return this.soundKey; //this.getDataTracker().get(SOUND_KEY);
    }

    public void setSoundKey(int soundKey) {
        this.soundKey = soundKey; //this.getDataTracker().set(SOUND_KEY, soundKey);
    }

    public boolean isValid(ItemStack stack) {
        return stack.isIn(ItemTags.MUSIC_DISCS) && this.getStack().isEmpty();
    }

    public boolean isPlayingRecord() {
        return !this.getStack().isEmpty() && this.isPlaying;
    }

    protected void sendPacket(boolean start) {
        if (!this.getWorld().isClient()) {
            this.initSoundKey();

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(start ? this.getStack().writeNbt(new NbtCompound()) : new NbtCompound());
            buf.writeInt(this.getId());
            buf.writeInt(this.getSoundKey());
            for (ServerPlayerEntity player : PlayerLookup.tracking(this))
                ServerPlayNetworking.send(player, ModPackets.POLYPPIE_UPDATES, buf);
        }
    }

    public void startPlaying() {
        this.recordStartTick = this.tickCount;
        this.isPlaying = true;
        this.getWorld().emitGameEvent(GameEvent.JUKEBOX_PLAY, this.getPos(), GameEvent.Emitter.of(this));

        this.sendPacket(true);
    }

    public void stopPlaying() {
        this.isPlaying = false;
        this.getWorld().emitGameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.getPos(), GameEvent.Emitter.of(this));

        this.sendPacket(false);
    }

    public boolean isSongFinished(MusicDiscItem disc) {
        if (Phantasm.isVinURLLoaded() && getStack().getTranslationKey().equals("item.vinurl.custom_record")) {
            NbtCompound nbt = getStack().getOrCreateNbt();
            return this.tickCount > this.recordStartTick + nbt.getInt(Constants.DURATION_KEY) * 20L;
        }
        return this.tickCount >= this.recordStartTick + (long) disc.getSongLengthInTicks() + 20L;
    }

    public void spawnNoteParticle() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d vec3d = this.getPos().add(0.0, 1.2, 0.0);
            serverWorld.spawnParticles(ParticleTypes.NOTE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0,
                    serverWorld.getRandom().nextInt(4) / 24.0f, 0.0, 0.0, 1.0);
        }
    }

    private boolean hasSecondPassed() {
        return this.ticksThisSecond >= 20;
    }

    @Override
    public boolean isPersistent() {
        return super.isPersistent() || !this.getStack().isEmpty();
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        this.stopPlaying();
        this.dropStack(this.getStack());
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isOf(ModBlocks.CHORAL_BLOCK.asItem()) && !this.hasPassengers()) {
            PolyppieEntity polyppie = ModEntities.POLYPPIE.create(this.getWorld());
            polyppie.setPosition(this.getPos().add(0, 1, 0));
            polyppie.startRiding(this, true);
            this.getWorld().spawnEntity(polyppie);

            return ActionResult.success(player.getWorld().isClient());
        }

        if (!this.isValid(stack)) {
            if (!this.getStack().isEmpty()) {
                this.stopPlaying();
                this.dropStack(this.getStack());
                this.setStack(ItemStack.EMPTY);
                return ActionResult.success(player.getWorld().isClient());
            }
            return ActionResult.PASS;
        }

        this.setStack(stack.copyWithCount(1));
        stack.decrement(1);
        return ActionResult.success(player.getWorld().isClient());
    }

    @Override
    public void tick() {
        super.tick();

        this.ticksThisSecond++;

        if (this.isPlayingRecord()) {
            if (this.getStack().getItem() instanceof MusicDiscItem disc) {
                if (this.isSongFinished(disc)) {
                    this.stopPlaying();
                } else if (this.hasSecondPassed()) {
                    this.ticksThisSecond = 0;
                    this.getWorld().emitGameEvent(GameEvent.JUKEBOX_PLAY, this.getPos(), GameEvent.Emitter.of(this));
                    this.spawnNoteParticle();
                }
            }
        }


        else if (this.tickCount % 20 == 0 && !this.hasVehicle()) {
            Band band = new Band(this);
            if (band.getPlaying() == null)
                band.startRandom(this.getRandom());
        }

        this.tickCount++;
    }


    public static class Band {
        protected List<PolyppieEntity> members = new ArrayList<>();

        public Band(PolyppieEntity base) {
            this.members = base.getRootVehicle().streamPassengersAndSelf().filter(e -> e instanceof PolyppieEntity)
                    .map(e -> (PolyppieEntity) e).toList();
        }

        public void add(PolyppieEntity polyppie) {
            this.members.add(polyppie);
        }

        public void startRandom(Random random) {
            List<PolyppieEntity> singers = new ArrayList<>();
            for (PolyppieEntity polyppie : this.members) {
                if (!polyppie.getStack().isEmpty())
                    singers.add(polyppie);
            }
            if (singers.isEmpty())
                return;
            singers.get(random.nextInt(singers.size())).startPlaying();
        }

        public PolyppieEntity getPlaying() {
            PolyppieEntity singer = null;
            for (PolyppieEntity polyppie : this.members) {
                if (polyppie.isPlayingRecord())
                    singer = polyppie;
            }
            return singer;
        }
    }
}
