package com.silvercraft.silvercraft;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WhiteAshParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SilverAshParticle extends WhiteAshParticle {

    protected SilverAshParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed,
                                float scale, SpriteSet sprites) {
        // 直接调用 WhiteAshParticle 的构造函数，完美继承其上升轨迹和轮廓
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, scale, sprites);
        
        // 强制纯白色
        this.setColor(1.0F, 1.0F, 1.0F); 
    }

    @Override
    public int getLightCoords(float partialTick) {
        // 强制全亮（15级方块光 + 15级天空光），确保在暗处也纯白
        return 15728880; 
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       RandomSource random) {
            return new SilverAshParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.sprites);
        }
    }
}