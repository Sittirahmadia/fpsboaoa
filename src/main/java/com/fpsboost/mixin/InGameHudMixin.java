package com.fpsboost.mixin;

import com.fpsboost.module.ModuleManager;
import com.fpsboost.module.hud.FPSDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderHud(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getDebugHud().shouldShowDebugHud()) return;

        int y = 4;

        // FPS Display
        var fpsMod = ModuleManager.get().getById("fps_display");
        if (fpsMod != null && fpsMod.isEnabled()) {
            ctx.drawTextWithShadow(mc.textRenderer, FPSDisplay.getFPSText(), 4, y, FPSDisplay.getFPSColor());
            y += 12;
        }

        // Coordinates
        var coordMod = ModuleManager.get().getById("coordinates");
        if (coordMod != null && coordMod.isEnabled()) {
            String coords = String.format("XYZ: %.1f / %.1f / %.1f",
                mc.player.getX(), mc.player.getY(), mc.player.getZ());
            ctx.drawTextWithShadow(mc.textRenderer, coords, 4, y, 0xFFE8EEFF);
            y += 12;
        }

        // Toggle Sprint indicator
        var sprintMod = ModuleManager.get().getById("toggle_sprint");
        if (sprintMod != null && sprintMod.isEnabled()) {
            if (mc.player.isSprinting()) {
                ctx.drawTextWithShadow(mc.textRenderer, "[Sprinting]", 4, y, 0xFF50E88A);
            }
        }

        // Module count (bottom-left)
        int enabled = ModuleManager.get().countEnabled();
        int total = ModuleManager.get().getModules().size();
        String modText = "FPSBoost: " + enabled + "/" + total + " modules";
        ctx.drawTextWithShadow(mc.textRenderer, modText,
            4, mc.getWindow().getScaledHeight() - 12, 0xFF414D6A);
    }
}
