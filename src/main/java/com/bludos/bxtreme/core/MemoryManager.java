package com.bludos.bxtreme.core;

import com.bludos.bxtreme.Main;

/**
 * Advanced Memory Manager
 * Optimizes garbage collection for mobile devices
 */
public class MemoryManager {
    
    private static long lastGCTime = 0;
    private static final long GC_COOLDOWN = 10000; // 10 seconds minimum between GCs
    private static final double MEMORY_THRESHOLD = 0.80; // GC at 80% memory usage
    
    private static int gcCount = 0;
    private static long totalMemoryFreed = 0;
    
    /**
     * Check if we should perform garbage collection
     */
    public static boolean shouldPerformGC() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsagePercent = (double) usedMemory / maxMemory;
        
        // Only GC if we're above threshold AND cooldown has passed
        long currentTime = System.currentTimeMillis();
        boolean cooldownPassed = (currentTime - lastGCTime) > GC_COOLDOWN;
        boolean aboveThreshold = memoryUsagePercent > MEMORY_THRESHOLD;
        
        return cooldownPassed && aboveThreshold;
    }
    
    /**
     * Perform optimized garbage collection
     */
    public static void performOptimizedGC() {
        Runtime runtime = Runtime.getRuntime();
        long beforeFree = runtime.freeMemory();
        
        // Run GC
        System.gc();
        
        long afterFree = runtime.freeMemory();
        long freed = afterFree - beforeFree;
        
        lastGCTime = System.currentTimeMillis();
        gcCount++;
        totalMemoryFreed += freed;
        
        if (Main.config.get().showDetailedStats) {
            Main.LOGGER.info("GC #{} - Freed: {}MB - Total Freed: {}MB", 
                gcCount, 
                freed / 1024 / 1024,
                totalMemoryFreed / 1024 / 1024
            );
        }
    }
    
    /**
     * Get current memory usage percentage
     */
    public static int getMemoryUsagePercent() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return (int) ((double) usedMemory / maxMemory * 100);
    }
    
    /**
     * Get memory info string
     */
    public static String getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        return String.format("%dMB / %dMB (%d%%)", usedMemory, maxMemory, getMemoryUsagePercent());
    }
    
    /**
     * Force aggressive cleanup (for when switching dimensions, etc.)
     */
    public static void forceCleanup() {
        System.gc();
        System.runFinalization();
        System.gc();
        
        Main.LOGGER.info("Forced aggressive cleanup");
    }
}
