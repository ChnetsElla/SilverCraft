package com.silvercraft.silvercraft;

import net.minecraft.world.entity.LivingEntity;
import java.util.Set;

public final class SilverGlowState {
    private static volatile Set<Integer> glowingPlayerIds = Set.of();

    public static void setGlowingPlayers(Set<Integer> ids) {
        glowingPlayerIds = Set.copyOf(ids);
    }

    public static void clear() {
        glowingPlayerIds = Set.of();
    }

    // 给物品模型条件使用
    public static boolean shouldGlow(LivingEntity entity) {
        return entity != null && glowingPlayerIds.contains(entity.getId());
    }

    // 【新增】给盔甲渲染层使用（接收 int）
    public static boolean isGlowing(int entityId) {
        return glowingPlayerIds.contains(entityId);
    }

    private SilverGlowState() {}
}