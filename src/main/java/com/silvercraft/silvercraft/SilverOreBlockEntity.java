package com.silvercraft.silvercraft;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Crackiness.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SilverOreBlockEntity extends BlockEntity  {
    public SilverOreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SILVER_ORE_ENTITY, pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        // 直接交给 ModParticles 处理
        ModParticlesSilverOre.initialize();
    }
}
