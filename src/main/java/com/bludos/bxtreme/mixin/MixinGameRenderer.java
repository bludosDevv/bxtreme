package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    
    /**
     * NUCLEAR: Disable shader post-processing
     * This kills expensive framebuffer operations that translation layers hate
     */
    @Inject(method = "resize", at = @At("HEAD"))
    private void disableShaders(CallbackInfo ci) {
        GameRenderer renderer = (GameRenderer)(Object)this;
        if (Main.config.get().ultraLowQualityMode) {
            // Force disable shaders
            renderer.shutdownEffect();
        }
    }
    
    /**
     * NUCLEAR: Skip fabulous graphics mode entirely
     */
    @Inject(method = "checkEntityPostEffect", at = @At("HEAD"), cancellable = true)
    private void skipEntityEffects(CallbackInfo ci) {
        if (Main.config.get().ultraLowQualityMode) {
            ci.cancel(); // Don't apply entity shaders (creeper charge, spider eyes, etc)
        }
    }
}