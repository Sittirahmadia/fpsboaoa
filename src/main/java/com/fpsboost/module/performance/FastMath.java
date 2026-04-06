package com.fpsboost.module.performance;
import com.fpsboost.module.Module;
public class FastMath extends Module {
    public FastMath() { super("fast_math", "Fast Math", "Uses lookup tables for sin/cos calculations instead of Math.sin/cos", Category.PERFORMANCE, true); }
}
