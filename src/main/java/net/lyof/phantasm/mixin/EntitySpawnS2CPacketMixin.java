package net.lyof.phantasm.mixin;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntitySpawnS2CPacket.class)
public class EntitySpawnS2CPacketMixin implements PolyppieEntity.PolyppieSpawnPacket {
    @Unique private Identifier variant;
    @Unique private int songKey;
    @Unique private boolean isPolyppiePacket = false;

    @Override
    public void setPolyppieData(Identifier id, int key) {
        this.variant = id;
        this.songKey = key;
    }

    @Override
    public Identifier getVariant() {
        return this.variant;
    }

    @Override
    public int getSongKey() {
        return this.songKey;
    }

    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("TAIL"))
    private void read(PacketByteBuf buf, CallbackInfo ci) {
        boolean isEdited = buf.readBoolean();
        if (isEdited) {
            this.songKey = buf.readInt();
            this.variant = buf.readIdentifier();
        }
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void write(PacketByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(this.isPolyppiePacket);
        if (this.isPolyppiePacket) {
            buf.writeInt(this.songKey);
            buf.writeIdentifier(this.variant);
        }
    }
}
