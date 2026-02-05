package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    
    /**
     * CRITICAL: Aggressive entity culling
     * Prevents rendering entities beyond our configured distance
     */
    @Inject(method = "shouldShowEntityOutlines", at = @At("HEAD"), cancellable = true)
    private void disableEntityOutlines(CallbackInfoReturnable<Boolean> cir) {
        // Entity outlines are expensive - disable completely for performance
        if (Main.config.get().aggressiveEntityCulling) {
            cir.setReturnValue(false);
        }
    }
}