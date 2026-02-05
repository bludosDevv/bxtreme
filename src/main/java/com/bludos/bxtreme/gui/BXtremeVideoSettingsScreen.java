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
    
    private enum Category {
        PERFORMANCE("Performance"),
        QUALITY("Quality"),
        DETAILS("Details"),
        OTHER("Other");
        
        final String name;
        Category(String name) { this.name = name; }
    }
    
    private Category currentCategory = Category.PERFORMANCE;
    
    private static final String[] PRESETS = {"ULTRA LOW", "LOW", "MEDIUM", "HIGH", "CUSTOM"};
    private int currentPreset = 4; // CUSTOM
    
    public BXtremeVideoSettingsScreen(Screen lastScreen) {
        super(Component.literal("BXtreme Performance"));
        this.lastScreen = lastScreen;
        this.config = Main.config.get();
    }
    
    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        
        int centerX = this.width / 2;
        int y = 40;
        int buttonWidth = 200;
        int spacing = 25;
        
        // Title
        addRenderableWidget(Button.builder(
            Component.literal("=== BXTREME PERFORMANCE ==="),
            btn -> {}
        ).bounds(centerX - 100, 10, 200, 20).build());
        
        // Preset selector
        addRenderableWidget(Button.builder(
            Component.literal("Preset: " + PRESETS[currentPreset]),
            btn -> {
                currentPreset = (currentPreset + 1) % PRESETS.length;
                if (currentPreset < 4) applyPreset(currentPreset);
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // === PERFORMANCE SETTINGS ===
        
        // Render Distance
        int renderDist = Minecraft.getInstance().options.renderDistance().get();
        addRenderableWidget(Button.builder(
            Component.literal("Render Distance: " + renderDist),
            btn -> {
                int newVal = (renderDist % 16) + 2;
                Minecraft.getInstance().options.renderDistance().set(newVal);
                currentPreset = 4;
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Entity Distance
        addRenderableWidget(Button.builder(
            Component.literal("Entity Distance: " + config.maxEntityRenderDistance),
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
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Particle Limit
        addRenderableWidget(Button.builder(
            Component.literal("Particles: " + config.particleLimit),
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
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Graphics Mode
        String graphicsName = Minecraft.getInstance().options.graphicsMode().get().toString();
        addRenderableWidget(Button.builder(
            Component.literal("Graphics: " + graphicsName),
            btn -> {
                int current = Minecraft.getInstance().options.graphicsMode().get().getId();
                int next = (current + 1) % 3;
                Minecraft.getInstance().options.graphicsMode().set(net.minecraft.client.GraphicsStatus.byId(next));
                currentPreset = 4;
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Smooth Lighting
        boolean smoothLight = Minecraft.getInstance().options.ambientOcclusion().get();
        addRenderableWidget(Button.builder(
            Component.literal("Smooth Lighting: " + (smoothLight ? "ON" : "OFF")),
            btn -> {
                Minecraft.getInstance().options.ambientOcclusion().set(!smoothLight);
                config.disableSmoothLighting = !smoothLight;
                currentPreset = 4;
                Main.config.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Entity Shadows
        boolean shadows = Minecraft.getInstance().options.entityShadows().get();
        addRenderableWidget(Button.builder(
            Component.literal("Entity Shadows: " + (shadows ? "ON" : "OFF")),
            btn -> {
                Minecraft.getInstance().options.entityShadows().set(!shadows);
                currentPreset = 4;
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // Ultra Low Mode
        addRenderableWidget(Button.builder(
            Component.literal("Ultra Low Mode: " + (config.ultraLowQualityMode ? "ON" : "OFF")),
            btn -> {
                config.ultraLowQualityMode = !config.ultraLowQualityMode;
                currentPreset = 4;
                Main.config.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing;
        
        // FPS Overlay
        addRenderableWidget(Button.builder(
            Component.literal("FPS Overlay: " + (config.showFPSOverlay ? "ON" : "OFF")),
            btn -> {
                config.showFPSOverlay = !config.showFPSOverlay;
                Main.config.save();
                this.rebuildWidgets();
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
        y += spacing + 10;
        
        // Done button
        addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> {
                Main.config.save();
                this.minecraft.setScreen(lastScreen);
            }
        ).bounds(centerX - buttonWidth / 2, y, buttonWidth, 20).build());
    }
    
    private void applyPreset(int preset) {
        switch (preset) {
            case 0: // ULTRA LOW
                config.maxEntityRenderDistance = 16;
                config.particleLimit = 10;
                config.chunkUpdateBudget = 1;
                config.ultraLowQualityMode = false; // DISABLE ultra low - it causes invisible blocks
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
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // FPS counter
        if (Main.performanceMonitor != null) {
            int fps = Main.performanceMonitor.getFPS();
            graphics.drawString(this.font, "FPS: " + fps, 10, this.height - 15, 0xFFFFFF);
        }
    }
    
    @Override
    public void onClose() {
        Main.config.save();
        this.minecraft.setScreen(lastScreen);
    }
}
