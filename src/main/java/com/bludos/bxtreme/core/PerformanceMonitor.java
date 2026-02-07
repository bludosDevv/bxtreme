package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Simple Performance Monitor
 */
public class PerformanceMonitor {
    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    
    public void update() {
        frames++;
        long currentTime = System.nanoTime();
        
        if (currentTime - lastTime >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            lastTime = currentTime;
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
        
        if (Main.config.get().showDetailedStats) {
            y += 10;
            
            // Memory
            Runtime runtime = Runtime.getRuntime();
            long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            long maxMB = runtime.maxMemory() / 1024 / 1024;
            graphics.drawString(font, "Mem: " + usedMB + "MB / " + maxMB + "MB", 2, y, 0xAAAAAA, false);
            
            y += 10;
            int rd = mc.options.renderDistance().get();
            graphics.drawString(font, "RD: " + rd + " chunks", 2, y, 0xAAAAAA, false);
        }
    }
    
    private int getFPSColor(int fps) {
        if (fps >= 60) return 0x00FF00;
        if (fps >= 45) return 0xFFFF00;
        if (fps >= 30) return 0xFF8800;
        return 0xFF0000;
    }
    
    public int getFPS() {
        return fps;
    }
}
