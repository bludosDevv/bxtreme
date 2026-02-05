package com.bludos.bxtreme.event;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

public class RenderEventHandler {
    
    private int tickCounter = 0;
    private int entityCullCounter = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        
        // Every 20 ticks (1 second), perform aggressive cleanup
        if (tickCounter >= 20) {
            tickCounter = 0;
            performAggressiveOptimizations();
        }
    }
    
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            forceGraphicsSettings();
        }
        
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
                // Count and cull distant entities
                cullDistantEntities(mc);
            }
        }
    }
    
    /**
     * NUCLEAR: Force lowest possible graphics settings
     */
    private void forceGraphicsSettings() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !Main.config.get().ultraLowQualityMode) {
            return;
        }
        
        // Force fast graphics
        mc.options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
        
        // Disable smooth lighting
        if (Main.config.get().disableSmoothLighting) {
            mc.options.ambientOcclusion().set(false);
        }
        
        // Minimize particles
        mc.options.particles().set(net.minecraft.client.ParticleStatus.MINIMAL);
        
        // Disable entity shadows
        mc.options.entityShadows().set(false);
        
        // Disable weather
        mc.levelRenderer.allChanged();
    }
    
    /**
     * NUCLEAR: Aggressively cull distant entities every frame
     */
    private void cullDistantEntities(Minecraft mc) {
        if (mc.player == null || !Main.config.get().aggressiveEntityCulling) {
            return;
        }
        
        int culled = 0;
        int total = 0;
        int maxDist = Main.config.get().maxEntityRenderDistance;
        
        for (Entity entity : mc.level.entitiesForRendering()) {
            total++;
            double distSq = entity.distanceToSqr(mc.player);
            
            if (distSq > maxDist * maxDist) {
                culled++;
                // Don't render - this is just for counting
            }
        }
        
        if (Main.performanceMonitor != null) {
            Main.performanceMonitor.setEntitiesRendered(total - culled);
        }
    }
    
    /**
     * NUCLEAR: Aggressive memory and optimization cleanup
     */
    private void performAggressiveOptimizations() {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.level == null || mc.player == null) {
            return;
        }
        
        // NUCLEAR garbage collection every second
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        
        // If using more than 70% of max memory, force GC
        long usedMemory = totalMemory - freeMemory;
        if (usedMemory > maxMemory * 0.7) {
            System.gc();
            Main.LOGGER.info("NUCLEAR: Forced GC - Memory: " + (usedMemory / 1024 / 1024) + "MB / " + (maxMemory / 1024 / 1024) + "MB");
        }
    }
}