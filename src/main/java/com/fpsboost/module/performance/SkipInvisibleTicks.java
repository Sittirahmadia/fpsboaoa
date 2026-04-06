package com.fpsboost.module.performance;
import com.fpsboost.module.Module;
public class SkipInvisibleTicks extends Module {
    public SkipInvisibleTicks() { super("skip_invisible", "Skip Invisible Ticks", "Skips tick processing for entities not visible to the player", Category.PERFORMANCE, false); }
}
