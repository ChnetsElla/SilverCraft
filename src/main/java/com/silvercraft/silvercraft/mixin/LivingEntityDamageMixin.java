package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.ModSounds;
import com.silvercraft.silvercraft.ModToolMaterials;
import com.silvercraft.silvercraft.SilverRepelHandler;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel; 
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityDamageMixin {
        // 【1. 银具额外伤害】：拦截 hurtServer 方法
    @ModifyVariable(
        method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", 
        at = @At("HEAD"), 
        argsOnly = true, 
        ordinal = 0
    )
    private float modifyIncomingDamage(float amount, ServerLevel level, DamageSource source) {
        // 如果目标（this）是亡灵
        if (((LivingEntity)(Object)this).getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD)) {
            // 且攻击者是玩家
            if (source.getEntity() instanceof Player player) {
                // 且主手拿着银具
                if (player.getMainHandItem().is(ModToolMaterials.SILVER_TOOLS_AND_WEAPONS)) {
                    // 【核心】：额外增加 4 点伤害
                    return amount + ModToolMaterials.SILVER_TOOLS_AND_WEAPONS_ADD_ATTACHS; 
                }
            }
        }
        return amount;
    }

    // 记录激怒状态
    @Inject(method = "actuallyHurt", at = @At("TAIL"))
    private void onHurt(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) { // 参数加上了 ServerLevel
        if ((Object) this instanceof Mob mob && source.getEntity() instanceof Player player) {
            SilverRepelHandler.recordProvocation(mob, player);

            // 【新增】：判断是否是亡灵，且主手拿着银具
            if (mob.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD) 
            && player.getMainHandItem().is(ModToolMaterials.SILVER_TOOLS_AND_WEAPONS)) {
                // 播放嘶嘶声
                level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), 
                    ModSounds.SILVER_FIRES, // 或者用 SoundEvents.FIRE_EXTINGUISH
                    SoundSource.PLAYERS, 
                    0.8f, // 音量
                    1.0f  // 音调
                );


                // 2. 在亡灵的身体中心生成烟雾粒子
                double x = mob.getX();
                double y = mob.getY() + mob.getBbHeight() / 2.0;
                double z = mob.getZ();
                
                // 发送 15 个灰白烟（或你自定义的 PURE_WHITE_ASH）
                level.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE, // 或者 ParticleTypes.SMOKE
                    x, y, z,
                    10,     // 粒子数量
                    0.2, 0.2, 0.2, // 随机偏移量
                    0.07    // 粒子运动速度
                );
                
                // （可选）添加几个火星粒子，增加“灼烧感”
                level.sendParticles(
                    ParticleTypes.FLAME,
                    x, y, z,
                    7, 
                    0.2, 0.2, 0.2, 
                    0.02
                );
            }
        }
    }
}