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
            return true;
        }
        
        Vec3 cameraPos = camera.getPosition();
        double distanceSq = entity.distanceToSqr(cameraPos);
        double maxDistance = config.maxEntityRenderDistance;
        
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.player || entity == mc.player.getVehicle()) {
            return true;
        }
        
        if (distanceSq > maxDistance * maxDistance) {
            return false;
        }
        
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
     */
    public static int getOptimalRenderDistance(int currentFPS, int configuredDistance) {
        if (currentFPS >= 60) {
            return configuredDistance;
        } else if (currentFPS >= 30) {
            return Math.max(2, configuredDistance - 2);
        } else if (currentFPS >= 15) {
            return Math.max(2, configuredDistance - 4);
        } else {
            return 2;
        }
    }
    
    /**
     * FIXED: Don't skip tile entity rendering - causes invisible blocks
     */
    public static boolean shouldRenderTileEntity(double distanceSq) {
        // Always render tile entities - the invisible block bug was from this
        return true;
    }
}