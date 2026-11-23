package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverArmorGuard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.PoisonMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoisonMobEffect.class)
public class PoisonEffectMixin {
    /**
     * 机制一：全装银甲的玩家中毒尝试扣血时不损失生命值（血条保持中毒黄绿色），
     * 保留原版扣血动画，改为消耗银甲耐久（中毒II=7点/次，中毒I=3点/次）。
     * 与原版一致：生命值 <= 1 时中毒不再尝试扣血，因此也不消耗耐久。
     */
    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    private void silvercraft$blockPoisonDamage(ServerLevel level, LivingEntity mob, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if (mob instanceof ServerPlayer player && player.getHealth() > 1.0F) {
            if (SilverArmorGuard.tryBlockPoisonDamage(player, level, amplifier)) {
                cir.setReturnValue(true);
            }
        }
    }
}
