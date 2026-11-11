package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverRepelHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public abstract class DrownedShootMixin {

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void onDrownedShoot(LivingEntity target, float pullProgress, CallbackInfo ci) {
        Drowned drowned = (Drowned) (Object) this;

        if (SilverRepelHandler.isShotPenalized(drowned)) {
            // 如果处于恐惧惩罚状态，有 60% 的概率被吓得不敢扔出三叉戟
            if (drowned.getRandom().nextFloat() < 0.6F) {
                ci.cancel(); // 直接取消这次攻击
                return;
            }
            
            // 如果强行扔出去了，身体因为发抖，偏航角度极大
            drowned.setYRot(drowned.getYRot() + (drowned.getRandom().nextFloat() - 0.5F) * 60.0F);
            drowned.setYHeadRot(drowned.getYRot());
        }
    }
}