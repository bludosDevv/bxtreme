package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.gui.BXtremeVideoSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public class MixinVideoSettingsScreen {
    
    /**
     * REPLACE vanilla video settings with BXtreme
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void replaceWithBXtreme(CallbackInfo ci) {
        VideoSettingsScreen self = (VideoSettingsScreen)(Object)this;
        
        // Get the parent screen
        try {
            java.lang.reflect.Field lastScreenField = VideoSettingsScreen.class.getDeclaredField("lastScreen");
            lastScreenField.setAccessible(true);
            Screen lastScreen = (Screen) lastScreenField.get(self);
            
            // Replace with our screen
            Minecraft.getInstance().setScreen(new BXtremeVideoSettingsScreen(lastScreen));
            ci.cancel();
        } catch (Exception e) {
            // Fallback - just let vanilla load
        }
    }
}