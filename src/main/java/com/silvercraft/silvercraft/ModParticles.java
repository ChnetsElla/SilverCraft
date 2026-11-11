package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModParticles {
    // 使用 FabricParticleTypes.simple() 来创建，避免泛型不匹配
    public static final SimpleParticleType PURE_WHITE_ASH = FabricParticleTypes.simple();

    public static final SimpleParticleType SILVER_FLAME = FabricParticleTypes.simple();

    public static void initialize() {
        // 直接注册即可，Fabric 的 simple() 返回类型已适配 Registry.register
        Registry.register(
            BuiltInRegistries.PARTICLE_TYPE, 
            Identifier.fromNamespaceAndPath("silvercraft", "pure_white_ash"), 
            PURE_WHITE_ASH
        );

        Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            Identifier.fromNamespaceAndPath("silvercraft", "silver_flame"), 
            SILVER_FLAME
        );
    }

}