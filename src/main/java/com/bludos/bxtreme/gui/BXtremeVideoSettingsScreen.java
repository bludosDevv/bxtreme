package com.bludos.bxtreme.gui;

import com.bludos.bxtreme.Main;
import com.bludos.bxtreme.config.BXtremeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BXtremeVideoSettingsScreen extends Screen {
    
    private final Screen lastScreen;
    private BXtremeConfig config;
    
    // Categories
    private enum Category {
        QUALITY("Quality"),
        PERFORMANCE("Performance"),
        DETAILS("Details"),
        ANIMATIONS("Animations"),
        OTHER("Other");
        
        final String name;
        Category(String name) { this.name = name; }
    }
    
    private Category currentCategory = Category.PERFORMANCE;
    
    // Preset system
    private static final String[] PRESETS = {"ULTRA LOW", "LOW", "MEDIUM", "HIGH", "ULTRA", "CUSTOM"};
    private int currentPreset = 5; // Start with CUSTOM
    
    public BXtremeVideoSettingsScreen(Screen lastScreen) {
        super(Component.literal("BXtreme Video Settings"));
        this.lastScreen = lastScreen;
        this.config = Main.config.get();
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Clear existing widgets
        this.clearWidgets();
        
        int leftPanelWidth = 120;
        int rightPanelX = leftPanelWidth + 10;
        int topBarHeight = 30;
        
        // === TOP BAR - TITLE ===
        addRenderableWidget(Button.builder(
            Component.literal("§6§lBXTREME §f§lVIDEO SETTINGS"),
            btn -> {}
        ).bounds(this.width / 2 - 100, 5, 200, 20).build());
        
        // === LEFT PANEL - CATEGORIES ===
        int categoryY = topBarHeight + 5;
        int categorySpacing = 22;
        
        for (Category cat : Category.values()) {
            boolean selected = (cat == currentCategory);
            final Category finalCat = cat;
            addRenderableWidget(Button.builder(
                Component.literal((selected ? "§e§l> " : "§7") + cat.name),
                btn -> {
                    currentCategory = finalCat;
                    this.rebuildWidgets();
                }
            ).bounds(5, categoryY, leftPanelWidth, 20).build());
            categoryY += categorySpacing;
        }
        
        // === PRESET SELECTOR (Bottom of left panel) ===
        addRenderableWidget(Button.builder(
            Component.literal("§6" + PRESETS[currentPreset]),
            btn -> {
                currentPreset = (currentPreset + 1) % PRESETS.length;
                if (currentPreset < 5) {
                    applyPreset(currentPreset);
                }
                this.rebuildWidgets();
            }
        ).bounds(5, this.height - 50, leftPanelWidth, 20).build());
        
        // === RIGHT PANEL - SETTINGS ===
        buildRightPanel(rightPanelX, topBarHeight + 5);
        
        // === BOTTOM - DONE BUTTON ===
        addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> {
                Main.config.save();
                this.minecraft.setScreen(lastScreen);
            }
        ).bounds(this.width / 2 - 75, this.height - 30, 150, 20).build());
    }
    
    private void buildRightPanel(int startX, int startY) {
        int y = startY;
        int buttonWidth = Math.min(300, this.width - startX - 20);
        int spacing = 24;
        
        switch (currentCategory) {
            case PERFORMANCE:
                // Render Distance
                int renderDist = Minecraft.getInstance().options.renderDistance().get();
                addRenderableWidget(Button.builder(
                    Component.literal("Render Distance: §e" + renderDist),
                    btn -> {
                        int newVal = (renderDist % 32) + 2;
                        Minecraft.getInstance().options.renderDistance().set(newVal);
                        currentPreset = 5;
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Chunk Updates Per Frame
                addRenderableWidget(Button.builder(
                    Component.literal("Chunk Updates/Frame: §e" + config.chunkUpdateBudget),
                    btn -> {
                        config.chunkUpdateBudget = (config.chunkUpdateBudget % 10) + 1;
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Entity Distance
                addRenderableWidget(Button.builder(
                    Component.literal("Entity Distance: §e" + config.maxEntityRenderDistance),
                    btn -> {
                        int[] distances = {16, 24, 32, 48, 64, 96, 128};
                        int current = 0;
                        for (int i = 0; i < distances.length; i++) {
                            if (distances[i] == config.maxEntityRenderDistance) {
                                current = i;
                                break;
                            }
                        }
                        config.maxEntityRenderDistance = distances[(current + 1) % distances.length];
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Max FPS
                addRenderableWidget(Button.builder(
                    Component.literal("Max Framerate: §e" + config.maxFramerate),
                    btn -> {
                        int[] fps = {30, 60, 90, 120, 144, 240, 300};
                        int current = 0;
                        for (int i = 0; i < fps.length; i++) {
                            if (fps[i] == config.maxFramerate) {
                                current = i;
                                break;
                            }
                        }
                        config.maxFramerate = fps[(current + 1) % fps.length];
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Async Chunk Building
                addRenderableWidget(Button.builder(
                    Component.literal("Async Chunk Building: " + (config.asyncChunkBuilding ? "§aON" : "§cOFF")),
                    btn -> {
                        config.asyncChunkBuilding = !config.asyncChunkBuilding;
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                break;
                
            case QUALITY:
                // Graphics Quality
                int graphicsMode = Minecraft.getInstance().options.graphicsMode().get().getId();
                String[] graphicsModes = {"Fast", "Fancy", "Fabulous"};
                addRenderableWidget(Button.builder(
                    Component.literal("Graphics: §e" + graphicsModes[graphicsMode]),
                    btn -> {
                        int newMode = (graphicsMode + 1) % 3;
                        Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.byId(newMode));
                        currentPreset = 5;
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Smooth Lighting
                boolean smoothLighting = !config.disableSmoothLighting;
                addRenderableWidget(Button.builder(
                    Component.literal("Smooth Lighting: " + (smoothLighting ? "§aON" : "§cOFF")),
                    btn -> {
                        config.disableSmoothLighting = !config.disableSmoothLighting;
                        Minecraft.getInstance().options.ambientOcclusion().set(!config.disableSmoothLighting);
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Greedy Meshing
                addRenderableWidget(Button.builder(
                    Component.literal("Greedy Meshing §7(Bedrock-style): " + (config.simplifyBlockModels ? "§aON" : "§cOFF")),
                    btn -> {
                        config.simplifyBlockModels = !config.simplifyBlockModels;
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Ultra Low Quality Mode
                addRenderableWidget(Button.builder(
                    Component.literal("Ultra Low Quality Mode: " + (config.ultraLowQualityMode ? "§aON" : "§cOFF")),
                    btn -> {
                        config.ultraLowQualityMode = !config.ultraLowQualityMode;
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                break;
                
            case DETAILS:
                // Clouds
                int cloudsMode = Minecraft.getInstance().options.cloudStatus().get().getId();
                String[] cloudsModes = {"Off", "Fast", "Fancy"};
                addRenderableWidget(Button.builder(
                    Component.literal("Clouds: §e" + cloudsModes[cloudsMode]),
                    btn -> {
                        int newMode = (cloudsMode + 1) % 3;
                        Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.byId(newMode));
                        currentPreset = 5;
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Particles
                addRenderableWidget(Button.builder(
                    Component.literal("Particle Limit: §e" + config.particleLimit),
                    btn -> {
                        int[] limits = {10, 50, 100, 250, 500, 1000, 2000, 4000};
                        int current = 0;
                        for (int i = 0; i < limits.length; i++) {
                            if (limits[i] == config.particleLimit) {
                                current = i;
                                break;
                            }
                        }
                        config.particleLimit = limits[(current + 1) % limits.length];
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Entity Shadows
                boolean entityShadows = Minecraft.getInstance().options.entityShadows().get();
                addRenderableWidget(Button.builder(
                    Component.literal("Entity Shadows: " + (entityShadows ? "§aON" : "§cOFF")),
                    btn -> {
                        Minecraft.getInstance().options.entityShadows().set(!entityShadows);
                        currentPreset = 5;
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                break;
                
            case ANIMATIONS:
                // View Bobbing
                boolean viewBobbing = Minecraft.getInstance().options.bobView().get();
                addRenderableWidget(Button.builder(
                    Component.literal("View Bobbing: " + (viewBobbing ? "§aON" : "§cOFF")),
                    btn -> {
                        Minecraft.getInstance().options.bobView().set(!viewBobbing);
                        currentPreset = 5;
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Reduce Animations
                addRenderableWidget(Button.builder(
                    Component.literal("Reduce Animations: " + (config.reduceAnimations ? "§aON" : "§cOFF")),
                    btn -> {
                        config.reduceAnimations = !config.reduceAnimations;
                        currentPreset = 5;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                break;
                
            case OTHER:
                // FPS Overlay
                addRenderableWidget(Button.builder(
                    Component.literal("Show FPS Overlay: " + (config.showFPSOverlay ? "§aON" : "§cOFF")),
                    btn -> {
                        config.showFPSOverlay = !config.showFPSOverlay;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Detailed Stats
                addRenderableWidget(Button.builder(
                    Component.literal("Show Detailed Stats: " + (config.showDetailedStats ? "§aON" : "§cOFF")),
                    btn -> {
                        config.showDetailedStats = !config.showDetailedStats;
                        Main.config.save();
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // Fullscreen
                boolean fullscreen = Minecraft.getInstance().options.fullscreen().get();
                addRenderableWidget(Button.builder(
                    Component.literal("Fullscreen: " + (fullscreen ? "§aON" : "§cOFF")),
                    btn -> {
                        Minecraft.getInstance().options.fullscreen().set(!fullscreen);
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                // VSync
                boolean vsync = Minecraft.getInstance().options.enableVsync().get();
                addRenderableWidget(Button.builder(
                    Component.literal("VSync: " + (vsync ? "§aON" : "§cOFF")),
                    btn -> {
                        Minecraft.getInstance().options.enableVsync().set(!vsync);
                        this.rebuildWidgets();
                    }
                ).bounds(startX, y, buttonWidth, 20).build());
                y += spacing;
                
                break;
        }
    }
    
    private void applyPreset(int preset) {
        switch (preset) {
            case 0: // ULTRA LOW
                config.maxEntityRenderDistance = 16;
                config.particleLimit = 10;
                config.chunkUpdateBudget = 1;
                config.ultraLowQualityMode = true;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                config.disableSmoothLighting = true;
                config.maxFramerate = 60;
                Minecraft.getInstance().options.renderDistance().set(2);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
                Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.OFF);
                Minecraft.getInstance().options.entityShadows().set(false);
                Minecraft.getInstance().options.ambientOcclusion().set(false);
                break;
            case 1: // LOW
                config.maxEntityRenderDistance = 24;
                config.particleLimit = 50;
                config.chunkUpdateBudget = 2;
                config.ultraLowQualityMode = true;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                config.disableSmoothLighting = false;
                config.maxFramerate = 90;
                Minecraft.getInstance().options.renderDistance().set(4);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
                Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.FAST);
                Minecraft.getInstance().options.entityShadows().set(false);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
            case 2: // MEDIUM
                config.maxEntityRenderDistance = 32;
                config.particleLimit = 100;
                config.chunkUpdateBudget = 3;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                config.disableSmoothLighting = false;
                config.maxFramerate = 120;
                Minecraft.getInstance().options.renderDistance().set(6);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FANCY);
                Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.FAST);
                Minecraft.getInstance().options.entityShadows().set(true);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
            case 3: // HIGH
                config.maxEntityRenderDistance = 48;
                config.particleLimit = 500;
                config.chunkUpdateBudget = 4;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = false;
                config.asyncChunkBuilding = true;
                config.disableSmoothLighting = false;
                config.maxFramerate = 144;
                Minecraft.getInstance().options.renderDistance().set(10);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FANCY);
                Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.FANCY);
                Minecraft.getInstance().options.entityShadows().set(true);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
            case 4: // ULTRA
                config.maxEntityRenderDistance = 128;
                config.particleLimit = 4000;
                config.chunkUpdateBudget = 10;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = false;
                config.asyncChunkBuilding = true;
                config.disableSmoothLighting = false;
                config.maxFramerate = 300;
                Minecraft.getInstance().options.renderDistance().set(16);
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FANCY);
                Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.FANCY);
                Minecraft.getInstance().options.entityShadows().set(true);
                Minecraft.getInstance().options.ambientOcclusion().set(true);
                break;
        }
        Main.config.save();
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        
        // Draw left panel background
        graphics.fill(0, 25, 125, this.height - 35, 0x88000000);
        
        // Draw right panel background  
        graphics.fill(130, 25, this.width - 5, this.height - 35, 0x44000000);
        
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // Draw FPS counter
        if (Main.performanceMonitor != null) {
            int fps = Main.performanceMonitor.getFPS();
            int color = fps >= 60 ? 0x00FF00 : fps >= 30 ? 0xFFFF00 : 0xFF0000;
            graphics.drawString(this.font, "FPS: " + fps, this.width - 70, this.height - 15, color);
        }
    }
    
    @Override
    public void onClose() {
        Main.config.save();
        this.minecraft.setScreen(lastScreen);
    }
}