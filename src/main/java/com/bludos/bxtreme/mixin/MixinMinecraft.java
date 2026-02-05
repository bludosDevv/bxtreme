package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    private long lastFrameTime = System.nanoTime();
    
    /**
     * Frame rate limiter for battery saving and consistent frame pacing
     */
    @Inject(method = "runTick", at = @At("HEAD"))
    private void limitFrameRate(CallbackInfo ci) {
        int maxFPS = Main.config.get().maxFramerate;
        
        if (maxFPS <= 0 || maxFPS > 300) {
            return; // No limit
        }
        
        long targetFrameTime = 1_000_000_000L / maxFPS;
        long currentTime = System.nanoTime();
        long elapsed = currentTime - lastFrameTime;
        
        if (elapsed < targetFrameTime) {
            try {
                Thread.sleep((targetFrameTime - elapsed) / 1_000_000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        lastFrameTime = System.nanoTime();
    }
}