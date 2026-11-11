package com.silvercraft.silvercraft;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {

    protected ModBlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        // 参数1：要掉落原矿的方块；参数2：掉落物
        add(ModBlocks.SILVER_ORE, createOreDrop(ModBlocks.SILVER_ORE, ModItems.RAW_SILVER));
        add(ModBlocks.DEEPSLATE_SILVER_ORE, createOreDrop(ModBlocks.DEEPSLATE_SILVER_ORE, ModItems.RAW_SILVER));

        add(ModBlocks.SPARSE_SILVER_ORE, createOreDrop(ModBlocks.SPARSE_SILVER_ORE, ModItems.SILVER_NUGGET));
        add(ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE, createOreDrop(ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE, ModItems.SILVER_NUGGET));

        add(ModBlocks.DEEPSLATE_RICH_SILVER_ORE, LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ContextIntProviders.exactly(1))
            .add(LootItem.lootTableItem(ModItems.RAW_SILVER)
            // 使用 ContextIntProviders.between 控制 2~5 个
            .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 5)))))
        );

        add(ModBlocks.RICH_SILVER_ORE, LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ContextIntProviders.exactly(1))
            .add(LootItem.lootTableItem(ModItems.RAW_SILVER)
            // 使用 ContextIntProviders.between 控制 2~5 个
            .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 5)))))
        );

        //污秽矿石掉落设置
        add(ModBlocks.OXIDATION_SILVER_ORE, createOreDrop(ModBlocks.OXIDATION_SILVER_ORE, ModItems.OXIDATION_RAW_SILVER));
        add(ModBlocks.OXIDATION_DEEPSLATE_SILVER_ORE, createOreDrop(ModBlocks.OXIDATION_DEEPSLATE_SILVER_ORE, ModItems.OXIDATION_RAW_SILVER));

        add(ModBlocks.OXIDATION_SPARSE_SILVER_ORE, createOreDrop(ModBlocks.OXIDATION_SPARSE_SILVER_ORE, ModItems.OXIDATION_SILVER_NUGGET));
        add(ModBlocks.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE, createOreDrop(ModBlocks.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE, ModItems.OXIDATION_SILVER_NUGGET));

        add(ModBlocks.OXIDATION_RICH_SILVER_ORE, LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ContextIntProviders.exactly(1))
            .add(LootItem.lootTableItem(ModItems.OXIDATION_RAW_SILVER)
            // 使用 ContextIntProviders.between 控制 2~5 个
            .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 5)))))
        );

        add(ModBlocks.OXIDATION_DEEPSLATE_RICH_SILVER_ORE, LootTable.lootTable().withPool(LootPool.lootPool()
            .setRolls(ContextIntProviders.exactly(1))
            .add(LootItem.lootTableItem(ModItems.OXIDATION_RAW_SILVER)
            // 使用 ContextIntProviders.between 控制 2~5 个
            .apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 5)))))
        );

        dropSelf(ModBlocks.RAW_SILVER_BLOCK);
        dropSelf(ModBlocks.SILVER_BLOCK);
    }
}