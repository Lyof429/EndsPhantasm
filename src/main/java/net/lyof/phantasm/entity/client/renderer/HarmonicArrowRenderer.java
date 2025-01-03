package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.HarmonicArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class HarmonicArrowRenderer extends ProjectileEntityRenderer<HarmonicArrowEntity> {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/harmonic_arrow.png");

    public HarmonicArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(HarmonicArrowEntity entity) {
        return TEXTURE;
    }
}
