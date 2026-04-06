package com.fpsboost.mixin;

import com.fpsboost.module.ModuleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    private int particleCounter = 0;

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z,
                                double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        var particleOpt = ModuleManager.get().getById("particle_optimizer");
        if (particleOpt != null && particleOpt.isEnabled()) {
            // Skip 60% of particles
            if (++particleCounter % 5 < 3) {
                cir.setReturnValue(null);
            }
        }

        var minParticles = ModuleManager.get().getById("minimal_particles");
        if (minParticles != null && minParticles.isEnabled()) {
            // Skip 80% of particles
            if (particleCounter % 5 < 4) {
                cir.setReturnValue(null);
            }
        }
    }
}
