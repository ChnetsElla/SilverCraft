package com.silvercraft.silvercraft;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class UndeadNearProperty implements ConditionalItemModelProperty {
    public static final UndeadNearProperty INSTANCE = new UndeadNearProperty();
    public static final MapCodec<UndeadNearProperty> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return CODEC;
    }

    @Override
    public boolean get(ItemStack stack, ClientLevel world, LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return SilverGlowState.shouldGlow(entity);
    }

    private UndeadNearProperty() {}
}