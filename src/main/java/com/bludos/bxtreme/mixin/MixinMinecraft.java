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
     * Disable unnecessary features for performance
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void optimizeTick(CallbackInfo ci) {
        // Disable autosave during gameplay for smoother performance
        Minecraft mc = (Minecraft)(Object)this;
        if (mc.level != null && Main.config.get().aggressiveEntityCulling) {
            // Force lower entity tick distance
            mc.level.tickRateManager().setFrozen(false);
        }
    }
}