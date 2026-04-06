package com.fpsboost.module;

import com.fpsboost.module.performance.*;
import com.fpsboost.module.hud.*;
import com.fpsboost.module.visual.*;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central module registry and configuration persistence.
 */
public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("fpsboost.json");

    public static ModuleManager get() { return INSTANCE; }

    public void init() {
        // ── Performance Modules ─────────────────────────────────────────
        register(new OptimizedChunkLoading());
        register(new EntityCulling());
        register(new ParticleOptimizer());
        register(new LightOptimizer());
        register(new LazyChunkLoading());
        register(new FastMath());
        register(new MemoryOptimizer());
        register(new SmartEntityRendering());
        register(new ReduceAnimations());
        register(new SkipInvisibleTicks());

        // ── HUD Modules ─────────────────────────────────────────────────
        register(new FPSDisplay());
        register(new CoordinatesHUD());
        register(new KeystrokesHUD());
        register(new ArmorStatusHUD());
        register(new PotionTimersHUD());
        register(new CPSDisplay());
        register(new PingDisplay());
        register(new ToggleSprintHUD());

        // ── Visual Modules ──────────────────────────────────────────────
        register(new FullBright());
        register(new CleanView());
        register(new NoFog());
        register(new LowFire());
        register(new MinimalParticles());
        register(new SmoothCamera());
        register(new NoHurtCam());
        register(new Freelook());

        load();
    }

    private void register(Module m) { modules.add(m); }

    public List<Module> getModules() { return Collections.unmodifiableList(modules); }

    public List<Module> getByCategory(Module.Category cat) {
        return modules.stream().filter(m -> m.getCategory() == cat).collect(Collectors.toList());
    }

    public Module getById(String id) {
        return modules.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public int countEnabled() {
        return (int) modules.stream().filter(Module::isEnabled).count();
    }

    public void tickAll() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }

    public void renderAll(float tickDelta) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRender(tickDelta);
        }
    }

    // ── Config persistence ──────────────────────────────────────────────

    public void save() {
        JsonObject root = new JsonObject();
        for (Module m : modules) {
            root.addProperty(m.getId(), m.isEnabled());
        }
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(root, w);
            }
        } catch (Exception e) {
            System.err.println("[FPSBoost] Config save error: " + e.getMessage());
        }
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) return;
        try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
            for (Module m : modules) {
                if (root.has(m.getId())) {
                    m.setEnabled(root.get(m.getId()).getAsBoolean());
                }
            }
        } catch (Exception e) {
            System.err.println("[FPSBoost] Config load error: " + e.getMessage());
        }
    }
}
