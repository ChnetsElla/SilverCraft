package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class ModParticlesSilverOre {
    /** 附近玩家检测半径（格） */
    private static final double DETECTION_RADIUS = 8.0;
    /** 附近粒子生成间隔（tick），20 tick = 1秒 */
    private static final int NEARBY_PARTICLE_INTERVAL = 40;
    /** 破坏时粒子生成概率（0.0 ~ 1.0） */
    private static final float BREAK_PARTICLE_CHANCE = 0.3f;

    public static void initialize() {
        registerBreakParticles();
    }

    private static void registerBreakParticles() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (!(level instanceof ServerLevel serverLevel)) return true;
            if (!isSilverOre(state)) return true;
            if (serverLevel.getRandom().nextFloat() < BREAK_PARTICLE_CHANCE) {
                serverLevel.sendParticles(ParticleTypes.CLOUD,
                    pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                    3, 0.3, 0.1, 0.3, 0.01
                );
            }
            return true;
        });
    }

      private static boolean isSilverOre(BlockState state) {
        return state.is(ModBlocks.RICH_SILVER_ORE) || state.is(ModBlocks.DEEPSLATE_RICH_SILVER_ORE);
    }
}
