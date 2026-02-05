package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class PerformanceMonitor {
    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    private long lastMemory = 0;
    
    // Performance tracking
    private int entitiesRendered = 0;
    private int particlesActive = 0;
    private int chunksRendered = 0;
    
    public void update() {
        frames++;
        long currentTime = System.nanoTime();
        
        if (currentTime - lastTime >= 1_000_000_000L) { // 1 second
            fps = frames;
            frames = 0;
            lastTime = currentTime;
            
            // Update memory info
            Runtime runtime = Runtime.getRuntime();
            lastMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024; // MB
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
            graphics.drawString(font, "Mem: " + lastMemory + "MB", 2, y, 0xAAAAAA, false);
            y += 10;
            
            // Entities
            graphics.drawString(font, "Entities: " + entitiesRendered, 2, y, 0xAAAAAA, false);
            y += 10;
            
            // Particles
            graphics.drawString(font, "Particles: " + particlesActive, 2, y, 0xAAAAAA, false);
            y += 10;
            
            // Chunks
            graphics.drawString(font, "Chunks: " + chunksRendered, 2, y, 0xAAAAAA, false);
        }
    }
    
    private int getFPSColor(int fps) {
        if (fps >= 60) return 0x00FF00; // Green
        if (fps >= 30) return 0xFFFF00; // Yellow
        if (fps >= 15) return 0xFF8800; // Orange
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