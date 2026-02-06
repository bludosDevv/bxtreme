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
 * SODIUM-STYLE UI - Professional Settings Screen
 * Categories on left, settings on right
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
        ADVANCED("Advanced");
        
        final String name;
        Category(String name) { this.name = name; }
    }
    
    private Category currentCategory = Category.PERFORMANCE;
    private EditBox searchBox;
    
    // Preset system
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
        
        // Search bar at top
        searchBox = new EditBox(this.font, this.width / 2 - 100, 10, 200, 20, Component.literal("Search..."));
        searchBox.setHint(Component.literal("Search options..."));
        addRenderableWidget(searchBox);
        
        // Build sidebar categories
        buildSidebar();
        
        // Build content area
        buildContentArea();
        
        // Bottom buttons
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
                // Preset Selector
                addSettingButton(
                    "Preset: " + PRESETS[currentPreset],
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        currentPreset = (currentPreset + 1) % PRESETS.length;
                        if (currentPreset < 4) applyPreset(currentPreset);
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // FPS Overlay
                addSettingButton(
                    "FPS Overlay: " + (config.showFPSOverlay ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.showFPSOverlay = !config.showFPSOverlay;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Detailed Stats
                addSettingButton(
                    "Show Detailed Stats: " + (config.showDetailedStats ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.showDetailedStats = !config.showDetailedStats;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                break;
                
            case QUALITY:
                // Graphics Mode
                String graphicsName = Minecraft.getInstance().options.graphicsMode().get().toString();
                addSettingButton(
                    "Graphics: " + graphicsName,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        int current = Minecraft.getInstance().options.graphicsMode().get().getId();
                        int next = (current + 1) % 3;
                        Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.byId(next));
                        currentPreset = 4;
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Smooth Lighting
                boolean smoothLight = Minecraft.getInstance().options.ambientOcclusion().get();
                addSettingButton(
                    "Smooth Lighting: " + (smoothLight ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        Minecraft.getInstance().options.ambientOcclusion().set(!smoothLight);
                        config.disableSmoothLighting = !smoothLight;
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Entity Shadows
                boolean shadows = Minecraft.getInstance().options.entityShadows().get();
                addSettingButton(
                    "Entity Shadows: " + (shadows ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        Minecraft.getInstance().options.entityShadows().set(!shadows);
                        currentPreset = 4;
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Greedy Meshing
                addSettingButton(
                    "Greedy Meshing: " + (config.enableGreedyMeshing ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.enableGreedyMeshing = !config.enableGreedyMeshing;
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                break;
                
            case PERFORMANCE:
                // Render Distance
                int renderDist = Minecraft.getInstance().options.renderDistance().get();
                addSettingButton(
                    "Render Distance: " + renderDist,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        int newVal = (renderDist % 16) + 2;
                        Minecraft.getInstance().options.renderDistance().set(newVal);
                        currentPreset = 4;
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Entity Distance
                addSettingButton(
                    "Entity Distance: " + config.maxEntityRenderDistance,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        int[] vals = {16, 24, 32, 48, 64};
                        int idx = 0;
                        for (int i = 0; i < vals.length; i++) {
                            if (vals[i] == config.maxEntityRenderDistance) idx = i;
                        }
                        config.maxEntityRenderDistance = vals[(idx + 1) % vals.length];
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Particle Limit
                addSettingButton(
                    "Particle Limit: " + config.particleLimit,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        int[] vals = {10, 50, 100, 500, 1000};
                        int idx = 0;
                        for (int i = 0; i < vals.length; i++) {
                            if (vals[i] == config.particleLimit) idx = i;
                        }
                        config.particleLimit = vals[(idx + 1) % vals.length];
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Chunk Updates
                addSettingButton(
                    "Chunk Updates/Frame: " + config.chunkUpdateBudget,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.chunkUpdateBudget = (config.chunkUpdateBudget % 10) + 1;
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                break;
                
            case ADVANCED:
                // Max FPS
                addSettingButton(
                    "Max Framerate: " + config.maxFramerate,
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        int[] vals = {30, 60, 90, 120, 144, 240};
                        int idx = 0;
                        for (int i = 0; i < vals.length; i++) {
                            if (vals[i] == config.maxFramerate) idx = i;
                        }
                        config.maxFramerate = vals[(idx + 1) % vals.length];
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Ultra Low Mode
                addSettingButton(
                    "Ultra Low Mode: " + (config.ultraLowQualityMode ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.ultraLowQualityMode = !config.ultraLowQualityMode;
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                // Async Chunk Building
                addSettingButton(
                    "Async Chunks: " + (config.asyncChunkBuilding ? "ON" : "OFF"),
                    CONTENT_X, y, buttonWidth,
                    btn -> {
                        config.asyncChunkBuilding = !config.asyncChunkBuilding;
                        currentPreset = 4;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                );
                y += spacing;
                
                break;
        }
    }
    
    private void addSettingButton(String text, int x, int y, int width, Button.OnPress onPress) {
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
        
        // Apply button
        addRenderableWidget(Button.builder(
            Component.literal("Apply"),
            btn -> Main.config.save()
        ).bounds(this.width / 2 - buttonWidth - gap / 2, bottomY, buttonWidth, 20).build());
        
        // Done button
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
                config.enableGreedyMeshing = true;
                config.maxFramerate = 120;
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
        
        // Draw sidebar background (dark)
        graphics.fill(0, 35, SIDEBAR_WIDTH + 15, this.height - 35, 0xDD000000);
        
        // Draw content area background (lighter)
        graphics.fill(CONTENT_X - 5, 35, this.width - 5, this.height - 35, 0x88000000);
        
        // Draw category title
        graphics.drawString(this.font, currentCategory.name, CONTENT_X, TOP_Y - 20, 0xFFFFFF);
        
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // FPS counter
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