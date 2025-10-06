package net.lyof.phantasm.util;

public interface MixinAccess<T> {
    void setMixinValue(T value);
    T getMixinValue();
}
