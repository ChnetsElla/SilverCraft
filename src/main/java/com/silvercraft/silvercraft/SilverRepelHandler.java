package com.silvercraft.silvercraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.*;

import java.util.*;

public final class SilverRepelHandler {
    public static final boolean SUPPRESS_UNDEAD_AGGRO = true;
    public static final boolean SILVER_MECHANIC_ENABLED = true;

    private static final int EVAL_INTERVAL = 4;
    private static final double FLEE_DIST = 6.0;
    private static final double FLEE_SPEED = 1.1;
    private static final double CORNER_PRESS_RANGE = 4.0;
    private static final int EVALS_TO_CORNER = 15;
    private static final double STALL_MOVE = 0.35;

    // 惩罚参数
    public static final int BOW_INTERVAL_MULTIPLIER = 3;
    public static final int MELEE_COOLDOWN_MULTIPLIER = 4;
    public static final int MELEE_INTERVAL_TICKS = 20 * MELEE_COOLDOWN_MULTIPLIER;
    public static final double MELEE_DAMAGE_REDUCTION = 0.70;

    private static final Identifier MELEE_DREAD_ID = Identifier.fromNamespaceAndPath("silvercraft", "silver_melee_dread");
    private static final AttributeModifier MELEE_DREAD = new AttributeModifier(MELEE_DREAD_ID, -MELEE_DAMAGE_REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private static int tickCounter = 0;

    private static final HashMap<Integer, Long> NEXT_MELEE_SWING = new HashMap<>();
    private static final HashMap<Integer, Long> NEXT_RANGED_SHOT = new HashMap<>();
    private static final HashMap<Integer, Long> FLEE_NEXT_SHOT = new HashMap<>();
    private static final HashMap<Integer, Set<UUID>> PROVOKED = new HashMap<>();
    private static final HashMap<Integer, CornerState> CORNER_STATE = new HashMap<>();

    private static final class CornerState {
        double lastX, lastZ;
        int stalledEvals;
        boolean cornered;
        CornerState(Mob mob) { this.lastX = mob.getX(); this.lastZ = mob.getZ(); }
    }

    public static void tick(Mob mob) {
        if (!(mob.level() instanceof ServerLevel server)) return;
        if (!mob.isAlive() || !isRepelAffected(mob)) return;

        // 检查并清理激怒状态，顺带判断是否依然处于激怒中
        boolean provoked = reconcileProvoked(mob, server);

        // 如果不是激怒状态，且没在角落反咬，强行清除对银甲玩家的仇恨
        if (SUPPRESS_UNDEAD_AGGRO && !provoked && !isCornered(mob)) {
            LivingEntity target = mob.getTarget();
            if (isSilverWearer(target)) mob.setTarget(null);
        }

        if (!SILVER_MECHANIC_ENABLED) return;
        if (++tickCounter % EVAL_INTERVAL != 0) return;
        
        // 如果处于激怒状态，不再处理逃跑逻辑，直接返回（交由原版追击）
        if (provoked) {
            holdProvoked(mob);
            return;
        }

        LivingEntity threat = null;
        double nearestDistSq = Double.MAX_VALUE;
        for (Player player : server.players()) {
            if (!player.isAlive() || player.isSpectator()) continue;
            double range = repelRangeFor(player, mob);
            if (range <= 0) continue;
            double distSq = player.distanceToSqr(mob);
            if (distSq <= range * range && distSq < nearestDistSq) {
                nearestDistSq = distSq;
                threat = player;
            }
        }

        if (threat != null) {
            handleRepelLogic(mob, threat);
        } else {
            CORNER_STATE.remove(mob.getId());
            removeDreadModifier(mob);
        }
    }

    private static void handleRepelLogic(Mob mob, LivingEntity threat) {
        CornerState st = CORNER_STATE.computeIfAbsent(mob.getId(), k -> new CornerState(mob));
        boolean pressed = threat.distanceToSqr(mob) <= CORNER_PRESS_RANGE * CORNER_PRESS_RANGE;
        
        double moved = Math.hypot(mob.getX() - st.lastX, mob.getZ() - st.lastZ);
        st.lastX = mob.getX();
        st.lastZ = mob.getZ();
        
        if (moved < STALL_MOVE) st.stalledEvals++;
        else st.stalledEvals = 0;

        if (st.cornered && !pressed) {
            st.cornered = false;
            removeDreadModifier(mob);
        }
        if (!st.cornered && pressed && st.stalledEvals >= EVALS_TO_CORNER) {
            st.cornered = true;
            st.stalledEvals = 0;
        }

        if (st.cornered) {
            // 绝路反咬：锁定目标
            if (mob.getTarget() != threat) {
                mob.setTarget(threat);
            }
            addDreadModifier(mob);
        } else {
            // 恐惧逃跑
            removeDreadModifier(mob);
            if (mob.getTarget() == threat) mob.setTarget(null);
            
            // 计算逃跑向量
            double dx = mob.getX() - threat.getX();
            double dz = mob.getZ() - threat.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len > 0.001) {
                double scale = FLEE_DIST / len;
                mob.getNavigation().moveTo(mob.getX() + dx * scale, mob.getY(), mob.getZ() + dz * scale, FLEE_SPEED);
            }
            mob.getLookControl().setLookAt(threat);
            
            // 驱动逃跑中的回身反击
            fleeCounterShot(mob, threat, mob.level().getGameTime());
        }
    }

