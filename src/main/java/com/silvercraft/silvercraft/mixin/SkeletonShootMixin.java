package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverRepelHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class SkeletonShootMixin {

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void onSkeletonShoot(LivingEntity target, float pullProgress, CallbackInfo ci) {
        AbstractSkeleton skeleton = (AbstractSkeleton) (Object) this;
        
        if (SilverRepelHandler.isShotPenalized(skeleton)) {
            // 【核心修改】：不搞强行偏转。因为被吓到了，有 40% 概率直接吓到拉不开弓（取消射击）
            if (skeleton.getRandom().nextFloat() < 0.4F) {
                ci.cancel(); 
                // 播放一个弓弦弹回的音效模拟手抖
                skeleton.level().playSound(null, skeleton.blockPosition(), net.minecraft.sounds.SoundEvents.ARROW_HIT_PLAYER, net.minecraft.sounds.SoundSource.HOSTILE, 0.5F, 2.0F);
            }
        }
    }
}