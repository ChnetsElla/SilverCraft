package com.silvercraft.silvercraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SilverTorchBlock extends TorchBlock {

    public SilverTorchBlock(Properties properties) {
        super(ParticleTypes.FLAME, properties); // 父类随便填，因为我们要重写
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.7;
		double z = pos.getZ() + 0.5;
		level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
		level.addParticle(ModParticles.SILVER_FLAME, x, y, z, 0.0, 0.0, 0.0);
    }

}