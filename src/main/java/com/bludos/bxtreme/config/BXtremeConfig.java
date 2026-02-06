package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 24;
    public boolean reduceTileEntityUpdates = false;
    
    // Particle System
    public int particleLimit = 50;
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = false;
    
    // Greedy Meshing System
    public boolean enableGreedyMeshing = true; // NEW: Enable bedrock-style meshing
    public int greedyMeshMaxSize = 16; // NEW: Maximum quad size (16x16 blocks)
    public boolean aggressiveFaceCulling = true; // NEW: Cull internal faces
    
    // Experimental
    public boolean asyncChunkBuilding = false;
    public boolean aggressiveOcclusionCulling = false;
    public boolean ultraLowQualityMode = false;
    
    // Mobile Optimizations
    public boolean reduceAnimations = false;
    public boolean simplifyBlockModels = false;
    public int maxFramerate = 90;
    
    // Rendering
    public int chunkUpdateBudget = 2;
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
