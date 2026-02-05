package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderDispatcher.class)
public class MixinChunkRenderDispatcher {
    
    @Shadow
    private volatile int toBatchCount;
    
    /**
     * CRITICAL: Reduce chunk update batching for smoother movement
     * This prevents the massive FPS drops when moving that killed ChatGPT's version
     */
    @Inject(method = "uploadAllPendingUploads", at = @At("HEAD"), cancellable = true)
    private void optimizeChunkUploads(CallbackInfo ci) {
        if (!Main.config.get().aggressiveEntityCulling) {
            return;
        }
        
        // Limit chunks processed per frame to prevent stuttering
        // Vanilla tries to do too many at once = FPS drop on movement
        if (toBatchCount > 2) {
            // Only process 2 chunks per frame max on low-end devices
            ci.cancel();
        }
    }
}