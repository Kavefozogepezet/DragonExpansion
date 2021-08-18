package com.kavefozogepezet.dragonexpansion.client.render;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.client.model.RideableDragonModel;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RideableDragonRenderer extends MobRenderer<RideableDragonEntity, RideableDragonModel<RideableDragonEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(DragonExpansion.MOD_ID, "textures/entities/red_dragon.png");

    public RideableDragonRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new RideableDragonModel<>(), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(RideableDragonEntity p_110775_1_) {
        return TEXTURE;
    }
}
