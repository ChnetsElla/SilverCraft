package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverRepelHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobTargetMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void onSetTarget(LivingEntity target, CallbackInfo ci) {
        if (SilverRepelHandler.shouldBlockTarget((Mob) (Object) this, target)) {
            ci.cancel(); // 打断仇恨设置
        }
    }
}