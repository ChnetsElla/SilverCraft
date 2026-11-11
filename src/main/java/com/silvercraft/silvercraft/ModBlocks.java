package com.silvercraft.silvercraft;

import com.lighthlib.LightHLib;
import com.lighthlib.api.HLight;
import com.lighthlib.api.HLightBlockProperties;
import com.lighthlib.api.HLightDrop;
import com.lighthlib.api.LightCurve;

import java.util.function.Function;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    public static Block register(ResourceKey<Block> blockKey, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, SilverCraft.id(blockKey.identifier().getPath()));
            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void registerOre(String name) {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, SilverCraft.id(name))
        );
    }

    public static void initialize() {
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.DEEPSLATE_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_DEEPSLATE_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.RICH_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.DEEPSLATE_RICH_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_RICH_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_DEEPSLATE_RICH_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SPARSE_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_SPARSE_SILVER_ORE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE));

        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.RAW_SILVER_BLOCK));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_RAW_SILVER_BLOCK));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SILVER_BLOCK));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.OXIDATION_SILVER_BLOCK));        
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SILVER_TORCH));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SILVER_LANTERN));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((creativeTab) -> creativeTab.accept(ModBlocks.SILVER_CHAIN));

        // registerOre("oxidation_silver_ore_placed");
        // registerOre("oxidation_sparse_silver_ore_placed");
        // registerOre("silver_ore_placed");
        // registerOre("sparse_silver_ore_placed");
    }

    public static Block SILVER_ORE = register(ModBlockIds.SILVER_ORE, Block::new ,  BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block DEEPSLATE_SILVER_ORE = register(ModBlockIds.DEEPSLATE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);

    public static Block RICH_SILVER_ORE = register(ModBlockIds.RICH_SILVER_ORE, Block::new , BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block DEEPSLATE_RICH_SILVER_ORE = register(ModBlockIds.DEEPSLATE_RICH_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);

    public static Block RAW_SILVER_BLOCK = register(ModBlockIds.RAW_SILVER_BLOCK, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block OXIDATION_RAW_SILVER_BLOCK = register(ModBlockIds.OXIDATION_RAW_SILVER_BLOCK, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block SILVER_BLOCK = register(ModBlockIds.SILVER_BLOCK, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER_GOLEM_STATUE)
    , true);

    public static Block OXIDATION_SILVER_ORE = register(ModBlockIds.OXIDATION_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block OXIDATION_DEEPSLATE_SILVER_ORE = register(ModBlockIds.OXIDATION_DEEPSLATE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);

    public static Block OXIDATION_RICH_SILVER_ORE = register(ModBlockIds.OXIDATION_RICH_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block OXIDATION_DEEPSLATE_RICH_SILVER_ORE = register(ModBlockIds.OXIDATION_DEEPSLATE_RICH_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);


    // 1. 只声明，不直接赋值（避免静态初始化崩溃）
    public static Block SILVER_TORCH;
    public static Block SILVER_WALL_TORCH;
    public static Block SILVER_LANTERN;

    // 2. 写一个在 onInitialize 阶段调用的注册方法
    public static void registerBlocks() {
        // ---------------------------------------------------
        // 创建光源对象（等级 17，纯白，原版线性衰减）
        // ---------------------------------------------------
        HLight torchLight = HLight.register(new HLight(
            ResourceKey.create(HLight.REGISTRY_KEY, SilverCraft.id("silver_torch_light")),
            new HLightDrop()
                .HlightLevel(state -> 17)
                .HLightColor("#FFFFFF")
                .intensityCurve(LightCurve.MAGIC_EASE) // 原版风格
                .colorCurve(LightCurve.MAGIC_EASE) // 原版风格
        ));

        // 创建灯笼光源对象（等级 18，纯白，原版线性衰减）
        HLight lanternLight = HLight.register(new HLight(
            ResourceKey.create(HLight.REGISTRY_KEY, SilverCraft.id("silver_lantern_light")),
            new HLightDrop()
                .HlightLevel(state -> 18)
                .HLightColor("#FFFFFF")
                .intensityCurve(LightCurve.MAGIC_EASE)
                .colorCurve(LightCurve.MAGIC_EASE)
        ));

        // ---------------------------------------------------
        // 注册方块并绑定 HLight
        // ---------------------------------------------------
        
        // 银火把
        BlockBehaviour.Properties torchProps = BlockBehaviour.Properties.of()
            .noCollision()
            .instabreak()
            .sound(SoundType.WOOD)
            .noOcclusion()
            .pushReaction(PushReaction.POPPED);
            
        torchProps = ((HLightBlockProperties) torchProps).setHLightEntity(torchLight);
        
        SILVER_TORCH = register(ModBlockIds.SILVER_TORCH, 
            SilverTorchBlock::new, 
            torchProps, 
            false // 物品在 ModItems 注册
        );

        // 墙上银火把
        BlockBehaviour.Properties wallTorchProps = BlockBehaviour.Properties.of()
            .noCollision()
            .instabreak()
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.POPPED);
            
        wallTorchProps = ((HLightBlockProperties) wallTorchProps).setHLightEntity(torchLight);
        
        SILVER_WALL_TORCH = register(ModBlockIds.SILVER_WALL_TORCH, 
            SilverWallTorchBlock::new, 
            wallTorchProps, 
            false
        );

        // 银灯笼
        BlockBehaviour.Properties lanternProps = BlockBehaviour.Properties.of()
            .strength(3.5f)
            .sound(SoundType.LANTERN)
            .noOcclusion();
            
        lanternProps = ((HLightBlockProperties) lanternProps).setHLightEntity(lanternLight);
        
        SILVER_LANTERN = register(ModBlockIds.SILVER_LANTERN, 
            LanternBlock::new, 
            lanternProps, 
            true
        );

        registerOre("oxidation_silver_ore_placed");
        registerOre("oxidation_sparse_silver_ore_placed");
        registerOre("silver_ore_placed");
        registerOre("sparse_silver_ore_placed");

    }

    public static Block SILVER_CHAIN = register(ModBlockIds.SILVER_CHAIN,  ChainBlock::new, BlockBehaviour.Properties.of()
        .strength(5.0f, 6.0f)
        .sound(SoundType.CHAIN)
        .noOcclusion()
    , true);

    public static Block SPARSE_SILVER_ORE = register(ModBlockIds.SPARSE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block DEEPSLATE_SPARSE_SILVER_ORE = register(ModBlockIds.DEEPSLATE_SPARSE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);

    public static Block OXIDATION_SPARSE_SILVER_ORE = register(ModBlockIds.OXIDATION_SPARSE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);

    public static Block OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE = register(ModBlockIds.OXIDATION_DEEPSLATE_SPARSE_SILVER_ORE, Block::new, BlockBehaviour.Properties.of()
        .strength(4.5f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.DEEPSLATE)
    , true);

    public static Block OXIDATION_SILVER_BLOCK = register(ModBlockIds.OXIDATION_SILVER_BLOCK, Block::new, BlockBehaviour.Properties.of()
        .strength(3.0f, 3.0f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
    , true);
}
