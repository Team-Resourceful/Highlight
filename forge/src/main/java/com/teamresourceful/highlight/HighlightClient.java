package com.teamresourceful.highlight;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class HighlightClient {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(HighlightClient::onRegisterPackFinder);
    }

    private static void onRegisterPackFinder(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo info = ModList.get().getModFileById("highlight");

            if (!FMLLoader.isProduction()) {
                for (IModInfo mod : ModList.get().getMods()) {
                    if (mod.getModId().startsWith("generated_")) {
                        info = mod.getOwningFile();
                        break;
                    }
                }
            }

            Path resourcePath = info.getFile().findResource("resourcepacks/highlight_extended");

            final Pack pack = Pack.readMetaAndCreate(
                    "builtin/add_pack_finders_test", Component.literal("Highlight Extended"),
                    false,
                    (path) -> new PathPackResources(path, true, resourcePath),
                    PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, new PackSource() {
                        @Override
                        public @NotNull Component decorate(@NotNull Component arg) {
                            return PackSource.NO_DECORATION.apply(arg);
                        }

                        @Override
                        public boolean shouldAddAutomatically() {
                            return true;
                        }
                    }
            );
            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }

}
