package net.lyof.phantasm.screen.access;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.lyof.phantasm.entity.access.PolyppieCarrier;
import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.lyof.phantasm.setup.ModPackets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class PolyppieInventory implements Inventory {
    private final PolyppieCarrier owner;

    public PolyppieInventory(PolyppieCarrier owner) {
        this.owner = owner;
    }

    @Override
    public void markDirty() {
        PolyppieEntity polyppie = this.owner.phantasm_getPolyppie();
        if (polyppie != null)
            polyppie.stopPlaying();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getStack(0).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if (this.owner.phantasm_getPolyppie() == null) return ItemStack.EMPTY;
        return this.owner.phantasm_getPolyppie().getStack();
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.removeStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = this.getStack(slot);
        if (!this.isEmpty())
            this.owner.phantasm_getPolyppie().setStack(ItemStack.EMPTY);
        this.markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        PolyppieEntity polyppie = this.owner.phantasm_getPolyppie();
        if (polyppie != null)
            polyppie.setStack(stack);
        this.markDirty();
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player instanceof PolyppieCarrier;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        PolyppieEntity polyppie = this.owner.phantasm_getPolyppie();
        return polyppie != null && polyppie.isValidDisc(stack);
    }

    @Override
    public void clear() {
        this.removeStack(0);
    }


    public interface Handler {
        int x = -32, y = 100;

        void phantasm_toggleVisibility();
        boolean phantasm_isOpen();

        boolean phantasm_isEnabled();

        int phantasm_getSlotX();
        int phantasm_getSlotY();

        static void onButtonClick(PlayerEntity player, int id) {
            if (player.getWorld().isClient()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(id);
                ClientPlayNetworking.send(ModPackets.POLYPPIE_CLIENT_UPDATE, buf);
            }

            if (player instanceof PolyppieCarrier carrier) {
                switch (id) {
                    case 0 -> carrier.phantasm_getPolyppie().togglePaused();
                    case 1 -> carrier.phantasm_getPolyppie().stopPlaying();
                }
            }
        }
    }
}