    private static void fleeCounterShot(Mob mob, LivingEntity threat, long time) {
        if (!(mob instanceof RangedAttackMob shooter)) return;
        
        if (!holdsRangedWeapon(mob) || !mob.getSensing().hasLineOfSight(threat)) return;
        
        Long next = FLEE_NEXT_SHOT.get(mob.getId());
        if (next != null && time < next) return;

        // 设置下一次射击时间（9~12 秒）
        FLEE_NEXT_SHOT.put(mob.getId(), time + 180 + mob.getRandom().nextInt(60));
        
        // 1. 停下脚步
        mob.getNavigation().stop();
        
        // 2. 转身看向玩家
        mob.getLookControl().setLookAt(threat, 30.0F, 30.0F);
        
        // 3. 强制同步身体朝向
        mob.setYRot(mob.getYHeadRot());
        
        // 4. 射出这一箭
        shooter.performRangedAttack(threat, 0.95F);
    }

    public static boolean allowMeleeAttack(Mob mob, LivingEntity target) {
        if (!SILVER_MECHANIC_ENABLED || !isSilverWearer(target) || isProvoked(mob)) {
            NEXT_MELEE_SWING.remove(mob.getId());
            return true;
        }
        long now = mob.level().getGameTime();
        Long next = NEXT_MELEE_SWING.get(mob.getId());
        if (next != null && now < next) return false;
        NEXT_MELEE_SWING.put(mob.getId(), now + MELEE_INTERVAL_TICKS);
        return true;
    }

    public static boolean isShotPenalized(LivingEntity shooter) {
        if (!SILVER_MECHANIC_ENABLED) return false;
        if (!(shooter instanceof Mob mob)) return false;
        return !isProvoked(mob) && isSilverWearer(mob.getTarget());
    }

    public static boolean shouldBlockTarget(Mob mob, LivingEntity target) {
        if (!SUPPRESS_UNDEAD_AGGRO || isProvoked(mob) || !isSilverWearer(target)) return false;
        return isRepelAffected(mob);
    }

    public static void recordProvocation(Mob mob, Player player) {
        if (!isRepelAffected(mob) || !player.isAlive() || countSilverPieces(player) <= 0) return;
        PROVOKED.computeIfAbsent(mob.getId(), k -> new HashSet<>()).add(player.getUUID());
    }

    // 【这就是你缺失的 reconcileProvoked，现已加入死后不记仇的规则】
    private static boolean reconcileProvoked(Mob mob, ServerLevel server) {
        Set<UUID> provokers = PROVOKED.get(mob.getId());
        if (provokers == null || provokers.isEmpty()) return false;

        // 【修复 1】：使用正确的常量名
        boolean forgiveDeadPlayers = server.getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS);

        Iterator<UUID> it = provokers.iterator();
        while (it.hasNext()) {
            UUID uuid = it.next();
            
            // 【修复 2】：通过 server.getServer().getPlayerList() 获取玩家
            ServerPlayer player = server.getServer().getPlayerList().getPlayer(uuid);

            if (player == null) {
                it.remove(); // 离线直接清
                continue;
            }
            if (player.isSpectator() || countSilverPieces(player) <= 0) {
                it.remove(); // 脱甲或旁观直接清
                continue;
            }
            if (!player.isAlive()) {
                if (forgiveDeadPlayers) {
                    it.remove(); // 规则为死后不记仇 -> 清除
                }
                continue; // 规则为死后记仇 -> 保留
            }
        }

        if (provokers.isEmpty()) {
            PROVOKED.remove(mob.getId());
            removeDreadModifier(mob);
            return false;
        }
        return true;
    }

    private static void holdProvoked(Mob mob) {
        CORNER_STATE.remove(mob.getId());
        if (isSilverWearer(mob.getTarget())) addDreadModifier(mob);
        else removeDreadModifier(mob);
    }

    private static void addDreadModifier(Mob mob) {
        AttributeInstance inst = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null && !inst.hasModifier(MELEE_DREAD_ID)) inst.addTransientModifier(MELEE_DREAD);
    }

    private static void removeDreadModifier(Mob mob) {
        AttributeInstance inst = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (inst != null && inst.hasModifier(MELEE_DREAD_ID)) inst.removeModifier(MELEE_DREAD_ID);
    }

    public static boolean isProvoked(Mob mob) {
        Set<UUID> provokers = PROVOKED.get(mob.getId());
        return provokers != null && !provokers.isEmpty();
    }

    public static boolean isCornered(Mob mob) {
        CornerState st = CORNER_STATE.get(mob.getId());
        return st != null && st.cornered;
    }

    private static boolean isRepelAffected(Mob mob) {
        if (!mob.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD)) return false;
        return mob.getType() != EntityTypes.WITHER && mob.getType() != EntityTypes.ENDER_DRAGON;
    }

    public static boolean isSilverWearer(LivingEntity entity) {
        if (!(entity instanceof Player p) || !p.isAlive()) return false;
        return countSilverPieces(p) > 0;
    }

    private static int countSilverPieces(Player player) {
        int count = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (player.getItemBySlot(slot).is(ModToolMaterials.SILVER_ARMORS)) count++;
        }
        return count;
    }

    private static double repelRangeFor(Player player, Mob mob) {
        int pieces = countSilverPieces(player);
        if (pieces <= 0) return 0;
        return mob.getAttributeValue(Attributes.FOLLOW_RANGE) * pieces / 4.0;
    }

    private static boolean holdsRangedWeapon(Mob mob) {
        if (mob instanceof AbstractSkeleton) {
            return mob.isHolding(Items.BOW);
        }
        if (mob instanceof Drowned) {
            return mob.isHolding(Items.TRIDENT);
        }
        return false;
    }
}