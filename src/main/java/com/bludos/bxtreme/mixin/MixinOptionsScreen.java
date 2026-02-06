package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.gui.BXtremeVideoSettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    
    protected MixinOptionsScreen(Component title) {
        super(title);
    }
    
    /**
     * Add BXtreme button at BOTTOM - perfectly aligned with Done button
     * Layout: [BXtreme Performance] [Done]
     */
    @Inject(method = "init", at = @At("RETURN"))
    private void addBXtremeButton(CallbackInfo ci) {
        // Bottom Y position (same as Done button)
        int bottomY = this.height - 27;
        
        // Button dimensions - make them equal width and fit nicely
        int buttonWidth = 150;
        int gap = 10; // Gap between buttons
        
        // Calculate positions to center both buttons
        int totalWidth = (buttonWidth * 2) + gap;
        int startX = (this.width - totalWidth) / 2;
        
        // BXtreme button on LEFT
        this.addRenderableWidget(Button.builder(
            Component.literal("BXtreme Performance"),
            btn -> this.minecraft.setScreen(new BXtremeVideoSettingsScreen(this))
        ).bounds(startX, bottomY, buttonWidth, 20).build());
        
        // Done button on RIGHT (we need to move it)
        // First, remove the existing Done button
        this.children().removeIf(widget -> {
            if (widget instanceof Button btn) {
                String msg = btn.getMessage().getString();
                return msg.contains("Done") || msg.contains("done");
            }
            return false;
        });
        
        // Add new Done button in correct position
        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> this.minecraft.setScreen(null)
        ).bounds(startX + buttonWidth + gap, bottomY, buttonWidth, 20).build());
    }
}