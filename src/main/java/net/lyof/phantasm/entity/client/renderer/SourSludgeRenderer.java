package net.lyof.phantasm.entity.client.renderer;

import net.lyof.phantasm.Phantasm;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MagmaCubeEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

public class SourSludgeRenderer extends SlimeEntityRenderer {
    private static final Identifier TEXTURE = Phantasm.makeID("textures/entity/sour_sludge/sour_sludge.png");

    public SourSludgeRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SlimeEntity slimeEntity) {
        return TEXTURE;
    }
}
