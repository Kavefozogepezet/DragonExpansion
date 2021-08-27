package com.kavefozogepezet.dragonexpansion.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BlockItemNonStackable extends BlockItem {
    public BlockItemNonStackable(Block blockIn, ItemGroup itemGroup) {
        super(blockIn, new Item.Properties()
                .tab(itemGroup)
                .stacksTo(1))
        ;
    }
}
