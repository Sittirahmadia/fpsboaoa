package com.fpsboost.mixin;

import com.fpsboost.module.ModuleManager;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 2)
    private float modifyGamma(float gamma) {
        var mod = ModuleManager.get().getById("fullbright");
        if (mod != null && mod.isEnabled()) {
            return 16.0f; // Max brightness
        }
        return gamma;
    }
}
