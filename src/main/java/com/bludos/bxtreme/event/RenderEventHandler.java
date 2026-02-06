package com.bludos.bxtreme.event;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * SIMPLIFIED Event Handler - Only proven optimizations
 */
public class RenderEventHandler {
    
    private int tickCounter = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        
        // Only do cleanup every 20 ticks (1 second)
        if (tickCounter >= 20) {
            tickCounter = 0;
            applySimpleOptimizations();
        }
    }
    
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Main.performanceMonitor != null) {
            Main.performanceMonitor.update();
        }
    }
    
    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (Main.performanceMonitor != null) {
            Main.performanceMonitor.render(event.getGuiGraphics());
        }
    }
    
    private void applySimpleOptimizations() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        
        // Only force FAST graphics if explicitly enabled
        if (Main.config.get().ultraLowQualityMode) {
            mc.options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
            mc.options.particles().set(net.minecraft.client.ParticleStatus.MINIMAL);
        }
    }
}
