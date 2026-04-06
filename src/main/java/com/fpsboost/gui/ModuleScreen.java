package com.fpsboost.gui;

import com.fpsboost.module.Module;
import com.fpsboost.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Lunar/Feather-style module configuration GUI.
 * Dark themed with category sidebar, module cards with toggles, and visual effects.
 */
public class ModuleScreen extends Screen {

    // Layout
    private static final int SIDEBAR_W = 140;
    private static final int CARD_H = 48;

    // Theme
    private static final int BG         = 0xFF080A10;
    private static final int SIDEBAR_BG = 0xFF0D1120;
    private static final int CARD_BG    = 0xFF101828;
    private static final int CARD_HOV   = 0xFF141F30;
    private static final int CARD_ON    = 0xFF121E34;
    private static final int BORDER     = 0xFF1A2240;
    private static final int BORDER_ON  = 0xFF1E3860;
    private static final int ACCENT     = 0xFF4FC8FF;
    private static final int GREEN      = 0xFF50E88A;
    private static final int YELLOW     = 0xFFF0C040;
    private static final int RED        = 0xFFF05E7A;
    private static final int TEXT_W     = 0xFFE8EEFF;
    private static final int TEXT_G     = 0xFF7A88B0;
    private static final int TEXT_D     = 0xFF414D6A;
    private static final int TEXT_DD    = 0xFF252E45;
    private static final int TOGGLE_OFF = 0xFF2A3048;

    private Module.Category currentCategory = Module.Category.PERFORMANCE;
    private long openTime;
    private float pulsePhase = 0;
    private int scrollY = 0;

    public ModuleScreen() {
        super(Text.literal("FPS Boost Optimizer"));
    }

    @Override
    protected void init() { openTime = System.currentTimeMillis(); }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        pulsePhase += delta * 0.06f;

        // Background
        ctx.fill(0, 0, width, height, BG);
        drawRadialGlow(ctx, -30, -30, 250, 0x204FC8FF);
        drawRadialGlow(ctx, width - 180, height - 180, 220, 0x1850E88A);

        // ── Sidebar ─────────────────────────────────────────────────────
        ctx.fill(0, 0, SIDEBAR_W, height, SIDEBAR_BG);
        ctx.fill(SIDEBAR_W, 0, SIDEBAR_W + 1, height, BORDER);

        // Brand
        drawHGlow(ctx, 8, 8, SIDEBAR_W - 16, 26, 0x204FC8FF);
        ctx.drawTextWithShadow(textRenderer, "⚡ FPS Boost", 14, 14, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "   Optimizer v1.0", 14, 28, TEXT_DD);

        // Stats
        int enabled = ModuleManager.get().countEnabled();
        int total = ModuleManager.get().getModules().size();
        ctx.drawTextWithShadow(textRenderer, enabled + "/" + total + " active", 14, 46, TEXT_D);

        // Category buttons
        int catY = 68;
        for (Module.Category cat : Module.Category.values()) {
            boolean active = cat == currentCategory;
            boolean hov = mouseX >= 6 && mouseX < SIDEBAR_W - 6 && mouseY >= catY && mouseY < catY + 36;

            if (active) {
                ctx.fill(6, catY, SIDEBAR_W - 6, catY + 36, 0xFF142050);
                drawHGlow(ctx, 6, catY, SIDEBAR_W - 12, 36, (cat.color & 0x00FFFFFF) | 0x20000000);
                ctx.fill(6, catY + 8, 9, catY + 28, cat.color);
            } else if (hov) {
                ctx.fill(6, catY, SIDEBAR_W - 6, catY + 36, 0xFF111830);
            }

            int textCol = active ? TEXT_W : (hov ? TEXT_G : TEXT_D);
            String icon = switch (cat) {
                case PERFORMANCE -> "⚡";
                case HUD -> "📊";
                case VISUAL -> "🎨";
            };
            ctx.drawTextWithShadow(textRenderer, icon + " " + cat.label, 16, catY + 8, textCol);

            // Module count
            int count = (int) ModuleManager.get().getByCategory(cat).stream().filter(Module::isEnabled).count();
            int totalCat = ModuleManager.get().getByCategory(cat).size();
            ctx.drawTextWithShadow(textRenderer, count + "/" + totalCat, 16, catY + 22, active ? cat.color : TEXT_DD);

            catY += 40;
        }

        // Sidebar footer
        ctx.fill(8, height - 32, SIDEBAR_W - 8, height - 31, BORDER);
        ctx.drawTextWithShadow(textRenderer, "R-Shift: GUI", 14, height - 22, TEXT_DD);

        // ── Main Content ────────────────────────────────────────────────
        int cx = SIDEBAR_W + 16;
        int cy = 12;
        int cw = width - SIDEBAR_W - 32;

        // Header
        drawHGlow(ctx, cx - 4, cy - 2, cw, 24, (currentCategory.color & 0x00FFFFFF) | 0x10000000);
        ctx.drawTextWithShadow(textRenderer, currentCategory.label + " Modules", cx, cy + 2, TEXT_W);
        String catDesc = switch (currentCategory) {
            case PERFORMANCE -> "Boost FPS and reduce lag";
            case HUD -> "On-screen information displays";
            case VISUAL -> "Visual enhancements and effects";
        };
        ctx.drawTextWithShadow(textRenderer, catDesc, cx + textRenderer.getWidth(currentCategory.label + " Modules") + 14, cy + 2, TEXT_D);
        cy += 28;

