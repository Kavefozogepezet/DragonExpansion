package com.kavefozogepezet.dragonexpansion;

import com.kavefozogepezet.dragonexpansion.client.render.RideableDragonRenderer;
import com.kavefozogepezet.dragonexpansion.common.entities.RideableDragonEntity;
import com.kavefozogepezet.dragonexpansion.core.init.EntityTypeInit;
import com.kavefozogepezet.dragonexpansion.core.init.ItemInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(DragonExpansion.MOD_ID)
public class DragonExpansion {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "kaves_de";

    public DragonExpansion() {
        IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();
        BUS.addListener(this::setup);
        BUS.addListener((this::doClientStuff));

        ItemInit.ITEMS.register(BUS);
        EntityTypeInit.ENTITY_TYPES.register(BUS);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(()->{
            GlobalEntityTypeAttributes.put(
                    EntityTypeInit.RIDEABLE_DRAGON.get(),
                    RideableDragonEntity.createAttributes().build());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeInit.RIDEABLE_DRAGON.get(), new IRenderFactory<RideableDragonEntity>() {
            @Override
            public EntityRenderer<? super RideableDragonEntity> createRenderFor(EntityRendererManager manager) {
                return new RideableDragonRenderer(manager);
            }
        });
    }
}
