package yaboichips.geckomod.client.entity.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import yaboichips.geckomod.common.entities.FlowerGeckoBossEntity;

public class FlowerGeckoBossModel<T extends FlowerGeckoBossEntity> extends EntityModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer neck;
    private final ModelRenderer cube_r3;
    private final ModelRenderer neck1;
    private final ModelRenderer spike_r1;
    private final ModelRenderer neck2;
    private final ModelRenderer cube_r4;
    private final ModelRenderer neck3;
    private final ModelRenderer spike_r2;
    private final ModelRenderer body;
    private final ModelRenderer cube_r5;
    private final ModelRenderer rightarm;
    private final ModelRenderer cube_r6;
    private final ModelRenderer spike3;
    private final ModelRenderer leftarm;
    private final ModelRenderer cube_r7;
    private final ModelRenderer spike2;
    private final ModelRenderer rightleg;
    private final ModelRenderer cube_r8;
    private final ModelRenderer spike;
    private final ModelRenderer leftleg;
    private final ModelRenderer cube_r9;
    private final ModelRenderer spike4;
    private final ModelRenderer tail;
    private final ModelRenderer spike_r3;
    private final ModelRenderer tail2;
    private final ModelRenderer spike_r4;

    public FlowerGeckoBossModel() {
        textureWidth = 32;
        textureHeight = 32;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.5F, 10.0F, -2.0F);
        setRotationAngle(head, 0.2618F, 0.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0454F, 7.2187F, 2.531F);
        head.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 1.5708F, 0.0F);
        cube_r1.setTextureOffset(11, 0).addBox(3.0216F, -9.2333F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r1.setTextureOffset(11, 0).addBox(3.0216F, -9.2333F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0454F, 7.2187F, 2.531F);
        head.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 1.5708F, 0.0F);
        cube_r2.setTextureOffset(2, 9).addBox(2.0216F, -8.2333F, -2.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        cube_r2.setTextureOffset(0, 7).addBox(5.0216F, -7.2333F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        neck = new ModelRenderer(this);
        neck.setRotationPoint(-1.0F, 23.0F, 0.0F);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(1.5454F, -5.7813F, 0.531F);
        neck.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 1.5708F, 0.0F);
        cube_r3.setTextureOffset(11, 3).addBox(1.4856F, -7.2187F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        cube_r3.setTextureOffset(0, 0).addBox(1.4856F, -7.7187F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        neck1 = new ModelRenderer(this);
        neck1.setRotationPoint(8.0F, -2.0F, 0.0F);
        neck.addChild(neck1);
        setRotationAngle(neck1, 0.0F, 0.0F, -0.48F);


        spike_r1 = new ModelRenderer(this);
        spike_r1.setRotationPoint(-3.9793F, -6.3345F, 0.531F);
        neck1.addChild(spike_r1);
        setRotationAngle(spike_r1, -1.5708F, 1.0908F, -1.0908F);
        spike_r1.setTextureOffset(0, 0).addBox(3.0067F, -6.1655F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        spike_r1.setTextureOffset(21, 4).addBox(3.0067F, -5.6512F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        neck2 = new ModelRenderer(this);
        neck2.setRotationPoint(0.0F, 0.0F, 0.0F);
        neck.addChild(neck2);
        setRotationAngle(neck2, 0.0F, 0.0F, -0.829F);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(5.3065F, -2.7664F, 0.531F);
        neck2.addChild(cube_r4);
        setRotationAngle(cube_r4, -1.5708F, 0.7418F, -0.7418F);
        cube_r4.setTextureOffset(0, 0).addBox(2.7144F, -4.7336F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r4.setTextureOffset(19, 4).addBox(2.7144F, -4.2107F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        neck3 = new ModelRenderer(this);
        neck3.setRotationPoint(0.0F, 0.0F, 0.0F);
        neck.addChild(neck3);
        setRotationAngle(neck3, 0.0F, 0.0F, 0.1309F);


        spike_r2 = new ModelRenderer(this);
        spike_r2.setRotationPoint(0.7776F, -5.9336F, 0.531F);
        neck3.addChild(spike_r2);
        setRotationAngle(spike_r2, 1.5708F, 1.4399F, 1.4399F);
        spike_r2.setTextureOffset(0, 0).addBox(-2.2776F, -4.0705F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        spike_r2.setTextureOffset(15, 6).addBox(-1.7469F, -5.0705F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.5454F, -6.7813F, 0.531F);
        body.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 1.5708F, 0.0F);
        cube_r5.setTextureOffset(28, 0).addBox(0.4856F, 2.7813F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r5.setTextureOffset(0, 0).addBox(-3.0454F, 1.7813F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r5.setTextureOffset(0, 0).addBox(-3.0454F, -0.2187F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r5.setTextureOffset(20, 0).addBox(-2.5144F, -1.2187F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        cube_r5.setTextureOffset(23, 1).addBox(0.4856F, -3.2187F, -1.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        cube_r5.setTextureOffset(20, 0).addBox(0.4856F, -2.2187F, -2.5F, 1.0F, 5.0F, 5.0F, 0.0F, false);
        cube_r5.setTextureOffset(18, 2).addBox(-1.5144F, -3.2187F, -2.5F, 2.0F, 6.0F, 5.0F, 0.0F, false);
        cube_r5.setTextureOffset(0, 0).addBox(-2.0454F, -2.2187F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        rightarm = new ModelRenderer(this);
        rightarm.setRotationPoint(-2.0F, -8.75F, 1.75F);
        body.addChild(rightarm);
        setRotationAngle(rightarm, 0.2182F, 0.0F, 0.0F);


        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(2.5454F, 1.6582F, -1.6162F);
        rightarm.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 1.5708F, 0.0F);
        cube_r6.setTextureOffset(28, 0).addBox(4.0875F, -0.9159F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r6.setTextureOffset(17, 5).addBox(-1.9125F, -1.9159F, -3.5F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        spike3 = new ModelRenderer(this);
        spike3.setRotationPoint(0.0F, 0.0F, 0.0F);
        rightarm.addChild(spike3);
        setRotationAngle(spike3, 0.6545F, -0.3491F, 0.0F);


        leftarm = new ModelRenderer(this);
        leftarm.setRotationPoint(3.3223F, 15.5F, 1.5F);
        setRotationAngle(leftarm, 0.2182F, 0.0F, 0.0F);


        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(-2.7769F, 1.4682F, -1.3181F);
        leftarm.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 1.5708F, 0.0F);
        cube_r7.setTextureOffset(16, 3).addBox(3.458F, -0.9017F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r7.setTextureOffset(19, 6).addBox(-1.542F, -1.9017F, 2.5F, 5.0F, 2.0F, 1.0F, 0.0F, false);

        spike2 = new ModelRenderer(this);
        spike2.setRotationPoint(0.0F, 0.0F, 0.0F);
        leftarm.addChild(spike2);
        setRotationAngle(spike2, 0.6545F, 0.3491F, 0.0F);


        rightleg = new ModelRenderer(this);
        rightleg.setRotationPoint(-1.5F, 20.375F, 1.0F);


        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(2.0454F, -3.1563F, -0.469F);
        rightleg.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 1.5708F, 0.0F);
        cube_r8.setTextureOffset(0, 9).addBox(0.4856F, 4.7813F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r8.setTextureOffset(17, 5).addBox(-1.5144F, 3.7813F, -3.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        cube_r8.setTextureOffset(0, 16).addBox(0.4856F, 5.7813F, -3.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r8.setTextureOffset(17, 6).addBox(-1.5144F, 2.7813F, -3.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        spike = new ModelRenderer(this);
        spike.setRotationPoint(0.0F, 0.0F, 0.0F);
        rightleg.addChild(spike);
        setRotationAngle(spike, -0.48F, 0.0F, -0.1309F);


        leftleg = new ModelRenderer(this);
        leftleg.setRotationPoint(3.0F, 20.375F, 1.0F);


        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(-2.4546F, -3.1563F, -0.469F);
        leftleg.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 1.5708F, 0.0F);
        cube_r9.setTextureOffset(0, 9).addBox(0.4856F, 4.7813F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r9.setTextureOffset(23, 3).addBox(-1.5144F, 3.7813F, 1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        cube_r9.setTextureOffset(0, 16).addBox(0.4856F, 5.7813F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r9.setTextureOffset(0, 9).addBox(-1.5144F, 2.7813F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        spike4 = new ModelRenderer(this);
        spike4.setRotationPoint(0.0F, 0.0F, 0.0F);
        leftleg.addChild(spike4);


        tail = new ModelRenderer(this);
        tail.setRotationPoint(1.0F, 21.0F, -3.0F);
        setRotationAngle(tail, 0.0F, 0.0F, -0.6981F);


        spike_r3 = new ModelRenderer(this);
        spike_r3.setRotationPoint(2.0823F, -3.1889F, 3.531F);
        tail.addChild(spike_r3);
        setRotationAngle(spike_r3, -1.5708F, 0.8727F, -0.8727F);
        spike_r3.setTextureOffset(0, 0).addBox(-6.0586F, 0.6889F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        spike_r3.setTextureOffset(1, 7).addBox(-6.0586F, 1.2088F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        tail2 = new ModelRenderer(this);
        tail2.setRotationPoint(-1.0F, 3.0F, 3.0F);
        tail.addChild(tail2);
        setRotationAngle(tail2, 0.0F, 0.0F, 0.6981F);


        spike_r4 = new ModelRenderer(this);
        spike_r4.setRotationPoint(-1.6169F, -6.7223F, 0.531F);
        tail2.addChild(spike_r4);
        setRotationAngle(spike_r4, 0.0F, 1.5708F, 0.0F);
        spike_r4.setTextureOffset(0, 0).addBox(-6.3521F, 5.2223F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        spike_r4.setTextureOffset(0, 4).addBox(-6.3521F, 5.7223F, -1.5F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        spike_r4.setTextureOffset(22, 4).addBox(-8.3521F, 5.7223F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        spike_r4.setTextureOffset(19, 12).addBox(-5.3521F, 4.7223F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        neck.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        leftarm.render(matrixStack, buffer, packedLight, packedOverlay);
        rightleg.render(matrixStack, buffer, packedLight, packedOverlay);
        leftleg.render(matrixStack, buffer, packedLight, packedOverlay);
        tail.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
        this.rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
