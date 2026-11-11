package com.silvercraft.silvercraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class SilverCombatHandler {

    public static final TagKey<Item> SILVER_TOOLS = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath("silvercraft", "silver_tools_and_weapons")
    );

    /**
     * 由 Mixin 在伤害应用前调用。
     * 如果攻击者是手持银具的玩家，且目标是亡灵，则额外增加 3 点伤害。
     */
    public static float modifyDamage(LivingEntity target, DamageSource source, float originalAmount) {
        if (!target.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD)) return originalAmount;
        if (!(source.getEntity() instanceof Player player)) return originalAmount;
        if (!player.getMainHandItem().is(SILVER_TOOLS)) return originalAmount;
        return originalAmount + 10.0F;
    }

    private SilverCombatHandler() {}
}