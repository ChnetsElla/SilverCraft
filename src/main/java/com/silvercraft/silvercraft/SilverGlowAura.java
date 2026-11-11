package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public final class SilverGlowAura {
    private static int tickCounter = 0;
    private static int soundCounter = 0;

    public static final TagKey<Item> SILVER_TOOLS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("silvercraft", "silver_tools_and_weapons"));
    public static final TagKey<Item> SILVER_ARMORS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("silvercraft", "silver_armors"));

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(SilverGlowAura::tick);
    }

    private static void tick(Minecraft client) {
        if (++tickCounter < 2) return;
        tickCounter = 0;

        ClientLevel world = client.level;
        Player self = client.player;
        if (world == null || self == null) {
            SilverGlowState.clear();
            return;
        }

        Set<Integer> glowing = new HashSet<>();
        AABB scanBox = self.getBoundingBox().inflate(ModToolMaterials.GROWING_XZ_ACRION, ModToolMaterials.GROWING_Y_ACRION, ModToolMaterials.GROWING_XZ_ACRION);

        for (Player player : world.getEntitiesOfClass(Player.class, scanBox, p -> !p.isSpectator())) {
            boolean hasSilver = holdsSilverTool(player) || wearsSilverArmor(player);
            if (hasSilver && isUndeadNear(world, player)) {
                glowing.add(player.getId());
                spawnParticles(world, player, client);
            }
        }
        SilverGlowState.setGlowingPlayers(glowing);
    }

    private static boolean holdsSilverTool(Player player) {
        return player.getMainHandItem().is(SILVER_TOOLS) || player.getOffhandItem().is(SILVER_TOOLS);
    }

    private static boolean wearsSilverArmor(Player player) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (player.getItemBySlot(slot).is(SILVER_ARMORS)) return true;
        }
        return false;
    }

    private static boolean isUndeadNear(ClientLevel world, Player player) {
        AABB scan = player.getBoundingBox().inflate(ModToolMaterials.GROWING_XZ_ACRION, ModToolMaterials.GROWING_Y_ACRION, ModToolMaterials.GROWING_XZ_ACRION);
        var undeads = world.getEntitiesOfClass(Mob.class, scan, 
            mob -> mob.isAlive() && mob.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD));
        
        if (undeads.isEmpty()) return false;
        
        double maxFollow = undeads.stream().mapToDouble(m -> m.getAttributeValue(Attributes.FOLLOW_RANGE)).max().orElse(35.0);
        double closestDist = undeads.stream().mapToDouble(m -> m.distanceTo(player)).min().orElse(Double.MAX_VALUE);
        return closestDist <= maxFollow * 1.1;
    }

    private static void spawnParticles(ClientLevel world, Player player, Minecraft client) {
        if (holdsSilverTool(player)) {
            Vec3 anchor = weaponAnchor(client, world, player, client.player);
            for (int i = 0; i < 2; i++) {
                world.addParticle(ModParticles.PURE_WHITE_ASH, 
                    anchor.x + (world.getRandom().nextDouble() - 0.5) * 0.2, 
                    anchor.y + (world.getRandom().nextDouble() - 0.5) * 0.2, 
                    anchor.z + (world.getRandom().nextDouble() - 0.5) * 0.2, 
                    0.0, 0.12, 0.0);
            }
        }
        if (wearsSilverArmor(player)) {
            AABB box = player.getBoundingBox();
            for (int i = 0; i < 2; i++) {
                world.addParticle(ModParticles.PURE_WHITE_ASH, 
                    box.minX + world.getRandom().nextDouble() * (box.maxX - box.minX), 
                    box.minY + 0.6 + world.getRandom().nextDouble() * 0.8, 
                    box.minZ + world.getRandom().nextDouble() * (box.maxZ - box.minZ), 
                    0.0, 0.12, 0.0);
            }
        }
    }

    private static Vec3 weaponAnchor(Minecraft client, ClientLevel world, Player holder, Player self) {
        var random = world.getRandom();
        if (holder == self && client.options.getCameraType().isFirstPerson()) {
            Vec3 look = holder.getViewVector(1.0F);
            Vec3 right = new Vec3(-look.z, 0.0, look.x); 
            return holder.getEyePosition().add(look.scale(0.62)).add(right.scale(0.36)).add(0.0, -0.42, 0.0);
        }
        Vec3 handOffset = holder.getHandHoldingItemAngle(holder.getMainHandItem().getItem());
        return new Vec3(holder.getX(), holder.getY(), holder.getZ()).add(handOffset).add(0.0, 1.15, 0.0);
    }

    private SilverGlowAura() {}
}