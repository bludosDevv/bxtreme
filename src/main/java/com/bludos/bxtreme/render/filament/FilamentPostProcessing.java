package com.bludos.bxtreme.render.filament;

import com.bludos.bxtreme.Main;
import com.google.android.filament.View;

/**
 * Filament Post-Processing Manager
 * Manages all visual effects using Filament's built-in systems
 */
public class FilamentPostProcessing {
    
    private FilamentRenderer renderer;
    
    // Effect settings
    private boolean bloomEnabled = false;
    private float bloomStrength = 0.1f;
    
    private boolean aoEnabled = true;
    private View.QualityLevel aoQuality = View.QualityLevel.MEDIUM;
    
    private boolean dofEnabled = false;
    private float dofFocusDistance = 10.0f;
    private float dofBlurScale = 1.0f;
    
    private View.AntiAliasing aaMode = View.AntiAliasing.FXAA;
    
    public FilamentPostProcessing(FilamentRenderer renderer) {
        this.renderer = renderer;
    }
    
    /**
     * Apply all post-processing settings
     */
    public void applySettings() {
        if (!renderer.isInitialized()) return;
        
        // Bloom
        renderer.setBloomEnabled(bloomEnabled, bloomStrength);
        
        // Ambient Occlusion
        renderer.setAmbientOcclusion(aoEnabled, aoQuality);
        
        // Depth of Field
        renderer.setDepthOfField(dofEnabled, dofFocusDistance, dofBlurScale);
        
        // Anti-aliasing
        renderer.setAntiAliasing(aaMode);
        
        Main.LOGGER.info("Applied Filament post-processing settings");
    }
    
    // ===== BLOOM =====
    
    public void setBloomEnabled(boolean enabled) {
        this.bloomEnabled = enabled;
        renderer.setBloomEnabled(enabled, bloomStrength);
    }
    
    public void setBloomStrength(float strength) {
        this.bloomStrength = Math.max(0.0f, Math.min(1.0f, strength));
        if (bloomEnabled) {
            renderer.setBloomEnabled(true, bloomStrength);
        }
    }
    
    public boolean isBloomEnabled() {
        return bloomEnabled;
    }
    
    public float getBloomStrength() {
        return bloomStrength;
    }
    
    // ===== AMBIENT OCCLUSION =====
    
    public void setAmbientOcclusionEnabled(boolean enabled) {
        this.aoEnabled = enabled;
        renderer.setAmbientOcclusion(enabled, aoQuality);
    }
    
    public void setAmbientOcclusionQuality(View.QualityLevel quality) {
        this.aoQuality = quality;
        if (aoEnabled) {
            renderer.setAmbientOcclusion(true, quality);
        }
    }
    
    public boolean isAmbientOcclusionEnabled() {
        return aoEnabled;
    }
    
    public View.QualityLevel getAmbientOcclusionQuality() {
        return aoQuality;
    }
    
    // ===== DEPTH OF FIELD =====
    
    public void setDepthOfFieldEnabled(boolean enabled) {
        this.dofEnabled = enabled;
        renderer.setDepthOfField(enabled, dofFocusDistance, dofBlurScale);
    }
    
    public void setDepthOfFieldDistance(float distance) {
        this.dofFocusDistance = distance;
        if (dofEnabled) {
            renderer.setDepthOfField(true, dofFocusDistance, dofBlurScale);
        }
    }
    
    public void setDepthOfFieldBlur(float blur) {
        this.dofBlurScale = blur;
        if (dofEnabled) {
            renderer.setDepthOfField(true, dofFocusDistance, dofBlurScale);
        }
    }
    
    public boolean isDepthOfFieldEnabled() {
        return dofEnabled;
    }
    
    // ===== ANTI-ALIASING =====
    
    public void setAntiAliasing(View.AntiAliasing mode) {
        this.aaMode = mode;
        renderer.setAntiAliasing(mode);
    }
    
    public View.AntiAliasing getAntiAliasing() {
        return aaMode;
    }
    
    // ===== PRESET MODES =====
    
    /**
     * Ultra Low preset - Everything off
     */
    public void applyUltraLowPreset() {
        setBloomEnabled(false);
        setAmbientOcclusionEnabled(false);
        setDepthOfFieldEnabled(false);
        setAntiAliasing(View.AntiAliasing.NONE);
        applySettings();
    }
    
    /**
     * Low preset - Minimal effects
     */
    public void applyLowPreset() {
        setBloomEnabled(false);
        setAmbientOcclusionEnabled(false);
        setDepthOfFieldEnabled(false);
        setAntiAliasing(View.AntiAliasing.FXAA);
        applySettings();
    }
    
    /**
     * Medium preset - Balanced
     */
    public void applyMediumPreset() {
        setBloomEnabled(true);
        setBloomStrength(0.1f);
        setAmbientOcclusionEnabled(true);
        setAmbientOcclusionQuality(View.QualityLevel.MEDIUM);
        setDepthOfFieldEnabled(false);
        setAntiAliasing(View.AntiAliasing.FXAA);
        applySettings();
    }
    
    /**
     * High preset - All effects
     */
    public void applyHighPreset() {
        setBloomEnabled(true);
        setBloomStrength(0.15f);
        setAmbientOcclusionEnabled(true);
        setAmbientOcclusionQuality(View.QualityLevel.HIGH);
        setDepthOfFieldEnabled(false);
        setAntiAliasing(View.AntiAliasing.FXAA);
        applySettings();
    }
    
    /**
     * Ultra preset - Maximum quality
     */
    public void applyUltraPreset() {
        setBloomEnabled(true);
        setBloomStrength(0.2f);
        setAmbientOcclusionEnabled(true);
        setAmbientOcclusionQuality(View.QualityLevel.HIGH);
        setDepthOfFieldEnabled(true);
        setDepthOfFieldDistance(10.0f);
        setDepthOfFieldBlur(1.0f);
        setAntiAliasing(View.AntiAliasing.FXAA);
        applySettings();
    }
}
