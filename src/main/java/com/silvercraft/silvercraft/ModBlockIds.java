package com.silvercraft.silvercraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class ModBlockIds {

    public static ResourceKey<Block> create(String name) {

        ResourceKey<Block> ResourceBlock = ResourceKey.create(Registries.BLOCK, SilverCraft.id(name));
        return ResourceBlock;
    }

    public static final ResourceKey<Block> RICH_SILVER_ORE = create("rich_silver_ore");

    public static final ResourceKey<Block> SILVER_ORE = create("silver_ore");

    public static final ResourceKey<Block> DEEPSLATE_RICH_SILVER_ORE = create("deepslate_rich_silver_ore");

    public static final ResourceKey<Block> DEEPSLATE_SILVER_ORE = create("deepslate_silver_ore");
    
    public static final ResourceKey<Block> RAW_SILVER_BLOCK =  create("raw_silver_block");

    public static final ResourceKey<Block> OXIDATION_RAW_SILVER_BLOCK = create("oxidation_raw_silver_block");

    public static final ResourceKey<Block> SILVER_BLOCK = create("silver_block");

    public static final ResourceKey<Block> OXIDATION_SILVER_ORE = create("oxidation_silver_ore");

    public static final ResourceKey<Block> OXIDATION_DEEPSLATE_SILVER_ORE = create("oxidation_deepslate_silver_ore");

    public static final ResourceKey<Block> SILVER_TORCH = create("silver_torch");

    public static final ResourceKey<Block> SILVER_WALL_TORCH = create("silver_wall_torch");

    public static final ResourceKey<Block> SILVER_LANTERN = create("silver_lantern");

    public static final ResourceKey<Block> SILVER_CHAIN  = create("silver_chain");

    public static final ResourceKey<Block> OXIDATION_RICH_SILVER_ORE = create("oxidation_rich_silver_ore");

    public static final ResourceKey<Block> OXIDATION_DEEPSLATE_RICH_SILVER_ORE = create("oxidation_deepslate_rich_silver_ore");

    public static final ResourceKey<Block> SPARSE_SILVER_ORE = create("sparse_silver_ore");

    public static final ResourceKey<Block> DEEPSLATE_SPARSE_SILVER_ORE = create("deepslate_sparse_silver_ore");

    public static final ResourceKey<Block> OXIDATION_SPARSE_SILVER_ORE = create("oxidation_sparse_silver_ore");

    public static final ResourceKey<Block> OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE = create("oxidation_deepslate_sparse_silver_ore");

    public static final ResourceKey<Block> OXIDATION_SILVER_BLOCK = create("oxidation_silver_block");

}
