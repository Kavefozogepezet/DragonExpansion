package com.kavefozogepezet.dragonexpansion.common.entities;

import com.kavefozogepezet.dragonexpansion.common.blocks.RideableDragonEgg;
import com.kavefozogepezet.dragonexpansion.common.goals.DragonBreedGoal;
import com.kavefozogepezet.dragonexpansion.core.init.EntityTypeInit;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;

public class RideableDragonEntity extends AnimalEntity implements IJumpingMount {

    // -------------------- variables --------------------
    private static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(RideableDragonEntity.class, DataSerializers.BOOLEAN);
    private boolean isJumping = false;
    protected float playerJumpPendingScale;
    public boolean isStill = false;
    Vector3d prevPos = Vector3d.ZERO;
    //for animation
    public AnimationHolder deltaRotY = new AnimationHolder(0f);
    public AnimationHolder bodyRotX = new AnimationHolder(0f);
    public AnimationHolder legAnimation = new AnimationHolder(0f);
    public AnimationHolder wingAnimation = new AnimationHolder(0f);

    // -------------------- Mandatory methods --------------------
    public RideableDragonEntity(EntityType<? extends AnimalEntity> p_i48563_1_, World p_i48563_2_) {
        super(p_i48563_1_, p_i48563_2_);
        this.maxUpStep = 1.6f;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40d)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 16.0D)
                .add(Attributes.JUMP_STRENGTH, 1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));

        //this.goalSelector.addGoal(1, new SmallDragonEntity.DragonFireballAttack(this));

        //this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new DragonBreedGoal(this, 1.0D, RideableDragonEntity.class));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        //this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
        //this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));

        //this.goalSelector.addGoal(1, new SmallDragonEntity.DragonMeleeAttack(this, 1.0D, true));
        //his.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.7D));*/

        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
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

    // -------------------- sound --------------------
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

    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
    }

    // -------------------- custom functions and overrides --------------------
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (passenger instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)passenger;
            this.yBodyRot = mobentity.yBodyRot;
        }
        float f1 = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F)) * 1.375F;
        float f2 = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F)) * 1.375F;
        passenger.setPos(this.getX() - (double)(f1), this.getY() + 2.5D + passenger.getMyRidingOffset(), this.getZ() + (double)(f2));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }

    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            boolean b1 = this.entityData.get(FLYING);
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if(b1) {
                    double angle = (livingentity.yRot % 360d) - yRot;
                    if(angle > 180d) { angle -= 360d; }
                    if(angle < -180d) { angle += 360d; }
                    if(Math.abs(angle) > 0.1d) { angle /= 15d; }

                    this.yRot += angle;
                    deltaRotY.updateAnimation((float)angle);
                    this.yRotO = this.yRot;
                    this.xRot = livingentity.xRot * 0.5F;
                    this.setRot(this.yRot, this.xRot);
                    this.yBodyRot = this.yRot;
                    this.yHeadRot = this.yBodyRot;

                    Vector3d look = livingentity.getLookAngle();
                    double f3 = Math.sqrt(1 - Math.pow(look.y, 2d));
                    Vector3d movement = new Vector3d(f3 * -Math.sin(Math.toRadians(this.yRot)), look.y, f3 * Math.cos(Math.toRadians(this.yRot)));

                    if(this.isControlledByLocalInstance()) {
                        if (f1 > 0) {
                            double deltaMovement = this.getDeltaMovement().length();
                            if (deltaMovement < movement.length()) {
                                movement = movement.scale(deltaMovement + 0.02f);
                            }
                            this.setDeltaMovement(movement);
                            this.move(MoverType.SELF, this.getDeltaMovement());
                        } else if (f1 < 0) {
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.75d));
                            this.move(MoverType.SELF, this.getDeltaMovement());
                        } else {
                            super.travel(travelVector);
                        }
                    }
                } else {
                    this.yRot = livingentity.yRot;
                    this.yRotO = this.yRot;
                    this.xRot = livingentity.xRot * 0.5F;
                    this.setRot(this.yRot, this.xRot);
                    this.yBodyRot = this.yRot;
                    this.yHeadRot = this.yBodyRot;
                    if (f1 <= 0.0F) {
                        f1 *= 0.25F;
                    }

                    if (this.playerJumpPendingScale > 0.0F && !isJumping && this.onGround) {
                        double d0 = this.getJumpPotential() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                        double d1;
                        if (this.hasEffect(Effects.JUMP)) {
                            d1 = d0 + (double)((float)(this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                        } else {
                            d1 = d0;
                        }

                        Vector3d vector3d = this.getDeltaMovement();
                        this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                        isJumping = true;
                        this.hasImpulse = true;
                        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                        if (f1 > 0.0F) {
                            float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                            float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                            this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.playerJumpPendingScale), 0.0D, (double)(0.4F * f3 * this.playerJumpPendingScale)));
                        }

                        this.playerJumpPendingScale = 0.0F;
                    }

                    this.flyingSpeed = this.getSpeed() * 0.1F;
                    if (this.isControlledByLocalInstance()) {
                        this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        super.travel(new Vector3d((double)f, travelVector.y, (double)f1));
                    } else if (livingentity instanceof PlayerEntity) {
                        this.setDeltaMovement(Vector3d.ZERO);
                    }
                }

                if (this.onGround) {
                    this.playerJumpPendingScale = 0.0F;
                    isJumping = false;
                }
                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(travelVector);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isControlledByLocalInstance() || this.isEffectiveAi()) {
            if (this.onGround) {
                this.setFlying(false);
            }
            if(!this.onGround){
                if(this.getJumpHeight() > 4){
                    this.setFlying(true);
                };
            }
        }
        this.wingAnimation.updateAnimation((float)((this.wingAnimation.curr + 1d / 15d * Math.PI) % (2 * Math.PI)));
        this.legAnimation.updateAnimation((float)((this.legAnimation.curr + 1d / 5d * Math.PI) % (2 * Math.PI)));

        Vector3d movement = this.position().subtract(prevPos);
        prevPos = this.position();
        double speed = new Vector3d(movement.x, 0.0f, movement.z).length();
        isStill = speed == 0;

        if(this.isFlying()){
            float rotX = speed < 0.4d ? -(float)((0.4d - speed) * 2.5d * Math.PI / 4d) : 0f;
            this.bodyRotX.updateAnimation(rotX);
        } else {
            this.bodyRotX.updateAnimation(0f);
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Vector3d getLookAngle() {
        if(isVehicle()){
            return getControllingPassenger().getLookAngle();
        }
        return super.getLookAngle();
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean isFallFlying() {
        return entityData.get(FLYING);
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected void doPlayerRide(PlayerEntity p_110237_1_) {
        if (!this.level.isClientSide) {
            p_110237_1_.yRot = this.yRot;
            p_110237_1_.xRot = this.xRot;
            p_110237_1_.startRiding(this);
        }
    }

    public double getJumpPotential() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }
    public boolean isFlying(){
        return this.entityData.get(FLYING);
    }

    private int getJumpHeight() {
        BlockPos pos = this.getOnPos();
        int height = 0;
        while(this.level.getBlockState(pos).isAir()){
            pos = pos.below();
            height++;
        }
        return height;
    }

    private float getCircularAnimation(float prev, float curr, float partialTicks){
        if(prev > curr){
            return (prev + (curr + 2 * (float)Math.PI - prev) * partialTicks) % (2 * (float)Math.PI);
        } else {
            return prev + (curr - prev) * partialTicks;
        }
    }

    private float getLinearAnimation(float prev, float curr, float partialTicks){
        return prev + (curr - prev) * partialTicks;
    }

    public float getDeltaRotY(float partialTicks){
        return getLinearAnimation(deltaRotY.prev, deltaRotY.curr, partialTicks);
    }

    public float getBodyRotX(float partialTicks){
        return getLinearAnimation(bodyRotX.prev, bodyRotX.curr, partialTicks);
    }

    public float getLegAnimation(float partialTicks){
        return getCircularAnimation(this.legAnimation.prev, this.legAnimation.curr, partialTicks);
    }

    public float getWingAnimation(float partialTicks){
        return getCircularAnimation(this.wingAnimation.prev, this.wingAnimation.curr, partialTicks);
    }

    protected void setFlying(boolean flying){
        this.entityData.set(FLYING, flying);
    }

    // -------------------- jumping mount --------------------
    @Override
    public void onPlayerJump(int value) {
        if (true) { // saddled
            if (value < 0) {
                value = 0;
            }
            if (value >= 90) {
                this.playerJumpPendingScale = 1.0F;
            } else {
                this.playerJumpPendingScale = 0.4F + 0.4F * (float)value / 90.0F;
            }
        }
    }

    @Override
    public boolean canJump() {
        return true; // saddled
    }

    @Override
    public void handleStartJump(int i) {
        this.playJumpSound();
    }

    @Override
    public void handleStopJump() {
    }

    public static class AnimationHolder {
        public float prev;
        public float curr;

        public AnimationHolder(float base) {
            prev = base;
            curr = prev;
        }

        public void updateAnimation(float value){
            prev = curr;
            curr = value;
        }
    }
}