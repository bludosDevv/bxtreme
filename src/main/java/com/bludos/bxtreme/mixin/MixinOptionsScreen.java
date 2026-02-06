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
     * Add BXtreme button at BOTTOM next to Done button
     */
    @Inject(method = "init", at = @At("RETURN"))
    private void addBXtremeButton(CallbackInfo ci) {
        // Position: Bottom of screen, left side (Done is on right)
        int bottomY = this.height - 27;
        
        // BXtreme button on LEFT
        this.addRenderableWidget(Button.builder(
            Component.literal("BXtreme Performance"),
            btn -> this.minecraft.setScreen(new BXtremeVideoSettingsScreen(this))
        ).bounds(this.width / 2 - 155, bottomY, 150, 20).build());
        
        // Done button is already on RIGHT by vanilla at (width/2 + 5, bottomY)
    }
}
