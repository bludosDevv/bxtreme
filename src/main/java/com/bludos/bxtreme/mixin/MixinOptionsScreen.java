package com.bludos.bxtreme.mixin;

import com.bludos.bxtreme.gui.BXtremeVideoSettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
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
     * Replace Video Settings with BXtreme
     */
    @Inject(method = "createFooter", at = @At("RETURN"))
    private void replacedVideoSettings(CallbackInfo ci) {
        // Find and remove the vanilla Video Settings button
        this.children().removeIf(widget -> {
            if (widget instanceof Button btn) {
                String msg = btn.getMessage().getString();
                return msg.contains("Video") || msg.contains("Settings");
            }
            return false;
        });
        
        // Add BXtreme button in its place
        this.addRenderableWidget(Button.builder(
            Component.literal("BXtreme Performance..."),
            btn -> this.minecraft.setScreen(new BXtremeVideoSettingsScreen(this))
        ).bounds(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20).build());
    }
}
