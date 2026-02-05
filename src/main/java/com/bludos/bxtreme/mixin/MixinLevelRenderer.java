package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    
    @Shadow
    private Frustum cullingFrustum;
    
    /**
     * NUCLEAR: Disable entity outlines completely
     */
    @Inject(method = "shouldShowEntityOutlines", at = @At("HEAD"), cancellable = true)
    private void disableEntityOutlines(CallbackInfoReturnable<Boolean> cir) {
        if (Main.config.get().aggressiveEntityCulling) {
            cir.setReturnValue(false);
        }
    }
    
    /**
     * NUCLEAR: Simplify chunk rendering
     */
    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void beforeRenderLevel(CallbackInfo ci) {
        if (Main.config.get().ultraLowQualityMode) {
            Minecraft mc = Minecraft.getInstance();
            // Disable smooth lighting in ultra-low mode
            mc.options.ambientOcclusion().set(false);
        }
    }
}