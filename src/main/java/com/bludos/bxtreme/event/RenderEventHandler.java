package com.bludos.bxtreme.event;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

public class RenderEventHandler {
    
    private int tickCounter = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        
        // Every 20 ticks (1 second), perform cleanup
        if (tickCounter >= 20) {
            tickCounter = 0;
            performPeriodicOptimizations();
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
    
    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        // This is where we'll hook into chunk rendering in Phase 2
        // For now, we just track what's being rendered
        
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && Main.performanceMonitor != null) {
                // Update performance stats
                int entityCount = mc.level.getEntityCount();
                Main.performanceMonitor.setEntitiesRendered(entityCount);
            }
        }
    }
    
    private void performPeriodicOptimizations() {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.level == null || mc.player == null) {
            return;
        }
        
        // Suggest garbage collection on low-end devices to prevent stutters
        // Only do this if we're configured for aggressive optimization
        if (Main.config.get().aggressiveEntityCulling) {
            Runtime runtime = Runtime.getRuntime();
            long freeMemory = runtime.freeMemory();
            long totalMemory = runtime.totalMemory();
            
            // If less than 20% memory free, suggest GC
            if (freeMemory < totalMemory * 0.2) {
                System.gc();
            }
        }
    }
}