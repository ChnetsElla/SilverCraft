package com.silvercraft.silvercraft;

import java.rmi.registry.Registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        ResourceKey<Item> ResourceItem = ResourceKey.create(Registries.ITEM, SilverCraft.id(name));
        return ResourceItem;
    }

    public static final ResourceKey<Item> SUSPICIOUS_SUBSTANCE = create("suspicious_substance");

    public static final ResourceKey<Item> SILVER_APPLE = create("silver_apple");

    //银系基础物品

    public static final ResourceKey<Item> RAW_SILVER = create("raw_silver");

    public static final ResourceKey<Item> SILVER_INGOT = create("silver_ingot");

    public static final ResourceKey<Item> SILVER_NUGGET = create("silver_nugget");

    //银质工具与武器
    public static final ResourceKey<Item> UNBLESSED_SILVER_SWORD = create("unblessed_silver_sword");

    public static final ResourceKey<Item> SILVER_SWORD = create("silver_sword");

    public static final ResourceKey<Item> SILVER_SPEAR = create("silver_spear");

    public static final ResourceKey<Item> SILVER_PICKAXE = create("silver_pickaxe");

    public static final ResourceKey<Item> SILVER_AXE = create("silver_axe");

    public static final ResourceKey<Item> SILVER_SHOVEL = create("silver_shovel");

    public static final ResourceKey<Item> SILVER_HOE = create("silver_hoe");

    //银质盔甲
    public static final ResourceKey<Item> SILVER_HELMET = create("silver_helmet");

    public static final ResourceKey<Item> SILVER_CHESTPLATE = create("silver_chestplate");

    public static final ResourceKey<Item> SILVER_LEGGINGS = create("silver_leggings");

    public static final ResourceKey<Item> SILVER_BOOTS = create("silver_boots");

    //污秽的
    public static final ResourceKey<Item> OXIDATION_RAW_SILVER = create("oxidation_raw_silver");

    public static final ResourceKey<Item> OXIDATION_SILVER_NUGGET = create("oxidation_silver_nugget");

    public static final ResourceKey<Item> OXIDATION_SILVER_INGOT = create("oxidation_silver_ingot");


    public static final ResourceKey<Item> SILVER_TORCH = create("silver_torch");
}
