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
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && Main.performanceMonitor != null) {
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
        
        // Aggressive GC for low memory situations
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