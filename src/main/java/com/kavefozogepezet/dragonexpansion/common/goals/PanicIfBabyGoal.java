package com.kavefozogepezet.dragonexpansion.common.goals;

import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import net.minecraft.entity.CreatureEntity;

public class PanicIfBabyGoal extends net.minecraft.entity.ai.goal.PanicGoal {
    public PanicIfBabyGoal(CreatureEntity entity, double speed) {
        super(entity, speed);
    }

    public boolean canUse() {
        return this.mob.isBaby() && super.canUse();
    }
}
