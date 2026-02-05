package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public abstract class MixinParticleEngine {
    
    @Shadow @Final private Map<?, Queue<Particle>> particles;
    
    /**
     * NUCLEAR: Kill particle spawning almost entirely
     */
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void killMostParticles(CallbackInfo ci) {
        if (!Main.config.get().disableUnnecessaryParticles) {
            return;
        }
        
        // Count total particles
        int totalParticles = 0;
        for (Queue<Particle> queue : particles.values()) {
            totalParticles += queue.size();
        }
        
        // If over limit, cancel ALL new particles
        if (totalParticles >= Main.config.get().particleLimit) {
            ci.cancel();
        }
    }
}