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
            addRenderableWidget(Button.builder(
                Component.literal((selected ? "§e§l> " : "§7") + cat.name),
                btn -> {
                    currentCategory = cat;
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
                addRenderableWidget(createSlider(
                    "Render Distance",
                    startX, y, buttonWidth,
                    2, 32, Minecraft.getInstance().options.renderDistance().get(),
                    val -> {
                        Minecraft.getInstance().options.renderDistance().set(val);
                        currentPreset = 5; // Set to CUSTOM
                    }
                ));
                y += spacing;
                
                // Chunk Updates Per Frame
                addRenderableWidget(createSlider(
                    "Chunk Updates/Frame",
                    startX, y, buttonWidth,
                    1, 10, config.chunkUpdateBudget,
                    val -> {
                        config.chunkUpdateBudget = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Entity Distance
                addRenderableWidget(createSlider(
                    "Entity Distance",
                    startX, y, buttonWidth,
                    8, 128, config.maxEntityRenderDistance,
                    val -> {
                        config.maxEntityRenderDistance = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Max FPS
                addRenderableWidget(createSlider(
                    "Max Framerate",
                    startX, y, buttonWidth,
                    30, 300, config.maxFramerate,
                    val -> {
                        config.maxFramerate = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Async Chunk Building
                addRenderableWidget(createToggle(
                    "Async Chunk Building",
                    startX, y, buttonWidth,
                    config.asyncChunkBuilding,
                    val -> {
                        config.asyncChunkBuilding = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                break;
                
            case QUALITY:
                // Graphics Quality
                addRenderableWidget(createCycleButton(
                    "Graphics",
                    startX, y, buttonWidth,
                    new String[]{"Fast", "Fancy", "Fabulous"},
                    getGraphicsIndex(),
                    val -> {
                        setGraphicsMode(val);
                        currentPreset = 5;
                    }
                ));
                y += spacing;
                
                // Smooth Lighting
                addRenderableWidget(createToggle(
                    "Smooth Lighting",
                    startX, y, buttonWidth,
                    !config.disableSmoothLighting,
                    val -> {
                        config.disableSmoothLighting = !val;
                        Minecraft.getInstance().options.ambientOcclusion().set(val);
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Greedy Meshing
                addRenderableWidget(createToggle(
                    "Greedy Meshing §7(Bedrock-style)",
                    startX, y, buttonWidth,
                    config.simplifyBlockModels,
                    val -> {
                        config.simplifyBlockModels = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Ultra Low Quality Mode
                addRenderableWidget(createToggle(
                    "Ultra Low Quality Mode",
                    startX, y, buttonWidth,
                    config.ultraLowQualityMode,
                    val -> {
                        config.ultraLowQualityMode = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                break;
                
            case DETAILS:
                // Clouds
                addRenderableWidget(createCycleButton(
                    "Clouds",
                    startX, y, buttonWidth,
                    new String[]{"Off", "Fast", "Fancy"},
                    getCloudsIndex(),
                    val -> {
                        setCloudsMode(val);
                        currentPreset = 5;
                    }
                ));
                y += spacing;
                
                // Particles
                addRenderableWidget(createSlider(
                    "Particle Limit",
                    startX, y, buttonWidth,
                    10, 4000, config.particleLimit,
                    val -> {
                        config.particleLimit = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Entity Shadows
                addRenderableWidget(createToggle(
                    "Entity Shadows",
                    startX, y, buttonWidth,
                    Minecraft.getInstance().options.entityShadows().get(),
                    val -> {
                        Minecraft.getInstance().options.entityShadows().set(val);
                        currentPreset = 5;
                    }
                ));
                y += spacing;
                
                break;
                
            case ANIMATIONS:
                // View Bobbing
                addRenderableWidget(createToggle(
                    "View Bobbing",
                    startX, y, buttonWidth,
                    Minecraft.getInstance().options.bobView().get(),
                    val -> {
                        Minecraft.getInstance().options.bobView().set(val);
                        currentPreset = 5;
                    }
                ));
                y += spacing;
                
                // Reduce Animations
                addRenderableWidget(createToggle(
                    "Reduce Animations",
                    startX, y, buttonWidth,
                    config.reduceAnimations,
                    val -> {
                        config.reduceAnimations = val;
                        currentPreset = 5;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                break;
                
            case OTHER:
                // FPS Overlay
                addRenderableWidget(createToggle(
                    "Show FPS Overlay",
                    startX, y, buttonWidth,
                    config.showFPSOverlay,
                    val -> {
                        config.showFPSOverlay = val;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Detailed Stats
                addRenderableWidget(createToggle(
                    "Show Detailed Stats",
                    startX, y, buttonWidth,
                    config.showDetailedStats,
                    val -> {
                        config.showDetailedStats = val;
                        Main.config.save();
                    }
                ));
                y += spacing;
                
                // Fullscreen
                addRenderableWidget(createToggle(
                    "Fullscreen",
                    startX, y, buttonWidth,
                    Minecraft.getInstance().options.fullscreen().get(),
                    val -> {
                        Minecraft.getInstance().options.fullscreen().set(val);
                    }
                ));
                y += spacing;
                
                // VSync
                addRenderableWidget(createToggle(
                    "VSync",
                    startX, y, buttonWidth,
                    Minecraft.getInstance().options.enableVsync().get(),
                    val -> {
                        Minecraft.getInstance().options.enableVsync().set(val);
                    }
                ));
                y += spacing;
                
                break;
        }
    }
    
    // Helper methods for creating UI elements
    private Button createSlider(String label, int x, int y, int width, int min, int max, int current, java.util.function.Consumer<Integer> onChange) {
        return Button.builder(
            Component.literal(label + ": §e" + current),
            btn -> {
                int newVal = current + 1;
                if (newVal > max) newVal = min;
                onChange.accept(newVal);
                this.rebuildWidgets();
            }
        ).bounds(x, y, width, 20).build();
    }
    
    private Button createToggle(String label, int x, int y, int width, boolean current, java.util.function.Consumer<Boolean> onChange) {
        return Button.builder(
            Component.literal(label + ": " + (current ? "§aON" : "§cOFF")),
            btn -> {
                onChange.accept(!current);
                this.rebuildWidgets();
            }
        ).bounds(x, y, width, 20).build();
    }
    
    private Button createCycleButton(String label, int x, int y, int width, String[] options, int current, java.util.function.Consumer<Integer> onChange) {
        return Button.builder(
            Component.literal(label + ": §e" + options[current]),
            btn -> {
                int newVal = (current + 1) % options.length;
                onChange.accept(newVal);
                this.rebuildWidgets();
            }
        ).bounds(x, y, width, 20).build();
    }
    
    private int getGraphicsIndex() {
        return Minecraft.getInstance().options.graphicsMode().get().getId();
    }
    
    private void setGraphicsMode(int index) {
        Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.byId(index));
    }
    
    private int getCloudsIndex() {
        return Minecraft.getInstance().options.cloudStatus().get().getId();
    }
    
    private void setCloudsMode(int index) {
        Minecraft.getInstance().options.cloudStatus().set(net.minecraft.client.CloudStatus.byId(index));
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