package com.fpsboost;

import com.fpsboost.gui.ModuleScreen;
import com.fpsboost.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class FPSBoostMod implements ClientModInitializer {
    public static final String MOD_ID = "fpsboost";
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        // Initialize all modules
        ModuleManager.get().init();

        // Register GUI keybinding (0)
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fpsboost.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_0,
                "category.fpsboost"
        ));

        // Tick handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openGuiKey.wasPressed()) {
                client.setScreen(new ModuleScreen());
            }
            ModuleManager.get().tickAll();
        });

        // Render handler
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            ModuleManager.get().renderAll(tickCounter.getTickDelta(true));
        });

        System.out.println("[FPSBoost] Mod initialized! " + ModuleManager.get().countEnabled() + " modules active. Press 0 for GUI.");
    }
}
