package net.lyof.phantasm.setup.compat;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class MixinSquaredCompat implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targets, String mixin) {
        if (mixin.equals("net.lyof.phantasm.mixin.PlayerScreenHandlerMixinSquared")) {
            try {
                Class.forName("dev.emi.trinkets.api.TrinketsApi");
            } catch (ClassNotFoundException e) {
                return true;
            }
            return false;
        }
        return false;
    }
}
