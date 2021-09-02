package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundEventInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DragonExpansion.MOD_ID);

    public static final RegistryObject<SoundEvent> RIDEABLE_DRAGON_AMBIENT = SOUNDS.register(
            "entity.rideable_dragon.ambient", () -> new SoundEvent(new ResourceLocation(DragonExpansion.MOD_ID, "entity.rideable_dragon.ambient"))
    );
    public static final RegistryObject<SoundEvent> RIDEABLE_DRAGON_BABY_AMBIENT = SOUNDS.register(
            "entity.rideable_dragon.babyambient", () -> new SoundEvent(new ResourceLocation(DragonExpansion.MOD_ID, "entity.rideable_dragon.babyambient"))
    );
}
