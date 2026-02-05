package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    /**
     * Simple optimization - reduce entity updates
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void optimizeTick(CallbackInfo ci) {
        Minecraft mc = (Minecraft)(Object)this;
        if (mc.level != null && Main.config.get().aggressiveEntityCulling) {
            // Reduce simulation distance for better performance
            // This is just a marker - actual culling happens elsewhere
        }
    }
}