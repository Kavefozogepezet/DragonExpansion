package com.kavefozogepezet.dragonexpansion.common.entities;

import com.kavefozogepezet.dragonexpansion.common.blocks.RideableDragonEgg;
import com.kavefozogepezet.dragonexpansion.common.goals.DragonBreedGoal;
import com.kavefozogepezet.dragonexpansion.common.goals.MeleeAttackIfAdultGoal;
import com.kavefozogepezet.dragonexpansion.common.goals.PanicIfBabyGoal;
import com.kavefozogepezet.dragonexpansion.core.init.EntityTypeInit;
import com.kavefozogepezet.dragonexpansion.core.init.SoundEventInit;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RideableDragonEntity extends AnimalEntity implements IJumpingMount, IAngerable {

    // -------------------- variables --------------------
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHORUS_FLOWER);
    private static final DataParameter<Boolean> HATCHED_BY_DRAGON = EntityDataManager.defineId(RideableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(RideableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private boolean isJumping = false;
    protected float playerJumpPendingScale;
    public boolean isStill = false;
    Vector3d prevPos = Vector3d.ZERO;
    //for animation
    private double bodyRotX0 = 0;
    private AnimationHolder deltaRotY = new AnimationHolder(0f);
    private AnimationHolder bodyRotX = new AnimationHolder(0f);
    private AnimationHolder legAnimation = new AnimationHolder(0f);
    private AnimationHolder wingAnimation = new AnimationHolder(0f);

    // -------------------- Mandatory methods --------------------
    public RideableDragonEntity(EntityType<? extends AnimalEntity> p_i48563_1_, World p_i48563_2_) {
        super(p_i48563_1_, p_i48563_2_);
        this.maxUpStep = 1.6f;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40d)
                .add(Attributes.MOVEMENT_SPEED, 0.3d)
                .add(Attributes.ATTACK_DAMAGE, 12.0d)
                .add(Attributes.ATTACK_KNOCKBACK, 16.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6d)
                .add(Attributes.JUMP_STRENGTH, 1.0d)
                .add(Attributes.FOLLOW_RANGE, 48d);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackIfAdultGoal(this, 1.25d, true));
        this.goalSelector.addGoal(1, new PanicIfBabyGoal(this, 2.0d));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new DragonBreedGoal(this, 1.0D, RideableDragonEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class,  10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0d, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HATCHED_BY_DRAGON, true);
        this.entityData.define(FLYING, false);
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return EntityTypeInit.PURPUR_DRAGON.get().create(serverWorld);
    }

    // -------------------- NBT --------------------
    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        this.addPersistentAngerSaveData(p_213281_1_);
        p_213281_1_.putBoolean("HatchedByDragon", this.getHatchType());
        p_213281_1_.putBoolean("IsFlying", this.entityData.get(FLYING));
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if(!level.isClientSide) {
            this.readPersistentAngerSaveData((ServerWorld) this.level, p_70037_1_);
            this.setHatchType(p_70037_1_.getBoolean("HatchedByDragon"));
            this.entityData.set(FLYING, p_70037_1_.getBoolean("IsFlying"));
        }
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

    // -------------------- sound --------------------
    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    protected float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    protected float getSoundVolume() {
        return this.isBaby() ? 0.3f : 1.0f;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEventInit.RIDEABLE_DRAGON_BABY_AMBIENT.get() : SoundEventInit.RIDEABLE_DRAGON_AMBIENT.get();
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
        this.playSound(SoundEvents.COW_STEP, 0.15f, 1.0f);
    }

    protected void playJumpSound() {
        this.playSound(SoundEvents.COW_STEP, 0.4F, 1.0F);
    }

    protected void playFlapSound() {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_FLAP, this.getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
    }

    // -------------------- overrides --------------------
    @Override
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
        float f1 = MathHelper.sin(this.yBodyRot * ((float)Math.PI / 180F)) * 1.4F;
        float f2 = MathHelper.cos(this.yBodyRot * ((float)Math.PI / 180F)) * 1.4F;
        passenger.setPos(this.getX() - (double)(f1), this.getY() + 2.5D + passenger.getMyRidingOffset(), this.getZ() + (double)(f2));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public Vector3d getDismountLocationForPassenger(LivingEntity p_230268_1_) {
        Vector3d vector3d = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_230268_1_.getBbWidth(), this.yRot + (p_230268_1_.getMainArm() == HandSide.RIGHT ? 90.0F : -90.0F));
        Vector3d vector3d1 = this.getDismountLocationInDirection(vector3d, p_230268_1_);
        if (vector3d1 != null) {
            return vector3d1;
        } else {
            Vector3d vector3d2 = getCollisionHorizontalEscapeVector((double)this.getBbWidth(), (double)p_230268_1_.getBbWidth(), this.yRot + (p_230268_1_.getMainArm() == HandSide.LEFT ? 90.0F : -90.0F));
            Vector3d vector3d3 = this.getDismountLocationInDirection(vector3d2, p_230268_1_);
            return vector3d3 != null ? vector3d3 : this.position();
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return FOOD_ITEMS.test(itemStack);
    }

    private float getCorrespondingAngle(float angle) {
        float result = angle % 360f;
        return result >= 0 ? result : 360f + result;
    }

    @Override
    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            boolean b1 = this.entityData.get(FLYING);
            this.yRot = getCorrespondingAngle(this.yRot);
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                double angle = getCorrespondingAngle(livingentity.yRot) - this.yRot;
                if(angle > 180d) { angle -= 360d; }
                if(angle < -180d) { angle += 360d; }
                if(b1) {
                    if(Math.abs(angle) > 0.1d) { angle /= 15d; }
                    angle = MathHelper.clamp(angle, -18, 18);
                    this.yRot = getCorrespondingAngle(this.yRot + (float)angle);

                    double movementY = MathHelper.clamp(livingentity.getLookAngle().y, -0.64d, 0.64d);
                    double movementScale = 1.1d;// - movementY;
                    bodyRotX0 = -Math.asin(movementY);
                    double f3 = Math.sqrt(1 - Math.pow(movementY, 2d));
                    Vector3d movement = new Vector3d(f3 * -Math.sin(Math.toRadians(this.yRot)), movementY, f3 * Math.cos(Math.toRadians(this.yRot))).scale(movementScale);

                    if(this.isControlledByLocalInstance()) {
                        if (f1 > 0) {
                            double deltaMovement = this.getDeltaMovement().length();
                            if (deltaMovement < movement.length()) {
                                movement = movement.scale(deltaMovement + 0.02f);
                            }
                            this.setDeltaMovement(movement);
                            this.move(MoverType.SELF, this.getDeltaMovement());
                        } else if (f1 < 0) {
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.9d));
                            this.move(MoverType.SELF, this.getDeltaMovement());
                        } else {
                            super.travel(travelVector);
                        }
                    }
                } else {
                    if(Math.abs(angle) > 0.1d) { angle /= 5d; }
                    angle = MathHelper.clamp(angle, -36, 36);
                    this.yRot += angle;

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

                deltaRotY.updateAnimation((float)angle);
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                float deltaHeadRotY = getCorrespondingAngle(livingentity.yRot) - this.yRot;
                if(deltaHeadRotY > 180d) { deltaHeadRotY -= 360d; }
                if(deltaHeadRotY < -180d) { deltaHeadRotY += 360d; }
                this.yHeadRot = this.yRot + deltaHeadRotY;

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
        this.wingAnimation.updateAnimation((float)((this.wingAnimation.curr + 1d / 10d * Math.PI) % (2 * Math.PI)));
        this.legAnimation.updateAnimation((float)((this.legAnimation.curr + 1d / 5d * Math.PI) % (2 * Math.PI)));

        Vector3d movement = this.position().subtract(prevPos);
        prevPos = this.position();
        double speed = new Vector3d(movement.x, 0.0f, movement.z).length();
        isStill = speed == 0;

        if(this.isFlying()){
            double rotXScale = (bodyRotX0 + Math.PI/4d) * (speed / 0.4d) - Math.PI/4d;
            float rotX = (float)(speed < 0.4d ? rotXScale : bodyRotX0);
            this.bodyRotX.updateAnimation(rotX);
        } else {
            this.bodyRotX.updateAnimation(0f);
        }

        if(!this.isVehicle()){
            deltaRotY.updateAnimation(0);
        }
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

    @Override
    public float getWalkTargetValue(BlockPos p_205022_1_, IWorldReader p_205022_2_) {
        return p_205022_2_.getBlockState(p_205022_1_.below()).is(Blocks.END_STONE) ? 10.0F : - 0.5F;
    }

    // -------------------- custom functions --------------------
    public boolean getHatchType() {
        return this.entityData.get(HATCHED_BY_DRAGON);
    }

    public void setHatchType(boolean type) {
        this.entityData.set(HATCHED_BY_DRAGON, type);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    protected void doPlayerRide(PlayerEntity p_110237_1_) {
        if (!this.level.isClientSide) {
            p_110237_1_.yRot = this.yRot;
            p_110237_1_.xRot = this.xRot;
            p_110237_1_.startRiding(this);
        }
    }

    @Nullable
    private Vector3d getDismountLocationInDirection(Vector3d p_234236_1_, LivingEntity p_234236_2_) {
        double d0 = this.getX() + p_234236_1_.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + p_234236_1_.z;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Pose pose : p_234236_2_.getDismountPoses()) {
            blockpos$mutable.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75D;

            while(true) {
                double d4 = this.level.getBlockFloorHeight(blockpos$mutable);
                if ((double)blockpos$mutable.getY() + d4 > d3) {
                    break;
                }

                if (TransportationHelper.isBlockFloorValid(d4)) {
                    AxisAlignedBB axisalignedbb = p_234236_2_.getLocalBoundsForPose(pose);
                    Vector3d vector3d = new Vector3d(d0, (double)blockpos$mutable.getY() + d4, d2);
                    if (TransportationHelper.canDismountTo(this.level, p_234236_2_, axisalignedbb.move(vector3d))) {
                        p_234236_2_.setPose(pose);
                        return vector3d;
                    }
                }

                blockpos$mutable.move(Direction.UP);
                if (!((double)blockpos$mutable.getY() < d3)) {
                    break;
                }
            }
        }

        return null;
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
        while(this.level.getBlockState(pos).isAir() && pos.getY() != 0){
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

    public static boolean checkRideableDragonSpawnRules(EntityType<? extends MobEntity> p_223315_0_, IWorld p_223315_1_, SpawnReason p_223315_2_, BlockPos p_223315_3_, Random p_223315_4_) {
        boolean result =  p_223315_1_.getBlockState(p_223315_3_.below()).is(Blocks.END_STONE);
        //int chance = Math.abs(p_223315_4_.nextInt()) % 2;
        /*if(Minecraft.getInstance().player != null){
            Minecraft.getInstance().player.chat(p_223315_2_.toString());
        }*/
        return /*(p_223315_2_ != SpawnReason.NATURAL) &&*/ result;
    }

    // -------------------- anger --------------------
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.remainingPersistentAngerTime = i;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
    }

    // for animations
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