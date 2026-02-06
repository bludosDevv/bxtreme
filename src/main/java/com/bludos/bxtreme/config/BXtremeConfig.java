package com.bludos.bxtreme.config;

public class BXtremeConfig {
    // Entity Rendering
    public boolean aggressiveEntityCulling = true;
    public int maxEntityRenderDistance = 32;
    public boolean reduceTileEntityUpdates = false;
    
    // Particle System
    public int particleLimit = 100;
    public boolean disableUnnecessaryParticles = true;
    
    // Performance Monitor
    public boolean showFPSOverlay = true;
    public boolean showDetailedStats = false;
    
    // Greedy Meshing
    public boolean enableGreedyMeshing = false;
    public int greedyMeshMaxSize = 16;
    public boolean aggressiveFaceCulling = false;
    
    // Mobile Optimizations
    public boolean reduceAnimations = false;
    public boolean simplifyBlockModels = false;
    public int maxFramerate = 144;
    
    // Rendering
    public int chunkUpdateBudget = 3;
    public boolean disableSmoothLighting = false;
    public int entityTickDistance = 5;
    
    // === FILAMENT POST-PROCESSING ===
    
    // General
    public boolean enableFilament = true;
    public boolean filamentPostProcessing = true;
    
    // Bloom
    public boolean bloomEnabled = false;
    public int bloomStrength = 10; // 0-100
    
    // Ambient Occlusion
    public boolean ambientOcclusionEnabled = true;
    public int aoQuality = 1; // 0=Low, 1=Medium, 2=High
    
    // Depth of Field
    public boolean depthOfFieldEnabled = false;
    public int dofFocusDistance = 10; // blocks
    public int dofBlurAmount = 50; // 0-100
    
    // Anti-Aliasing
    public int antiAliasing = 1; // 0=None, 1=FXAA, 2=MSAA
    
    // Shadow Quality
    public int shadowQuality = 1; // 0=Off, 1=Low, 2=Medium, 3=High
    
    // Dynamic Resolution (maintains FPS)
    public boolean dynamicResolution = true;
    public int minResolutionScale = 50; // 50-100%
}
