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
    
    // Greedy Meshing (disabled until properly implemented)
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
    
    // Translation Layer Optimizations
    public boolean disableFog = false;
    public boolean disablePostProcessing = false;
    public boolean disableEntityShaders = false;
    public boolean minimizeTextureBinds = false;
    
    // === CUSTOM POST-PROCESSING (LWJGL-based) ===
    
    // General
    public boolean enableCustomPostProcessing = false; // Master switch
    
    // Bloom (Glow Effect)
    public boolean bloomEnabled = false;
    public int bloomStrength = 10; // 0-100
    public int bloomRadius = 2; // 1-5 pixels
    
    // Vignette (Dark Edges)
    public boolean vignetteEnabled = false;
    public int vignetteStrength = 40; // 0-100
    
    // Color Grading
    public boolean colorGradingEnabled = false;
    public int brightness = 100; // 50-150 (100 = normal)
    public int contrast = 100; // 50-150 (100 = normal)
    public int saturation = 100; // 0-200 (100 = normal, 0 = grayscale)
    
    // Sharpening
    public boolean sharpenEnabled = false;
    public int sharpenAmount = 50; // 0-100
    
    // Film Grain
    public boolean filmGrainEnabled = false;
    public int grainStrength = 30; // 0-100
    
    // Chromatic Aberration
    public boolean chromaticAberrationEnabled = false;
    public int aberrationStrength = 20; // 0-100
    
    // Ambient Occlusion (Screen Space)
    public boolean ssaoEnabled = false;
    public int ssaoQuality = 1; // 0=Low, 1=Medium, 2=High
    public int ssaoRadius = 2; // blocks
    
    // Advanced
    public boolean ultraLowQualityMode = false;
}
