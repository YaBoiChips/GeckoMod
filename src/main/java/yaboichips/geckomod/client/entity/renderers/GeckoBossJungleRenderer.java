package yaboichips.geckomod.client.entity.renderers;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.GeckoBossJungleModel;
import yaboichips.geckomod.common.entities.GeckoBossEntity;

public class GeckoBossJungleRenderer extends MobRenderer<GeckoBossEntity, GeckoBossJungleModel<GeckoBossEntity>> {


    public GeckoBossJungleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new GeckoBossJungleModel<GeckoBossEntity>(), 1f);
    }

    @Override
    public ResourceLocation getEntityTexture(GeckoBossEntity entity) {
        return new ResourceLocation(GeckoMod.MOD_ID, "");
    }
}
