package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.common.tiles.RideableDragonEggTE;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeInit {
    public static final DeferredRegister<TileEntityType<?>> TILES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DragonExpansion.MOD_ID);

    public static final RegistryObject<TileEntityType<RideableDragonEggTE>> RIDEABLE_DRAGON_EGG_TE = TILES.register(
            "rideable_dragon_egg_te", () -> TileEntityType.Builder.of(RideableDragonEggTE::new,
                    BlockInit.RIDEABLE_DRAGON_EGG.get()
            ).build(null)
    );
}
