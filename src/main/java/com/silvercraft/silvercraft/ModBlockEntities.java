package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<SilverOreBlockEntity> SILVER_ORE_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        SilverCraft.id("silver_ore"),
        FabricBlockEntityTypeBuilder.create(
            SilverOreBlockEntity::new,
            ModBlocks.RICH_SILVER_ORE,
            ModBlocks.DEEPSLATE_RICH_SILVER_ORE,
            ModBlocks.RAW_SILVER_BLOCK,
            ModBlocks.SILVER_BLOCK,
            ModBlocks.SILVER_ORE,
            ModBlocks.DEEPSLATE_SILVER_ORE,
            ModBlocks.OXIDATION_SILVER_ORE,
            ModBlocks.OXIDATION_DEEPSLATE_SILVER_ORE,
            ModBlocks.OXIDATION_RICH_SILVER_ORE,
            ModBlocks.OXIDATION_DEEPSLATE_RICH_SILVER_ORE,
            ModBlocks.SPARSE_SILVER_ORE,
            ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE,
            ModBlocks.OXIDATION_SPARSE_SILVER_ORE,
            ModBlocks.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE,
            ModBlocks.OXIDATION_RAW_SILVER_BLOCK,
            ModBlocks.OXIDATION_SILVER_BLOCK
        ).build()
    );

    public static void initialize() {
        
    }
}