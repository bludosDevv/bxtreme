package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;
import net.minecraft.client.Minecraft;

/**
 * Dynamic Render Distance Optimizer
 * Automatically adjusts render distance based on movement and FPS
 */
public class RenderDistanceOptimizer {
    
    private static int targetFPS = 60;
    private static int minRenderDistance = 2;
    private static int maxRenderDistance = 16;
    
    private static boolean isPlayerMovingFast = false;
    private static int consecutiveLowFPSFrames = 0;
    private static final int LOW_FPS_THRESHOLD = 45;
    
    /**
     * Update based on player movement
     */
    public static void updateMovementState(double movementSpeed) {
        // Consider "fast" if moving faster than sprinting speed
        isPlayerMovingFast = movementSpeed > 0.2; // Elytra speed threshold
    }
    
    /**
     * Optimize render distance based on FPS
     */
    public static void optimizeRenderDistance(int currentFPS) {
        if (!Main.config.get().enableDynamicRenderDistance) {
            return;
        }
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        
        int currentRenderDistance = mc.options.renderDistance().get();
        
        // Track low FPS
        if (currentFPS < LOW_FPS_THRESHOLD) {
            consecutiveLowFPSFrames++;
        } else {
            consecutiveLowFPSFrames = 0;
        }
        
        // If moving fast (elytra) and FPS is low, reduce render distance
        if (isPlayerMovingFast && consecutiveLowFPSFrames >= 60) { // 3 seconds of low FPS
            int newDistance = Math.max(minRenderDistance, currentRenderDistance - 2);
            if (newDistance != currentRenderDistance) {
                mc.options.renderDistance().set(newDistance);
                Main.LOGGER.info("Reduced render distance to {} due to fast movement + low FPS", newDistance);
                consecutiveLowFPSFrames = 0;
            }
        }
        
        // If standing still with good FPS, can increase render distance
        if (!isPlayerMovingFast && currentFPS > 70 && consecutiveLowFPSFrames == 0) {
            int newDistance = Math.min(maxRenderDistance, currentRenderDistance + 1);
            if (newDistance != currentRenderDistance) {
                mc.options.renderDistance().set(newDistance);
                Main.LOGGER.info("Increased render distance to {} (good performance)", newDistance);
            }
        }
    }
    
    /**
     * Get recommended render distance for current conditions
     */
    public static int getRecommendedRenderDistance(int currentFPS, boolean movingFast) {
        if (movingFast) {
            if (currentFPS < 40) return 2;
            if (currentFPS < 50) return 4;
            return 6;
        } else {
            if (currentFPS > 80) return 12;
            if (currentFPS > 60) return 8;
            if (currentFPS > 45) return 6;
            return 4;
        }
    }
}
