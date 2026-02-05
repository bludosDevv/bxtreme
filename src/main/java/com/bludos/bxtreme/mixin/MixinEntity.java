package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import com.bludos.bxtreme.core.RenderOptimizer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    
    /**
     * Skip rendering distant entities
     */
    @Inject(method = "shouldRender(DDD)Z", at = @At("HEAD"), cancellable = true)
    private void optimizeEntityRendering(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!Main.config.get().aggressiveEntityCulling) {
            return;
        }
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) {
            return;
        }
        
        Camera camera = mc.gameRenderer.getMainCamera();
        Entity entity = (Entity) (Object) this;
        
        if (!RenderOptimizer.shouldRenderEntity(entity, camera)) {
            cir.setReturnValue(false);
        }
    }
}