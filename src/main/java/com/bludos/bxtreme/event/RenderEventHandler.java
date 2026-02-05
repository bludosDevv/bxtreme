package com.bludos.bxtreme.event;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

public class RenderEventHandler {
    
    private int tickCounter = 0;
    private int fpsCheckCounter = 0;
    private int lowFpsStreak = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        fpsCheckCounter++;
        
        // Every 20 ticks (1 second), perform cleanup
        if (tickCounter >= 20) {
            tickCounter = 0;
            performPeriodicOptimizations();
        }
        
        // Every 60 ticks (3 seconds), check FPS and adjust render distance
        if (fpsCheckCounter >= 60) {
            fpsCheckCounter = 0;
            adjustRenderDistanceBasedOnFPS();
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
    
    /**
     * CRITICAL: Dynamic render distance adjustment
     * Automatically reduces chunks if FPS drops
     */
    private void adjustRenderDistanceBasedOnFPS() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || Main.performanceMonitor == null) {
            return;
        }
        
        int currentFPS = Main.performanceMonitor.getFPS();
        Options options = mc.options;
        int currentRenderDistance = options.renderDistance().get();
        
        // If FPS is consistently below 50, reduce render distance
        if (currentFPS < 50) {
            lowFpsStreak++;
            
            if (lowFpsStreak >= 3 && currentRenderDistance > 4) {
                options.renderDistance().set(currentRenderDistance - 1);
                Main.LOGGER.info("Low FPS detected, reducing render distance to " + (currentRenderDistance - 1));
                lowFpsStreak = 0;
            }
        } else if (currentFPS > 70) {
            // FPS is good, can increase render distance if it was reduced
            lowFpsStreak = 0;
            
            int maxRenderDistance = 8; // Cap at 8 chunks for mobile
            if (currentRenderDistance < maxRenderDistance) {
                options.renderDistance().set(currentRenderDistance + 1);
                Main.LOGGER.info("Good FPS, increasing render distance to " + (currentRenderDistance + 1));
            }
        } else {
            lowFpsStreak = 0; // Reset streak if FPS is between 50-70
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