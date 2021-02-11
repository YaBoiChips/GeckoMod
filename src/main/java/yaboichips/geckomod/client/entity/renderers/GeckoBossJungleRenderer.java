package yaboichips.geckomod.client.entity.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.GeckoBossJungleModel;
import yaboichips.geckomod.common.entities.GeckoBossEntity;

public class GeckoBossJungleRenderer<T extends GeckoBossEntity> extends MobRenderer<T, GeckoBossJungleModel<T>> {

    public GeckoBossJungleRenderer(EntityRendererManager renderManagerIn, float scale) {
        super(renderManagerIn, new GeckoBossJungleModel<>(), 1f);
    }

    public GeckoBossJungleRenderer(EntityRendererManager manager){
        super (manager, new GeckoBossJungleModel<>(), 1f);
    }


    @Override
    protected void preRenderCallback(T entitylivingbaseIn, MatrixStack matrices, float partialTickTime) {
        matrices.scale( 12f,  10f, 15f);
        super.preRenderCallback(entitylivingbaseIn, matrices, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(GeckoBossEntity entity) {
       return new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/green_gecko.png");
    }

}
