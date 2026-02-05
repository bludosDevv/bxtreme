package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRenderDispatcher.class)
public class MixinChunkRenderDispatcher {
    
    @Shadow
    private int toBatchCount;
    
    /**
     * GREEDY MESHING: Limit chunk updates per frame
     * Bedrock-style chunking system
     */
    @Inject(method = "pollTask", at = @At("HEAD"), cancellable = true)
    private void limitChunkUpdates(CallbackInfoReturnable<Object> cir) {
        if (!Main.config.get().ultraLowQualityMode) {
            return;
        }
        
        // Only allow 1 chunk compile per frame for smooth movement
        if (toBatchCount > Main.config.get().chunkUpdateBudget) {
            cir.setReturnValue(null); // Skip this update
        }
    }
}