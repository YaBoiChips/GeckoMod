package yaboichips.geckomod.client.entity.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.IceGeckoBossModel;
import yaboichips.geckomod.common.entities.IceGeckoBossEntity;

public class IceGeckoBossRenderer<T extends IceGeckoBossEntity> extends MobRenderer<T, IceGeckoBossModel<T>> {

    public IceGeckoBossRenderer(EntityRendererManager renderManagerIn, float scale) {
        super(renderManagerIn, new IceGeckoBossModel<>(), 1f);
    }

    public IceGeckoBossRenderer(EntityRendererManager manager){
        super (manager, new IceGeckoBossModel<>(), 7f);
    }


    @Override
    protected void preRenderCallback(T entitylivingbaseIn, MatrixStack matrices, float partialTickTime) {
        matrices.scale( 15f,  15f, 15f);
        super.preRenderCallback(entitylivingbaseIn, matrices, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(IceGeckoBossEntity entity) {
       return new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/gecko_boss_ice.png");
    }

}
