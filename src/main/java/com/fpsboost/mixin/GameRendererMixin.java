package com.fpsboost.mixin;

import com.fpsboost.module.ModuleManager;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onHurtCam(CallbackInfo ci) {
        var mod = ModuleManager.get().getById("no_hurtcam");
        if (mod != null && mod.isEnabled()) {
            ci.cancel();
        }
    }
}
