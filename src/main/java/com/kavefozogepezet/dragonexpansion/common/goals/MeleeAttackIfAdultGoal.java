package com.kavefozogepezet.dragonexpansion.common.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class MeleeAttackIfAdultGoal extends MeleeAttackGoal {
    public MeleeAttackIfAdultGoal(CreatureEntity p_i1636_1_, double p_i1636_2_, boolean p_i1636_4_) {
        super(p_i1636_1_, p_i1636_2_, p_i1636_4_);
    }

    @Override
    public boolean canUse() {
        return !this.mob.isBaby() && super.canUse();
    }
}
