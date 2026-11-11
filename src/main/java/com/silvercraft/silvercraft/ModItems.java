package com.silvercraft.silvercraft;

import java.util.Optional;
import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public class ModItems {
    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties setting) {
        // Create the item instance.
        Item item = itemFactory.apply(setting.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        // // Get the event for modifying entries in the ingredients group.
        // // And register an event handler that adds our suspicious item to the ingredients group.
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.SUSPICIOUS_SUBSTANCE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_APPLE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.RAW_SILVER));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_INGOT));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_NUGGET));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.OXIDATION_RAW_SILVER));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.OXIDATION_SILVER_INGOT));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(ModItems.OXIDATION_SILVER_NUGGET));
        // //工具武器注册:工具
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_PICKAXE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_AXE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_SHOVEL));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_HOE));
        // //工具武器注册：武器
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_SWORD));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_SPEAR));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_AXE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.UNBLESSED_SILVER_SWORD));
        // //盔甲注册
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_HELMET));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_CHESTPLATE));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_LEGGINGS));
        // CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register((creativeTab) -> creativeTab.accept(ModItems.SILVER_BOOTS));       
    }
    
    public static final Item SUSPICIOUS_SUBSTANCE = register(ModItemIds.SUSPICIOUS_SUBSTANCE, Item::new, new Item.Properties()
        .component(DataComponents.COMPOSTABLE,new Compostable(new ResolvableInt.Constant(100)))
        .component(DataComponents.COOKING_FUEL,  new CookingFuel(new ResolvableInt.Constant(120 * 20),new ResolvableFloat.Constant(1.0f)))
    );

    public static final Item SILVER_APPLE = register(ModItemIds.SILVER_APPLE, Item::new , new Item.Properties()
        .component(DataComponents.FOOD, new FoodProperties
            .Builder()
            .nutrition(10)
            .saturationModifier(0.3f)
            .alwaysEdible()
            .build()
        )
        .component(DataComponents.CONSUMABLE, Consumable
            .builder()
            .consumeSeconds(1.5f)
            .animation(ItemUseAnimation.EAT)
            .sound(SoundEvents.ARMOR_EQUIP_CHAIN)
            .soundAfterConsume(SoundEvents.BREEZE_WIND_CHARGE_BURST)
            .hasConsumeParticles(true)
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON,700,1),1.0f))
            .build()
        ));

    public static final Item RAW_SILVER = register(ModItemIds.RAW_SILVER, Item::new , new Item.Properties());

    public static final Item SILVER_INGOT = register(ModItemIds.SILVER_INGOT, Item::new , new Item.Properties());

    public static final Item SILVER_NUGGET = register(ModItemIds.SILVER_NUGGET, Item::new, new Item.Properties());

    //工具与武器
    public static final Item UNBLESSED_SILVER_SWORD = register(ModItemIds.UNBLESSED_SILVER_SWORD, Item::new, new Item.Properties()
        .sword(ModToolMaterials.SILVER, 1.0f, -2.4f));

    public static final Item SILVER_SWORD = register(ModItemIds.SILVER_SWORD, Item::new, new Item.Properties()
        .sword(ModToolMaterials.SILVER, 2.0f, -2.4f));
    
    public static final Item SILVER_SPEAR = register(ModItemIds.SILVER_SPEAR, Item::new, new Item.Properties()
        .spear(ModToolMaterials.SILVER, 
            0.35f,  // 2. attackDuration: 攻击速度修饰符
            2.02f,  // 3. damageMultiplier: 动能伤害倍率
            0.35f,  // 4. delay: 蓄力延迟（秒）
            7.0f,   // 5. dismountTime: 下马窗口（秒）
            6.0f,   // 6. dismountThreshold: 下马速度阈值
            3.25f,  // 7. knockbackTime: 击退窗口（秒）
            3.1f,   // 8. knockbackThreshold: 击退速度阈值
            21.5f,  // 9. damageTime: 伤害窗口（秒）
            2.3f    // 10. damageThreshold: 伤害速度阈值
        ));

    public static final Item SILVER_PICKAXE = register(ModItemIds.SILVER_PICKAXE,Item::new, new Item.Properties()
        .pickaxe(ModToolMaterials.SILVER, 0.0f, -2.8f));

    public static final Item SILVER_AXE = register(ModItemIds.SILVER_AXE, Item::new, new Item.Properties()
        .axe(ModToolMaterials.SILVER, 6.0f, -3.0f));

    public static final Item SILVER_SHOVEL = register(ModItemIds.SILVER_SHOVEL, Item::new, new Item.Properties()
        .shovel(ModToolMaterials.SILVER, 0.5f, -3.0f));

    public static final Item SILVER_HOE = register(ModItemIds.SILVER_HOE, Item::new, new Item.Properties()
        .hoe(ModToolMaterials.SILVER, -1.0f, -1.0f));
    
    //盔甲
    public static final Item SILVER_HELMET = register(ModItemIds.SILVER_HELMET, Item::new, new Item.Properties()
        .humanoidArmor(ModArmorMaterials.SILVER_MATERIAL, ArmorType.HELMET)
        .durability(ArmorType.HELMET.getDurability(ModArmorMaterials.SILVER_MATERIAL.durability()))
    );

    public static final Item SILVER_CHESTPLATE = register(ModItemIds.SILVER_CHESTPLATE, Item::new, new Item.Properties()
        .humanoidArmor(ModArmorMaterials.SILVER_MATERIAL, ArmorType.CHESTPLATE)
        .durability(ArmorType.CHESTPLATE.getDurability(ModArmorMaterials.SILVER_MATERIAL.durability()))
    );

    public static final Item SILVER_LEGGINGS = register(ModItemIds.SILVER_LEGGINGS, Item::new, new Item.Properties()
        .humanoidArmor(ModArmorMaterials.SILVER_MATERIAL, ArmorType.LEGGINGS)
        .durability(ArmorType.LEGGINGS.getDurability(ModArmorMaterials.SILVER_MATERIAL.durability()))
    );

    public static final Item SILVER_BOOTS = register(ModItemIds.SILVER_BOOTS, Item::new, new Item.Properties()
        .humanoidArmor(ModArmorMaterials.SILVER_MATERIAL, ArmorType.BOOTS)
        .durability(ArmorType.BOOTS.getDurability(ModArmorMaterials.SILVER_MATERIAL.durability()))
    );

    public static final Item OXIDATION_RAW_SILVER = register(ModItemIds.OXIDATION_RAW_SILVER, Item::new, new Item.Properties());

    public static final Item OXIDATION_SILVER_NUGGET = register(ModItemIds.OXIDATION_SILVER_NUGGET, Item::new, new Item.Properties());

    public static final Item OXIDATION_SILVER_INGOT = register(ModItemIds.OXIDATION_SILVER_INGOT,Item::new, new Item.Properties());


    public static final Item SILVER_TORCH = register(
        ModItemIds.SILVER_TORCH, 
            
        (properties) -> new StandingAndWallBlockItem(
        ModBlocks.SILVER_TORCH,       // 地面火把
        ModBlocks.SILVER_WALL_TORCH,  // 墙面火把
        Direction.DOWN,               // 墙面附着方向（原版火把固定朝下）
        properties
        ),
           
        new Item.Properties().useBlockDescriptionPrefix() 
    );
    
}
