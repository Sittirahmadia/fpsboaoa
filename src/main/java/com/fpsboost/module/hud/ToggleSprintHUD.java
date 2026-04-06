package com.fpsboost.module.hud;
import com.fpsboost.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
public class ToggleSprintHUD extends Module {
    private boolean sprinting = false;
    public ToggleSprintHUD() { super("toggle_sprint", "Toggle Sprint", "Automatically holds sprint key — shows [Sprinting] on HUD", Category.HUD, true); }
    @Override public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && mc.player.input != null) {
            // Force sprint when moving forward
            if (mc.player.input.playerInput.forward()) {
                mc.options.sprintKey.setPressed(true);
                sprinting = true;
            } else {
                sprinting = false;
            }
        }
    }
    public boolean isSprinting() { return sprinting; }
}
