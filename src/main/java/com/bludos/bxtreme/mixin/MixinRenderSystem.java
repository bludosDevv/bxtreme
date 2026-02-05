package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
    
    /**
     * NUCLEAR: Disable shader-based fog (expensive on translation layer)
     */
    @Inject(method = "setShaderFogStart", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableFog(float fogStart, CallbackInfo ci) {
        if (Main.config.get().ultraLowQualityMode) {
            ci.cancel(); // Don't set fog - saves shader complexity
        }
    }
}