package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 32; // blocks (vanilla: 64+)
    public boolean reduceTileEntityUpdates = true;
    
    // Particle System - REDUCED FOR MOBILE
    public int particleLimit = 50; // REDUCED from 100 (vanilla: 4000)
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = false;
    
    // Experimental (Phase 2+)
    public boolean asyncChunkBuilding = false;
    public boolean aggressiveOcclusionCulling = false;
    public boolean ultraLowQualityMode = false;
    
    // Mobile Optimizations
    public boolean reduceAnimations = true;
    public boolean simplifyBlockModels = false;
    public int maxFramerate = 90; // REDUCED from 120 - better battery life
    
    // NEW: Chunk rendering optimization
    public int chunkUpdateBudget = 2; // Max chunk updates per frame
    public boolean skipBackfaceCulling = false; // Phase 3 feature
}