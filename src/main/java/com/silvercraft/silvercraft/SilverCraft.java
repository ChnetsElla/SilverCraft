package com.silvercraft.silvercraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.Compostable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lighthlib.LightHLib;

public class SilverCraft implements ModInitializer, ClientModInitializer, DataGeneratorEntrypoint  {
	public static final String MOD_ID = "silvercraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

        ModBlocks.registerBlocks();
        ModItems.initialize();
        ModBlockEntities.initialize();
        ModSounds.initialize();
        ModParticles.initialize();
        ModPotions.initialize();
        SilverServerMonsterHandler.register();
        ModItemGroup.initialize(); 
        
	}

	@Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        // 注册客户端粒子事件
        ModParticlesSilverOre2.initialize();

        // 注册条件属性
        ConditionalItemModelProperties.ID_MAPPER.put(
            Identifier.fromNamespaceAndPath("silvercraft", "undead_near"), 
            UndeadNearProperty.CODEC
        );

        // 启动客户端扫描器
        SilverGlowAura.register();

        // 注册粒子的工厂和贴图集
        // 注意：这里我们直接借用原版 white_ash 的贴图集！
        ParticleProviderRegistry.getInstance().register(
            ModParticles.PURE_WHITE_ASH, 
            SilverAshParticle.Provider::new
        );

        ParticleProviderRegistry.getInstance().register(
            ModParticles.SILVER_FLAME, 
            FlameParticle.Provider::new // 复用原版火焰的渲染逻辑
        );

        SilverArmorGlowFeature.register();

        ModToolMaterials.registerClientTooltips();
    }

    /**
     * 初始化数据生成器的方法
     * 这个方法会在数据生成阶段被调用，用于注册各种数据提供器
     * @param fabricDataGenerator Fabric数据生成器实例，用于创建数据包
     */
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // 在这里创建 Pack 并添加你的 Provider
        // 创建一个新的数据包，用于存放所有生成的数据
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        // 示例：添加进度提供器
        // 这行代码被注释掉了，如果要添加进度表数据，可以取消注释
//        pack.addProvider(ModAdvancementProvider::new); 
        // 添加方块战利品表提供器，用于生成方块的战利品表数据
		pack.addProvider(ModBlockLootTableProvider::new);
    }

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
