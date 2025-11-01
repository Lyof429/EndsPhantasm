package net.lyof.phantasm.entity.access;

import net.lyof.phantasm.entity.custom.PolyppieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface PolyppieCarrier {
    void setCarriedPolyppie(@Nullable PolyppieEntity polyppie);
    @Nullable PolyppieEntity getCarriedPolyppie();

    interface ScreenHandler {
        void openPolyppieInventory();
        boolean isPolyppieInventoryOpen();

        boolean isPolyppieInventoryEnabled();
    }

    class Inventory implements net.minecraft.inventory.Inventory {
        private final PolyppieCarrier owner;

        public Inventory(PolyppieCarrier owner) {
            this.owner = owner;
        }

        @Override
        public void markDirty() {
            PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
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
            if (this.owner.getCarriedPolyppie() == null) return ItemStack.EMPTY;
            return this.owner.getCarriedPolyppie().getStack();
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return this.removeStack(slot);
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack stack = this.getStack(slot);
            if (!this.isEmpty())
                this.owner.getCarriedPolyppie().setStack(ItemStack.EMPTY);
            this.markDirty();
            return stack;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
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
            PolyppieEntity polyppie = this.owner.getCarriedPolyppie();
            return polyppie != null && polyppie.isValidDisc(stack);
        }

        @Override
        public void clear() {
            this.removeStack(0);
        }
    }
}
