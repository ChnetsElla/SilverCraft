package com.silvercraft.silvercraft;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class SilverServerMonsterHandler {
    private static final Map<UUID, BlockPos> activeLights = new HashMap<>();

    // 【新增】：银质武器/工具的标签
    public static final TagKey<Item> SILVER_TOOLS = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath("silvercraft", "silver_tools_and_weapons")
    );

        public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (!player.isAlive() || player.isSpectator()) {
                    removeLight(player);
                    continue;
                }

                if (!wearsSilverArmor(player) && !holdsSilverTool(player)) {
                    removeLight(player);
                    continue;
                }

                ServerPlayConnectionEvents.DISCONNECT.register((handler, quitServer) -> {
                    // 换个变量名，比如 quittingPlayer
                    ServerPlayer quittingPlayer = handler.getPlayer();
                    removeLight(quittingPlayer);
                    LightCleaner.clear16ChunksAsync(player);
                });
                

                AABB scan = player.getBoundingBox().inflate(ModToolMaterials.GROWING_XZ_ACRION, ModToolMaterials.GROWING_Y_ACRION,ModToolMaterials.GROWING_XZ_ACRION);
                var undeads = player.level().getEntitiesOfClass(Mob.class, scan,
                        mob -> mob.isAlive() && mob.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD));

                boolean trigger = false;
                if (!undeads.isEmpty()) {
                    double maxFollow = undeads.stream()
                            .mapToDouble(m -> m.getAttributeValue(Attributes.FOLLOW_RANGE))
                            .max().orElse(35.0);
                    double closestDist = undeads.stream()
                            .mapToDouble(m -> m.distanceTo(player))
                            .min().orElse(Double.MAX_VALUE);
                    trigger = closestDist <= maxFollow;
                }

                if (trigger) {
                    placeLight(player);
                } else {
                    removeLight(player);
                }
            }
        });
    }

    private static boolean wearsSilverArmor(ServerPlayer player) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (player.getItemBySlot(slot).is(ModToolMaterials.SILVER_ARMORS)) return true;
        }
        return false;
    }

    // 【新增】：检查主手和副手
    private static boolean holdsSilverTool(ServerPlayer player) {
        return player.getMainHandItem().is(SILVER_TOOLS) || player.getOffhandItem().is(SILVER_TOOLS);
    }

    private static void placeLight(ServerPlayer player) {
        BlockPos newPos = player.blockPosition();
        BlockPos oldPos = activeLights.get(player.getUUID());

        if (oldPos != null && oldPos.equals(newPos)) return;

        if (oldPos != null && player.level().getBlockState(oldPos).is(Blocks.LIGHT)) {
            player.level().setBlockAndUpdate(oldPos, Blocks.AIR.defaultBlockState());
        }

        // 只有纯空气才能放置光源，防止水流被堵
        if (player.level().getBlockState(newPos).isAir()) {
            player.level().setBlockAndUpdate(newPos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 7));
            activeLights.put(player.getUUID(), newPos);
        }
    }

    private static void removeLight(ServerPlayer player) {
        BlockPos oldPos = activeLights.remove(player.getUUID());
        if (oldPos != null && player.level().getBlockState(oldPos).is(Blocks.LIGHT)) {
            player.level().setBlockAndUpdate(oldPos, Blocks.AIR.defaultBlockState());
        }
    }
}

class LightCleaner  {
    public static void clear16ChunksAsync(ServerPlayer player) {
        ServerLevel level = player.level();
        
        // 【核心修复】：直接通过玩家坐标 >> 4 计算出区块中心坐标
        // 这样就完全不需要去碰 ChunkPos 那些私有字段了
        int centerChunkX = player.blockPosition().getX() >> 4;
        int centerChunkZ = player.blockPosition().getZ() >> 4;
        
        List<ChunkAccess> chunksToScan = new ArrayList<>();
        
        // 遍历 4x4 共 16 个区块
        for (int cx = centerChunkX - 2; cx <= centerChunkX + 1; cx++) {
            for (int cz = centerChunkZ - 2; cz <= centerChunkZ + 1; cz++) {
                // 直接获取区块对象
                ChunkAccess chunk = level.getChunk(cx, cz);
                if (chunk != null) {
                    chunksToScan.add(chunk);
                }
            }
        }

        if (chunksToScan.isEmpty()) return;

        // 替换为你 Lib 里的 HLIGHT 方块判断
        Predicate<BlockState> isHlight = state -> state.is(Blocks.LIGHT);

        // 多线程异步扫描
        CompletableFuture.supplyAsync(() -> {
            List<BlockPos> foundLights = new ArrayList<>();

            for (ChunkAccess chunk : chunksToScan) {
                // 直接调用原版的 findBlocks，它返回的 pos 就是绝对世界坐标！
                chunk.findBlocks(isHlight, (pos, state) -> {
                    // 必须调用 immutable，否则拿到的全是同一个坐标对象
                    foundLights.add(pos.immutable()); 
                });
            }
            return foundLights;
            
        }).thenAcceptAsync(foundLights -> {
            // 回到主线程安全地删方块
            level.getServer().execute(() -> {
                for (BlockPos pos : foundLights) {
                    if (level.getBlockState(pos).is(Blocks.LIGHT)) {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
                if (!foundLights.isEmpty()) {
                    System.out.println("[AaksouLib] 异步清理了 " + foundLights.size() + " 个残留光源！");
                }
            });
        });
    }
}