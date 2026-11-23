package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 银甲套装防护机制（需四件全装银甲）：
 *
 * 机制一【中毒防护】：中毒尝试扣血时不损失生命值，
 * 保留原版中毒扣血动画（红闪、受击音效），血条维持中毒黄绿色，
 * 并以消耗银甲耐久作为补偿——中毒II每次扣血尝试消耗7点耐久，中毒I消耗3点。
 *
 * 机制二【低生命自愈】：生命值低于6点（3颗心）时每半秒回复1点直至6点，
 * 之后每4秒回复1点直至10点（5颗心）。
 * 两种机制触发时，血条都会呈现与再生药水相同的心形波动起伏动画（见 HudMixin）。
 */
public final class SilverArmorGuard {
    // === 机制一：中毒防护耐久消耗 ===
    /** 中毒I（放大器0）：每次尝试扣血消耗的耐久 */
    public static final int POISON_I_DURABILITY_COST = 3;
    /** 中毒II及以上（放大器>=1）：每次尝试扣血消耗的耐久 */
    public static final int POISON_II_DURABILITY_COST = 7;

    // === 机制二：低生命自愈 ===
    /** 生命低于此值进入快速回复（每半秒1点） */
    public static final float FAST_REGEN_BELOW = 6.0F;
    /** 最终回复目标（每4秒1点，直到此值） */
    public static final float REGEN_TARGET = 10.0F;
    /** 快速回复间隔：0.5秒 = 10 tick */
    public static final int FAST_REGEN_INTERVAL_TICKS = 10;
    /** 缓慢回复间隔：4秒 = 80 tick */
    public static final int SLOW_REGEN_INTERVAL_TICKS = 80;

    private static final EquipmentSlot[] ARMOR_SLOTS = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    /** 耐久轮转指针：每次消耗后推进，保证四件银甲磨损均匀 */
    private static int durabilityRotation = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                tickRegen(player);
            }
        });
    }

    // ---------- 机制一：中毒防护 ----------

    /**
     * 由 PoisonEffectMixin 在中毒尝试扣血时调用。
     * 全装银甲则拦截本次伤害：不扣血，改为消耗耐久并广播原版扣血动画。
     *
     * @param amplifier 中毒放大器（0=中毒I，1=中毒II）
     * @return true 表示已拦截本次伤害
     */
    public static boolean tryBlockPoisonDamage(ServerPlayer player, ServerLevel level, int amplifier) {
        if (!hasFullSilverArmor(player)) return false;

        // 中毒II及以上消耗7点耐久，中毒I消耗3点
        int cost = amplifier >= 1 ? POISON_II_DURABILITY_COST : POISON_I_DURABILITY_COST;
        consumeArmorDurability(player, cost);

        // 广播原版伤害事件：客户端会播放与真实扣血完全一致的动画
        // （红屏闪烁、受击音效、模型受击扰动），但生命值不变
        level.broadcastDamageEvent(player, player.damageSources().magic());
        return true;
    }

    /**
     * 将耐久消耗按轮转顺序均摊到四件银甲上：每件每次扣1点，
     * 耗尽的护甲件正常破损（含破损音效）。轮转起点每次推进，使磨损均匀。
     */
    private static void consumeArmorDurability(ServerPlayer player, int totalCost) {
        if (totalCost <= 0) return;

        int start = durabilityRotation % ARMOR_SLOTS.length;
        int remaining = totalCost;
        while (remaining > 0) {
            boolean consumedThisRound = false;
            for (int i = 0; i < ARMOR_SLOTS.length && remaining > 0; i++) {
                EquipmentSlot slot = ARMOR_SLOTS[(start + i) % ARMOR_SLOTS.length];
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.is(ModToolMaterials.SILVER_ARMORS)) {
                    stack.hurtAndBreak(1, player, slot);
                    remaining--;
                    consumedThisRound = true;
                }
            }
            // 护甲已破损不全、无件可扣时终止
            if (!consumedThisRound) break;
        }
        durabilityRotation = (durabilityRotation + 1) % ARMOR_SLOTS.length;
    }

    // ---------- 机制二：低生命自愈 ----------

    /**
     * 每个服务器 tick 结束时检查全装银甲的玩家：
     * 生命 &lt; 6 点：每 0.5 秒回复 1 点（快速阶段）；
     * 6 ~ 10 点：每 4 秒回复 1 点（缓慢阶段），直到 10 点为止。
     * 借助 player.tickCount 对齐回复节拍，无需维护额外状态。
     */
    private static void tickRegen(ServerPlayer player) {
        if (!player.isAlive() || player.isSpectator()) return;
        if (!hasFullSilverArmor(player)) return;

        float health = player.getHealth();
        if (health >= REGEN_TARGET) return;

        if (health < FAST_REGEN_BELOW) {
            // 快速阶段：每半秒回1点，直到6点
            if (player.tickCount % FAST_REGEN_INTERVAL_TICKS == 0) {
                player.heal(1.0F);
            }
        } else {
            // 缓慢阶段：每4秒回1点，直到10点
            if (player.tickCount % SLOW_REGEN_INTERVAL_TICKS == 0) {
                player.heal(1.0F);
            }
        }
    }

    // ---------- 公共判定 ----------

    /** 四件盔甲位是否全部为银甲（套装判定） */
    public static boolean hasFullSilverArmor(Player player) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (!player.getItemBySlot(slot).is(ModToolMaterials.SILVER_ARMORS)) return false;
        }
        return true;
    }

    /**
     * 客户端心形波动判定（HudMixin 调用）：
     * 全装银甲且处于中毒防护中，或处于低生命自愈阶段（生命值未回到10点）时，
     * 血条呈现与再生药水相同的心形波动起伏动画。
     */
    public static boolean shouldShowHeartWave(Player player) {
        if (!hasFullSilverArmor(player)) return false;
        // 中毒防护中：中毒状态被银甲抵御
        if (player.hasEffect(MobEffects.POISON)) return true;
        // 自愈中：生命值仍在回复途中
        return player.getHealth() < REGEN_TARGET;
    }

    private SilverArmorGuard() {}
}
