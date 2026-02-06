package com.bludos.bxtreme.render;

/**
 * Custom Post-Processing Configuration
 * Shader-free visual effects optimized for mobile
 */
public class PostProcessingConfig {
    
    // Bloom (glow effect)
    public boolean enableBloom = false;
    public int bloomIntensity = 50; // 0-100
    public int bloomRadius = 2; // pixels
    
    // Enhanced Shadows
    public boolean enhancedShadows = false;
    public int shadowQuality = 1; // 0=Low, 1=Medium, 2=High
    public int shadowDistance = 32; // blocks
    
    // Ambient Occlusion (Contact Shadows)
    public boolean customAO = false;
    public int aoQuality = 1; // 0=Low, 1=Medium, 2=High
    
    // Color Grading
    public boolean colorGrading = false;
    public int brightness = 100; // 0-200
    public int contrast = 100; // 0-200
    public int saturation = 100; // 0-200
    
    // Depth of Field (Blur background)
    public boolean depthOfField = false;
    public int dofStrength = 50; // 0-100
    
    // Motion Blur
    public boolean motionBlur = false;
    public int motionBlurAmount = 30; // 0-100
    
    // Vignette (Dark edges)
    public boolean vignette = false;
    public int vignetteStrength = 40; // 0-100
    
    // Screen Space Reflections
    public boolean ssr = false;
    public int ssrQuality = 0; // 0=Low, 1=Medium, 2=High
    
    // God Rays (Light shafts)
    public boolean godRays = false;
    public int godRayQuality = 0; // 0=Low, 1=Medium, 2=High
    
    // Chromatic Aberration
    public boolean chromaticAberration = false;
    public int aberrationStrength = 20; // 0-100
    
    // Film Grain
    public boolean filmGrain = false;
    public int grainStrength = 30; // 0-100
    
    // Sharpen
    public boolean sharpen = false;
    public int sharpenAmount = 50; // 0-100
}
