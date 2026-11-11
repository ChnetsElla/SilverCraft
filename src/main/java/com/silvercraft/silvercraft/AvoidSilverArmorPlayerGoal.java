package com.silvercraft.silvercraft;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.EnumSet;

public class AvoidSilverArmorPlayerGoal extends Goal {
    private final PathfinderMob mob;
    private final float distance;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;
    private Player playerToAvoid;

    public AvoidSilverArmorPlayerGoal(PathfinderMob mob, float distance, double walkSpeed, double sprintSpeed) {
        this.mob = mob;
        this.distance = distance;
        this.walkSpeedModifier = walkSpeed;
        this.sprintSpeedModifier = sprintSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 找最近的玩家
        Player nearestPlayer = this.mob.level().getNearestPlayer(this.mob, this.distance);
        if (nearestPlayer != null && wearsSilverArmor(nearestPlayer)) {
            this.playerToAvoid = nearestPlayer;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.playerToAvoid != null && this.playerToAvoid.isAlive() && this.mob.distanceToSqr(this.playerToAvoid) < (this.distance * this.distance);
    }

    @Override
    public void start() {
        // 【核心修复 1】：清空原版仇恨，防止它一边想打你，一边想跑，导致原地抽搐
        this.mob.setTarget(null);
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.playerToAvoid == null) return;
        
        this.mob.getLookControl().setLookAt(this.playerToAvoid, 30.0F, 30.0F);
        
        // 【核心修复 2】：如果距离较近或者被卡住，强制面向反方向并加速
        double d = this.mob.distanceToSqr(this.playerToAvoid.getX(), this.playerToAvoid.getY(), this.playerToAvoid.getZ());
        
        if (d < 16.0D) { // 4格以内
            // 计算逃跑方向
            double dx = this.mob.getX() - this.playerToAvoid.getX();
            double dz = this.mob.getZ() - this.playerToAvoid.getZ();
            double length = Math.sqrt(dx * dx + dz * dz);
            if (length > 0.001) {
                dx /= length;
                dz /= length;
            }
            
            // 强制移动到远离玩家 6 格的位置
            this.mob.getNavigation().moveTo(
                this.mob.getX() + dx * 6.0D, 
                this.mob.getY(), 
                this.mob.getZ() + dz * 6.0D, 
                d < 4.0D ? this.sprintSpeedModifier : this.walkSpeedModifier
            );
        }
    }

    private static boolean wearsSilverArmor(Player player) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            // 这里为了以防万一，直接硬编码检测，防止标签未加载
            if (stack.getItem() == ModItems.SILVER_HELMET || 
                stack.getItem() == ModItems.SILVER_CHESTPLATE || 
                stack.getItem() == ModItems.SILVER_LEGGINGS || 
                stack.getItem() == ModItems.SILVER_BOOTS ||
                stack.is(SilverGlowAura.SILVER_ARMORS)) { 
                return true;
            }
        }
        return false;
    }
}