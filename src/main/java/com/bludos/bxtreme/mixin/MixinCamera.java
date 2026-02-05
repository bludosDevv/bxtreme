package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class MixinCamera {
    
    @Shadow
    private float xRot;
    
    @Shadow
    private float yRot;
    
    private float lastXRot;
    private float lastYRot;
    
    /**
     * Smooth camera movement to reduce chunk rebuild triggers
     * This prevents the stuttering when walking
     */
    @Inject(method = "setup", at = @At("RETURN"))
    private void smoothCameraMovement(net.minecraft.world.level.BlockGetter level, Entity entity,
                                     boolean detached, boolean mirrored, float partialTick,
                                     CallbackInfo ci) {
        if (!Main.config.get().reduceAnimations) {
            return;
        }
        
        // Smooth rotation changes to prevent excessive chunk updates
        float rotationThreshold = 1.0f; // degrees
        
        if (Math.abs(xRot - lastXRot) < rotationThreshold) {
            xRot = lastXRot;
        }
        
        if (Math.abs(yRot - lastYRot) < rotationThreshold) {
            yRot = lastYRot;
        }
        
        lastXRot = xRot;
        lastYRot = yRot;
    }
}