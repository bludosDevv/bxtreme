package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;
import com.bludos.bxtreme.config.BXtremeConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RenderOptimizer {
    
    /**
     * Determines if an entity should be rendered based on distance and config settings
     */
    public static boolean shouldRenderEntity(Entity entity, Camera camera) {
        BXtremeConfig config = Main.config.get();
        
        if (!config.aggressiveEntityCulling) {
            return true; // Let vanilla handle it
        }
        
        Vec3 cameraPos = camera.getPosition();
        double distanceSq = entity.distanceToSqr(cameraPos);
        double maxDistance = config.maxEntityRenderDistance;
        
        // Don't cull the player or their mount
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.player || entity == mc.player.getVehicle()) {
            return true;
        }
        
        // Check distance
        if (distanceSq > maxDistance * maxDistance) {
            return false;
        }
        
        // Additional culling: check if entity is in camera frustum
        // This is a simple check - more advanced frustum culling comes in Phase 2
        return true;
    }
    
    /**
     * Determines if a particle should spawn based on current particle limit
     */
    public static boolean shouldSpawnParticle(int currentParticleCount) {
        BXtremeConfig config = Main.config.get();
        
        if (!config.disableUnnecessaryParticles) {
            return true;
        }
        
        return currentParticleCount < config.particleLimit;
    }
    
    /**
     * Calculate optimal chunk render distance based on current FPS
     * This is dynamic - reduces render distance if FPS drops
     */
    public static int getOptimalRenderDistance(int currentFPS, int configuredDistance) {
        if (currentFPS >= 60) {
            return configuredDistance; // Use configured distance
        } else if (currentFPS >= 30) {
            return Math.max(2, configuredDistance - 2); // Reduce by 2
        } else if (currentFPS >= 15) {
            return Math.max(2, configuredDistance - 4); // Reduce by 4
        } else {
            return 2; // Minimum 2 chunks
        }
    }
    
    /**
     * Checks if we should skip tile entity rendering for performance
     */
    public static boolean shouldRenderTileEntity(double distanceSq) {
        BXtremeConfig config = Main.config.get();
        
        if (!config.reduceTileEntityUpdates) {
            return true;
        }
        
        // Don't render tile entities beyond 32 blocks
        return distanceSq <= 32 * 32;
    }
}