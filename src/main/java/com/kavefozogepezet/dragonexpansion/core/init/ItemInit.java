package com.kavefozogepezet.dragonexpansion.core.init;

import com.kavefozogepezet.dragonexpansion.DragonExpansion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DragonExpansion.MOD_ID);

    public static final RegistryObject<Item> EXAMPLE_ITEM =
            ITEMS.register("example_item", ()->new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
}
