package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.common.entities.ExampleDragon;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, DragonExpansion.MOD_ID);

    public static final RegistryObject<EntityType<ExampleDragon>> RIDEABLE_DRAGON =
            ENTITY_TYPES.register("example_dragon", ()->EntityType.Builder
                    .of(ExampleDragon::new, EntityClassification.CREATURE)
                    .sized(3.0f, 3.0f)
                    .fireImmune()
                    .build((new ResourceLocation(DragonExpansion.MOD_ID, "rideable_dragon").toString())));
}
