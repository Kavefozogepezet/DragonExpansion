package com.kavefozogepezet.dragonexpansion.common.goals;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import com.kavefozogepezet.dragonexpansion.common.tiles.RideableDragonEggTE;
import com.kavefozogepezet.dragonexpansion.core.init.BlockInit;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.Goal.Flag;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class DragonBreedGoal extends Goal {
    private static final EntityPredicate PARTNER_TARGETING = (new EntityPredicate()).range(16.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    protected final AnimalEntity animal;
    private final Class<? extends AnimalEntity> partnerClass;
    protected final World level;
    protected AnimalEntity partner;
    private int loveTime;
    private final double speedModifier;

    public DragonBreedGoal(AnimalEntity p_i1619_1_, double p_i1619_2_) {
        this(p_i1619_1_, p_i1619_2_, p_i1619_1_.getClass());
    }

    public DragonBreedGoal(AnimalEntity p_i47306_1_, double p_i47306_2_, Class<? extends AnimalEntity> p_i47306_4_) {
        this.animal = p_i47306_1_;
        this.level = p_i47306_1_.level;
        this.partnerClass = p_i47306_4_;
        this.speedModifier = p_i47306_2_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
    }

    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= 60 && this.animal.distanceToSqr(this.partner) < 36.0D) {
            this.breed();
        }
    }

    @Nullable
    private AnimalEntity getFreePartner() {
        List<AnimalEntity> lvt_1_1_ = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(16.0D));
        double lvt_2_1_ = 1.7976931348623157E308D;
        AnimalEntity lvt_4_1_ = null;
        Iterator var5 = lvt_1_1_.iterator();

        while(var5.hasNext()) {
            AnimalEntity lvt_6_1_ = (AnimalEntity)var5.next();
            if (this.animal.canMate(lvt_6_1_) && this.animal.distanceToSqr(lvt_6_1_) < lvt_2_1_) {
                lvt_4_1_ = lvt_6_1_;
                lvt_2_1_ = this.animal.distanceToSqr(lvt_6_1_);
            }
        }

        return lvt_4_1_;
    }

    protected void breed() {
        BlockPos eggPos = new BlockPos(this.animal.position());
        while(level.getBlockState(eggPos.below()).isAir() && eggPos.below().getY() != 0){
            eggPos = eggPos.below();
        }
        if(eggPos.below().getY() != 0) {
            this.level.setBlock(eggPos, BlockInit.RIDEABLE_DRAGON_EGG.get().defaultBlockState(), 3);
            TileEntity tile = this.level.getBlockEntity(eggPos);
            if(tile instanceof RideableDragonEggTE){
                ((RideableDragonEggTE) tile).ignite(true);
            }
        }
        this.animal.resetLove();
        this.partner.resetLove();
    }
}
