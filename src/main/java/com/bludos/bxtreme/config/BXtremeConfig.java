package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 24; // Safe default
    public boolean reduceTileEntityUpdates = false; // DISABLED - causes invisible blocks
    
    // Particle System
    public int particleLimit = 50; // Reasonable default
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = false;
    
    // Experimental - ALL DISABLED BY DEFAULT
    public boolean asyncChunkBuilding = false;
    public boolean aggressiveOcclusionCulling = false;
    public boolean ultraLowQualityMode = false; // DISABLED - causes bugs
    
    // Mobile Optimizations
    public boolean reduceAnimations = false;
    public boolean simplifyBlockModels = false;
    public int maxFramerate = 90;
    
    // Rendering
    public int chunkUpdateBudget = 2; // Safe default
    public boolean skipBackfaceCulling = false;
    public boolean disableSmoothLighting = false;
    public boolean disableFancyGraphics = false;
    public int entityTickDistance = 4;
    
    // Translation Layer Optimizations
    public boolean disableFog = false;
    public boolean disablePostProcessing = false;
    public boolean disableEntityShaders = false;
    public boolean minimizeTextureBinds = false;
}