        // Module cards
        List<Module> mods = ModuleManager.get().getByCategory(currentCategory);
        for (Module mod : mods) {
            boolean on = mod.isEnabled();
            boolean cardHov = mouseX >= cx && mouseX < cx + cw && mouseY >= cy && mouseY < cy + CARD_H;

            // Card background
            int bg = on ? CARD_ON : (cardHov ? CARD_HOV : CARD_BG);
            ctx.fill(cx, cy, cx + cw, cy + CARD_H, bg);
            drawBorder(ctx, cx, cy, cw, CARD_H, on ? BORDER_ON : BORDER);

            // Active glow
            if (on) {
                int glowW = Math.min(cw / 3, 120);
                int pulse = (int)(Math.sin(pulsePhase + mods.indexOf(mod) * 0.5) * 15 + 50);
                for (int i = 0; i < glowW; i++) {
                    int a = Math.max(0, pulse - i * pulse / glowW);
                    ctx.fill(cx + 8 + i, cy + 1, cx + 9 + i, cy + 2, (a << 24) | (currentCategory.color & 0x00FFFFFF));
                }
            }

            // Status indicator dot
            int dotColor = on ? GREEN : 0xFF3A4060;
            ctx.fill(cx + 12, cy + 19, cx + 18, cy + 25, dotColor);
            if (on) {
                // Glow around dot
                ctx.fill(cx + 11, cy + 18, cx + 19, cy + 19, (GREEN & 0x00FFFFFF) | 0x30000000);
                ctx.fill(cx + 11, cy + 25, cx + 19, cy + 26, (GREEN & 0x00FFFFFF) | 0x30000000);
            }

            // Module name
            ctx.drawTextWithShadow(textRenderer, mod.getName(), cx + 26, cy + 12, on ? TEXT_W : TEXT_G);

            // Description
            ctx.drawTextWithShadow(textRenderer, mod.getDescription(), cx + 26, cy + 26, TEXT_D);

            // Toggle
            int togX = cx + cw - 42;
            int togY = cy + 16;
            drawToggle(ctx, togX, togY, on, mouseX, mouseY);

            cy += CARD_H + 6;
        }

        // ── Bottom info bar ─────────────────────────────────────────────
        ctx.fill(SIDEBAR_W + 1, height - 26, width, height, 0xFF0A0E18);
        ctx.fill(SIDEBAR_W + 1, height - 27, width, height - 26, BORDER);
        ctx.drawTextWithShadow(textRenderer, "Compatible with Zalith Launcher (Android) + Desktop", SIDEBAR_W + 14, height - 18, TEXT_DD);
        ctx.drawTextWithShadow(textRenderer, "Minecraft 1.21.4", width - textRenderer.getWidth("Minecraft 1.21.4") - 12, height - 18, TEXT_DD);

        super.render(ctx, mouseX, mouseY, delta);
    }

    // ── Drawing helpers ─────────────────────────────────────────────────

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color);
        ctx.fill(x + w - 1, y, x + w, y + h, color);
    }

    private void drawHGlow(DrawContext ctx, int x, int y, int w, int h, int color) {
        int a = (color >>> 24) & 0xFF;
        int rgb = color & 0x00FFFFFF;
        for (int i = 0; i < w; i++) {
            float t = 1f - Math.abs(i - w / 2f) / (w / 2f);
            int alpha = (int)(a * t * t);
            if (alpha > 0) ctx.fill(x + i, y, x + i + 1, y + h, (alpha << 24) | rgb);
        }
    }

    private void drawRadialGlow(DrawContext ctx, int x, int y, int size, int color) {
        int a = (color >>> 24) & 0xFF;
        int rgb = color & 0x00FFFFFF;
        int steps = Math.min(size / 4, 16);
        for (int i = 0; i < steps; i++) {
            float t = 1f - (float) i / steps;
            int alpha = (int)(a * t * t);
            if (alpha > 0) {
                int s = i * size / (steps * 2);
                ctx.fill(x + s, y + s, x + size - s, y + size - s, (alpha << 24) | rgb);
            }
        }
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean on, int mx, int my) {
        boolean hov = mx >= x && mx < x + 30 && my >= y && my < y + 16;
        int bg = on ? GREEN : TOGGLE_OFF;
        if (hov) bg = on ? 0xFF60F09A : 0xFF343C58;
        ctx.fill(x, y, x + 30, y + 16, bg);
        drawBorder(ctx, x, y, 30, 16, on ? (GREEN & 0x00FFFFFF) | 0x80000000 : BORDER);
        int dotX = on ? x + 17 : x + 3;
        ctx.fill(dotX, y + 3, dotX + 10, y + 13, on ? 0xFFFFFFFF : TEXT_D);
    }

    // ── Input ───────────────────────────────────────────────────────────

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        // Category clicks
        int catY = 68;
        for (Module.Category cat : Module.Category.values()) {
            if (mouseX >= 6 && mouseX < SIDEBAR_W - 6 && mouseY >= catY && mouseY < catY + 36) {
                currentCategory = cat;
                return true;
            }
            catY += 40;
        }

        // Module toggle clicks
        int cx = SIDEBAR_W + 16;
        int cy = 12 + 28;
        int cw = width - SIDEBAR_W - 32;

        for (Module mod : ModuleManager.get().getByCategory(currentCategory)) {
            // Toggle area
            int togX = cx + cw - 42;
            int togY = cy + 16;
            if (mouseX >= togX && mouseX < togX + 30 && mouseY >= togY && mouseY < togY + 16) {
                mod.toggle();
                ModuleManager.get().save();
                return true;
            }

            // Card click = toggle too
            if (mouseX >= cx && mouseX < cx + cw && mouseY >= cy && mouseY < cy + CARD_H) {
                mod.toggle();
                ModuleManager.get().save();
                return true;
            }

            cy += CARD_H + 6;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
