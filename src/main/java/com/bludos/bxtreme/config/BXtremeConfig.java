package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // NUCLEAR Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 20; // EVEN LOWER for translation overhead
    public boolean reduceTileEntityUpdates = true;
    
    // NUCLEAR Particle System
    public int particleLimit = 10; // ONLY 10 PARTICLES (vanilla: 4000)
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = true;
    
    // NUCLEAR OPTIONS - OPTIMIZED FOR TRANSLATION LAYER
    public boolean asyncChunkBuilding = true;
    public boolean aggressiveOcclusionCulling = true;
    public boolean ultraLowQualityMode = true;
    
    // NUCLEAR Mobile Optimizations
    public boolean reduceAnimations = true;
    public boolean simplifyBlockModels = true;
    public int maxFramerate = 60;
    
    // NUCLEAR Rendering - TRANSLATION LAYER OPTIMIZED
    public int chunkUpdateBudget = 1; // Minimize state changes
    public boolean skipBackfaceCulling = false;
    public boolean disableSmoothLighting = true;
    public boolean disableFancyGraphics = true;
    public int entityTickDistance = 2;
    
    // NEW: Translation Layer Optimizations
    public boolean disableFog = true; // Shader complexity killer
    public boolean disablePostProcessing = true; // Framebuffer overhead killer
    public boolean disableEntityShaders = true; // Creeper glow, spider eyes, etc
    public boolean minimizeTextureBinds = true; // Reduce GL state changes
}