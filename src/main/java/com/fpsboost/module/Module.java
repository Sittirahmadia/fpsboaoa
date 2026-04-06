package com.fpsboost.module;

/**
 * Base class for all modules. Each module can be toggled on/off.
 */
public abstract class Module {
    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public enum Category {
        PERFORMANCE("Performance", 0xFF4FC8FF),
        HUD("HUD", 0xFF50E88A),
        VISUAL("Visual", 0xFFF0C040);

        public final String label;
        public final int color;
        Category(String label, int color) { this.label = label; this.color = color; }
    }

    public Module(String id, String name, String description, Category category, boolean defaultEnabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = defaultEnabled;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) {
        boolean wasEnabled = this.enabled;
        this.enabled = enabled;
        if (enabled && !wasEnabled) onEnable();
        else if (!enabled && wasEnabled) onDisable();
    }

    public void toggle() { setEnabled(!enabled); }

    /** Called when module is enabled */
    protected void onEnable() {}
    /** Called when module is disabled */
    protected void onDisable() {}
    /** Called every client tick if enabled */
    public void onTick() {}
    /** Called every render frame if enabled */
    public void onRender(float tickDelta) {}
}
