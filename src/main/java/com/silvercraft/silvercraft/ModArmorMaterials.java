package com.silvercraft.silvercraft;

import java.util.Map;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class ModArmorMaterials {
    // 1. 定义修复物品的标签 (Tag)
    public static final TagKey<Item> REPAIRS_SILVER_ARMOR = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath("silvercraft", "repairs_silver_armor")
    );

    // 2. 定义装备资产键 (EquipmentAsset Key)，游戏用它来查找穿戴贴图
    public static final ResourceKey<EquipmentAsset> SILVER_ARMOR_ASSET = ResourceKey.create(
        EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath("silvercraft", "silver")
    );

    // 3. 创建 ArmorMaterial 实例
    public static final ArmorMaterial SILVER_MATERIAL = new ArmorMaterial(
        12, // 基础耐久度 (BASE_DURABILITY)
        Map.of( // 各部位防御力 (头盔, 胸甲, 护腿, 靴子)
            ArmorType.HELMET, 2,
            ArmorType.CHESTPLATE, 6,
            ArmorType.LEGGINGS, 4,
            ArmorType.BOOTS, 1
        ),
        28,   // 附魔能力
        SoundEvents.ARMOR_EQUIP_IRON, // 穿戴音效
        1.0F, // 盔甲韧性 (Toughness)
        0.0F, // 击退抗性 (Knockback Resistance)
        REPAIRS_SILVER_ARMOR, // 修复物品标签
        SILVER_ARMOR_ASSET    // 装备资产键
    );
}
