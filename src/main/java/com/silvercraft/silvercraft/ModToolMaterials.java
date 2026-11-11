package com.silvercraft.silvercraft;

import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ModToolMaterials {

    public static final int SILVER_TOOLS_AND_WEAPONS_ADD_ATTACHS = 7;

    public static final int GROWING_XZ_ACRION = 32;

    public static final int GROWING_Y_ACRION = 16;

    public static final DataComponentType<String> CUSTOM_TOOLTIP_TOOLS = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath("silvercraft", "custom_tooltip"),
        DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    // 定义"无法正确采集"的方块标签，决定工具的挖掘等级
    public static final TagKey<Block> INCORRECT_FOR_SILVER_TOOL = TagKey.create(
        Registries.BLOCK, 
        Identifier.fromNamespaceAndPath("silvercraft", "incorrect_for_silver_tool"));

    // 修复物品标签（铁砧修复用）
     public static final TagKey<Item> REPAIRS_SILVER_TOOL = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath("silvercraft", "repairs_silver_tool")
    );

    public static final TagKey<Item> SILVER_ARMORS = TagKey.create(
        Registries.ITEM, 
        Identifier.fromNamespaceAndPath("silvercraft", "silver_armors")
    );

    public static final TagKey<Item> SILVER_TOOLS_AND_WEAPONS = TagKey.create(
        Registries.ITEM, 
        Identifier.fromNamespaceAndPath("silvercraft", "silver_tools_and_weapons")
    );

    public static final ToolMaterial SILVER = new ToolMaterial(
        ModToolMaterials.INCORRECT_FOR_SILVER_TOOL,// 无法采集的方块标签
        167,                                // 耐久度（参考：铁为250）
        14,                                     // 挖掘速度（参考：铁为6.0）
        1.0f,                     //攻击力加成
        24,                        //附魔能力
        ModToolMaterials.REPAIRS_SILVER_TOOL         //修复物品标签
    );

    @Environment(EnvType.CLIENT)
    public static void registerClientTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
            // 如果物品属于银质工具/武器标签
            if (stack.is(SILVER_TOOLS_AND_WEAPONS)) {
                // 动态读取常量，并添加灰色提示
                lines.add(Component.translatable(" %s tooltip.silvercraft.silver_erosion".formatted(SILVER_TOOLS_AND_WEAPONS_ADD_ATTACHS))
                    .withStyle(ChatFormatting.DARK_GREEN));
            }
        });
    }
}

