package com.kavefozogepezet.dragonexpansion.core.enums;

import java.util.Arrays;
import java.util.Comparator;

public enum DragonVariant {
    RED(0),
    YELLOW(1),
    GREEN(2),
    BLUE(3),
    PURPLE(4);

    private static final DragonVariant[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(DragonVariant::getId)).toArray((dv_id) -> {
        return new DragonVariant[dv_id];
    });

    private final int id;
    private DragonVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DragonVariant getVariant(int idIn) {
        return VALUES[idIn];
    }
}
