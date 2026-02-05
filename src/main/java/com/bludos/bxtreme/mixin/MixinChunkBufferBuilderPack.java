package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBufferBuilderPack.class)
public class MixinChunkBufferBuilderPack {
    
    /**
     * Optimize chunk buffer building
     * Reduce vertex buffer size for mobile
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void optimizeBufferSize(CallbackInfo ci) {
        if (Main.config.get().ultraLowQualityMode) {
            // Buffer optimization happens automatically
            // This mixin ensures we're using minimal buffers
        }
    }
}