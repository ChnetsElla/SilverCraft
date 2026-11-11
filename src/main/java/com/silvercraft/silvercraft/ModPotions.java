package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class ModPotions {
    public static final Holder<Potion> TATER_POTION = Registry.registerForHolder(BuiltInRegistries.POTION, ModPotionIds.TATER_POTION,
        new Potion("tater", new MobEffectInstance(MobEffects.HASTE, 3600, 0))
    );

    public static ItemStack createPotionStack(Holder<Potion> potion) {
        ItemStack stack = new ItemStack(Items.POTION);

        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return stack;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> output.accept(createPotionStack(TATER_POTION)));
    }
}