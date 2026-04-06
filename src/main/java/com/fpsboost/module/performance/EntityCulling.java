package com.fpsboost.module.performance;

import com.fpsboost.module.Module;

public class EntityCulling extends Module {
    public EntityCulling() {
        super("entity_culling", "Entity Culling", "Skips rendering entities that are behind walls or out of view frustum", Category.PERFORMANCE, true);
    }
}
