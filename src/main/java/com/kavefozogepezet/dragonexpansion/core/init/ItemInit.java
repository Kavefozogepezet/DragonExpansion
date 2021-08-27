package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import com.kavefozogepezet.dragonexpansion.common.items.BlockItemNonStackable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.kavefozogepezet.dragonexpansion.core.init.BlockInit.RIDEABLE_DRAGON_EGG;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DragonExpansion.MOD_ID);

    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register(
            "example_item", ()->new Item(new Item.Properties().tab(ItemGroup.TAB_MISC))
    );
    public static final RegistryObject<Item> RIDEABLE_DRAGON_EGG_ITEM = ITEMS.register(
            "rideable_dragon_egg", () -> new BlockItemNonStackable(RIDEABLE_DRAGON_EGG.get(), ItemGroup.TAB_MISC)
    );
}
