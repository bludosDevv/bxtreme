package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // NUCLEAR Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 24; // REDUCED from 32
    public boolean reduceTileEntityUpdates = true;
    
    // NUCLEAR Particle System
    public int particleLimit = 20; // DRASTICALLY REDUCED (vanilla: 4000)
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = true; // ENABLE to see what's killing FPS
    
    // NUCLEAR OPTIONS - ENABLED BY DEFAULT
    public boolean asyncChunkBuilding = true; // Enable async
    public boolean aggressiveOcclusionCulling = true; // Enable aggressive culling
    public boolean ultraLowQualityMode = true; // ENABLE ULTRA LOW
    
    // NUCLEAR Mobile Optimizations
    public boolean reduceAnimations = true;
    public boolean simplifyBlockModels = true; // ENABLE
    public int maxFramerate = 60; // Cap at 60 for stability
    
    // NUCLEAR Rendering
    public int chunkUpdateBudget = 1; // ONLY 1 chunk update per frame
    public boolean skipBackfaceCulling = false;
    public boolean disableSmoothLighting = true; // NEW: Kill smooth lighting
    public boolean disableFancyGraphics = true; // NEW: Force fast graphics
    public int entityTickDistance = 2; // NEW: Only tick entities 2 chunks away
}