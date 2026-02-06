package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class PerformanceMonitor {
    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    
    private int entitiesRendered = 0;
    private int particlesActive = 0;
    private int chunksRendered = 0;
    
    public void update() {
        frames++;
        long currentTime = System.nanoTime();
        
        if (currentTime - lastTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
            
            // Check if we should do GC
            if (Main.config.get().enableSmartGC && MemoryManager.shouldPerformGC()) {
                MemoryManager.performOptimizedGC();
            }
            
            // Update render distance optimizer
            if (Minecraft.getInstance().player != null) {
                double speed = Minecraft.getInstance().player.getDeltaMovement().length();
                RenderDistanceOptimizer.updateMovementState(speed);
                RenderDistanceOptimizer.optimizeRenderDistance(fps);
            }
        }
    }
    
    public void render(GuiGraphics graphics) {
        if (!Main.config.get().showFPSOverlay) {
            return;
        }
        
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        
        int y = 2;
        int color = getFPSColor(fps);
        
        // FPS Display
        graphics.drawString(font, "FPS: " + fps, 2, y, color, false);
        y += 10;
        
        if (Main.config.get().showDetailedStats) {
            // Memory
            if (Main.config.get().showMemoryUsage) {
                String memInfo = MemoryManager.getMemoryInfo();
                int memColor = getMemoryColor(MemoryManager.getMemoryUsagePercent());
                graphics.drawString(font, "Mem: " + memInfo, 2, y, memColor, false);
                y += 10;
            }
            
            // Entities
            graphics.drawString(font, "Entities: " + entitiesRendered, 2, y, 0xAAAAAA, false);
            y += 10;
            
            // Particles
            graphics.drawString(font, "Particles: " + particlesActive, 2, y, 0xAAAAAA, false);
            y += 10;
            
            // Render Distance
            int rd = mc.options.renderDistance().get();
            graphics.drawString(font, "RD: " + rd + " chunks", 2, y, 0xAAAAAA, false);
            y += 10;
        }
    }
    
    private int getFPSColor(int fps) {
        if (fps >= 60) return 0x00FF00; // Green
        if (fps >= 45) return 0xFFFF00; // Yellow
        if (fps >= 30) return 0xFF8800; // Orange
        return 0xFF0000; // Red
    }
    
    private int getMemoryColor(int percent) {
        if (percent < 70) return 0x00FF00; // Green
        if (percent < 85) return 0xFFFF00; // Yellow
        return 0xFF0000; // Red
    }
    
    public void setEntitiesRendered(int count) {
        this.entitiesRendered = count;
    }
    
    public void setParticlesActive(int count) {
        this.particlesActive = count;
    }
    
    public void setChunksRendered(int count) {
        this.chunksRendered = count;
    }
    
    public int getFPS() {
        return fps;
    }
}
