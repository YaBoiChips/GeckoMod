package yaboichips.geckomod.client.entity.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import yaboichips.geckomod.GeckoMod;
import yaboichips.geckomod.client.entity.models.GeckoModel;
import yaboichips.geckomod.common.entities.GeckoEntity;

public class GeckoArmorLayer<T extends GeckoEntity> extends LayerRenderer<T, GeckoModel<T>> {
    private final GeckoModel<GeckoEntity> geckoModel = new GeckoModel<>();

    public GeckoArmorLayer(IEntityRenderer<T, GeckoModel<T>> model) {
        super(model);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, GeckoEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.getArmor() == 1){
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/armor/gecko_armor_iron.png")));
            this.geckoModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0, 0, 0, 1.0F);
        } else if (entitylivingbaseIn.getArmor() == 2) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/armor/gecko_armor_gold.png")));
            this.geckoModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0, 0, 0, 1.0F);
        } else if (entitylivingbaseIn.getArmor() == 3) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/armor/gecko_armor_diamond.png")));
            this.geckoModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0, 0, 0, 1.0F);
        }else if (entitylivingbaseIn.getArmor() == 4) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(GeckoMod.MOD_ID, "textures/entity/gecko/armor/gecko_armor_netherite.png")));
            this.geckoModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0, 0, 0, 1.0F);
        }
    }
}
