package com.kavefozogepezet.dragonexpansion.common.entities;

import com.kavefozogepezet.dragonexpansion.core.enums.DragonVariant;
import com.kavefozogepezet.dragonexpansion.core.init.EntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class RideableDragonEntity extends AnimalEntity {
    public double legAnimation = 0;
    public boolean isStill = true;

    public RideableDragonEntity(EntityType<? extends AnimalEntity> p_i48563_1_, World p_i48563_2_) {
        super(p_i48563_1_, p_i48563_2_);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40d)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 16.0D);
        //.add(Attributes.HORSE_JUMP_STRENGTH, 1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));

        //this.goalSelector.addGoal(1, new SmallDragonEntity.DragonFireballAttack(this));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        //this.goalSelector.addGoal(2, new SmallDragonEntity.DragonBreedGoal(this, 1.0D, SmallDragonEntity.class));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
        //this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));

        //this.goalSelector.addGoal(1, new SmallDragonEntity.DragonMeleeAttack(this, 1.0D, true));
        //his.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.7D));*/

        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    /*@Nullable
    @Override
    protected SoundEvent getAngrySound() {
        return null;
    }*/

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15f, 1.0f);
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return EntityTypeInit.RIDEABLE_DRAGON.get().create(serverWorld);
    }

    // -------------------- animation --------------------

    public void updateLegAnimation(float partialTicks) {
        Vector3d motion = new Vector3d(this.getMotionDirection().getStepX(), 0.0d, this.getMotionDirection().getStepZ());
        double speed = motion.length() * partialTicks;
        this.legAnimation = (this.legAnimation + speed * Math.PI) % (2 * Math.PI);
        isStill = speed == 0;
    }
}
