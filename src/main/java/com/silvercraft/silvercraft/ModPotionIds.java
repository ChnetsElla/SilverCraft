package com.silvercraft.silvercraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotionIds {
    public static final ResourceKey<Potion> TATER_POTION = create("tater");

    private static ResourceKey<Potion> create(String name) {
        Identifier id = SilverCraft.id(name);
        return ResourceKey.create(Registries.POTION, id);
    }
}
