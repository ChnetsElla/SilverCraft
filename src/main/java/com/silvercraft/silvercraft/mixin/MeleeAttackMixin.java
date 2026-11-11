package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverRepelHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob; // 【新增导入】
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MeleeAttackGoal.class)
public class MeleeAttackMixin {
    // 【核心修复】：将 Mob 改为 PathfinderMob，与源码完全一致
    @Shadow @Final protected PathfinderMob mob; 

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void onCanUse(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        LivingEntity target = mob.getTarget();
        
        // 如果目标是穿银甲的玩家，并且未被激怒，则施加冷却惩罚
        if (target != null && SilverRepelHandler.isSilverWearer(target) && !SilverRepelHandler.isProvoked(mob)) {
            if (!SilverRepelHandler.allowMeleeAttack(mob, target)) {
                cir.setReturnValue(false); // 强制取消本次攻击
            }
        }
    }
}