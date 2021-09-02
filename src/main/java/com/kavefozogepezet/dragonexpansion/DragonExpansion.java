package com.kavefozogepezet.dragonexpansion;

import com.kavefozogepezet.dragonexpansion.client.render.RideableDragonRenderer;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import com.kavefozogepezet.dragonexpansion.core.init.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Mod(DragonExpansion.MOD_ID)
public class DragonExpansion {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "kaves_de";

    public DragonExpansion() {
        IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();
        BUS.addListener(this::setup);
        BUS.addListener(this::doClientStuff);

        SoundEventInit.SOUNDS.register(BUS);
        ItemInit.ITEMS.register(BUS);
        BlockInit.BLOCKS.register(BUS);
        TileEntityTypeInit.TILES.register(BUS);
        EntityTypeInit.ENTITY_TYPES.register(BUS);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, EntityTypeInit::onBiomeLoadingEvent);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(()->{
            GlobalEntityTypeAttributes.put(
                    EntityTypeInit.PURPUR_DRAGON.get(),
                    RideableDragonEntity.createAttributes().build());
        });
        EntitySpawnPlacementRegistry.register(
                EntityTypeInit.PURPUR_DRAGON.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                RideableDragonEntity::checkRideableDragonSpawnRules);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeInit.PURPUR_DRAGON.get(), new IRenderFactory<RideableDragonEntity>() {
            @Override
            public EntityRenderer<? super RideableDragonEntity> createRenderFor(EntityRendererManager manager) {
                return new RideableDragonRenderer(manager);
            }
        });
        RenderTypeLookup.setRenderLayer(BlockInit.RIDEABLE_DRAGON_EGG.get(), RenderType.cutout());
    }
}
