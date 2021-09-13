package com.kavefozogepezet.dragonexpansion.common.entities;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PurpurDragon extends RideableDragonEntity{
    public PurpurDragon(EntityType<? extends AnimalEntity> p_i48563_1_, World p_i48563_2_) {
        super(p_i48563_1_, p_i48563_2_);
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        /*if(Minecraft.getInstance().player != null){
            Minecraft.getInstance().player.chat(this.level.isClientSide ? "client: " : "server: " + (this.getHatchType() ? "true" : "false"));
        }*/
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        if (!this.isBaby()) {
            if (this.isVehicle()) {
                return super.mobInteract(p_230254_1_, p_230254_2_);
            }
        }

        if (!itemstack.isEmpty()) {
            if (this.isFood(itemstack)) {
                return this.fedFood(p_230254_1_, itemstack);
            }

            ActionResultType actionresulttype = itemstack.interactLivingEntity(p_230254_1_, this, p_230254_2_);
            if (actionresulttype.consumesAction()) {
                return actionresulttype;
            }
        }
        if (this.isBaby()) {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        } else if(!this.getHatchType()) {
            this.doPlayerRide(p_230254_1_);
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        return ActionResultType.PASS;
    }

    public ActionResultType fedFood(PlayerEntity p_241395_1_, ItemStack p_241395_2_) {
        boolean flag = this.handleEating(p_241395_1_, p_241395_2_);
        if (!p_241395_1_.abilities.instabuild) {
            p_241395_2_.shrink(1);
        }

        if (this.level.isClientSide) {
            return ActionResultType.CONSUME;
        } else {
            return flag ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
    }

    protected boolean handleEating(PlayerEntity p_190678_1_, ItemStack p_190678_2_) {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        Item item = p_190678_2_.getItem();
        if (item == Items.CHORUS_FRUIT) {
            f = 2.0F;
            i = 20;
            j = 3;
        } if (item == Items.CHORUS_FLOWER) {
            f = 4.0F;
            i = 20;
            j = 3;
            if (!this.level.isClientSide && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(p_190678_1_);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && i > 0) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
            if (!this.level.isClientSide) {
                this.ageUp(i);
            }

            flag = true;
        }

        return flag;
    }
}
