package com.fpsboost.module.performance;
import com.fpsboost.module.Module;
public class LazyChunkLoading extends Module {
    public LazyChunkLoading() { super("lazy_chunks", "Lazy Chunk Loading", "Delays loading chunks outside render distance priority", Category.PERFORMANCE, false); }
}
