package com.silvercraft.silvercraft;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    // 注册音效事件
    public static final SoundEvent SILVER_FIRES = SoundEvent.createVariableRangeEvent(
        Identifier.fromNamespaceAndPath("silvercraft", "silver_fires")
    );

    public static void initialize() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, SILVER_FIRES.location(), SILVER_FIRES);
    }
}