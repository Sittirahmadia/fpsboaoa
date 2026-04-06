package com.fpsboost.module.performance;
import com.fpsboost.module.Module;
public class MemoryOptimizer extends Module {
    public MemoryOptimizer() { super("memory_optimizer", "Memory Optimizer", "Periodically hints GC and reduces texture cache bloat", Category.PERFORMANCE, false); }
    private int tickCounter = 0;
    @Override public void onTick() {
        if (++tickCounter >= 6000) { // Every 5 minutes
            tickCounter = 0;
            System.gc();
        }
    }
}
