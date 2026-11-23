package com.silvercraft.silvercraft.mixin;

import com.silvercraft.silvercraft.SilverArmorGuard;
import net.minecraft.client.gui.Hud;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Hud.class)
public class HudMixin {
    /**
     * 心形波动动画：原版仅在玩家拥有再生效果时，血条心形才会产生波动起伏。
     * 这里将判定扩展——全装银甲且处于中毒防护（血条为中毒黄绿色）或
     * 低生命自愈阶段时，同样驱动该波动动画，效果与再生药水完全一致。
     */
    @Redirect(
        method = "extractPlayerHealth",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/core/Holder;)Z"
        )
    )
    private boolean silvercraft$silverHeartWave(Player player, Holder<MobEffect> effect) {
        // 原版判定优先：真实再生效果仍然生效
        if (player.hasEffect(effect)) return true;
        // 银甲防护判定：仅在检查再生效果时附加银甲波动
        return effect == MobEffects.REGENERATION && SilverArmorGuard.shouldShowHeartWave(player);
    }
}
