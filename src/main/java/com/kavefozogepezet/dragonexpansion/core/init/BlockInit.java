package com.kavefozogepezet.dragonexpansion.core.init;

import com.google.common.io.Closer;
import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.common.blocks.RideableDragonEgg;
import com.kavefozogepezet.dragonexpansion.common.items.BlockItemNonStackable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonExpansion.MOD_ID);

    public static final RegistryObject<Block> RIDEABLE_DRAGON_EGG = BLOCKS.register(
            "rideable_dragon_egg", RideableDragonEgg::new
    );
}
