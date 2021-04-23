package yaboichips.geckomod.client.entity.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.JungleGeckoBossModel;
import yaboichips.geckomod.common.entities.JungleGeckoBossEntity;

public class JungleGeckoBossRenderer<T extends JungleGeckoBossEntity> extends MobRenderer<T, JungleGeckoBossModel<T>> {

    public JungleGeckoBossRenderer(EntityRendererManager renderManagerIn, float scale) {
        super(renderManagerIn, new JungleGeckoBossModel<>(), 1f);
    }

    public JungleGeckoBossRenderer(EntityRendererManager manager){
        super (manager, new JungleGeckoBossModel<>(), 1f);
    }


    @Override
    protected void preRenderCallback(T entitylivingbaseIn, MatrixStack matrices, float partialTickTime) {
        matrices.scale( 12f,  10f, 15f);
        super.preRenderCallback(entitylivingbaseIn, matrices, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(JungleGeckoBossEntity entity) {
       return new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/green_gecko.png");
    }

}
