package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ModParticlesSilverOre2 {

   /** 附近玩家检测半径（格） */
    private static final double DETECTION_RADIUS = 8.0;
    /** 附近粒子生成间隔（tick），20 tick = 1秒 */
    private static final int NEARBY_PARTICLE_INTERVAL = 40;
    /** 破坏时粒子生成概率（0.0 ~ 1.0） */
    private static final float BREAK_PARTICLE_CHANCE = 0.7f;

    public static void initialize() {
        registerBreakParticles();
    }

    private static void registerBreakParticles() {
         ClientTickEvents.START_CLIENT_TICK.register(client -> {
            Player player = client.player;
            MultiPlayerGameMode gameMode = client.gameMode;
            if (player == null || gameMode == null || client.level == null) return;

            // 关键：判断玩家是否正在破坏方块
            if (!gameMode.isDestroying()) return;

            // 获取玩家瞄准的方块
            if (!(client.hitResult instanceof BlockHitResult hit)) return;
            BlockPos pos = hit.getBlockPos();
            BlockState state = client.level.getBlockState(pos);

            if (!isSliverOre(state)) return;

            if (client.level.getGameTime() % NEARBY_PARTICLE_INTERVAL != 0) return;

            if (Math.random() >= BREAK_PARTICLE_CHANCE) return;

            // 每 tick 生成 1~2 个粒子（按住越久越多，可加进度判断）
            int count = 1 + (int) (Math.random() * 2);

            for (int i = 0; i < count; i++) {
                client.level.addParticle(
                    ParticleTypes.END_ROD,
                    pos.getX() + 0.5 + (Math.random() - 0.5) * 0.8,
                    pos.getY() + 0.5 + (Math.random() - 0.5) * 0.8,
                    pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.8,
                    0.0, 0.15, 0.0
                );
            }
        });
    }

    private static boolean isSliverOre(BlockState state) {
        return state.is(ModBlocks.RICH_SILVER_ORE) || state.is(ModBlocks.DEEPSLATE_RICH_SILVER_ORE)
            || state.is(ModBlocks.RAW_SILVER_BLOCK) || state.is(ModBlocks.SILVER_BLOCK)
            || state.is(ModBlocks.SILVER_ORE) || state.is(ModBlocks.DEEPSLATE_SILVER_ORE)
            || state.is(ModBlocks.SPARSE_SILVER_ORE) || state.is(ModBlocks.DEEPSLATE_SPARSE_SILVER_ORE);
    }

}
