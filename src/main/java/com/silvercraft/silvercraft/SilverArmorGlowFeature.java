package com.silvercraft.silvercraft;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class SilverArmorGlowFeature extends RenderLayer<AvatarRenderState, PlayerModel> {

    private static final Identifier GLOW_HUMANOID = 
        Identifier.fromNamespaceAndPath("silvercraft", "textures/entity/equipment/humanoid/silver_glowmask.png");
    private static final Identifier GLOW_LEGGINGS = 
        Identifier.fromNamespaceAndPath("silvercraft", "textures/entity/equipment/humanoid_leggings/silver_glowmask.png");

    private static final int FULL_BRIGHT_LIGHT = 15728880; 
    private static final int GLOW_TINT = 0xFFFFFFFF;

    private final ArmorModelSet<HumanoidModel<AvatarRenderState>> armorModels;

    public SilverArmorGlowFeature(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, 
                                  ArmorModelSet<HumanoidModel<AvatarRenderState>> armorModels) {
        super(renderer);
        this.armorModels = armorModels;
    }

        @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!SilverGlowState.isGlowing(state.id)) return;

        // 【核心修复】：把整体放大 1.02 倍，防止发光层被藏在里面
        poseStack.pushPose();
        poseStack.scale(1.02F, 1.02F, 1.02F);

        // 1. 头盔（独立判断）
        if (state.headEquipment.is(SilverGlowAura.SILVER_ARMORS)) {
            submitNodeCollector.submitModel(
                    armorModels.head(), 
                    state, poseStack,
                    RenderTypes.armorCutoutNoCull(GLOW_HUMANOID),
                    FULL_BRIGHT_LIGHT, OverlayTexture.NO_OVERLAY,
                    GLOW_TINT, null, state.outlineColor
            );
        }

        // 2. 胸甲（独立判断）
        if (state.chestEquipment.is(SilverGlowAura.SILVER_ARMORS)) {
            submitNodeCollector.submitModel(
                    armorModels.chest(), 
                    state, poseStack,
                    RenderTypes.armorCutoutNoCull(GLOW_HUMANOID),
                    FULL_BRIGHT_LIGHT, OverlayTexture.NO_OVERLAY,
                    GLOW_TINT, null, state.outlineColor
            );
        }

        // 3. 护腿（独立判断，使用第二层贴图）
        if (state.legsEquipment.is(SilverGlowAura.SILVER_ARMORS)) {
            submitNodeCollector.submitModel(
                    armorModels.legs(), 
                    state, poseStack,
                    RenderTypes.armorCutoutNoCull(GLOW_LEGGINGS),
                    FULL_BRIGHT_LIGHT, OverlayTexture.NO_OVERLAY,
                    GLOW_TINT, null, state.outlineColor
            );
        }

        // 4. 靴子（独立判断）
        if (state.feetEquipment.is(SilverGlowAura.SILVER_ARMORS)) {
            submitNodeCollector.submitModel(
                    armorModels.feet(), 
                    state, poseStack,
                    RenderTypes.armorCutoutNoCull(GLOW_HUMANOID),
                    FULL_BRIGHT_LIGHT, OverlayTexture.NO_OVERLAY,
                    GLOW_TINT, null, state.outlineColor
            );
        }

        poseStack.popPose();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof AvatarRenderer<?> avatarRenderer) {
                // 烘焙专门用于 64x32 盔甲 UV 的模型
                ArmorModelSet<HumanoidModel<AvatarRenderState>> armorModels = ModelLayers.PLAYER_ARMOR.map(
                    layerLocation -> new HumanoidModel<>(context.getModelSet().bakeLayer(layerLocation))
                );
                registrationHelper.register((RenderLayer) new SilverArmorGlowFeature((AvatarRenderer) avatarRenderer, armorModels));
            }
        });
    }
}