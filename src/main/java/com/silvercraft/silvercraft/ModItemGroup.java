package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {
    public static ResourceKey<CreativeModeTab> create(String name) {
        ResourceKey<CreativeModeTab> resourceGroup = ResourceKey.create(Registries.CREATIVE_MODE_TAB, SilverCraft.id("silver_group"));
        return resourceGroup;
    }

    public static final ResourceKey<CreativeModeTab> SILVER_GROUP = create("silver_group");

    public static final CreativeModeTab SILVER_TAB  = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(ModItems.SILVER_PICKAXE))
        .title(Component.translatable("itemGroup.silvercraft.silver_group"))
        .displayItems((params, output) -> {
            output.accept(ModBlocks.SILVER_ORE);
            output.accept(ModBlocks.DEEPSLATE_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_DEEPSLATE_SILVER_ORE);
            output.accept(ModBlocks.SPARSE_SILVER_ORE);
            output.accept(ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_SPARSE_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE);
            output.accept(ModBlocks.RICH_SILVER_ORE);
            output.accept(ModBlocks.DEEPSLATE_RICH_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_RICH_SILVER_ORE);
            output.accept(ModBlocks.OXIDATION_DEEPSLATE_RICH_SILVER_ORE);
            output.accept(ModBlocks.RAW_SILVER_BLOCK);
            output.accept(ModBlocks.OXIDATION_RAW_SILVER_BLOCK);
            output.accept(ModBlocks.SILVER_BLOCK);
            output.accept(ModBlocks.OXIDATION_SILVER_BLOCK);
            output.accept(ModItems.RAW_SILVER);
            output.accept(ModItems.SILVER_INGOT);
            output.accept(ModItems.SILVER_NUGGET);
            output.accept(ModItems.OXIDATION_RAW_SILVER);
            output.accept(ModItems.OXIDATION_SILVER_INGOT);
            output.accept(ModItems.OXIDATION_SILVER_NUGGET);
            output.accept(ModItems.SILVER_SHOVEL);
            output.accept(ModItems.SILVER_PICKAXE);
            output.accept(ModItems.SILVER_AXE);
            output.accept(ModItems.SILVER_HOE);
            output.accept(ModItems.SILVER_SWORD);
            output.accept(ModItems.SILVER_SPEAR);
            output.accept(ModItems.UNBLESSED_SILVER_SWORD);
            output.accept(ModItems.SILVER_HELMET);
            output.accept(ModItems.SILVER_CHESTPLATE);
            output.accept(ModItems.SILVER_LEGGINGS);
            output.accept(ModItems.SILVER_BOOTS);
            output.accept(ModItems.SILVER_TORCH);
            output.accept(ModBlocks.SILVER_LANTERN);
            output.accept(ModBlocks.SILVER_CHAIN);
            output.accept(ModItems.SILVER_APPLE);
        }) .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SILVER_GROUP, SILVER_TAB);
    }
}
