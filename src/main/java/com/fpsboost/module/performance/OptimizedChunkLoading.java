package com.fpsboost.module.performance;

import com.fpsboost.module.Module;

public class OptimizedChunkLoading extends Module {
    public OptimizedChunkLoading() {
        super("optimized_chunks", "Optimized Chunk Loading", "Reduces chunk rebuild frequency and prioritizes visible chunks", Category.PERFORMANCE, true);
    }
}
