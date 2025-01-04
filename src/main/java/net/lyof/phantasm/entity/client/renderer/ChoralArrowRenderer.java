package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
import net.lyof.phantasm.entity.custom.ChoralArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ChoralArrowRenderer extends ProjectileEntityRenderer<ChoralArrowEntity> {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/choral_arrow.png");

    public ChoralArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ChoralArrowEntity entity) {
        return TEXTURE;
    }
}
