package com.silvercraft.silvercraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SilverWallTorchBlock extends WallTorchBlock {

    public SilverWallTorchBlock(Properties properties) {
        super(ParticleTypes.FLAME, properties); // 父类随便填，因为我们要重写
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;

        // 【核心】：生成你的纯白火焰粒子！
        level.addParticle(ModParticles.SILVER_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        
        // 如果你想要白烟，可以加上原版烟雾
//        level.addParticle(ParticleTypes.SMOKE, x, y + 0.2D, z, 0.0D, 0.0D, 0.0D);
    }

}
