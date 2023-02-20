package com.teamresourceful.highlight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HighlightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        String namespace = "highlight";
        ModContainer container = FabricLoader.getInstance().getModContainer("highlight").get();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ModContainer generatedContainer = FabricLoader.getInstance().getAllMods()
                    .stream()
                    .filter(mc -> mc.getMetadata().getId().startsWith("generated_"))
                    .findFirst()
                    .orElse(null);
            if (generatedContainer != null) {
                namespace = generatedContainer.getMetadata().getId();
                container = generatedContainer;
            }
        }

        ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation(namespace, "highlight_extended"),
                container,
                Component.literal("Highlight Extended"),
                ResourcePackActivationType.NORMAL
        );
    }
}
