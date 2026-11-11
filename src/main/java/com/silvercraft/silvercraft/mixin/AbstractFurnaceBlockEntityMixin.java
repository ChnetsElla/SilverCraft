package com.silvercraft.silvercraft.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    // 秽银类物品标签
    private static final TagKey<Item> IMPURE_SILVER_TAG = TagKey.create(
        net.minecraft.core.registries.Registries.ITEM,
        Identifier.fromNamespaceAndPath("silvercraft", "impure_silver_items")
    );

    // 【完美匹配源码的签名】：(NonNullList, int, ItemStack)
    @Inject(
        method = "canBurn(Lnet/minecraft/core/NonNullList;ILnet/minecraft/world/item/ItemStack;)Z", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private static void restrictImpureSilverFuel(
            NonNullList<ItemStack> items, 
            int maxStackSize, 
            ItemStack burnResult, 
            CallbackInfoReturnable<Boolean> cir) {

        // 确保物品列表至少有输入槽(0)和燃料槽(1)
        if (items.size() < 2) return;

        ItemStack input = items.get(0);
        ItemStack fuel = items.get(1);

        // 规则：如果输入的是秽银，但燃料不是木炭，直接返回 false 阻止烧炼
        if (input.is(IMPURE_SILVER_TAG) && !fuel.is(Items.CHARCOAL)) {
            cir.setReturnValue(false);
        }
    }
}