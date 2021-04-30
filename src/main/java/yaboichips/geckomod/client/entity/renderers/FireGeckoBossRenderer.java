package yaboichips.geckomod.client.entity.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.FireGeckoBossModel;
import yaboichips.geckomod.common.entities.FireGeckoBossEntity;

public class FireGeckoBossRenderer<T extends FireGeckoBossEntity> extends MobRenderer<T, FireGeckoBossModel<T>> {

    public FireGeckoBossRenderer(EntityRendererManager manager) {
        super(manager, new FireGeckoBossModel<>(), 7f);
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, MatrixStack matrices, float partialTickTime) {
        matrices.scale(15f, 15f, 15f);
        super.preRenderCallback(entitylivingbaseIn, matrices, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(FireGeckoBossEntity entity) {
        return new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/gecko_boss_fire.png");
    }

}
