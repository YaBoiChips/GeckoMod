package yaboichips.geckomod.client.entity.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.monster.MonsterEntity;
import yaboichips.geckomod.common.entities.GeckoBossEntity;

public class GeckoBossJungleModel<G extends MonsterEntity> extends EntityModel<GeckoBossEntity> {
    @Override
    public void setRotationAngles(GeckoBossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}
