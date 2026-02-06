package com.bludos.bxtreme.gui;

import com.bludos.bxtreme.Main;
import com.bludos.bxtreme.config.BXtremeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPLETE BXtreme Settings GUI
 * Sodium-style with all post-processing effects
 */
public class BXtremeVideoSettingsScreen extends Screen {
    
    private final Screen lastScreen;
    private BXtremeConfig config;
    
    // UI Layout
    private static final int SIDEBAR_WIDTH = 120;
    private static final int SIDEBAR_X = 10;
    private static final int CONTENT_X = SIDEBAR_WIDTH + 20;
    private static final int TOP_Y = 40;
    
    // Categories
    private enum Category {
        GENERAL("General"),
        QUALITY("Quality"),
        PERFORMANCE("Performance"),
        POST_PROCESSING("Post Processing"),
        ADVANCED("Advanced");
        
        final String name;
        Category(String name) { this.name = name; }
    }
    
    private Category currentCategory = Category.PERFORMANCE;
    private EditBox searchBox;
    
    private static final String[] PRESETS = {"ULTRA LOW", "LOW", "MEDIUM", "HIGH", "CUSTOM"};
    private int currentPreset = 4;
    
    private List<Button> categoryButtons = new ArrayList<>();
    private List<Button> settingButtons = new ArrayList<>();
    
    public BXtremeVideoSettingsScreen(Screen lastScreen) {
        super(Component.literal("BXtreme Performance"));
        this.lastScreen = lastScreen;
        this.config = Main.config.get();
    }
    
    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        categoryButtons.clear();
        settingButtons.clear();
        
        // Search bar
        searchBox = new EditBox(this.font, this.width / 2 - 100, 10, 200, 20, Component.literal("Search..."));
        searchBox.setHint(Component.literal("Search options..."));
        addRenderableWidget(searchBox);
        
