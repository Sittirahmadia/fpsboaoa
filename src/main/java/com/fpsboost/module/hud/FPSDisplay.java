package com.fpsboost.module.hud;

import com.fpsboost.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FPSDisplay extends Module {
    public FPSDisplay() { super("fps_display", "FPS Display", "Shows current FPS on screen with color indicator", Category.HUD, true); }

    @Override public void onRender(float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getDebugHud().shouldShowDebugHud()) return;
        // FPS rendering handled in InGameHudMixin
    }

    public static String getFPSText() {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        return fps + " FPS";
    }

    public static int getFPSColor() {
        int fps = MinecraftClient.getInstance().getCurrentFps();
        if (fps >= 60) return 0xFF50E88A;  // green
        if (fps >= 30) return 0xFFF0C040;  // yellow
        return 0xFFF05E7A;                  // red
    }
}
