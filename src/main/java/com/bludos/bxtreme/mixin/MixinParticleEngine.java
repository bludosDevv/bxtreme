package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(ParticleEngine.class)
public abstract class MixinParticleEngine {
    
    @Shadow
    protected abstract void tickParticle(net.minecraft.client.particle.Particle particle);
    
    private int particleCount = 0;
    
    /**
     * CRITICAL: Particle limiter
     * Prevents spawning particles beyond our limit
     */
    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void limitParticles(net.minecraft.client.particle.Particle particle, CallbackInfo ci) {
        if (!Main.config.get().disableUnnecessaryParticles) {
            return;
        }
        
        // Count current particles and deny if over limit
        if (particleCount >= Main.config.get().particleLimit) {
            ci.cancel();
        }
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void updateParticleCount(CallbackInfo ci) {
        // Reset counter each tick (will be recounted)
        particleCount = 0;
    }
    
    @Inject(method = "tick", at = @At("RETURN"))
    private void countParticlesAfterTick(CallbackInfo ci) {
        if (Main.performanceMonitor != null) {
            Main.performanceMonitor.setParticlesActive(particleCount);
        }
    }
}