        buildSidebar();
        buildContentArea();
        buildBottomButtons();
    }
    
    private void buildSidebar() {
        int y = TOP_Y;
        int spacing = 25;
        
        for (Category cat : Category.values()) {
            boolean selected = (cat == currentCategory);
            final Category finalCat = cat;
            
            Button btn = Button.builder(
                Component.literal((selected ? "> " : "") + cat.name),
                button -> {
                    currentCategory = finalCat;
                    this.rebuildWidgets();
                }
            ).bounds(SIDEBAR_X, y, SIDEBAR_WIDTH, 20).build();
            
            categoryButtons.add(btn);
            addRenderableWidget(btn);
            y += spacing;
        }
    }
    
    private void buildContentArea() {
        int y = TOP_Y;
        int buttonWidth = Math.min(300, this.width - CONTENT_X - 20);
        int spacing = 24;
        
        switch (currentCategory) {
            case GENERAL:
                // Preset
                addSetting("Preset: " + PRESETS[currentPreset], CONTENT_X, y, buttonWidth, btn -> {
                    currentPreset = (currentPreset + 1) % PRESETS.length;
                    if (currentPreset < 4) applyPreset(currentPreset);
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // FPS Overlay
                addSetting("FPS Overlay: " + (config.showFPSOverlay ? "ON" : "OFF"), 
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.showFPSOverlay = !config.showFPSOverlay;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Detailed Stats
                addSetting("Detailed Stats: " + (config.showDetailedStats ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.showDetailedStats = !config.showDetailedStats;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                break;
                
            case QUALITY:
                // Graphics
                String gfx = Minecraft.getInstance().options.graphicsMode().get().toString();
                addSetting("Graphics: " + gfx, CONTENT_X, y, buttonWidth, btn -> {
                    int c = Minecraft.getInstance().options.graphicsMode().get().getId();
                    Minecraft.getInstance().options.graphicsMode().set(
                        net.minecraft.client.GraphicsStatus.byId((c + 1) % 3));
                    currentPreset = 4;
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Smooth Lighting
                boolean smooth = Minecraft.getInstance().options.ambientOcclusion().get();
                addSetting("Smooth Lighting: " + (smooth ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    Minecraft.getInstance().options.ambientOcclusion().set(!smooth);
                    config.disableSmoothLighting = !smooth;
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Entity Shadows
                boolean shadows = Minecraft.getInstance().options.entityShadows().get();
                addSetting("Entity Shadows: " + (shadows ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    Minecraft.getInstance().options.entityShadows().set(!shadows);
                    currentPreset = 4;
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Greedy Meshing
                addSetting("Greedy Meshing: " + (config.enableGreedyMeshing ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.enableGreedyMeshing = !config.enableGreedyMeshing;
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                break;
                
            case PERFORMANCE:
                // Render Distance
                int rd = Minecraft.getInstance().options.renderDistance().get();
                addSetting("Render Distance: " + rd, CONTENT_X, y, buttonWidth, btn -> {
                    int newVal = (rd % 16) + 2;
                    Minecraft.getInstance().options.renderDistance().set(newVal);
                    currentPreset = 4;
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Entity Distance
                addSetting("Entity Distance: " + config.maxEntityRenderDistance,
                    CONTENT_X, y, buttonWidth, btn -> {
                    int[] vals = {16, 24, 32, 48, 64};
                    int idx = 0;
                    for (int i = 0; i < vals.length; i++) {
                        if (vals[i] == config.maxEntityRenderDistance) idx = i;
                    }
                    config.maxEntityRenderDistance = vals[(idx + 1) % vals.length];
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Particles
                addSetting("Particle Limit: " + config.particleLimit,
                    CONTENT_X, y, buttonWidth, btn -> {
                    int[] vals = {10, 50, 100, 250, 500, 1000};
                    int idx = 0;
                    for (int i = 0; i < vals.length; i++) {
                        if (vals[i] == config.particleLimit) idx = i;
                    }
                    config.particleLimit = vals[(idx + 1) % vals.length];
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Chunk Updates
                addSetting("Chunk Updates/Frame: " + config.chunkUpdateBudget,
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.chunkUpdateBudget = (config.chunkUpdateBudget % 10) + 1;
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Max FPS
                addSetting("Max Framerate: " + config.maxFramerate,
                    CONTENT_X, y, buttonWidth, btn -> {
                    int[] vals = {30, 60, 90, 120, 144, 240};
                    int idx = 0;
                    for (int i = 0; i < vals.length; i++) {
                        if (vals[i] == config.maxFramerate) idx = i;
                    }
                    config.maxFramerate = vals[(idx + 1) % vals.length];
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                break;
                
            case POST_PROCESSING:
                // Master Switch
                addSetting("Post-Processing: " + (config.enableCustomPostProcessing ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.enableCustomPostProcessing = !config.enableCustomPostProcessing;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Bloom
                addSetting("Bloom: " + (config.bloomEnabled ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.bloomEnabled = !config.bloomEnabled;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                if (config.bloomEnabled) {
                    addSetting("  Bloom Strength: " + config.bloomStrength + "%",
                        CONTENT_X, y, buttonWidth, btn -> {
                        config.bloomStrength = (config.bloomStrength + 10) % 110;
                        Main.config.save();
                        this.rebuildWidgets();
                    });
                    y += spacing;
                }
                
                // Vignette
                addSetting("Vignette: " + (config.vignetteEnabled ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.vignetteEnabled = !config.vignetteEnabled;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                if (config.vignetteEnabled) {
                    addSetting("  Vignette Strength: " + config.vignetteStrength + "%",
                        CONTENT_X, y, buttonWidth, btn -> {
                        config.vignetteStrength = (config.vignetteStrength + 10) % 110;
                        Main.config.save();
                        this.rebuildWidgets();
                    });
                    y += spacing;
                }
                
                // Color Grading
                addSetting("Color Grading: " + (config.colorGradingEnabled ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.colorGradingEnabled = !config.colorGradingEnabled;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                if (config.colorGradingEnabled) {
                    addSetting("  Brightness: " + config.brightness,
                        CONTENT_X, y, buttonWidth, btn -> {
                        config.brightness = ((config.brightness - 50 + 10) % 110) + 50;
                        Main.config.save();
                        this.rebuildWidgets();
                    });
                    y += spacing;
                    
                    addSetting("  Contrast: " + config.contrast,
                        CONTENT_X, y, buttonWidth, btn -> {
                        config.contrast = ((config.contrast - 50 + 10) % 110) + 50;
                        Main.config.save();
                        this.rebuildWidgets();
                    });
                    y += spacing;
                    
                    addSetting("  Saturation: " + config.saturation,
                        CONTENT_X, y, buttonWidth, btn -> {
                        config.saturation = (config.saturation + 10) % 210;
                        Main.config.save();
                        this.rebuildWidgets();
                    });
                    y += spacing;
                }
                
                // Sharpening
                addSetting("Sharpening: " + (config.sharpenEnabled ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.sharpenEnabled = !config.sharpenEnabled;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // SSAO
                addSetting("Ambient Occlusion: " + (config.ssaoEnabled ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.ssaoEnabled = !config.ssaoEnabled;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                break;
                
            case ADVANCED:
                // Ultra Low Mode
                addSetting("Ultra Low Mode: " + (config.ultraLowQualityMode ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.ultraLowQualityMode = !config.ultraLowQualityMode;
                    currentPreset = 4;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Disable Fog
                addSetting("Disable Fog: " + (config.disableFog ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.disableFog = !config.disableFog;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                // Entity Culling
                addSetting("Entity Culling: " + (config.aggressiveEntityCulling ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth, btn -> {
                    config.aggressiveEntityCulling = !config.aggressiveEntityCulling;
                    Main.config.save();
                    this.rebuildWidgets();
                });
                y += spacing;
                
                break;
        }
    }
    
    private void addSetting(String text, int x, int y, int width, Button.OnPress onPress) {
        Button btn = Button.builder(Component.literal(text), onPress)
            .bounds(x, y, width, 20)
            .build();
        settingButtons.add(btn);
        addRenderableWidget(btn);
    }
    
    private void buildBottomButtons() {
        int bottomY = this.height - 30;
        int buttonWidth = 100;
        int gap = 10;
        
        addRenderableWidget(Button.builder(
            Component.literal("Apply"),
            btn -> Main.config.save()
        ).bounds(this.width / 2 - buttonWidth - gap / 2, bottomY, buttonWidth, 20).build());
        
        addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> {
                Main.config.save();
                this.minecraft.setScreen(lastScreen);
            }
        ).bounds(this.width / 2 + gap / 2, bottomY, buttonWidth, 20).build());
    }
    
    private void applyPreset(int preset) {
        switch (preset) {
            case 0: // ULTRA LOW
                config.maxEntityRenderDistance = 16;
                config.particleLimit = 10;
                config.chunkUpdateBudget = 1;
                config.ultraLowQualityMode = false;
                config.enableGreedyMeshing = true;
                config.maxFramerate = 60;
                config.enableCustomPostProcessing = false;
                Minecraft.getInstance().options.renderDistance().set(2);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
                Minecraft.getInstance().options.entityShadows().set(false);
                Minecraft.getInstance().options.ambientOcclusion().set(false);
                break;
            case 1: // LOW
                config.maxEntityRenderDistance = 24;
                config.particleLimit = 50;
                config.chunkUpdateBudget = 2;
                config.ultraLowQualityMode = false;
                config.enableGreedyMeshing = true;
                config.maxFramerate = 90;
                config.enableCustomPostProcessing = false;
                Minecraft.getInstance().options.renderDistance().set(4);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
                Minecraft.getInstance().options.entityShadows().set(false);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
            case 2: // MEDIUM
                config.maxEntityRenderDistance = 32;
                config.particleLimit = 100;
                config.chunkUpdateBudget = 3;
                config.ultraLowQualityMode = false;
                config.enableGreedyMeshing = false;
                config.maxFramerate = 120;
                config.enableCustomPostProcessing = true;
                config.bloomEnabled = true;
                Minecraft.getInstance().options.renderDistance().set(6);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FANCY);
                Minecraft.getInstance().options.entityShadows().set(true);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
            case 3: // HIGH
                config.maxEntityRenderDistance = 48;
                config.particleLimit = 500;
                config.chunkUpdateBudget = 5;
                config.ultraLowQualityMode = false;
                config.enableGreedyMeshing = false;
                config.maxFramerate = 144;
                config.enableCustomPostProcessing = true;
                config.bloomEnabled = true;
                config.vignetteEnabled = true;
                config.ssaoEnabled = true;
                Minecraft.getInstance().options.renderDistance().set(10);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FANCY);
                Minecraft.getInstance().options.entityShadows().set(true);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
        }
        Main.config.save();
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        
        graphics.fill(0, 35, SIDEBAR_WIDTH + 15, this.height - 35, 0xDD000000);
        graphics.fill(CONTENT_X - 5, 35, this.width - 5, this.height - 35, 0x88000000);
        
        graphics.drawString(this.font, currentCategory.name, CONTENT_X, TOP_Y - 20, 0xFFFFFF);
        
        super.render(graphics, mouseX, mouseY, partialTick);
        
        if (Main.performanceMonitor != null) {
            int fps = Main.performanceMonitor.getFPS();
            int color = fps >= 60 ? 0x00FF00 : fps >= 30 ? 0xFFFF00 : 0xFF0000;
            graphics.drawString(this.font, "FPS: " + fps, this.width - 70, 15, color);
        }
    }
    
    @Override
    public void onClose() {
        Main.config.save();
        this.minecraft.setScreen(lastScreen);
    }
}
