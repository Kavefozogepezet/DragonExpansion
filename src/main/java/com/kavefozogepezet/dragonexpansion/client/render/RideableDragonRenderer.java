package com.kavefozogepezet.dragonexpansion.client.render;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.client.model.RideableDragonModel;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RideableDragonRenderer extends MobRenderer<RideableDragonEntity, RideableDragonModel<RideableDragonEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(DragonExpansion.MOD_ID, "textures/entities/red_dragon.png");

    public RideableDragonRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new RideableDragonModel<>(true, 66.0F, 10.0F, 5.0F, 5.0F, 96), 2.0f);
    }

    @Override
    public void render(RideableDragonEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        //entityIn.updateAnimation(partialTicks);
        this.model.updateAnimation(entityIn, partialTicks);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(RideableDragonEntity p_110775_1_) {
        return TEXTURE;
    }
}
