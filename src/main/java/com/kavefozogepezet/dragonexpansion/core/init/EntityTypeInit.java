package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.common.entities.PurpurDragon;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, DragonExpansion.MOD_ID);

    public static final RegistryObject<EntityType<PurpurDragon>> PURPUR_DRAGON =
            ENTITY_TYPES.register("purpur_dragon", ()->EntityType.Builder
                    .of(PurpurDragon::new, EntityClassification.CREATURE)
                    .sized(3.0f, 3.0f)
                    .fireImmune()
                    .build((new ResourceLocation(DragonExpansion.MOD_ID, "purpur_dragon").toString()))
            );

    public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        setEntitiesSpawning(event);
    }

    public static void setEntitiesSpawning(BiomeLoadingEvent event) {
        /*if(event.getCategory() == Biome.Category.THEEND){
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityTypeInit.RIDEABLE_DRAGON.get(), 1, 2, 4));
        }*/
        setEntitySpawning(event, EntityTypeInit.PURPUR_DRAGON.get(), EntityClassification.CREATURE, 1, 1, 1, Biome.Category.THEEND);
    }

    private static void setEntitySpawning(BiomeLoadingEvent event, EntityType<?> entity, EntityClassification classification, int weight, int min, int max, Biome.Category... biomes){
        for(Biome.Category category : biomes){
            if(event.getCategory() == category){
                event.getSpawns().addSpawn(classification, new MobSpawnInfo.Spawners(entity, weight, min, max));
            }
        }

    }
}
