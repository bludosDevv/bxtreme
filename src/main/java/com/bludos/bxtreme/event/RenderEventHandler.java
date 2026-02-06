package com.bludos.bxtreme.event;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

public class RenderEventHandler {
    
    private int tickCounter = 0;
    private int gcCounter = 0; // Track GC calls
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        
        tickCounter++;
        gcCounter++;
        
        // Only do cleanup every 20 ticks (1 second)
        if (tickCounter >= 20) {
            tickCounter = 0;
            // GC only every 10 seconds instead of every second!
            if (gcCounter >= 200) {
                gcCounter = 0;
                performOptimizations();
            }
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
                cullDistantEntities(mc);
            }
        }
    }
    
    private void forceGraphicsSettings() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !Main.config.get().ultraLowQualityMode) {
            return;
        }
        
        // Apply ultra low settings
        mc.options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
        if (Main.config.get().disableSmoothLighting) {
            mc.options.ambientOcclusion().set(false);
        }
        mc.options.particles().set(net.minecraft.client.ParticleStatus.MINIMAL);
        mc.options.entityShadows().set(false);
        mc.options.cloudStatus().set(net.minecraft.client.CloudStatus.OFF);
    }
    
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
            }
        }
        
        if (Main.performanceMonitor != null) {
            Main.performanceMonitor.setEntitiesRendered(total - culled);
        }
    }
    
    private void performOptimizations() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        
        // Only GC if we're actually running low on memory
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        
        // Only GC if using more than 85% of max memory
        long usedMemory = totalMemory - freeMemory;
        if (usedMemory > maxMemory * 0.85) {
            System.gc();
            Main.LOGGER.info("Performed GC - Memory: " + (usedMemory / 1024 / 1024) + "MB / " + (maxMemory / 1024 / 1024) + "MB");
        }
    }
}
