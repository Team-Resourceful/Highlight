package com.teamresourceful.highlight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod("highlight")
public class Highlight {

    public Highlight() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> HighlightClient.init());
    }
}
